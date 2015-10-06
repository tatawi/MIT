package com.mit.mit;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
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
            this.projet.creerLesListes(daoJour, daoparticipant);
            setTitle(this.projet.nom);
            lb_description.setText(this.projet.description);
            lb_cout.setText(this.projet.prixSejour + " €");
            nbDays=this.projet.liste_jours.size();
            //tab_days=new ImageButton[(int)nbDays];

            lb_cout.setText("" + this.projet.liste_jours.size());

            //lb_description.setText("" + this.projet.dateDebut.toString() + " " + this.projet.dateFin.toString());



        }



        //container
        container_tableLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout LL = new LinearLayout(this);
        LL.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LL.setLayoutParams(LLParams);




        //AFFICHAGE
        Date currentDate=this.projet.dateDebut;

        //ADDING BLANK IMAGES
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-2;
        for(int j=0; j<dayOfWeek; j++)
        {
            System.out.print("ADD BLANK");
            System.out.println();
            ImageButton btn=new ImageButton(this);
            btn.setImageResource(R.drawable.ic_cal_blank);
            btn.setBackgroundColor(Color.TRANSPARENT);
            LL.addView(btn);
        }

        for(int i=dayOfWeek; i<nbDays+dayOfWeek; i++)
        {
            if(i>0 && i%7==0)
            {
                System.out.print("RETOUR LIGNE");
                System.out.println();
                container_tableLayout.addView(LL);
                LL = new LinearLayout(this);
                LL.setOrientation(LinearLayout.HORIZONTAL);
                LL.setLayoutParams(LLParams);
            }


            //ADD BUTTON
            ImageButton btn=new ImageButton(this);
            String fileName=getImageNameForDay(currentDate);
            int identifier = getResources().getIdentifier(fileName, "drawable", getPackageName());
            btn.setImageResource(identifier);
            btn.setBackgroundColor(Color.TRANSPARENT);
            LL.addView(btn);
            System.out.print("ADD BUTTON "+fileName );
            System.out.println();


            //INCREMENT DAY
            c.setTime(currentDate);
            c.add(Calendar.DATE, 1);
            currentDate = c.getTime();


        }
        container_tableLayout.addView(LL);







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

    private String getImageNameForDay(Date d)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        System.out.print("------------------------------------------");
        System.out.println();
        System.out.print("date : "+sdf.format(d));
        System.out.println();

        for(C_Jour j:this.projet.liste_jours)
        {

            System.out.print(sdf.format(d)+" =? "+sdf.format(j.jour));
            System.out.println();
            if(sdf.format(d).equals(sdf.format(j.jour)))
            {
                System.out.print("OK");
                System.out.println();
                sdf = new SimpleDateFormat("dd");

                if(j.isNotification(me))
                {
                    return "ic_cal_red_"+sdf.format(j.jour);
                }
                else
                {
                    return "ic_cal_black_"+sdf.format(j.jour);
                }
            }


        }


        return "ic_cal_black_01";
    }
















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
