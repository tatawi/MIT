package com.mit.mit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;

public class A_jour_Vue extends MainActivity {

    //variables
    private String jourID;
    private SimpleDateFormat sdf;

    private C_Participant part;
    private C_Options options;
    private C_Jour day;

    private TextView[] tb_hour;
    private TextView[] tb_tv1;
    private TextView[] tb_tv2;
    private TextView[] tb_tv3;


    //variables page
    private LinearLayout ll_hour;
    private LinearLayout ll_1;
    private LinearLayout ll_2;
    private LinearLayout ll_3;








    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_jour__vue);


        //Initialisations objets page
        ll_1 = (LinearLayout)findViewById(R.id.jour_vue_line1);
        ll_2 = (LinearLayout)findViewById(R.id.jour_vue_line2);
        ll_3 = (LinearLayout)findViewById(R.id.jour_vue_line3);
        ll_hour = (LinearLayout)findViewById(R.id.jour_vue_hour);

        //listeners
       /* btn_save.setOnClickListener(onSave);
        container_globalLayout = (LinearLayout) findViewById(R.id.jourPrep_layoutContent);
        btn_sort_date.setOnClickListener(onSortDate);
        btn_sort_finished.setOnClickListener(onSortFinished);
        btn_sort_name.setOnClickListener(onSortName);
        btn_sort_cat.setOnClickListener(onSortCat);*/


        //initialisations
        this.options=daoOptions.getOptionByUserId();
        this.part=daoparticipant.getParticipantById(options.userid);
        this.day=daoJour.getJourById(options.jourid);

        this.day.creerLesListes(daoSujet);
        jourID=day.nomJour;




        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(400, 110);





        //create TextBox tabs

        tb_hour = new TextView[48];
        for(int i = 0; i < 48; i++) {
            TextView tb = new TextView(this);
            tb.setLayoutParams(LLParams);
            tb.setTextSize(20);

            if(i%2==0)
            {
                if(i/2<10)  {tb.setText("0"+i/2+"h00");}
                else      {tb.setText(i/2+"h00");}
            }
            else
            {
                tb.setText("");
            }


            tb_hour[i] = tb;
            ll_hour.addView(tb_hour[i]);
        }


        tb_tv1 = new TextView[48];
        for(int i = 0; i < 48; i++) {
            TextView tb = new TextView(this);
            tb.setLayoutParams(LLParams);
            tb.setTextSize(10);
            tb_tv1[i] = tb;
            ll_1.addView(tb_tv1[i]);
        }

        tb_tv2 = new TextView[48];
        for(int i = 0; i < 48; i++) {
            TextView tb = new TextView(this);
            tb.setLayoutParams(LLParams);
            tb.setTextSize(10);
            tb_tv2[i] = tb;
            ll_2.addView(tb_tv2[i]);
        }

        tb_tv3 = new TextView[48];
        for(int i = 0; i < 48; i++) {
            TextView tb = new TextView(this);
            tb.setLayoutParams(LLParams);
            tb.setTextSize(10);
            tb_tv3[i] = tb;
            ll_3.addView(tb_tv3[i]);
        }





        affichage();

    }






//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------











//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

