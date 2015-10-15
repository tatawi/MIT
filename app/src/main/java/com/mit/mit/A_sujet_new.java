package com.mit.mit;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class A_sujet_new extends MainActivity {


    //objets de la page
    private EditText tb_titre;
    private EditText tb_description;
    private TextView lb_type;
    private ImageButton btn_logement;
    private ImageButton btn_repas;
    private ImageButton btn_transport;
    private ImageButton btn_visite;
    private ImageButton btn_loisir;
    private ImageButton btn_libre;
    private TimePicker tp_heureDebut;
    private EditText tb_duree;
    private ImageButton btn_creer;
    private TextView lb_time;
    private TextView lb_duree;
    private SeekBar sb_hour;
    private SeekBar sb_min;
    private SeekBar sb_duree;



    //variables
    private C_Jour day;
    private C_Sujet sujet;
    private SimpleDateFormat sdf;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_sujet_new);



        //initialisation objet page
        tb_titre = (EditText) findViewById(R.id.newsujet_tb_sujet);
        tb_description = (EditText) findViewById(R.id.newsujet_tb_description);
        lb_type = (TextView) findViewById(R.id.newsujet_lb_type);
        btn_logement = (ImageButton) findViewById(R.id.newsujet_btn_logement);
        btn_repas = (ImageButton) findViewById(R.id.newsujet_btn_repas);
        btn_transport = (ImageButton) findViewById(R.id.newsujet_btn_transport);
        btn_visite = (ImageButton) findViewById(R.id.newsujet_btn_visite);
        btn_loisir = (ImageButton) findViewById(R.id.newsujet_btn_loisir);
        btn_libre = (ImageButton) findViewById(R.id.newsujet_btn_libre);
       // tp_heureDebut = (TimePicker) findViewById(R.id.newsujet_timePicker);
       // tb_duree = (EditText) findViewById(R.id.newsujet_tb_duration);
        btn_creer = (ImageButton) findViewById(R.id.newsujet_btn_addSujet);
        lb_time = (TextView) findViewById(R.id.newsujet_lb_startTime);
        lb_duree = (TextView) findViewById(R.id.newsujet_lb_duration);
        sb_hour=(SeekBar)findViewById(R.id.newsujet_seekBar_hour);
        sb_min=(SeekBar)findViewById(R.id.newsujet_seekBar_min);
        sb_duree=(SeekBar)findViewById(R.id.newsujet_seekBar_duree);







        //listeners
        btn_logement.setOnClickListener(onClickButtonLogement);
        btn_repas.setOnClickListener(onClickButtonRepas);
        btn_transport.setOnClickListener(onClickButtonTransport);
        btn_visite.setOnClickListener(onClickButtonVisite);
        btn_loisir.setOnClickListener(onClickButtonLoisir);
        btn_libre.setOnClickListener(onClickButtonLibre);
        btn_creer.setOnClickListener(onClickButtonValider);
        sb_hour.setOnSeekBarChangeListener(new seekBarListener());
        sb_min.setOnSeekBarChangeListener(new seekBarListener());
        sb_duree.setOnSeekBarChangeListener(new seekBarListener());

        //récupération du jour
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            this.userID = extras.getString("userID");
            //chargement des données du projet
            this.day = daoJour.getJourById(extras.getString("idEntry"));
            this.day.creerLesListes(daoSujet);



        }








    }



