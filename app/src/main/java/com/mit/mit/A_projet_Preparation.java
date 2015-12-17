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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class A_projet_Preparation extends MainActivity {

/*          System.out.print("ADD BLANK");
            System.out.println();
            */

    //objets de la page
    private TextView lb_description;
    private TextView lb_cout;
    private TextView lb_text;
    private LinearLayout container_tableLayout;

    //variables

    private double nbDays;
    private String tab_correspButtons[];
    private SimpleDateFormat sdf;

    private C_Participant part;
    private C_Options options;
    private C_Projet projet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("**************************************************************");
        System.out.println("**A_projet_preparation : liste des jours du projet");

        //initialiser objets de la page
        setContentView(R.layout.activity_a_projet__preparation);
        lb_description = (TextView) findViewById(R.id.projetPrep_lb_description);
        lb_cout = (TextView) findViewById(R.id.projetPrep_lb_cout);
        lb_text= (TextView) findViewById(R.id.projetPrep_lb_txtCout);
        container_tableLayout = (LinearLayout) findViewById(R.id.projetPrep_tableLayout);


        //récupération du projet
        this.options=daoOptions.getOption();
        this.part=daoparticipant.getParticipantById(options.userid);
        this.projet=daoProjet.getProjetByName(options.projetid);

        this.projet.creerLesListes(daoJour, daoparticipant);
        setTitle(this.projet.nom);
        lb_description.setText(this.projet.description);
        lb_cout.setText(this.projet.prixSejour + " €");
        nbDays=this.projet.liste_jours.size();
        tab_correspButtons=new String[(int)nbDays+7];

        System.out.println("--utilisateur actuel : "+this.part.mail);
        System.out.println("--projet : "+this.projet.nom);
        System.out.println("--nombre de jours : "+nbDays);
       /* Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            this.userID = extras.getString("userID");
            this.part=daoparticipant.getParticipantById(this.userID);

            //chargement des données du projet
            this.projet = daoProjet.getProjetByName(extras.getString("idEntry"));
            this.projet.creerLesListes(daoJour, daoparticipant);
            setTitle(this.projet.nom);
            lb_description.setText(this.projet.description);
            lb_cout.setText(this.projet.prixSejour + " €");
            nbDays=this.projet.liste_jours.size();
            tab_correspButtons=new String[(int)nbDays+7];

            System.out.println("--utilisateur actuel : "+this.userID);
            System.out.println("--projet : "+this.projet.nom);
            System.out.println("--nombre de jours : "+nbDays);

        }*/


        /*---------------------------
        *AFFICHAGE
        */


        if(!this.projet.liste_jours.isEmpty()) {
            container_tableLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            sdf = new SimpleDateFormat("EEEE dd MMM yyyy");
            int nbParcours=0;
            for (C_Jour j : this.projet.liste_jours)
            {
                //layout ligne
                LinearLayout LL = new LinearLayout(this);
                LL.setOrientation(LinearLayout.HORIZONTAL);
                LL.setLayoutParams(LLParams);
                LL.setPadding(0,10,0,10);




                //IMAGE CALENDRIER
                ImageButton btn=new ImageButton(this);
                String str_buttonID=getDayIDFromDate(j.jour);
                String fileName=getImageNameForDay(j.jour);
                int identifier = getResources().getIdentifier(fileName, "drawable", getPackageName());

                //add listener
                tab_correspButtons[nbParcours]=getDayIDFromDate(j.jour);
                btn.setId(nbParcours);
                btn.setOnClickListener(onClickDay);
                btn.setImageResource(identifier);
                btn.setBackgroundColor(Color.TRANSPARENT);
                LL.addView(btn);


                //TEXTE
                LinearLayout LLtexte = new LinearLayout(this);
                LLtexte.setOrientation(LinearLayout.VERTICAL);
                LLtexte.setLayoutParams(LLParams);
                LLtexte.setPadding(20,0,40,0);

                TextView lb_jour = new TextView(this);
                sdf = new SimpleDateFormat("EEEE", Locale.FRANCE);
                lb_jour.setText(sdf.format(j.jour));
                lb_jour.setTextSize(18);
                LLtexte.addView(lb_jour);

                TextView lb_date = new TextView(this);
                sdf = new SimpleDateFormat("dd MMM yyyy");
                lb_date.setText(sdf.format(j.jour));
                LLtexte.addView(lb_date);

                LL.addView(LLtexte);


                //IMAGE MESSAGES
                ImageButton btnConv=new ImageButton(this);
                btnConv.setImageResource(R.drawable.ic_projet_prep_conv);
                btnConv.setBackgroundColor(Color.TRANSPARENT);
                btnConv.setId(nbParcours);
               // btnConv.setOnClickListener(onClickConv);
                LL.addView(btnConv);



                //INCREMENT DAY
                nbParcours++;
                container_tableLayout.addView(LL);
            }


        }























    }



//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------


    //BOUTON SELECTION JOUR
    View.OnClickListener onClickDay = new View.OnClickListener()
    {
        public void onClick(View v) {
            ImageButton button = (ImageButton)v;
            String dayID=tab_correspButtons[button.getId()];

            Intent intent = new Intent(A_projet_Preparation.this, A_jour_Preparation.class);
            options.jourid=dayID;
            daoOptions.modifier(options);
            //intent.putExtra("idEntry", dayID);
            //intent.putExtra("userID", userID);

            System.out.println(">>intent : user " + part.mail);
            System.out.println(">>intent : jourSelectionné : "+dayID);
            startActivity(intent);
        }
    };



    //BOUTON SELECTION MESSAGE
   /* View.OnClickListener onClickConv = new View.OnClickListener()
    {
        public void onClick(View v) {
            ImageButton button = (ImageButton)v;
            String dayID=tab_correspButtons[button.getId()];

            Intent intent = new Intent(A_projet_Preparation.this, A_jour_Preparation.class);
            intent.putExtra("idEntry", dayID);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }
    };*/











//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------


    private String getDayIDFromDate(Date d)
    {
        sdf = new SimpleDateFormat("dd/MM/yy");

        for(C_Jour j:this.projet.liste_jours)
        {
            if(sdf.format(d).equals(sdf.format(j.jour)))
            {
                return j.nomJour;
            }

        }
        return "";
    }


    private String getImageNameForDay(Date d)
    {
        System.out.println("getImageNameForDay");
        sdf = new SimpleDateFormat("dd/MM/yy");

        for(C_Jour j:this.projet.liste_jours)
        {
            if(sdf.format(d).equals(sdf.format(j.jour)))
            {
                j.creerLesListes(daoSujet);
                sdf = new SimpleDateFormat("dd");
                if(j.isNotification(part, daoMessage, daoparticipant))
                {
                    return "ic_cal_full_"+sdf.format(j.jour);
                }
                else
                {
                    return "ic_cal_"+sdf.format(j.jour);
                }
            }
        }
        return "ic_cal_black_01";
    }
















//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_affichage_projet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.menu_lprojet_refresh)
        {
            MajDAO();
        }

        if(id==R.id.menu_projetPrep_deconnexion)
        {
            daoOptions.supprimer(options);
            Intent intent = new Intent(A_projet_Preparation.this, MainActivity.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }
}
