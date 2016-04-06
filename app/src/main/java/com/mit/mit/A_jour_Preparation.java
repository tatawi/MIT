package com.mit.mit;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class A_jour_Preparation extends MainActivity {

    //objets de la page
    //private TextView lb_montant;
    private LinearLayout container_globalLayout;
    private EditText tb_ville;
    private ImageButton btn_save;
    private ImageButton btn_sort_date;
    private ImageButton btn_sort_finished;
    private ImageButton btn_sort_name;
    private ImageButton btn_sort_cat;


    //variables
    private String jourID;
    private SimpleDateFormat sdf;

    private C_Participant part;
    private C_Options options;
    private C_Jour day;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_jour__preparation);

        System.out.println("**************************************************************");
        System.out.println("**A_Jour_preparation : liste des sujets d'un jour");

        //Initialisations objets page
        tb_ville = (EditText) findViewById(R.id.jourPrep_tb_ville);
        btn_save = (ImageButton) findViewById(R.id.jourPrep_btn_save);
        //lb_montant = (TextView) findViewById(R.id.jourPrep_lb_cout);
        btn_sort_date = (ImageButton) findViewById(R.id.jourPrep_sortDate);
        btn_sort_finished = (ImageButton) findViewById(R.id.jourPrep_sortFinished);
        btn_sort_name = (ImageButton) findViewById(R.id.jourPrep_sortName);
        btn_sort_cat = (ImageButton) findViewById(R.id.jourPrep_sortCat);

        //listeners
        btn_save.setOnClickListener(onSave);
        container_globalLayout = (LinearLayout) findViewById(R.id.jourPrep_layoutContent);
        btn_sort_date.setOnClickListener(onSortDate);
        btn_sort_finished.setOnClickListener(onSortFinished);
        btn_sort_name.setOnClickListener(onSortName);
        btn_sort_cat.setOnClickListener(onSortCat);


        //initialisations
        this.options=daoOptions.getOptionByUserId();
        System.out.println("IS ONLINE ="+options.online);
        this.part=daoparticipant.getParticipantById(options.userid);
        this.day=daoJour.getJourById(options.jourid);

        this.day.creerLesListes(daoSujet);
        jourID=day.nomJour;

        sdf = new SimpleDateFormat("EEE d MMM");
        setTitle(sdf.format(this.day.jour));
        //lb_montant.setText("" + this.day.prixJournee + " €");


        //GESTION VILLE
        tb_ville.setText(day.ville);
        if(day.ville.equals(""))
        {
            tb_ville.setText("Entrez un emplacement");
            tb_ville.setTextColor(Color.LTGRAY);
            tb_ville.setTypeface(null, Typeface.ITALIC);
        }
        tb_ville.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tb_ville.setText("");
                tb_ville.setTextColor(Color.BLACK);
                tb_ville.setTypeface(null, Typeface.NORMAL);
            }
        });



        System.out.println("--utilisateur courant: " + options.userid);
        System.out.println("--jourEnparamétres: " + options.jourid);
        System.out.println("--jourCalculé : " + sdf.format(this.day.jour));
        System.out.println("--jourID : " + this.day.nomJour);
        System.out.println("--nb sujets : " + this.day.liste_sujets.size());

        /*
        //Gestion map = save loc
        try {
            Bundle extras = getIntent().getExtras();
            //Cas ou on vient de creer un sujet
            if (extras.getString("isFromMap").equals("oui")) {
                C_Sujet sujetToModify = daoSujet.getSujetById(extras.getString("sujetID"));
                sujetToModify.localisation = extras.getString("adresse");


                daoSujet.modifier(sujetToModify, options.online);
            }
        }
        catch (Exception e)
        {
        }*/

        trierList("Date");

    }





