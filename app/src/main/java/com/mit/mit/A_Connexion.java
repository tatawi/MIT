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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class A_Connexion extends MainActivity {

    private List<Thread> list_threads = new ArrayList<Thread>();


    private C_Options options;

    //objets de la page
    private Button btn_connect;
    private EditText tb_userID;
    private EditText tb_mdp;
    private CheckBox cb_remember;
    private ProgressBar pb_load;


    private Button btn_co1;
    private Button btn_co2;

    //variables
    private String userID;
    private String mdp;
    protected  C_Participant me;
    protected boolean offline = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__connexion);

        //-------------------------------------------------------------------------------------------
        //INITIALISATIONS
        System.out.println(">>>>A_Connexion");

        //find objects
        btn_connect = (Button) findViewById(R.id.main_btn_connect);
        tb_userID= (EditText) findViewById(R.id.main_tb_user);
        tb_mdp= (EditText) findViewById(R.id.main_tb_mdp);
        cb_remember = (CheckBox) findViewById(R.id.main_cb_remember);
        pb_load = (ProgressBar) findViewById(R.id.main_pb_load);

        btn_co1 = (Button) findViewById(R.id.button3);
        btn_co2 = (Button) findViewById(R.id.button4);

        //set listeners
        tb_userID.setOnClickListener(onUserClick);
        btn_connect.setOnClickListener(onConnectClick);

        btn_co1.setOnClickListener(onco1);
        btn_co2.setOnClickListener(onco2);

        pb_load.setVisibility(View.GONE);


        //-------------------------------------------------------------------------------------------
        //LOAD OPTIONS IF EXISTS
        try
        {
            System.out.println("--Verif Options & remember");
            this.options = daoOptions.getOptionByUserId();
            System.out.println("Option found");
            //vérification userSaved
            if (options.userSaved)
            {
                System.out.println("remember user is true : "+options.userid);
                Intent intent = new Intent(A_Connexion.this, A_projets.class);
                startActivity(intent);
            }
        }
        catch (Exception ex)
        {
            System.out.println("[ERROR]"+ex.getMessage());
        }


        //-------------------------------------------------------------------------------------------
        //LOAD ONLINE
        try
        {
            System.out.println("--Verif Online");
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                if( extras.getString("offline").equals("True"))
                {offline=true;
                    System.out.println("Offline");
                }
                else
                {offline=false;
                    System.out.println("Online");
                }


            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }



        //-------------------------------------------------------------------------------------------
        // PARSE - Enable Local Datastore
        try
        {
            System.out.println("--Loading parse");
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "ZyJ90wtX8SyHiJOBCztcGaAaxLRUyI3JZD4vUptQ", "CZYG6VU5JwXdOe5D4l8i2hWzAqmjKZZa3CeGtAYs");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }





        //co rapide - TESTS
        //btn_co1.setVisibility(View.INVISIBLE);
        //btn_co2.setVisibility(View.INVISIBLE);

    }










//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------


    //-------------------------------------------------------------------------------------------
    //TB_user
    View.OnClickListener onUserClick = new View.OnClickListener() {
        public void onClick(View v) {
            tb_userID.setText("");
        }
    };


    //-------------------------------------------------------------------------------------------
    //BTN_Connect
    View.OnClickListener onConnectClick = new View.OnClickListener() {
        public void onClick(View v) {
            boolean isOnline=false;
            boolean isUserSaved=false;


            //Chargement des données internet si online true
            try
            {
                pb_load.setVisibility(View.VISIBLE);
                //offline
                if(!offline)
                {
                    System.out.println("ONLINE");
                    Thread thread = new Thread(){
                        public void run(){
                            System.out.println("**offline non checked");
                            updateUserList();
                            MajDAO();

                            System.out.println("**end offline");
                        }};
                    thread.start();
                    thread.join();
                    isOnline=true;
                }

                /*// Wait for all of the threads to finish.
                for (Thread thread : list_threads)
                {
                    try
                    {thread.join();}
                    catch(Exception ex)
                    {System.out.println("[ERROR THREAD]"+ex.getMessage());}
                }
                pb_load.setVisibility(View.GONE);*/



                //VERIFY USER
                me=daoparticipant.getParticipantById(tb_userID.getText().toString());
                System.out.println("**user loaded");
                if(me.mdp.equals(tb_mdp.getText().toString()))
                {
                    //remermber user
                    if (cb_remember.isChecked())
                    {isUserSaved=true;}

                    System.out.println(" user : "+me.nom);
                    System.out.println(" user : "+me.mail);

                    //suppr OPTIONS existants
                    try
                    {daoOptions.supprimer();}
                    catch(Exception ex)
                    {System.out.println("[ERROR DEL OPT]"+ex.getMessage());}

                    //Création OPTION
                    C_Options MYoptions = new C_Options(me, isOnline, isUserSaved);
                    daoOptions.ajouter(MYoptions);

                    //OPEN
                    Intent intent = new Intent(A_Connexion.this, A_projets.class);
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
                System.out.println("[ERROR]"+e.getMessage());
                tb_userID.setText("Utilisateur inconnu");
                tb_mdp.setText("");
                pb_load.setVisibility(View.GONE);
            }

        }
    };



    //-------------------------------------------------------------------------------------------
    //
    View.OnClickListener onco1 = new View.OnClickListener() {
        public void onClick(View v) {
            tb_userID.setText("cnero@hotmail.fr");
            tb_mdp.setText("azerty");
        }
    };


    //-------------------------------------------------------------------------------------------
    //
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
        getMenuInflater().inflate(R.menu.menu_a__connexion, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //ON NEW USER
        if (id == R.id.menu_new)
        {
            Intent intent = new Intent(A_Connexion.this, A_user_new.class);
            if(offline)
            {
                intent.putExtra("offline", "True");
            }
            else
            {
                intent.putExtra("offline", "False");
            }
            startActivity(intent);
        }

        //ON GO TO LIST PARTICIPANTS
        if (id == R.id.menu_participants)
        {
            Intent intent = new Intent(A_Connexion.this, A_gestionParticipants.class);
            if(offline)
            {
                intent.putExtra("offline", "True");
            }
            else
            {
                intent.putExtra("offline", "False");
            }
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




}
