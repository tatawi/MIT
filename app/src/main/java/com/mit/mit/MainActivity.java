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

        //set listeners
        tb_userID.setOnClickListener(onUserClick);
        btn_connect.setOnClickListener(onConnectClick);

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
            userID =tb_userID.getText().toString();
            mdp=tb_mdp.getText().toString();
            me=daoparticipant.getParticipantById(userID);

            if(me!=null)
            {
                if (me.mdp.length()>2) {
                    if(me.mdp.equals(mdp)) {
                        Intent intent = new Intent(MainActivity.this, A_projets.class);
                        startActivity(intent);
                    }
                    else{
                        tb_userID.setText("mot de passe incorrect");
                    }
                }
                else {
                    tb_userID.setText("Utilisateur inconnu");
                }
            }
            else {
                tb_userID.setText("Utilisateur inconnu");
            }
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
            Intent intent = new Intent(MainActivity.this, A_newUser.class);
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