//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------


    private void affichage()
    {

    if(!this.day.liste_sujets.isEmpty())
    {
        container_globalLayout.removeAllViews();
        container_globalLayout.setOrientation(LinearLayout.VERTICAL);
        sdf = new SimpleDateFormat("HH:mm");
        String titreDescription = "";
        for (C_Sujet s : this.day.liste_sujets)
        {
            s.creerLesListes(daoMessage, daoparticipant);
            System.out.println("timeAff : " + s.heure.toString());
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
            TextView heure = new TextView(this);
            TextView duree = new TextView(this);
            TextView cout = new TextView(this);
            TextView valid = new TextView(this);

            //personnalisation champs
            switch (s.type) {
                case "Transport":
                    if(s.valide)
                        img.setImageResource(R.drawable.ic_jour_transports_fill);
                    else
                        img.setImageResource(R.drawable.ic_jour_transports);
                    desc.setTextColor(Color.parseColor("#e74c3c"));
                    titreDescription = "[Transport] ";
                    break;

                case "Repas":
                    if(s.valide)
                        img.setImageResource(R.drawable.ic_jour_repas_fill);
                    else
                        img.setImageResource(R.drawable.ic_jour_repas);
                    desc.setTextColor(Color.parseColor("#2980b9"));
                    titreDescription = "[Repas] ";
                    break;

                case "Visite":
                    if(s.valide)
                        img.setImageResource(R.drawable.ic_jour_visite_fill);
                    else
                        img.setImageResource(R.drawable.ic_jour_visite);
                    desc.setTextColor(Color.parseColor("#16a085"));
                    titreDescription = "[Visite] ";
                    break;

                case "Logement":
                    if(s.valide)
                        img.setImageResource(R.drawable.ic_jour_logement_fill);
                    else
                        img.setImageResource(R.drawable.ic_jour_logement);
                    desc.setTextColor(Color.parseColor("#f39c12"));
                    titreDescription = "[Hebergement] ";
                    break;

                case "Loisir":
                    if(s.valide)
                        img.setImageResource(R.drawable.ic_jour_loisir_fill);
                    else
                        img.setImageResource(R.drawable.ic_jour_loisir);
                    desc.setTextColor(Color.parseColor("#9b59b6"));
                    titreDescription = "[Loisir] ";
                    break;

                case "Libre":
                    if(s.valide)
                        img.setImageResource(R.drawable.ic_jour_libre_fill);
                    else
                        img.setImageResource(R.drawable.ic_jour_libre);
                    desc.setTextColor(Color.parseColor("#5b5b5b"));
                    titreDescription = "[Libre] ";
                    break;
            }

            //initialisation champs
            img.setBackgroundColor(Color.TRANSPARENT);
            desc.setTextSize(16);
            heure.setText(sdf.format(s.heure));
            heure.setPadding(10, 10, 10, 0);

            if(s.valide)
            {
                valid.setText("Validé");
            }
            else
            {
                valid.setText("Non validé");
            }

            //time
            int hour;
            int min;
            hour = s.duree / 60;
            min=s.duree-hour*60;
            duree.setText(hour + "h" + min);
            duree.setPadding(10, 10, 10, 0);
            cout.setText(" " + s.prix + " € ");
            cout.setPadding(10, 10, 10, 0);

            //notifications
            desc.setText(titreDescription + s.titre);
            System.out.println("sujet prep current user : "+part.mail );
            if(s.isNotificationForMe(part, daoparticipant))
            {
                desc.setText("*" +titreDescription + s.titre);
            }

            //ajout des champs aux pannels
            LLleft.addView(img);
            LLrightUp.addView(desc);
            LLrightDown.addView(heure);
            LLrightDown.addView(duree);
            LLrightDown.addView(cout);

            //ajouts panels
            LLright.addView(LLrightUp);
            LLright.addView(LLrightDown);
            LLright.addView(valid);

            LLglobal.addView(LLleft);
            LLglobal.addView(LLright);
            LLglobal.setOnClickListener(onClickLayout);
            LLglobal.setOnLongClickListener(onLongClickLayout);


            container_globalLayout.addView(LLglobal);
        }
    }
}


    private void trierList(String sort)
    {


        switch (sort) {
            case "Date":
                //Sorting
                Collections.sort(this.day.liste_sujets, new Comparator<C_Sujet>() {
                    @Override
                    public int compare(C_Sujet s1, C_Sujet s2) {

                        return s1.heure.compareTo(s2.heure);
                    }
                });

                break;

            case "Finished":
                //Sorting
                Collections.sort(this.day.liste_sujets, new Comparator<C_Sujet>() {
                    @Override
                    public int compare(C_Sujet s1, C_Sujet s2) {

                        return s1.personnesAyantAccepteToString.compareTo(s2.personnesAyantAccepteToString);
                    }
                });

                break;

            case "Name":
                //Sorting
                Collections.sort(this.day.liste_sujets, new Comparator<C_Sujet>() {
                    @Override
                    public int compare(C_Sujet s1, C_Sujet s2) {

                        return s1.idSujet.compareTo(s2.idSujet);
                    }
                });
                break;

            case "Cat":
                //Sorting
                Collections.sort(this.day.liste_sujets, new Comparator<C_Sujet>() {
                    @Override
                    public int compare(C_Sujet s1, C_Sujet s2) {

                        return s1.type.compareTo(s2.type);
                    }
                });

                break;
        }

        affichage();

    }





//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