//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //CREER SUJET
    View.OnClickListener onClickButtonValider = new View.OnClickListener() {
        public void onClick(View v) {
            int hour;
            int min;
            //sdf = new SimpleDateFormat("EEE d MMM");

            sujet =new C_Sujet();
            //public C_Sujet( String titre, String description, String type, String localisation, Date heure, int duree, boolean auFeeling, double prix)
            sujet.titre=tb_titre.getText().toString();
            sujet.description=tb_description.getText().toString();
            sujet.type=lb_type.getText().toString();
            sujet.localisation="loc";
            sujet.heure=day.jour;
            sujet.heure.setHours(sb_hour.getProgress());
            sujet.heure.setMinutes(sb_min.getProgress());
            sujet.duree=sb_duree.getProgress();
            sujet.auFeeling=false;
            sujet.prix=10;
            sujet.idSujet=day.nomJour+"_"+tb_titre.getText().toString();

            System.out.println("SUJET AVANT AJOUT*****************************");
            System.out.println("titre : " + sujet.titre);


            //save sujet
            daoSujet.ajouter(sujet);

            //maj jour
            day.liste_sujets.add(sujet);
            day.listeToString();
            daoJour.modifier(day);

            //retour activité
            Intent intent = new Intent(A_sujet_new.this, A_jour_Preparation.class);
            intent.putExtra("idEntry", day.nomJour);
            intent.putExtra("userID", userID);
            startActivity(intent);

        }
    };

    //bouton ajouter date debut
    View.OnClickListener onClickButtonLogement = new View.OnClickListener() {
        public void onClick(View v) {
            btn_logement.setBackgroundColor(Color.parseColor("#d2d2d2"));
            btn_repas.setBackgroundColor(Color.TRANSPARENT);
            btn_transport.setBackgroundColor(Color.TRANSPARENT);
            btn_visite.setBackgroundColor(Color.TRANSPARENT);
            btn_loisir.setBackgroundColor(Color.TRANSPARENT);
            btn_libre.setBackgroundColor(Color.TRANSPARENT);
            lb_type.setText("Logement");
            lb_type.setTextColor(Color.parseColor("#f39c12"));
        }
    };

    //bouton ajouter date debut
    View.OnClickListener onClickButtonRepas = new View.OnClickListener() {
        public void onClick(View v) {
            btn_repas.setBackgroundColor(Color.parseColor("#d2d2d2"));
            btn_logement.setBackgroundColor(Color.TRANSPARENT);
            btn_transport.setBackgroundColor(Color.TRANSPARENT);
            btn_visite.setBackgroundColor(Color.TRANSPARENT);
            btn_loisir.setBackgroundColor(Color.TRANSPARENT);
            btn_libre.setBackgroundColor(Color.TRANSPARENT);
            lb_type.setText("Repas");
            lb_type.setTextColor(Color.parseColor("#2980b9"));
        }
    };

    //bouton ajouter date debut
    View.OnClickListener onClickButtonTransport = new View.OnClickListener() {
        public void onClick(View v) {
            btn_transport.setBackgroundColor(Color.parseColor("#d2d2d2"));
            btn_repas.setBackgroundColor(Color.TRANSPARENT);
            btn_logement.setBackgroundColor(Color.TRANSPARENT);
            btn_visite.setBackgroundColor(Color.TRANSPARENT);
            btn_loisir.setBackgroundColor(Color.TRANSPARENT);
            btn_libre.setBackgroundColor(Color.TRANSPARENT);
            lb_type.setText("Transport");
            lb_type.setTextColor(Color.parseColor("#e74c3c"));
        }
    };

    //bouton ajouter date debut
    View.OnClickListener onClickButtonVisite = new View.OnClickListener() {
        public void onClick(View v) {
            btn_visite.setBackgroundColor(Color.parseColor("#d2d2d2"));
            btn_repas.setBackgroundColor(Color.TRANSPARENT);
            btn_transport.setBackgroundColor(Color.TRANSPARENT);
            btn_logement.setBackgroundColor(Color.TRANSPARENT);
            btn_loisir.setBackgroundColor(Color.TRANSPARENT);
            btn_libre.setBackgroundColor(Color.TRANSPARENT);
            lb_type.setText("Visite");
            lb_type.setTextColor(Color.parseColor("#16a085"));
        }
    };

    //bouton ajouter date debut
    View.OnClickListener onClickButtonLoisir= new View.OnClickListener() {
        public void onClick(View v) {
            btn_loisir.setBackgroundColor(Color.parseColor("#d2d2d2"));
            btn_repas.setBackgroundColor(Color.TRANSPARENT);
            btn_transport.setBackgroundColor(Color.TRANSPARENT);
            btn_visite.setBackgroundColor(Color.TRANSPARENT);
            btn_logement.setBackgroundColor(Color.TRANSPARENT);
            btn_libre.setBackgroundColor(Color.TRANSPARENT);
            lb_type.setText("Loisir");
            lb_type.setTextColor(Color.parseColor("#9b59b6"));
        }
    };

    //bouton ajouter date debut
    View.OnClickListener onClickButtonLibre = new View.OnClickListener() {
        public void onClick(View v) {
            btn_libre.setBackgroundColor(Color.parseColor("#d2d2d2"));
            btn_repas.setBackgroundColor(Color.TRANSPARENT);
            btn_transport.setBackgroundColor(Color.TRANSPARENT);
            btn_visite.setBackgroundColor(Color.TRANSPARENT);
            btn_loisir.setBackgroundColor(Color.TRANSPARENT);
            btn_logement.setBackgroundColor(Color.TRANSPARENT);
            lb_type.setText("Libre");
            lb_type.setTextColor(Color.parseColor("#5b5b5b"));
        }
    };



    //SEEKBAR
    private class seekBarListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            int hour = sb_hour.getProgress();
            int min = sb_min.getProgress();
            int duree = sb_duree.getProgress();

            lb_time.setText(hour+"h"+min);

            hour=duree/60;
            min=duree-hour*60;
            lb_duree.setText(hour+"h"+min);

        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

    }



//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------
















//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_sujet_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