private void affichage()
{
/*
    tb_tv1[0].setBackgroundColor(0xffF44336);
    tb_tv1[0].setText("1");
    tb_tv2[0].setBackgroundColor(0xff03A9F4);
    tb_tv2[0].setText("2");
    tb_tv3[0].setBackgroundColor(0xffCDDC39);
    tb_tv3[0].setText("3");

    tb_tv1[9].setBackgroundColor(0xffF44336);
    tb_tv1[9].setText("9");
    tb_tv2[12].setBackgroundColor(0xff03A9F4);
    tb_tv2[12].setText("12");
    tb_tv3[16].setBackgroundColor(0xffCDDC39);
    tb_tv3[16].setText("16");*/

    //trier la liste
    Collections.sort(this.day.liste_sujets, new Comparator<C_Sujet>() {
        @Override
        public int compare(C_Sujet s1, C_Sujet s2) {

            return s1.heure.compareTo(s2.heure);
        }
    });


    for (C_Sujet s : this.day.liste_sujets)
    {
        SimpleDateFormat sdff;
        s.creerLesListes(daoMessage, daoparticipant);

        //get heure
        sdff = new SimpleDateFormat("HH");
        int heure = Integer.parseInt(sdff.format(s.heure));

        //get min
        sdff = new SimpleDateFormat("mm");
        int min = Integer.parseInt(sdff.format(s.heure));


        //get durée
        int duree = s.duree;


        System.out.println("*********************************");
        System.out.println(s.titre + " : time="+heure+"h:"+min+"m");
        System.out.println("durée="+duree);
        System.out.println("inscrit dans tab "+heure);


        heure=heure*2;
        if(min>=30)
        {
            System.out.println("**commence aprés 30m");
            heure++;
        }

        if(s.type.equals("Transport"))
        {
            System.out.println("Transport : ligne 3");
            writeTab(tb_tv1, heure, min, duree, s.titre, s.type);
        }
        else
        {
            if(heure>1)
            {
                System.out.println("size txt avant : " + tb_tv2[heure - 1].getText().length());
                if(tb_tv2[heure-1].getText().length()<1)
                {
                    System.out.println("ligne 2");
                    writeTab(tb_tv2, heure, min, duree, s.titre, s.type);
                }
                else
                {
                    System.out.println("ligne 1");
                    writeTab(tb_tv3, heure, min, duree, s.titre, s.type);

                }
            }
            else
            {
                System.out.println("!!!00h00");
                System.out.println("ligne 2");
                writeTab(tb_tv2, heure, min, duree, s.titre, s.type);
            }


        }

    }


}

    private void writeTab(TextView[] tb, int heure, int min, int duree, String titre, String type)
    {

        //écrire dans la bonne case selon la demi heure de commencement
       /* heure=heure*2;
        if(min>=30)
        {
            System.out.println("**commence aprés 30m");
            heure++;
        }*/
        System.out.println("**position first ecriture : " + heure);
        tb[heure].setText(titre);
        tb[heure].setTextSize(20);

        switch (type) {
            case "Transport":
                tb[heure].setBackgroundColor(0xffF44336);
                break;

            case "Repas":
                tb[heure].setBackgroundColor(0xff2196F3);
                break;

            case "Visite":
                tb[heure].setBackgroundColor(0xff4CAF50);
                break;

            case "Logement":
                tb[heure].setBackgroundColor(0xffFFEB3B);
                break;

            case "Loisir":
                tb[heure].setBackgroundColor(0xff673AB7);
                break;

            case "Libre":
                tb[heure].setBackgroundColor(0x55000000);
                break;
        }





        //calcul de la durée en heure
        int ajout=duree/60;
        System.out.println("**durée de : "+ajout+" heures");
        //calcul de la durée restantes (sans les heures) en minuttes
        int minRest = duree-ajout*60;
        System.out.println("**min restantes : "+minRest);
        //x2 car 1h = 2 case de tableau (30m+30m)
        ajout=ajout*2;

        //si les min sont > 30, on utilise la case suivante
        if(minRest>=30){ajout++;}

        //si la tache dure moins de 30m, on insére quand meme une case

        System.out.println("**durée total en tab : "+ajout);

        //on a déja ecrit 1 fois :
        ajout--;

        for(int i=1; i<=ajout; i++)
        {
            System.out.println("dure +1");
            tb[heure+i].setTextSize(20);
            tb[heure+i].setText("_");
            switch (type) {
                case "Transport":
                    tb[heure+i].setBackgroundColor(0xffF44336);
                    tb[heure+i].setTextColor(0xffF44336);
                    break;

                case "Repas":
                    tb[heure+i].setBackgroundColor(0xff2196F3);
                    tb[heure+i].setTextColor(0xff2196F3);
                    break;

                case "Visite":
                    tb[heure+i].setBackgroundColor(0xff4CAF50);
                    tb[heure+i].setTextColor(0xff4CAF50);
                    break;

                case "Logement":
                    tb[heure+i].setBackgroundColor(0xffFFEB3B);
                    tb[heure+i].setTextColor(0xffFFEB3B);
                    break;

                case "Loisir":
                    tb[heure+i].setBackgroundColor(0xff673AB7);
                    tb[heure+i].setTextColor(0xff673AB7);
                    break;

                case "Libre":
                    tb[heure+i].setBackgroundColor(0x55000000);
                    tb[heure+i].setTextColor(0x55000000);
                    break;
            }
        }


    }











//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_jour__vue, menu);
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
