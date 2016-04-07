package com.mit.mit;

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
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class A_sujet_Work extends MainActivity
{


    //objets de la page
    private TextView lb_description;
    private ImageButton btn_type;
    private TextView lb_date;
    private TextView lb_duree;
    private TextView lb_montant;
    private ImageButton btn_map;
    private ImageButton btn_dep;
    private LinearLayout ll_parts;

    //android:id="@+id/sujetPreparation_lb_description"
    //sujetPreparation_btn_type





    //variables
    private C_Options options;
    private C_Participant part;
    private C_Projet projet;
    private C_Jour jour;
    private C_Sujet sujet;


    private SimpleDateFormat sdf;

    private String nomJour;








    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



        //initialisation objet page
        setContentView(R.layout.activity_a_sujet__work);
        lb_description = (TextView) findViewById(R.id.sujetWork_lb_description);
        btn_type = (ImageButton) findViewById(R.id.sujetWork_btn_type);
        lb_date = (TextView) findViewById(R.id.sujetWork_lb_date);
        lb_duree = (TextView) findViewById(R.id.sujetWork_lb_duree);
        lb_montant = (TextView) findViewById(R.id.sujetWork_lb_cout);
        btn_map = (ImageButton) findViewById(R.id.sujetWork_btn_map);
        ll_parts=(LinearLayout) findViewById(R.id.sujetWork_ll_particiants);
        btn_dep = (ImageButton) findViewById(R.id.sujetWork_btn_depenses);


        //listeners
        btn_map.setOnClickListener(onMapClick);
        btn_dep.setOnClickListener(onbtnDepenses);


        //initialisation variables
        sdf = new SimpleDateFormat("HH:MM");

        this.options=daoOptions.getOptionByUserId();

        this.part=daoparticipant.getParticipantById(options.userid);


        this.projet = daoProjet.getProjetByName(options.projetid);
        this.projet.creerLesListes(daoJour, daoparticipant);

        this.sujet=daoSujet.getSujetById(options.sujetid);
        this.sujet.creerLesListes(daoMessage, daoparticipant);

        this.nomJour =projet.nom+"_"+this.sujet.idSujet.split("_")[1];
        this.jour=daoJour.getJourById(options.jourid);
        this.jour.creerLesListes(daoSujet);



        //affichage
        majInterface();

    }







//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //bouton creer
    View.OnClickListener onMapClick = new View.OnClickListener() {
        public void onClick(View v) {


            Intent intent = new Intent(A_sujet_Work.this, A_sujet_Map_view.class);
            startActivity(intent);

        }
    };


    //bouton creer
    View.OnClickListener onbtnDepenses = new View.OnClickListener() {
        public void onClick(View v) {


            Intent intent = new Intent(A_sujet_Work.this, A_sujet_AddDepense.class);
            startActivity(intent);

        }
    };















//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    //MAJ de l'interface avec les données
    public void majInterface()
    {
        //Titre
        setTitle("[" + this.sujet.type + "] " + this.sujet.titre);

        //champs
        lb_description.setText(this.sujet.description);
        lb_date.setText(sdf.format(this.sujet.heure));
        lb_montant.setText(this.sujet.prix+" €");


        //heure
        int hour = this.sujet.duree/60;
        int min = this.sujet.duree-hour*60;
        lb_duree.setText(hour+"h"+min);


        //type
        switch (this.sujet.type) {
            case "Transport":
                btn_type.setImageResource(R.drawable.ic_jour_transports);
                lb_description.setTextColor(Color.parseColor("#e74c3c"));
                break;

            case "Repas":
                btn_type.setImageResource(R.drawable.ic_jour_repas);
                lb_description.setTextColor(Color.parseColor("#2980b9"));
                break;

            case "Visite":
                btn_type.setImageResource(R.drawable.ic_jour_visite);
                lb_description.setTextColor(Color.parseColor("#16a085"));
                break;

            case "Logement":
                btn_type.setImageResource(R.drawable.ic_jour_logement);
                lb_description.setTextColor(Color.parseColor("#f39c12"));
                break;

            case "loisir":
                btn_type.setImageResource(R.drawable.ic_jour_loisir);
                lb_description.setTextColor(Color.parseColor("#9b59b6"));
                break;

            case "Libre":
                btn_type.setImageResource(R.drawable.ic_jour_libre);
                lb_description.setTextColor(Color.parseColor("#5b5b5b"));
                break;
        }


        //afficher layout participants
        ll_parts.removeAllViews();

            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            for (C_Participant p : sujet.liste_participent)
            {
                boolean Apaye=false;
                double montant=0;
                int nbPos=0;
                int parcours=0;

                for (C_Participant part : sujet.liste_quiApaye)
                {

                        if(p.mail.equals(part.mail))
                        {
                            nbPos=parcours;
                            Apaye=true;
                        }
                        else {parcours++; }
                }

                if(Apaye)
                {
                    parcours=0;
                    for(Double mt :sujet.liste_combienApaye)
                    {
                        if(parcours==nbPos)
                        {
                            montant=mt;
                        }
                        parcours++;
                    }
                }








                //layout
                LinearLayout LLpart = new LinearLayout(this);
                LLpart.setOrientation(LinearLayout.HORIZONTAL);
                LLpart.setLayoutParams(LLParams);

                //image
                ImageButton img = new ImageButton(this);
                img.setImageResource(R.drawable.ic_sujet_part);
                if(Apaye) {img.setImageResource(R.drawable.ic_sujet_part_fill);}
                img.setBackgroundColor(Color.TRANSPARENT);

                //titre sujet (bas)
                TextView nomPart = new TextView(this);
                nomPart.setTextColor(Color.parseColor("#ac035d"));
                nomPart.setPadding(0, 0, 10, 0);
                if (Apaye)
                {
                    nomPart.setText(p.prenom+" a avancé "+montant+" €");
                }
                else
                {
                    nomPart.setText(p.prenom +" n'a pas avancé de sous.");
                }



                LLpart.addView(img);
                LLpart.addView(nomPart);

                ll_parts.addView(LLpart);
            }
        }




















//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_sujet__work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
