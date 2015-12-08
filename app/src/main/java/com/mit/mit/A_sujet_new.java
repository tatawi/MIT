package com.mit.mit;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
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
    private TextView lb_prix;
    private ImageButton btn_creer;
    private TextView lb_time;
    private TextView lb_duree;
    private SeekBar sb_hour;
    private SeekBar sb_min;
    private SeekBar sb_duree;
    private Button btn_0;
    private Button btn_1;
    private Button btn_5;
    private Button btn_10;
    private Button btn_50;
    private Button btn_100;
    private Switch sw_plus;
    private Switch sw_moins;


    //variables
    private C_Jour day;
    private C_Sujet sujet;
    private C_Participant partAcutel;
    private SimpleDateFormat sdf;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_sujet_new);

        System.out.println("**************************************************************");
        System.out.println("**A_sujet_New : Création d'un nouveau sujet");

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
        btn_creer = (ImageButton) findViewById(R.id.newsujet_btn_addSujet);
        lb_time = (TextView) findViewById(R.id.newsujet_lb_startTime);
        lb_duree = (TextView) findViewById(R.id.newsujet_lb_duration);
        lb_prix= (TextView) findViewById(R.id.newsujet_lb_prix);
        lb_prix.setText("0 €");
        sb_hour=(SeekBar)findViewById(R.id.newsujet_seekBar_hour);
        sb_min=(SeekBar)findViewById(R.id.newsujet_seekBar_min);
        sb_duree=(SeekBar)findViewById(R.id.newsujet_seekBar_duree);
        btn_0=(Button)findViewById(R.id.newsujet_btn_0);
        btn_1=(Button)findViewById(R.id.newsujet_btn_1);
        btn_5=(Button)findViewById(R.id.newsujet_btn_5);
        btn_10=(Button)findViewById(R.id.newsujet_btn_10);
        btn_50=(Button)findViewById(R.id.newsujet_btn_50);
        btn_100=(Button)findViewById(R.id.newsujet_btn_100);
        sw_plus=(Switch)findViewById(R.id.newsujet_switch_plus);
        sw_plus.setChecked(true);
        sw_moins=(Switch)findViewById(R.id.newsujet_switch_moins);
        sw_moins.setChecked(false);
        sb_hour.setProgress(12);
        sb_min.setProgress(0);
        lb_time.setText("12h00");
        lb_time.setText("00h00");


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
        btn_0.setOnClickListener(onClickButtonPrix);
        btn_1.setOnClickListener(onClickButtonPrix);
        btn_5.setOnClickListener(onClickButtonPrix);
        btn_10.setOnClickListener(onClickButtonPrix);
        btn_50.setOnClickListener(onClickButtonPrix);
        btn_100.setOnClickListener(onClickButtonPrix);


        //variables
        sujet =new C_Sujet();
        sujet.prix=0;
        //récupération du jour
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            //récupération utilisateur
            this.userID = extras.getString("userID");
            partAcutel=daoparticipant.getParticipantById(this.userID);

            //récupération jour
            this.day = daoJour.getJourById(extras.getString("idEntry"));
            this.day.creerLesListes(daoSujet);


            System.out.println("--user : " + userID);
            System.out.println("--jour : "+this.day.nomJour);


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

            //ini valeurs base
            sujet.titre="pas de titre";

            //ini valeurs page
            sujet.idSujet=day.nomJour+"_"+tb_titre.getText().toString();
            sujet.titre=tb_titre.getText().toString();
            sujet.description=tb_description.getText().toString();
            sujet.type=lb_type.getText().toString();
            sujet.localisation="loc";
            sujet.heure=day.jour;
            sujet.heure.setHours(sb_hour.getProgress());
            sujet.heure.setMinutes(sb_min.getProgress());
            sujet.duree=sb_duree.getProgress();
            sujet.auFeeling=false;
            sujet.valide=false;
            sujet.personnesAyantAccepte.add(partAcutel);




            //création du premier message auto
            C_Message message = new C_Message(sujet.idSujet, new Date(), "Création du sujet", partAcutel);
            daoMessage.ajouter(message, true);
            sujet.liste_messages.add(message);



            //save sujet
            daoSujet.ajouter(sujet, true);

            //maj jour
            day.liste_sujets.add(sujet);
            day.listeToString();
            daoJour.modifier(day);









            //retour activité
            Intent intent = new Intent(A_sujet_new.this, A_jour_Preparation.class);
            intent.putExtra("idEntry", day.nomJour);
            intent.putExtra("userID", userID);

            System.out.println(">>intent : user : " + userID);
            System.out.println(">>intent : jour : "+day.nomJour);

            startActivity(intent);

        }
    };

    //bouton logement
    View.OnClickListener onClickButtonLogement = new View.OnClickListener() {
        public void onClick(View v) {
            btn_logement.setImageResource(R.drawable.ic_jour_logement_fill);
            btn_repas.setImageResource(R.drawable.ic_jour_repas);
            btn_transport.setImageResource(R.drawable.ic_jour_transports);
            btn_visite.setImageResource(R.drawable.ic_jour_visite);
            btn_loisir.setImageResource(R.drawable.ic_jour_loisir);
            btn_libre.setImageResource(R.drawable.ic_jour_libre);
            lb_type.setText("Logement");
            lb_type.setTextColor(Color.parseColor("#f39c12"));
        }
    };

    //bouton repas
    View.OnClickListener onClickButtonRepas = new View.OnClickListener() {
        public void onClick(View v) {
            btn_logement.setImageResource(R.drawable.ic_jour_logement);
            btn_repas.setImageResource(R.drawable.ic_jour_repas_fill);
            btn_transport.setImageResource(R.drawable.ic_jour_transports);
            btn_visite.setImageResource(R.drawable.ic_jour_visite);
            btn_loisir.setImageResource(R.drawable.ic_jour_loisir);
            btn_libre.setImageResource(R.drawable.ic_jour_libre);
            lb_type.setText("Repas");
            lb_type.setTextColor(Color.parseColor("#2980b9"));
        }
    };

    //bouton transport
    View.OnClickListener onClickButtonTransport = new View.OnClickListener() {
        public void onClick(View v) {
            btn_logement.setImageResource(R.drawable.ic_jour_logement);
            btn_repas.setImageResource(R.drawable.ic_jour_repas);
            btn_transport.setImageResource(R.drawable.ic_jour_transports_fill);
            btn_visite.setImageResource(R.drawable.ic_jour_visite);
            btn_loisir.setImageResource(R.drawable.ic_jour_loisir);
            btn_libre.setImageResource(R.drawable.ic_jour_libre);
            lb_type.setText("Transport");
            lb_type.setTextColor(Color.parseColor("#e74c3c"));
        }
    };

    //bouton visite
    View.OnClickListener onClickButtonVisite = new View.OnClickListener() {
        public void onClick(View v) {
            btn_logement.setImageResource(R.drawable.ic_jour_logement);
            btn_repas.setImageResource(R.drawable.ic_jour_repas);
            btn_transport.setImageResource(R.drawable.ic_jour_transports);
            btn_visite.setImageResource(R.drawable.ic_jour_visite_fill);
            btn_loisir.setImageResource(R.drawable.ic_jour_loisir);
            btn_libre.setImageResource(R.drawable.ic_jour_libre);
            lb_type.setText("Visite");
            lb_type.setTextColor(Color.parseColor("#16a085"));
        }
    };

    //bouton loisirs
    View.OnClickListener onClickButtonLoisir= new View.OnClickListener() {
        public void onClick(View v) {
            btn_logement.setImageResource(R.drawable.ic_jour_logement);
            btn_repas.setImageResource(R.drawable.ic_jour_repas);
            btn_transport.setImageResource(R.drawable.ic_jour_transports);
            btn_visite.setImageResource(R.drawable.ic_jour_visite);
            btn_loisir.setImageResource(R.drawable.ic_jour_loisir_fill);
            btn_libre.setImageResource(R.drawable.ic_jour_libre);
            lb_type.setText("Loisir");
            lb_type.setTextColor(Color.parseColor("#9b59b6"));
        }
    };

    //bouton libre
    View.OnClickListener onClickButtonLibre = new View.OnClickListener() {
        public void onClick(View v) {
            btn_logement.setImageResource(R.drawable.ic_jour_logement);
            btn_repas.setImageResource(R.drawable.ic_jour_repas);
            btn_transport.setImageResource(R.drawable.ic_jour_transports);
            btn_visite.setImageResource(R.drawable.ic_jour_visite);
            btn_loisir.setImageResource(R.drawable.ic_jour_loisir);
            btn_libre.setImageResource(R.drawable.ic_jour_libre_fill);
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


    //toggle buttons
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView.getText().toString().equals("Plus"))
        {
            if (isChecked) {
                sw_moins.setChecked(false);
            }
        }
        if(buttonView.getText().toString().equals("Moins"))
        {
            if (isChecked) {
                sw_plus.setChecked(false);
            }
        }
    }


    //boutons prix
    View.OnClickListener onClickButtonPrix = new View.OnClickListener() {
        public void onClick(View v) {
            Button b = (Button) v;

            System.out.println("btn : "+ b.getText().toString());

            if(b.getText().toString().equals("0")) {
                sujet.prix=0;
            }

            if(b.getText().toString().equals("1")) {
                if(sw_plus.isChecked()) {
                    sujet.prix=sujet.prix+1;
                }
                if(sw_moins.isChecked()) {
                    sujet.prix=sujet.prix-1;
                    if (sujet.prix<0){
                        sujet.prix=0;
                    }
                }
            }


            if(b.getText().toString().equals("5")) {
                if(sw_plus.isChecked()) {
                    sujet.prix=sujet.prix+5;
                }
                if(sw_moins.isChecked()) {
                    sujet.prix=sujet.prix-5;
                    if (sujet.prix<0){
                        sujet.prix=0;
                    }
                }
            }
            if(b.getText().toString().equals("10")) {
                if(sw_plus.isChecked()) {
                    sujet.prix=sujet.prix+10;
                }
                if(sw_moins.isChecked()) {
                    sujet.prix=sujet.prix-10;
                    if (sujet.prix<0){
                        sujet.prix=0;
                    }
                }
            }
            if(b.getText().toString().equals("50")) {
                if(sw_plus.isChecked()) {
                    sujet.prix=sujet.prix+50;
                }
                if(sw_moins.isChecked()) {
                    sujet.prix=sujet.prix-50;
                    if (sujet.prix<0){
                        sujet.prix=0;
                    }
                }
            }
            if(b.getText().toString().equals("100")) {
                if(sw_plus.isChecked()) {
                    sujet.prix=sujet.prix+100;
                }
                if(sw_moins.isChecked()) {
                    sujet.prix=sujet.prix-100;
                    if (sujet.prix<0){
                        sujet.prix=0;
                    }
                }
            }
            lb_prix.setText(""+sujet.prix+" €");
        }
    };




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
