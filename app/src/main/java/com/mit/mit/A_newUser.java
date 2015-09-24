package com.mit.mit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class A_newUser extends MainActivity {

    //Objets de la page
    private EditText tb_nom;
    private EditText tb_prenom;
    private EditText tb_mail;
    private EditText tb_mdp;
    private EditText tb_mdp2;
    private TextView lb_msg;
    private Button btn_valider;

    //variables
    private String nom;
    private String prenom;
    private String mail;
    private String mdp;
    private String mdp2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //initialisations
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_a_new_user);
        tb_nom = (EditText) findViewById(R.id.newUser_tb_nom);
        tb_prenom = (EditText) findViewById(R.id.newUser_tb_prenom);
        tb_mail = (EditText) findViewById(R.id.newUser_tb_mail);
        tb_mdp = (EditText) findViewById(R.id.newUser_tb_mdp1);
        tb_mdp2 = (EditText) findViewById(R.id.newUser_tb_mdp2);
        lb_msg = (TextView) findViewById(R.id.newUser_lb_msg);
        btn_valider = (Button) findViewById(R.id.newUser_btn_valider);

        btn_valider.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean verifOK=true;
                lb_msg.setText("");
                nom=tb_nom.getText().toString();
                prenom=tb_prenom.getText().toString();
                mail=tb_mail.getText().toString();
                mdp=tb_mdp.getText().toString();
                mdp2=tb_mdp2.getText().toString();

                if(nom.length()<2){
                    verifOK=false;
                    lb_msg.setText("Nom d'utilisateur incorrect");
                }
                if(prenom.length()<2) {
                    verifOK=false;
                    lb_msg.setText("Prénom utilisateur incorrect");
                }
                if(mail.length()<2) {
                    verifOK=false;
                    lb_msg.setText("adresse mail incorrecte");
                }
                if(mdp.length()<2) {
                    verifOK=false;
                    lb_msg.setText("mot de passe trop court");
                }
                if(!mdp.equals(mdp2)) {
                    verifOK=false;
                    lb_msg.setText("les mots de passes ne correspondent pas");
                }

                if(verifOK) {
                    C_Participant newPart = new C_Participant(nom,prenom,mail,mdp);
                    daoparticipant.ajouter(newPart);
                    Intent intent = new Intent(A_newUser.this, MainActivity.class);
                    startActivity(intent);
                }


            }
        });
    }




















//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_new_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
