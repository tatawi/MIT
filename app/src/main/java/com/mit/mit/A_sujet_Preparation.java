package com.mit.mit;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class A_sujet_Preparation extends MainActivity {


    //objets de la page
    private TextView lb_description;
    private ImageButton btn_type;
    private TextView lb_date;
    private TextView lb_duree;
    private TextView lb_montant;
    private TextView lb_nbMessages;
    private TextView lb_nbNonLus;
    private ImageButton btn_conv;
    private SeekBar progressBar_participations;
    private ImageButton btn_participer;
    private TextView lb_nbParticipants;
    private TextView lb_nbParticipantsTotal;
    private TextView lb_erreurAdd;
    private ImageButton btn_map;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        //initialisation objet page
        setContentView(R.layout.activity_a_sujet__preparation);
        lb_description = (TextView) findViewById(R.id.sujetPreparation_lb_description);
        btn_type = (ImageButton) findViewById(R.id.sujetPreparation_btn_type);
        lb_date = (TextView) findViewById(R.id.sujetPreparation_lb_date);
        lb_duree = (TextView) findViewById(R.id.sujetPreparation_lb_duree);
        lb_montant = (TextView) findViewById(R.id.sujetPreparation_lb_cout);
        lb_nbMessages = (TextView) findViewById(R.id.sujetPreparation_lb_nbMessages);
        lb_nbNonLus = (TextView) findViewById(R.id.sujetPreparation_lb_nbNonLus);
        btn_conv = (ImageButton) findViewById(R.id.sujetPreparation_btn_conv);
        progressBar_participations = (SeekBar) findViewById(R.id.sujetPreparation_progressBar);
        btn_participer = (ImageButton) findViewById(R.id.sujetPreparation_btn_participer);
        lb_nbParticipants = (TextView) findViewById(R.id.sujetPreparation_lb_nbParticipants);
        lb_nbParticipantsTotal= (TextView) findViewById(R.id.sujetPreparation_lb_nbParticipantsTotal);
        lb_erreurAdd= (TextView) findViewById(R.id.sujetPreparation_lb_texteAjoutPart);
        btn_map = (ImageButton) findViewById(R.id.sujetPreparation_btn_map);

        //listeners
        btn_conv.setOnClickListener(onOpenConv);
        btn_participer.setOnClickListener(onParticiper);
        btn_map.setOnClickListener(onMapClick);



        //initialisation variables
        sdf = new SimpleDateFormat("HH:MM");
        lb_erreurAdd.setVisibility(View.GONE);

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

        //récupération du sujet
       /* Bundle extras = getIntent().getExtras();
        if (extras != null)
        {

            System.out.println("SUJET PREPARATION");

            //chargement user
            this.userID=extras.getString("userID");
            part=daoparticipant.getParticipantById(userID);
            System.out.println(">get user : ");
            System.out.println(part.mail);

            //chargement des données du sujet
            this.sujet = daoSujet.getSujetById(extras.getString("idEntry"));
            this.sujet.creerLesListes(daoMessage, daoparticipant);
            System.out.println(">Sujet chargé : " + sujet.titre);
            System.out.println(">id : "+ sujet.idSujet);

            //recupération projet courant
            nomProjet =this.sujet.idSujet.split("_")[0];
            System.out.println(">get projet from " +nomProjet+" : ");
            projet = daoProjet.getProjetByName(nomProjet);
            projet.creerLesListes(daoJour, daoparticipant);

            System.out.println(projet.nom);

            //récupération du jour courant
            nomJour =nomProjet+"_"+this.sujet.idSujet.split("_")[1];
            System.out.println(">get jour from " +nomJour+" : ");
            jour = daoJour.getJourById(nomJour);
            jour.creerLesListes(daoSujet);
            System.out.println(jour.id);

            //affichage
            majInterface();
        }*/

    }










