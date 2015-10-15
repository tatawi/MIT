package com.mit.mit;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class A_jour_Preparation extends MainActivity {

    //objets de la page
    private TextView lb_montant;
    private LinearLayout container_globalLayout;


    //variables
    private C_Jour day;
    private SimpleDateFormat sdf;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_jour__preparation);

        //initialisation objet page
        container_globalLayout = (LinearLayout) findViewById(R.id.jourPrep_layoutContent);
        lb_montant = (TextView) findViewById(R.id.jourPrep_lb_cout);



        //initialisation variables
        sdf = new SimpleDateFormat("EEE d MMM");

        //récupération du projet
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            this.userID = extras.getString("userID");
            System.out.println("jour preparation: "+this.userID);

            //chargement des données du projet
            this.day = daoJour.getJourById(extras.getString("idEntry"));


            System.out.println("sujets : "+day.sujetsToString);
            System.out.println("nombre sujets : "+day.liste_sujets.size());
            this.day.creerLesListes(daoSujet);
            System.out.println("CREER LISTES");
            System.out.println("sujets : " + day.sujetsToString);
            System.out.println("nombre sujets : "+day.liste_sujets.size());

            setTitle(sdf.format(this.day.jour));
        }


        //this.day.liste_sujets=creerDesSujets();





        if(!this.day.liste_sujets.isEmpty())
        {
            container_globalLayout.setOrientation(LinearLayout.VERTICAL);
            sdf = new SimpleDateFormat("HH:mm");
            String titreDescription = "";
            for (C_Sujet s : this.day.liste_sujets)
            //for(int i=0; i<10; i++)
            {
                //panel global
                LinearLayout LLglobal = new LinearLayout(this);
                LLglobal.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LLglobal.setLayoutParams(LLParams);
                LLglobal.setId(s.id);

                //panel button
                LinearLayout LLleft = new LinearLayout(this);
                LLleft.setOrientation(LinearLayout.HORIZONTAL);
                LLleft.setLayoutParams(LLParams);

                //panel messages
                LinearLayout LLright = new LinearLayout(this);
                LLright.setOrientation(LinearLayout.VERTICAL);
                LLright.setLayoutParams(LLParams);

                //panel description
                LinearLayout LLrightUp = new LinearLayout(this);
                LLrightUp.setOrientation(LinearLayout.HORIZONTAL);
                LLrightUp.setLayoutParams(LLParams);

                //panel heures
                LinearLayout LLrightDown = new LinearLayout(this);
                LLrightDown.setOrientation(LinearLayout.HORIZONTAL);
                LLrightDown.setLayoutParams(LLParams);


                //déclarations champs
                ImageButton img = new ImageButton(this);
                TextView desc = new TextView(this);
                TextView h1 = new TextView(this);
                TextView h2 = new TextView(this);
                TextView cout = new TextView(this);


                //personnalisation champs
                switch (s.type) {
                    case "Transport":
                        img.setImageResource(R.drawable.ic_jour_transports);
                        desc.setTextColor(Color.parseColor("#e74c3c"));
                        titreDescription = "[Transport] ";
                        break;

                    case "Repas":
                        img.setImageResource(R.drawable.ic_jour_repas);
                        desc.setTextColor(Color.parseColor("#2980b9"));
                        titreDescription = "[Repas] ";
                        break;

                    case "Visite":
                        img.setImageResource(R.drawable.ic_jour_visite);
                        desc.setTextColor(Color.parseColor("#16a085"));
                        titreDescription = "[Visite] ";
                        break;

                    case "Logement":
                        img.setImageResource(R.drawable.ic_jour_logement);
                        desc.setTextColor(Color.parseColor("#f39c12"));
                        titreDescription = "[Hebergement] ";
                        break;

                    case "loisir":
                        img.setImageResource(R.drawable.ic_jour_loisir);
                        desc.setTextColor(Color.parseColor("#9b59b6"));
                        titreDescription = "[Loisir] ";
                        break;

                    case "Libre":
                        img.setImageResource(R.drawable.ic_jour_libre);
                        desc.setTextColor(Color.parseColor("#5b5b5b"));
                        titreDescription = "[Libre] ";
                        break;
                }

                //initialisation champs
                img.setBackgroundColor(Color.TRANSPARENT);
                desc.setText(titreDescription + s.titre);
                desc.setTextSize(16);
                h1.setText(sdf.format(s.heure));
                h1.setPadding(10, 10, 10, 0);
                h2.setText(" " + s.duree + " m ");
                h2.setPadding(10, 10, 10, 0);
                cout.setText(" " + s.prix + " € ");
                cout.setPadding(10, 10, 10, 0);

                //ajout des champs aux pannels
                LLleft.addView(img);
                LLrightUp.addView(desc);
                LLrightDown.addView(h1);
                LLrightDown.addView(h2);
                LLrightDown.addView(cout);

                //ajouts panels
                LLright.addView(LLrightUp);
                LLright.addView(LLrightDown);
                LLglobal.addView(LLleft);
                LLglobal.addView(LLright);
                LLglobal.setOnClickListener(onClickLayout);
                container_globalLayout.addView(LLglobal);
            }
        }
    }

    //Selection d'un sujet
    View.OnClickListener onClickLayout = new View.OnClickListener() {
        public void onClick(View v) {
            LinearLayout selectedLL = (LinearLayout) v;

            for (C_Sujet s:day.liste_sujets)
            {
                if(s.id==selectedLL.getId())
                {
                    Intent intent = new Intent(A_jour_Preparation.this, A_sujet_Preparation.class);
                    intent.putExtra("idEntry", s.idSujet);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                }
            }

            //DialogFragment newFragment = new D_DatePickerFragment("newproject_debut");
            //newFragment.show(getFragmentManager(), "datePicker");
            //tb_dateDebut.setText(lb_date.getText());
        }
    };



    private List<C_Sujet> creerDesSujets()
    {
        List<C_Sujet>liste =new ArrayList<C_Sujet>();
        //public C_Sujet( String titre, String description, String type, String localisation, Date heure, int duree, boolean auFeeling, float prix)

        C_Sujet s1 = new C_Sujet("Hotel","description","Logement","loc",new Date(), 120, false, 40);
        C_Sujet s2 = new C_Sujet("Peti-dej","description","Repas","loc",new Date(), 160, false, 10);
        C_Sujet s3 = new C_Sujet("To : centre ville","description","Transport","loc",new Date(), 30, false, 2);
        C_Sujet s4 = new C_Sujet("Musée du Vietnam","description","Visite","loc",new Date(), 120, false, 5);
        C_Sujet s5 = new C_Sujet("Quartier Latin","description","Visite","loc",new Date(), 120, false, 5);
        C_Sujet s6 = new C_Sujet("To : quartier chinois","description","Transport","loc",new Date(), 20, false, 2);
        C_Sujet s7 = new C_Sujet("Midi","description","Repas","loc",new Date(), 60, false, 5);
        C_Sujet s8 = new C_Sujet("Quartier chinois","description","Visite","loc",new Date(), 120, false, 0);
        C_Sujet s9 = new C_Sujet("Pousse-pousse","description","loisir","loc",new Date(), 60, false, 5);
        C_Sujet s10 = new C_Sujet("Maison de Ho Chin Min","description","Visite","loc",new Date(), 120, false, 10);
        C_Sujet s11 = new C_Sujet("Achats","description","Libre","loc",new Date(), 120, false, 20);
        C_Sujet s12 = new C_Sujet("To : vers hotel","description","Transport","loc",new Date(), 40, false, 4);
        C_Sujet s13 = new C_Sujet("Soir","description","Repas","loc",new Date(), 120, false, 10);

        liste.add(s1);
        liste.add(s2);
        liste.add(s3);
        liste.add(s4);
        liste.add(s5);
        liste.add(s6);
        liste.add(s7);
        liste.add(s8);
        liste.add(s9);
        liste.add(s10);
        liste.add(s11);
        liste.add(s12);
        liste.add(s13);

        return liste;
    }




























    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_jour__preparation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //ON NEW USER
        if (id == R.id.menu_addSujet)
        {
            Intent intent = new Intent(A_jour_Preparation.this, A_sujet_new.class);
            intent.putExtra("idEntry", this.day.nomJour);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
