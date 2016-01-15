package com.mit.mit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    protected List<Thread>list_threads = new ArrayList<Thread>();

    //DAOs
    protected Context pContext = this;
    protected DAO_Participant daoparticipant = new DAO_Participant(this);
    protected DAO_Projet daoProjet = new DAO_Projet(this);
    protected DAO_Jour daoJour = new DAO_Jour(this);
    protected DAO_Sujet daoSujet = new DAO_Sujet(this);
    protected DAO_Message daoMessage = new DAO_Message(this);
    protected DAO_Options daoOptions = new DAO_Options(this);

    private C_Options options;

    private ImageButton img;
    private CheckBox cb_offline;
    private LinearLayout ll_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            options = daoOptions.getOptionByUserId();
            cb_offline.setChecked(options.online);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        img=(ImageButton) findViewById(R.id.mainAC_img);
        cb_offline=(CheckBox) findViewById(R.id.mainAC_cb);
        ll_main=(LinearLayout) findViewById(R.id.mainAC_ll);

        img.setOnClickListener(onClickImage);
        ll_main.setOnClickListener(onClickLayout);



    }


    //Image
    View.OnClickListener onClickImage = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, A_Connexion.class);

            if(cb_offline.isChecked())
            {
                intent.putExtra("offline", "True");
            }
            else
            {
                intent.putExtra("offline", "False");
            }
            startActivity(intent);

        }
    };

    //Layout
    View.OnClickListener onClickLayout = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, A_Connexion.class);
            if(cb_offline.isChecked())
            {
                intent.putExtra("offline", "True");
            }
            else
            {
                intent.putExtra("offline", "False");
            }
            startActivity(intent);
        }
    };







//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------





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

        return super.onOptionsItemSelected(item);
    }



//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    protected void updateUserList()
    {


       /* Thread thread = new Thread(){
            public void run(){
            };
                */
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

                        daoparticipant.ajouterOUmodifier(part, true);
                    }
                }
                catch (ParseException ex)
                {
                    Log.e("Error", ex.getMessage());
                    ex.printStackTrace();
                }
            }




    protected void MajDAO() {


                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Projet");
                System.out.println("--CHARGEMENT INTERNET : ");
                try {
                    //WORK WITH PROJECTS
                    List<ParseObject> projectsList = query.find();
                    System.out.println("--[projects] available to download : " + projectsList.size());
                    for (ParseObject parse : projectsList) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        C_Projet proj = new C_Projet();

                        proj.id = parse.getInt("id");
                        proj.nom = parse.getString("nom");
                        proj.description = parse.getString("description");
                        proj.prixSejour = (float) parse.getLong("prixSejour");
                        proj.statut = parse.getString("statut");
                        proj.participantsToString = parse.getString("participantsToString");
                        proj.joursToString = parse.getString("joursToString");
                        proj.couleur = parse.getString("couleur");

                        try {
                            proj.dateDebut = sdf.parse(parse.getString("dateDebut"));
                            proj.dateFin = sdf.parse(parse.getString("dateFin"));
                        } catch (java.text.ParseException e) {
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
                    for (ParseObject parse : jourList) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                        C_Jour jour = new C_Jour();

                        jour.nomJour = parse.getString("nomJour");
                        jour.prixJournee = (float) parse.getLong("prixJournee");
                        jour.sujetsToString = parse.getString("sujetsToString");
                        jour.ville = parse.getString("ville");
                        try {
                            jour.jour = sdf.parse(parse.getString("date"));
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        daoJour.ajouterOUmodifier(jour, true);
                        System.out.println("--[jours] " + jour.nomJour);
                        System.out.println("--[jours] - sujets " + jour.sujetsToString.toString());

                    }

                    //WORK WITH SUBJECTS
                    query = new ParseQuery<ParseObject>("Sujet");
                    List<ParseObject> subjectList = query.find();
                    System.out.println("--[sujet] available to download : " + subjectList.size());
                    for (ParseObject parse : subjectList) {
                        //SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy HH:mm:ss");
                        C_Sujet sujet = new C_Sujet();

                        sujet.idSujet = parse.getString("idSujet");
                        sujet.titre = parse.getString("titre");
                        sujet.description = parse.getString("description");
                        sujet.type = parse.getString("type");
                        sujet.localisation = parse.getString("localisation");
                        sujet.localisation2 = parse.getString("localisation2");
                        sujet.duree = parse.getInt("duree");
                        sujet.prix = parse.getDouble("prix");
                        sujet.messagesToString = parse.getString("messagesToString");
                        sujet.personnesAyantAccepteToString = parse.getString("personnesAyantAccepteToString");
                        sujet.auFeeling = parse.getBoolean("auFeeling");
                        sujet.participentToString= parse.getString("personnesParticipant");
                        sujet.quiApayeToString= parse.getString("personnesAyantPaye");
                        sujet.combienApayeToString= parse.getString("montantPaye");


                        if (parse.getString("valide").equals("true")) {
                            sujet.valide = true;
                        } else {
                            sujet.valide = false;
                        }

                        try {
                            sujet.heure = sdf.parse(parse.getString("heure"));
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        daoSujet.ajouterOUmodifier(sujet, true);
                        System.out.println("--[sujet] " + sujet.titre);
                        System.out.println("--[sujet] - messages " + sujet.messagesToString.toString());
                        System.out.println("--[sujet] - ayant acceptés " + sujet.personnesAyantAccepteToString.toString());
                    }


                    //WORK WITH MESSAGES
                    query = new ParseQuery<ParseObject>("Message");
                    List<ParseObject> messageList = query.find();
                    System.out.println("--[messages] available to download : " + messageList.size());
                    for (ParseObject parse : messageList) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy HH:mm");
                        C_Message mess = new C_Message();

                        mess.id = parse.getString("id");
                        mess.message = parse.getString("message");
                        mess.id_participantEmetteur = parse.getString("id_participantEmetteur");
                        mess.personnesAyantVuesToString = parse.getString("personnesAyantVuesToString");

                        try {
                            mess.heure = sdf.parse(parse.getString("heure"));
                        } catch (java.text.ParseException e) {
                            System.out.println("--error : " + e.getMessage());
                            e.printStackTrace();
                        }
                        daoMessage.ajouterOUmodifier(mess, true);

                        System.out.println("--msg : " + mess.id);
                        C_Message mss = daoMessage.getMessageById(mess.id);

                        System.out.println("--new msg : " + mss.id);
                        System.out.println("--[messages] " + mess.id);
                        System.out.println("--[messages] - emetteur " + mess.id_participantEmetteur);
                        System.out.println("--[messages] - ayant vu " + mess.personnesAyantVuesToString.toString());

                    }

                } catch (ParseException ex) {
                    Log.e("Error", ex.getMessage());
                    ex.printStackTrace();
                }

                System.out.println("END IMPORT");
                List<C_Projet> li = daoProjet.getProjets();
                System.out.println(li.size() + "projets disponibles");
                for (C_Projet pj : li) {
                    System.out.println("- " + pj.nom);
                }

            }







}
