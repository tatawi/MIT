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
    protected boolean online = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__connexion);

        System.out.println("--INI");

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


        try
        {

            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                if( extras.getString("offline").equals("true"))
                {
                    online=false;
                }
                else
                {
                    online=true;
                }

            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }



        // Enable Local Datastore
        try
        {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "ZyJ90wtX8SyHiJOBCztcGaAaxLRUyI3JZD4vUptQ", "CZYG6VU5JwXdOe5D4l8i2hWzAqmjKZZa3CeGtAYs");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }


        //load options
        try
        {
            System.out.println("--VERIF REMEMBER");
            this.options = daoOptions.getOptionByUserId();

            //vérification userSaved
            if (options.userSaved)
            {
            /*System.out.println("--OK = oui");
            Intent intent = new Intent(MainActivity.this, A_projets.class);
            startActivity(intent);*/
            }


        }
        catch (Exception ex)
        {
            System.out.println("[ERROR]"+ex.getMessage());
        }






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
            boolean isOnline=true;
            boolean isUserSaved=false;
            try
            {
                pb_load.setVisibility(View.VISIBLE);
                System.out.println("**options created");
                //offline
                if(!online)
                {
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

                // Wait for all of the threads to finish.
                for (Thread thread : list_threads)
                {
                    try
                    {
                        System.out.println("**wait thread");
                        thread.join();
                    }
                    catch(Exception ex)
                    {
                        System.out.println("[ERROR THREAD]"+ex.getMessage());
                    }

                }
                pb_load.setVisibility(View.GONE);

                //user
                me=daoparticipant.getParticipantById(tb_userID.getText().toString());
                System.out.println("**user loaded");
                if(me.mdp.equals(tb_mdp.getText().toString()))
                {
                    if (cb_remember.isChecked())
                    {
                        System.out.println("**save user");
                        isUserSaved=true;
                    }
                    System.out.println(" user : "+me.nom);
                    System.out.println(" user : "+me.mail);
                    C_Options MYoptions = new C_Options(me, isOnline, isUserSaved);
                    daoOptions.ajouter(MYoptions);
                    System.out.println("**add user to options");
                    System.out.println("**options saved");


                    /*System.out.println("----verif options");
                    C_Options op = daoOptions.getOption();
                    System.out.println("----user : "+op.userid);
                    System.out.println("----online : "+op.online);
                    System.out.println("----saved user : "+op.userSaved);*/


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
            Intent intent = new Intent(A_Connexion.this, A_user_new.class);
            startActivity(intent);
        }

        //ON GO TO LIST PARTICIPANTS
        if (id == R.id.menu_participants)
        {
            Intent intent = new Intent(A_Connexion.this, A_gestionParticipants.class);
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
