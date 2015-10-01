package com.mit.mit;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class A_projet_Preparation extends MainActivity {


    //objets de la page
    private TextView lb_description;
    private TextView lb_cout;
    private TextView lb_test;
    private LinearLayout container_tableLayout;
    private ImageButton[] tab_days;

    //variables
    private C_Projet projet;
    private double nbDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialiser objets de la page
        setContentView(R.layout.activity_a_affichage_projet);
        /*
        lb_description = (TextView) findViewById(R.id.projetPrep_lb_description);
        lb_cout = (TextView) findViewById(R.id.projetPrep_lb_cout);
        lb_test= (TextView) findViewById(R.id.projetPrep_lb_txtCout);
        container_tableLayout = (LinearLayout) findViewById(R.id.projetPrep_tableLayout);


        //récupération du projet
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            //chargement des données du projet
            this.projet = daoProjet.getProjetByName(extras.getString("idEntry"));
            this.projet.creerLesListes();
            setTitle(this.projet.nom);
            lb_description.setText(this.projet.description);
            lb_cout.setText(this.projet.prixSejour + " €");
            nbDays=projet.liste_jours.size();
            //nbDays=16;
            tab_days=new ImageButton[(int)nbDays];
            lb_test.setText(nbDays+"");
        }

        //création des boutons jours
        for(int i=0; i<tab_days.length; i++)
        {
            ImageButton btn=new ImageButton(this);
            btn.setImageResource(R.drawable.calendar);
            btn.setBackgroundColor(Color.TRANSPARENT);
            btn.setOnClickListener(onClickDay);
            btn.setId(i);

            container_tableLayout.addView(btn);

        }



*/




        //listeners











    }



//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------


    //bouton ajouter date debut
    View.OnClickListener onClickDay = new View.OnClickListener() {
        public void onClick(View v) {


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
        getMenuInflater().inflate(R.menu.menu_a_affichage_projet, menu);
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
