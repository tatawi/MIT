package com.mit.mit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //DAOs
    protected Context pContext = this;
    protected DAO_Participant daoparticipant = new DAO_Participant(this);
    protected DAO_Projet daoProjet = new DAO_Projet(this);
    protected DAO_Jour daoJour = new DAO_Jour(this);
    protected DAO_Sujet daoSujet = new DAO_Sujet(this);
    protected DAO_Message daoMessage = new DAO_Message(this);
    protected DAO_Options daoOptions = new DAO_Options(this);

    //objets de la page
    private Button btn_connect;
    private EditText tb_userID;
    private EditText tb_mdp;
    private Switch sw_remember;


    private Button btn_co1;
    private Button btn_co2;

    //variables
    private String userID;
    private String mdp;
    protected  C_Participant me;
    protected boolean onlineMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find objects
        btn_connect = (Button) findViewById(R.id.main_btn_connect);
        tb_userID= (EditText) findViewById(R.id.main_tb_user);
        tb_mdp= (EditText) findViewById(R.id.main_tb_mdp);
        sw_remember= (Switch) findViewById(R.id.main_sw_remember);



        btn_co1 = (Button) findViewById(R.id.button3);
        btn_co2 = (Button) findViewById(R.id.button4);

        //set listeners
        tb_userID.setOnClickListener(onUserClick);
        btn_connect.setOnClickListener(onConnectClick);

        btn_co1.setOnClickListener(onco1);
        btn_co2.setOnClickListener(onco2);



        // Enable Local Datastore
        try {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "ZyJ90wtX8SyHiJOBCztcGaAaxLRUyI3JZD4vUptQ", "CZYG6VU5JwXdOe5D4l8i2hWzAqmjKZZa3CeGtAYs");

            updateUserList();
            MajDAO();

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }


       /* String userSaved = daoOptions.getSavedUser();
        if(!userSaved.equals(""))
        {
            me=daoparticipant.getParticipantById(userSaved);
            Intent intent = new Intent(MainActivity.this, A_projets.class);
            intent.putExtra("userID", me.mail);
            startActivity(intent);
        }*/

    }










//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //TB_user
    View.OnClickListener onUserClick = new View.OnClickListener() {
        public void onClick(View v) {
            tb_userID.setText("");
        }
    };

    //BTN_Connect
    View.OnClickListener onConnectClick = new View.OnClickListener() {
        public void onClick(View v) {

            try
            {

                me=daoparticipant.getParticipantById(tb_userID.getText().toString());
                if(me.mdp.equals(tb_mdp.getText().toString()))
                {
                    C_Options options = new C_Options(me);
                    daoOptions.ajouter(options);

                    Intent intent = new Intent(MainActivity.this, A_projets.class);
                    //intent.putExtra("userID", me.mail);
                    startActivity(intent);
                }
                else
                {
                    tb_userID.setText("mot de passe incorrect");
                    tb_mdp.setText("");
                }
            }
            catch (Exception e)
            {
                tb_userID.setText("Utilisateur inconnu");
                tb_mdp.setText("");
            }

        }
    };




    View.OnClickListener onco1 = new View.OnClickListener() {
        public void onClick(View v) {
            tb_userID.setText("cnero@hotmail.fr");
            tb_mdp.setText("azerty");
        }
    };

    View.OnClickListener onco2 = new View.OnClickListener() {
        public void onClick(View v) {
            tb_userID.setText("audrey@mail.fr");
            tb_mdp.setText("azerty");

        }
    };






//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //ON NEW USER
        if (id == R.id.menu_new)
        {
            Intent intent = new Intent(MainActivity.this, A_user_new.class);
            startActivity(intent);
        }

        //ON GO TO LIST PARTICIPANTS
        if (id == R.id.menu_participants)
        {
            Intent intent = new Intent(MainActivity.this, A_gestionParticipants.class);
            startActivity(intent);
        }

        //ON REFRESH
        if(id==R.id.menu_sujet_refresh)
        {
            updateUserList();
            MajDAO();
        }
        return super.onOptionsItemSelected(item);
    }



