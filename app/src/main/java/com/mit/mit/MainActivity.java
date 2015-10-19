package com.mit.mit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    //objets de la page
    private Button btn_connect;
    private EditText tb_userID;
    private EditText tb_mdp;

    private Button btn_add1;
    private Button btn_add2;
    private Button btn_co1;
    private Button btn_co2;

    //variables
    private String userID;
    private String mdp;
    protected  C_Participant me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find objects
        btn_connect = (Button) findViewById(R.id.main_btn_connect);
        tb_userID= (EditText) findViewById(R.id.main_tb_user);
        tb_mdp= (EditText) findViewById(R.id.main_tb_mdp);

        btn_add1 = (Button) findViewById(R.id.button1);
        btn_add2 = (Button) findViewById(R.id.button2);
        btn_co1 = (Button) findViewById(R.id.button3);
        btn_co2 = (Button) findViewById(R.id.button4);


        //set listeners
        tb_userID.setOnClickListener(onUserClick);
        btn_connect.setOnClickListener(onConnectClick);

        btn_add1.setOnClickListener(onAdd1);
        btn_add2.setOnClickListener(onAdd2);
        btn_co1.setOnClickListener(onco1);
        btn_co2.setOnClickListener(onco2);



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

            for (C_Participant me : daoparticipant.getParticipants())
            {
                System.out.println("user :" + me.mail);
                System.out.println(me.mail + " vs " + tb_userID.getText().toString());
                if(me.mail.equals(tb_userID.getText().toString()))
                {
                    System.out.println(">found");
                    if(me.mdp.equals(tb_mdp.getText().toString()))
                    {
                        System.out.println(">ok");
                        Intent intent = new Intent(MainActivity.this, A_projets.class);
                        intent.putExtra("userID", me.mail);
                        startActivity(intent);
                    }
                    else
                    {
                        tb_userID.setText("mot de passe incorrect");
                        tb_mdp.setText("");
                    }
                }
                else
                {
                    tb_userID.setText("Utilisateur inconnu");
                    tb_mdp.setText("");
                }

            }
        }
    };




//provi
View.OnClickListener onAdd1 = new View.OnClickListener() {
    public void onClick(View v) {
        C_Participant part = new C_Participant("BAUDRAIS","Maxime","cnero@hotmail.f","azerty");
        daoparticipant.ajouter(part);
        tb_userID.setText(part.mail);
        tb_mdp.setText(part.mdp);
    }
};


    View.OnClickListener onAdd2 = new View.OnClickListener() {
        public void onClick(View v) {
            C_Participant part = new C_Participant("LELOUET","Audrey","audrey","azerty");
            daoparticipant.ajouter(part);
            tb_userID.setText(part.mail);
            tb_mdp.setText(part.mdp);
        }
    };

    View.OnClickListener onco1 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, A_projets.class);
            intent.putExtra("userID", "cnero@hotmail.f");
            startActivity(intent);
        }
    };

    View.OnClickListener onco2 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, A_projets.class);
            me=daoparticipant.getParticipantById(tb_userID.getText().toString());
            intent.putExtra("userID", "audrey");
            startActivity(intent);

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
        return super.onOptionsItemSelected(item);
    }
}
