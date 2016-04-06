package com.mit.mit;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class A_sujet_new extends MainActivity {


    //objets de la page
    private LinearLayout ll_participants;
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
    private C_Options options;
    private C_Projet projet;
    private C_Jour day;
    private C_Sujet sujet;
    private C_Participant partAcutel;
    private List<C_Participant>list_participants;

    private SimpleDateFormat sdf;
    private String userID;
    private boolean edit=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_sujet_new);

        System.out.println("**************************************************************");
        System.out.println("**A_sujet_New : Création d'un nouveau sujet");

        //initialisation objet page
        ll_participants = (LinearLayout) findViewById(R.id.newsujet_ll_participants);
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
        list_participants=new ArrayList<C_Participant>();

        //récupération du jour
        this.options=daoOptions.getOptionByUserId();
        System.out.println("IS ONLINE1 ="+options.online);
        this.partAcutel=daoparticipant.getParticipantById(options.userid);
        this.day=daoJour.getJourById(options.jourid);
        this.day.creerLesListes(daoSujet);

        this.projet=daoProjet.getProjetByName(options.projetid);
        this.projet.creerLesListes(daoJour, daoparticipant);

        //copie list
        for (C_Participant p : this.projet.liste_participants)
        {list_participants.add(p);}

        System.out.println("--user : " + userID);
        System.out.println("--jour : " + this.day.nomJour);
        System.out.println("--jour date : " + this.day.jour.toString());



        //IF EDIT
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            if(extras.getString("edit").equals("true"))
            {
                try
                {
                    this.edit = true;
                    sujet = daoSujet.getSujetById(options.sujetid);
                    sujet.creerLesListes(daoMessage, daoparticipant);
                    tb_titre.setText(sujet.titre);
                    tb_description.setText(sujet.description);
                    lb_prix.setText(""+sujet.prix);


                    //set time
                    sdf = new SimpleDateFormat("HH");
                    sb_hour.setProgress(Integer.parseInt(sdf.format(sujet.heure)));

                    sdf = new SimpleDateFormat("mm");
                    sb_min.setProgress(Integer.parseInt(sdf.format(sujet.heure)));

                    lb_time.setText(sb_hour.getProgress() + "h" + sb_min.getProgress());


                    //set durée
                    sb_duree.setProgress(sujet.duree);
                    int hour=sb_duree.getProgress()/60;
                    int min=sb_duree.getProgress()-hour*60;
                    lb_duree.setText(hour+"h"+min);


                    //set type
                    switch (sujet.type)
                    {
                        case "Repas":
                            btn_repas.setImageResource(R.drawable.ic_jour_repas_fill);
                            lb_type.setText("Repas");
                            lb_type.setTextColor(Color.parseColor("#2980b9"));
                            break;

                        case "Visite":
                            btn_visite.setImageResource(R.drawable.ic_jour_visite_fill);
                            lb_type.setText("Visite");
                            lb_type.setTextColor(Color.parseColor("#16a085"));
                            break;

                        case "Logement":
                            btn_logement.setImageResource(R.drawable.ic_jour_logement_fill);
                            lb_type.setText("Logement");
                            lb_type.setTextColor(Color.parseColor("#f39c12"));
                            break;

                        case "Loisir":
                            btn_loisir.setImageResource(R.drawable.ic_jour_loisir_fill);
                            lb_type.setText("Loisir");
                            lb_type.setTextColor(Color.parseColor("#9b59b6"));
                            break;

                        case "Libre":
                            btn_libre.setImageResource(R.drawable.ic_jour_libre_fill);
                            lb_type.setText("Libre");
                            lb_type.setTextColor(Color.parseColor("#5b5b5b"));
                            break;

                        case "Transport":
                            btn_transport.setImageResource(R.drawable.ic_jour_transports_fill);
                            lb_type.setText("Transport");
                            lb_type.setTextColor(Color.parseColor("#e74c3c"));

                            break;
                    }
                    sujet.personnesAyantAccepte.clear();
                }
                catch (Exception ex)
                {
                    System.out.println("[ERROR]A_sujet_new : while editing : "+ex.getMessage());
                }
            }
        } //END EDIT
        System.out.println("IS ONLINE2 ="+options.online);


        //gestion participants
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (C_Participant p : projet.liste_participants) {

            LinearLayout LL = new LinearLayout(this);
            LL.setOrientation(LinearLayout.HORIZONTAL);
            LL.setLayoutParams(LLParams);

            LinearLayout LLtxt = new LinearLayout(this);
            LLtxt.setOrientation(LinearLayout.VERTICAL);
            LLtxt.setLayoutParams(LLParams);

            ImageButton img_participate = new ImageButton(this);
            img_participate.setImageResource(R.drawable.ic_participate_fill);
            img_participate.setBackgroundColor(Color.TRANSPARENT);
            img_participate.setId(p.id);
            img_participate.setOnClickListener(onClickbtnParticipate);

            TextView txt_nom = new TextView(this);
            txt_nom.setText(p.prenom + " " + p.nom);
            txt_nom.setTextColor(Color.parseColor("#ac035d"));

            TextView txt_mail = new TextView(this);
            txt_mail.setText("@: "+p.mail);

            LLtxt.addView(txt_nom);
            LLtxt.addView(txt_mail);

            LL.addView(img_participate);
            LL.addView(LLtxt);

            ll_participants.addView(LL);

        }
        System.out.println("IS ONLINE3 ="+options.online);








    }



