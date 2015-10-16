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
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    private ProgressBar progressBar_participations;
    private ImageButton btn_participer;


    //android:id="@+id/sujetPreparation_lb_description"
    //sujetPreparation_btn_type





    //variables
    private C_Sujet sujet;
    private SimpleDateFormat sdf;
    private String userID;
    private C_Projet projet;
    private C_Participant part;





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
        progressBar_participations = (ProgressBar) findViewById(R.id.sujetPreparation_progressBar);
        btn_participer = (ImageButton) findViewById(R.id.sujetPreparation_btn_participer);

        //listeners
        btn_conv.setOnClickListener(onOpenConv);
        btn_participer.setOnClickListener(onParticiper);




        //initialisation variables
        sdf = new SimpleDateFormat("HH:MM");



        //récupération du sujet
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            //chargement user
            this.userID=extras.getString("userID");
            part=daoparticipant.getParticipantById(userID);

            //chargement des données du sujet
            this.sujet = daoSujet.getSujetById(extras.getString("idEntry"));
            this.sujet.creerLesListes(daoMessage, daoparticipant);


            //recupération projet courant
            String nomProjet =this.sujet.idSujet.split("_")[0];
            projet = daoProjet.getProjetByName(nomProjet);
            projet.creerLesListes(daoJour, daoparticipant);

            //affichage
            majInterface();
        }

    }










//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //bouton creer
    View.OnClickListener onOpenConv = new View.OnClickListener() {
        public void onClick(View v) {


            Intent intent = new Intent(A_sujet_Preparation.this, A_conversation.class);
            intent.putExtra("idEntry", sujet.idSujet);
            intent.putExtra("userID", userID);
            startActivity(intent);

        }
    };


    //bouton participer
    View.OnClickListener onParticiper = new View.OnClickListener() {
        public void onClick(View v)
        {
            sujet.personnesAyantAccepte.add(part);
            sujet.listeToString();
            daoSujet.modifier(sujet);
            progressBar_participations.incrementProgressBy(1);

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
    lb_nbMessages.setText("Aucun messages");
    lb_nbNonLus.setText("Pas de nouveaux messages");

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.sujet_conv) {

            Intent intent = new Intent(A_sujet_Preparation.this, A_conversation.class);
            intent.putExtra("idEntry", sujet.idSujet);
            intent.putExtra("userID", userID);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