//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    private void updateUserList()
    {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Participant");
        try
        {
            List<ParseObject> usersList = query.find();
            System.out.println("--load users : " + usersList.size());
            for(ParseObject parse : usersList)
            {
                String nom = parse.getString("nom");
                String prenom = parse.getString("prenom");
                String mail = parse.getString("mail");
                String mdp = parse.getString("mdp");
                C_Participant part = new C_Participant(nom, prenom, mail, mdp);
                System.out.println("--part find : " + part.mail);

                daoparticipant.ajouterOUmodifier(part);
            }
        }
        catch (ParseException ex)
        {
            Log.e("Error", ex.getMessage());
            ex.printStackTrace();
        }
    }

    protected void MajDAO()
    {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Projet");
        System.out.println("--CHARGEMENT INTERNET : " );
        try
        {
            //WORK WITH PROJECTS
            List<ParseObject> projectsList = query.find();
            System.out.println("--[projects] available to download : " + projectsList.size());
            for(ParseObject parse : projectsList)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                C_Projet proj = new C_Projet();

                proj.id=parse.getInt("id");
                proj.nom=parse.getString("nom");
                proj.description=parse.getString("description");
                proj.prixSejour=(float)parse.getLong("prixSejour");
                proj.statut=parse.getString("statut");
                proj.participantsToString=parse.getString("participantsToString");
                proj.joursToString=parse.getString("joursToString");
                proj.couleur=parse.getString("couleur");

                try {
                    proj.dateDebut=sdf.parse(parse.getString("dateDebut"));
                    proj.dateFin=sdf.parse(parse.getString("dateFin"));
                }
                catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                daoProjet.ajouterOUmodifier(proj);
                System.out.println("--[projects] " + proj.nom);
                System.out.println("--[projects] - participants " + proj.participantsToString.toString());
                System.out.println("--[projects] - jours " + proj.joursToString.toString());

            }


            //WORK WITH DAYS
            query = new ParseQuery<ParseObject>("Jour");
            List<ParseObject> jourList = query.find();
            System.out.println("--[jours] available to download : " + jourList.size());
            for(ParseObject parse : jourList)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                C_Jour jour = new C_Jour();

                jour.nomJour=parse.getString("nomJour");
                jour.prixJournee=(float)parse.getLong("prixJournee");
                jour.sujetsToString=parse.getString("sujetsToString");
                try {
                    jour.jour=sdf.parse(parse.getString("date"));
                }
                catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                daoJour.ajouterOUmodifier(jour);
                System.out.println("--[jours] " + jour.nomJour);
                System.out.println("--[jours] - sujets " + jour.sujetsToString.toString());
            }

            //WORK WITH SUBJECTS
            query = new ParseQuery<ParseObject>("Sujet");
            List<ParseObject> subjectList = query.find();
            System.out.println("--[sujet] available to download : " + subjectList.size());
            for(ParseObject parse : subjectList)
            {
                //SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy HH:mm:ss");
                C_Sujet sujet = new C_Sujet();

                sujet.idSujet=parse.getString("idSujet");
                sujet.titre=parse.getString("titre");
                sujet.description=parse.getString("description");
                sujet.type=parse.getString("type");
                sujet.localisation=parse.getString("localisation");
                sujet.duree=parse.getInt("duree");
                sujet.prix=parse.getDouble("prix");
                sujet.messagesToString=parse.getString("messagesToString");
                sujet.personnesAyantAccepteToString=parse.getString("personnesAyantAccepteToString");
                sujet.auFeeling=parse.getBoolean("auFeeling");

                if (parse.getString("valide").equals("true"))
                {sujet.valide=true;}
                else
                {sujet.valide=false;}

                try {
                    sujet.heure=sdf.parse(parse.getString("heure"));
                }
                catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                daoSujet.ajouterOUmodifier(sujet);
                System.out.println("--[sujet] " + sujet.titre);
                System.out.println("--[sujet] - messages " + sujet.messagesToString.toString());
                System.out.println("--[sujet] - ayant acceptés " + sujet.personnesAyantAccepteToString.toString());
            }


            //WORK WITH MESSAGES
            query = new ParseQuery<ParseObject>("Message");
            List<ParseObject> messageList = query.find();
            System.out.println("--[messages] available to download : " + messageList.size());
            for(ParseObject parse : messageList)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy HH:mm");
                C_Message mess = new C_Message();

                mess.id=parse.getString("id");
                mess.message=parse.getString("message");
                mess.id_participantEmetteur=parse.getString("id_participantEmetteur");
                mess.personnesAyantVuesToString=parse.getString("personnesAyantVuesToString");

                try {
                    mess.heure=sdf.parse(parse.getString("heure"));
                }
                catch (java.text.ParseException e) {
                    System.out.println("--error : " + e.getMessage());
                    e.printStackTrace();
                }
                daoMessage.ajouterOUmodifier(mess);

                System.out.println("--msg : " + mess.id);
                C_Message mss = daoMessage.getMessageById(mess.id);

                System.out.println("--new msg : " + mss.id);
                System.out.println("--[messages] " + mess.id);
                System.out.println("--[messages] - emetteur " + mess.id_participantEmetteur);
                System.out.println("--[messages] - ayant vu " + mess.personnesAyantVuesToString.toString());

            }

        }
        catch (ParseException ex)
        {
            Log.e("Error", ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("END IMPORT");
        List<C_Projet>li = daoProjet.getProjets();
        System.out.println(li.size() +"projets disponibles");
        for (C_Projet pj:li)
        {
            System.out.println("- "+pj.nom);
        }


    }




}