//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //bouton creer
    View.OnClickListener onOpenConv = new View.OnClickListener() {
        public void onClick(View v) {


            Intent intent = new Intent(A_sujet_Preparation.this, A_conversation.class);

            //intent.putExtra("idEntry", sujet.idSujet);
            //intent.putExtra("userID", userID);
            startActivity(intent);

        }
    };

    //bouton creer
    View.OnClickListener onMapClick = new View.OnClickListener() {
        public void onClick(View v) {


            Intent intent = new Intent(A_sujet_Preparation.this, A_sujet_Map_view.class);
            startActivity(intent);

        }
    };


    //bouton participer
    View.OnClickListener onParticiper = new View.OnClickListener() {
        public void onClick(View v)
        {

            System.out.println("user : " + part.mail);
            System.out.println("liste ayant acceptés : " + sujet.personnesAyantAccepte.size());
            for (C_Participant p : sujet.personnesAyantAccepte)
            {
                System.out.println("    user : " + p.mail);
                if(p.mail.equals(part.mail))
                {
                    System.out.println("A VOTE");
                }
            }

            //si l'user na pas encore voté
            if(pasEncoreVote(sujet.personnesAyantAccepte, part))
            {
                System.out.println("non présent dans la liste");
                sujet.personnesAyantAccepte.add(part);
                sujet.listeToString();
                daoSujet.modifier(sujet, options.online);

                progressBar_participations.setProgress(sujet.personnesAyantAccepte.size());
                lb_nbParticipants.setText("" + sujet.personnesAyantAccepte.size());

                //vérifier si on passe le sujet en valide
                int nbTotal=projet.liste_participants.size();
                int nbParticipants=sujet.personnesAyantAccepte.size();

                if(nbParticipants/nbTotal>0.5)
                {
                    //sauvegarde du sujet
                    sujet.valide=true;
                    daoSujet.modifier(sujet, options.online);

                    //sauvegarde du sujet dans jour
                    jour = daoJour.getJourById(nomJour);
                    jour.creerLesListes(daoSujet);

                    //mettre a jour le jour avec le prix
                    jour.calculerPrixJournee(projet.liste_participants.size());
                    daoJour.modifier(jour, options.online);

                    //mettre a jour le projet avec le prix
                    projet = daoProjet.getProjetByName(projet.nom);
                    projet.creerLesListes(daoJour, daoparticipant);
                    projet.calculerPrixSejour();
                    daoProjet.modifier(projet, options.online);

                }
            }
            else
            {
                lb_erreurAdd.setVisibility(View.VISIBLE);
                lb_erreurAdd.setText("Vote déjà enregistré");
            }

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

    //messages
    if(sujet.liste_messages.size()>1)
    {
        lb_nbMessages.setText(""+sujet.liste_messages.size()+" messages");
    }

    //notifications messages
    int nouveauMessages = this.nbMessagesNonVus();

    if(nouveauMessages>0)
    {
        lb_nbNonLus.setText(""+nouveauMessages+" nouveaux messages");
    }

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


    //participants
    progressBar_participations.setMax(projet.liste_participants.size());
    progressBar_participations.setProgress(sujet.personnesAyantAccepte.size());
    //progressBar_participations.setEnabled(false);

    lb_nbParticipants.setText(""+sujet.personnesAyantAccepte.size());
    lb_nbParticipantsTotal.setText(""+projet.liste_participants.size());
}


    private boolean pasEncoreVote(List<C_Participant> personnesAyantVote, C_Participant user)
    {
        for (C_Participant p : personnesAyantVote)
        {
            if(p.mail.equals(user.mail))
            {
                return false;
            }
        }
        return true;
    }


    private int nbMessagesNonVus()
    {
        int nbMessages =0;

        for (C_Message m:this.sujet.liste_messages)
        {
            m.creerLesListes(daoparticipant);
            System.out.println("/-/-/ messages : vues : " + m.personnesAyantVuesToString);
            System.out.println("/-/-/ messages : nbvuesliste : "+m.liste_personnesAyantVues.size());
            if (!m.aiJeVu(part))
            {
                nbMessages++;
            }
        }
        return nbMessages;
    }




//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_sujet__preparation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //CONVERSATION
        if (id == R.id.sujet_conv) {

            Intent intent = new Intent(A_sujet_Preparation.this, A_conversation.class);
            //intent.putExtra("idEntry", sujet.idSujet);
            //intent.putExtra("userID", userID);
            startActivity(intent);

            return true;
        }

        //RETOUR HOME
        if(id==R.id.sujet_home){
            Intent intent = new Intent(A_sujet_Preparation.this, A_projets.class);
            //intent.putExtra("userID", part.mail);
            startActivity(intent);
        }

        //EDIT
        if(id==R.id.menu_sujet_edit){
            Intent intent = new Intent(A_sujet_Preparation.this, A_sujet_new.class);
            intent.putExtra("edit", "true");
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