//Selection d'un sujet
View.OnClickListener onClickLayout = new View.OnClickListener() {
    public void onClick(View v) {
        LinearLayout selectedLL = (LinearLayout) v;

        for (C_Sujet s:day.liste_sujets)
        {
            if(s.id==selectedLL.getId())
            {
                Intent intent = new Intent(A_jour_Preparation.this, A_sujet_Preparation.class);
                options.sujetid=s.idSujet;
                daoOptions.modifier(options);
                //intent.putExtra("idEntry", s.idSujet);
                //intent.putExtra("userID", userID);
                startActivity(intent);
            }
        }

        //DialogFragment newFragment = new D_DatePickerFragment("newproject_debut");
        //newFragment.show(getFragmentManager(), "datePicker");
        //tb_dateDebut.setText(lb_date.getText());
    }
};

    //ON LONG CLICK = REMOVE
    View.OnLongClickListener onLongClickLayout = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            LinearLayout selectedLL = (LinearLayout) v;

            for (C_Sujet s:day.liste_sujets)
            {
                if(s.id==selectedLL.getId())
                {
                    s.creerLesListes(daoMessage, daoparticipant);
                    final C_Sujet sujetToDel = s;

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //REMOVE OK

                                    //delete messages
                                    for (C_Message msgToDel:sujetToDel.liste_messages)
                                    {
                                        daoMessage.supprimer(msgToDel.id, options.online);
                                    }

                                    //delete subject
                                    daoSujet.supprimer(sujetToDel.idSujet, options.online);

                                    //maj jour
                                    for (Iterator<C_Sujet> iter = day.liste_sujets.listIterator(); iter.hasNext(); )
                                    {
                                        C_Sujet subj = iter.next();
                                        if (subj.idSujet.equals(sujetToDel.idSujet))
                                        { iter.remove();}
                                    }
                                    //TODO prix
                                    day.listeToString();
                                    daoJour.modifier(day, options.online);

                                    affichage();


                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
                    builder.setMessage("Delete "+s.titre+" ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            }
            affichage();
            return true;
        }
    };


    //BOUTTON save ville
    View.OnClickListener onSave = new View.OnClickListener() {
        public void onClick(View v) {
            day.ville=tb_ville.getText().toString();
            daoJour.modifier(day, options.online);
            tb_ville.setTextColor(Color.parseColor("#ac035d"));

        }
    };


    //SORT - Date
    View.OnClickListener onSortDate = new View.OnClickListener() {
        public void onClick(View v) {
            btn_sort_date.setImageResource(R.drawable.ic_jour_sort_date_fill);
            btn_sort_finished.setImageResource(R.drawable.ic_jour_sort_finished);
            btn_sort_name.setImageResource(R.drawable.ic_jour_sort_name);
            btn_sort_cat.setImageResource(R.drawable.ic_jour_sort_cat);
            trierList("Date");
        }
    };

    //SORT - Finished
    View.OnClickListener onSortFinished = new View.OnClickListener() {
        public void onClick(View v) {
            btn_sort_date.setImageResource(R.drawable.ic_jour_sort_date);
            btn_sort_finished.setImageResource(R.drawable.ic_jour_sort_finished_fill);
            btn_sort_name.setImageResource(R.drawable.ic_jour_sort_name);
            btn_sort_cat.setImageResource(R.drawable.ic_jour_sort_cat);
            trierList("Finished");
        }
    };

    //SORT - Name
    View.OnClickListener onSortName = new View.OnClickListener() {
        public void onClick(View v) {
            btn_sort_date.setImageResource(R.drawable.ic_jour_sort_date);
            btn_sort_finished.setImageResource(R.drawable.ic_jour_sort_finished);
            btn_sort_name.setImageResource(R.drawable.ic_jour_sort_name_fill);
            btn_sort_cat.setImageResource(R.drawable.ic_jour_sort_cat);
            trierList("Name");
        }
    };

    //SORT - CAT
    View.OnClickListener onSortCat = new View.OnClickListener() {
        public void onClick(View v) {
            btn_sort_date.setImageResource(R.drawable.ic_jour_sort_date);
            btn_sort_finished.setImageResource(R.drawable.ic_jour_sort_finished);
            btn_sort_name.setImageResource(R.drawable.ic_jour_sort_name);
            btn_sort_cat.setImageResource(R.drawable.ic_jour_sort_cat_fill);
            trierList("Cat");
        }
    };





//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------

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

        //ADD SUJET
        if (id == R.id.menu_addSujet)
        {
            Intent intent = new Intent(A_jour_Preparation.this, A_sujet_new.class);
            System.out.println("Appel de new sujet. jour id = " + day.nomJour);

            //intent.putExtra("idEntry", day.nomJour);
           // intent.putExtra("userID", userID);
            System.out.println(">>intent :user : " + options.userid);
            System.out.println(">>intent :jour : " + options.jourid);
            startActivity(intent);
        }


        //DISPLAY MAP
        if(id==R.id.menu_jour_map)
        {
            Intent intent = new Intent(A_jour_Preparation.this, A_jour_Map_view.class);
            startActivity(intent);
        }

        //DISPLAY VIEW
        if(id==R.id.menu_jour_view)
        {
            Intent intent = new Intent(A_jour_Preparation.this, A_jour_Vue.class);
            startActivity(intent);
        }


        //REFRESH
        if(id==R.id.menu_jour_refresh)
        {
            if(options.online)
            {
                MajDAO();
            }
        }


        //DECO
        if(id==R.id.menu_jour_deconnexion)
        {
            //daoOptions.supprimerUser();
            daoOptions.supprimer();
            Intent intent = new Intent(A_jour_Preparation.this, MainActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
