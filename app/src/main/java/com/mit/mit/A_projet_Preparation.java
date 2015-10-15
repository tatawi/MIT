package com.mit.mit;

import android.app.DialogFragment;
import android.content.Intent;
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

/*          System.out.print("ADD BLANK");
            System.out.println();
            */

    //objets de la page
    private TextView lb_description;
    private TextView lb_cout;
    private TextView lb_text;
    private LinearLayout container_tableLayout;

    //variables
    private C_Projet projet;
    private double nbDays;
    private String tab_correspButtons[];
    private SimpleDateFormat sdf;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //initialiser objets de la page
        setContentView(R.layout.activity_a_affichage_projet);
        lb_description = (TextView) findViewById(R.id.projetPrep_lb_description);
        lb_cout = (TextView) findViewById(R.id.projetPrep_lb_cout);
        lb_text= (TextView) findViewById(R.id.projetPrep_lb_txtCout);
        container_tableLayout = (LinearLayout) findViewById(R.id.projetPrep_tableLayout);


        //récupération du projet
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            this.userID = extras.getString("userID");
            System.out.println("projets preparation : "+this.userID);

            //chargement des données du projet
            this.projet = daoProjet.getProjetByName(extras.getString("idEntry"));
            this.projet.creerLesListes(daoJour, daoparticipant);
            setTitle(this.projet.nom);
            lb_description.setText(this.projet.description);
            lb_cout.setText(this.projet.prixSejour + " €");
            nbDays=this.projet.liste_jours.size();
            tab_correspButtons=new String[(int)nbDays+7];
        }


        /*---------------------------
        *AFFICHAGE
        */
        Date currentDate=this.projet.dateDebut;
        Calendar c = Calendar.getInstance();

        container_tableLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout LL = new LinearLayout(this);
        LL.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LL.setLayoutParams(LLParams);


        //ADDING BLANK IMAGES
        c.setTime(currentDate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-2;
        for(int j=0; j<dayOfWeek; j++)
        {

            ImageButton btn=new ImageButton(this);
            btn.setImageResource(R.drawable.ic_cal_blank);
            btn.setBackgroundColor(Color.TRANSPARENT);
            LL.addView(btn);
        }

        //ADD IMAGES BUTTON
        for(int i=dayOfWeek; i<nbDays+dayOfWeek; i++)
        {
            //variables
            ImageButton btn=new ImageButton(this);
            sdf = new SimpleDateFormat("dd/MM/yy");
            String str_buttonID=getDayIDFromDate(currentDate);
            String fileName=getImageNameForDay(currentDate);
            int identifier = getResources().getIdentifier(fileName, "drawable", getPackageName());

            //ajoute la ligne suivante si ligne actuelle pleine
            if(i>0 && i%7==0)
            {
                container_tableLayout.addView(LL);
                LL = new LinearLayout(this);
                LL.setOrientation(LinearLayout.HORIZONTAL);
                LL.setLayoutParams(LLParams);
            }

            //add listener
            if (!str_buttonID.equals(""))
            {
                tab_correspButtons[i]=getDayIDFromDate(currentDate);
                btn.setId(i);
                btn.setOnClickListener(onClickDay);
            }

            btn.setImageResource(identifier);
            btn.setBackgroundColor(Color.TRANSPARENT);
            LL.addView(btn);


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
    View.OnClickListener onClickDay = new View.OnClickListener()
    {
        public void onClick(View v) {
            ImageButton button = (ImageButton)v;
            String dayID=tab_correspButtons[button.getId()];

            Intent intent = new Intent(A_projet_Preparation.this, A_jour_Preparation.class);
            intent.putExtra("idEntry", dayID);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }
    };











//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------


    private String getDayIDFromDate(Date d)
    {
        sdf = new SimpleDateFormat("dd/MM/yy");

        for(C_Jour j:this.projet.liste_jours)
        {
            if(sdf.format(d).equals(sdf.format(j.jour)))
            {
                return j.nomJour;
            }

        }
        return "";
    }


    private String getImageNameForDay(Date d)
    {
        sdf = new SimpleDateFormat("dd/MM/yy");


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



        return super.onOptionsItemSelected(item);
    }
}
