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

        //CASdeTESTS();

    }





private void CASdeTESTS()
{
    C_Participant part1 = new C_Participant("PART1","part1","part1","aaaa");
    daoparticipant.ajouter(part1);

    C_Participant part2 = new C_Participant("BAUDRAIS","Maxime","cnero@hotmail.f","azerty");
    daoparticipant.ajouter(part2);

    C_Participant part3 = new C_Participant("BAUDRAIS","Maxime","part2","azerty");
    daoparticipant.ajouter(part3);

    //projet 1
  /*  C_Projet proj1= new C_Projet(this,"proj1","ceci est un test",new Date(), new Date());
    proj1.creerLesListes();


    C_Jour proj1_jour1=new C_Jour("date1", new Date()); //String nomJour, Date jour)
    proj1_jour1.creerLesListes();

    C_Sujet proj1_jour1_suj1=new C_Sujet("proj1_jour1_suj1","Mon premier sujet", "description vide", "visite", "ici", new Date(), 60, false, 10); //( String idSujet, String titre, String description, String type, String localisation, Date heure, int duree, boolean auFeeling, float prix)
    proj1_jour1_suj1.creerLesListes();

    C_Message proj1_jour1_suj1_msg1=new C_Message(new Date(),"Premier message !",part1);
    C_Message proj1_jour1_suj1_msg2=new C_Message(new Date(),"Second  message !",part1);

    C_Sujet proj1_jour1_suj2=new C_Sujet("proj1_jour1_suj2","Mon second sujet", "description vide", "visite", "ici", new Date(), 60, false, 10);
    proj1_jour1_suj2.creerLesListes();

    C_Message proj1_jour1_suj2_msg1=new C_Message(new Date(),"Premier message !",part1);
    C_Message proj1_jour1_suj2_msg2=new C_Message(new Date(),"Second  message !",part1);*/



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
            boolean find=false;
            for (C_Participant me : daoparticipant.getParticipants())
            {
                System.out.println("user :" + me.mail);
                System.out.println(me.mail + " vs " + tb_userID.getText().toString());
                if(me.mail.equals(tb_userID.getText().toString()))
                {
                    find=true;
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

            }
            if(!find)
            {
                tb_userID.setText("Utilisateur inconnu");
                tb_mdp.setText("");
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