//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //CREER SUJET
    View.OnClickListener onClickButtonValider = new View.OnClickListener() {
        public void onClick(View v) {
            int hour;
            int min;
            System.out.println("IS ONLINE4 ="+options.online);
            //ini valeurs base
            sujet.titre="pas de titre";

            //ini valeurs page
            sujet.idSujet=day.nomJour+"_"+tb_titre.getText().toString();
            sujet.titre=tb_titre.getText().toString();
            sujet.description=tb_description.getText().toString();
            sujet.type=lb_type.getText().toString();
            sujet.localisation="loc";
            sujet.duree=sb_duree.getProgress();
            sujet.auFeeling=false;
            sujet.valide=false;
            sujet.personnesAyantAccepte.add(partAcutel);
            sujet.liste_participent=list_participants;


            //set heure
            try {
                sdf = new SimpleDateFormat("dd/MM/yy");
                System.out.println("//////////////////////////////////////");
                System.out.println("--jour date : " + day.jour.toString());
                String myTime = sdf.format(day.jour);
                System.out.println("day.jour = "+myTime);
                myTime = myTime + " " + sb_hour.getProgress() + ":" + sb_min.getProgress();

                sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
                sujet.heure = sdf.parse(myTime);
                System.out.println("date to add = "+sdf.parse(myTime));
                System.out.println("lecture : " + sujet.heure);
                System.out.println("lecture2 : " + sdf.format(sujet.heure));
                sdf = new SimpleDateFormat("dd/MM/yy");
                System.out.println("lecture3 : " + sdf.format(sujet.heure));

            }
            catch (Exception ex)
            {System.out.println("[ERROR] "+ex.getMessage());}

            //création du premier message auto
            if(!edit)
            {
                C_Message message = new C_Message(sujet.idSujet, new Date(), "Création du sujet", partAcutel);

                System.out.println("sujet new : add msg : " + message.personnesAyantVuesToString);
                daoMessage.ajouter(message, options.online);
                sujet.liste_messages.add(message);
            }


            System.out.println("IS ONLINE5 ="+options.online);
            //save sujet
            sujet.listeToString();
            System.out.println("IS ONLINE6 =" + options.online);
            daoSujet.ajouterOUmodifier(sujet, options.online);

            //maj jour
            if(!edit)
            {
                day.liste_sujets.add(sujet);
                day.listeToString();
                daoJour.modifier(day, options.online);
            }

            options.sujetid=sujet.idSujet;
            daoOptions.modifier(options);


            Intent intent = new Intent(A_sujet_new.this, A_sujet_Map_set.class);

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


    //select btn participants
    View.OnClickListener onClickbtnParticipate = new View.OnClickListener() {
        public void onClick(View v) {
            ImageButton selectedBtn = (ImageButton) v;
            System.out.println("in : " + projet.liste_participants.size());
            try {
                for (C_Participant p : projet.liste_participants) {
                    if (p.id == selectedBtn.getId()) {
                        System.out.println(p.id + " vs " + selectedBtn.getId());
                        System.out.println("found");
                        if (list_participants.contains(p)) {
                            System.out.println("contains=remove");
                            selectedBtn.setImageResource(R.drawable.ic_participate);
                            list_participants.remove(p);
                            System.out.println("Removed : " + p.mail);
                        } else {
                            System.out.println("not contains=add");
                            selectedBtn.setImageResource(R.drawable.ic_participate_fill);
                            list_participants.add(p);
                            System.out.println("add : " + p.mail);
                        }

                        for (C_Participant part : list_participants) {
                            System.out.println("list : " + part.mail);
                        }


                    }
                }

            } catch (Exception ex) {
                System.out.println("ERROR "+ex.getMessage());
            }
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
