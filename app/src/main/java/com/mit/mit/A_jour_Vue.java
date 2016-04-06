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

        tb_hour = new TextView[24];
        for(int i = 0; i < 24; i++) {
            TextView tb = new TextView(this);
            tb.setLayoutParams(LLParams);
            tb.setTextSize(20);

            if(i<10)  {tb.setText("0"+i+"h00");}
            else      {tb.setText(i+"h00");}

            tb_hour[i] = tb;
            ll_hour.addView(tb_hour[i]);
        }


        tb_tv1 = new TextView[24];
        for(int i = 0; i < 24; i++) {
            TextView tb = new TextView(this);
            tb.setLayoutParams(LLParams);
            tb.setTextSize(20);
            tb_tv1[i] = tb;
            ll_1.addView(tb_tv1[i]);
        }

        tb_tv2 = new TextView[24];
        for(int i = 0; i < 24; i++) {
            TextView tb = new TextView(this);
            tb.setLayoutParams(LLParams);
            tb.setTextSize(20);
            tb_tv2[i] = tb;
            ll_2.addView(tb_tv2[i]);
        }

        tb_tv3 = new TextView[24];
        for(int i = 0; i < 24; i++) {
            TextView tb = new TextView(this);
            tb.setLayoutParams(LLParams);
            tb.setTextSize(20);
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
        if(min>30&&heure<24){heure++;} //adjust hour

        //get durée
        int duree = s.duree/60;


        System.out.println("*********************************");
        System.out.println(s.titre + " : time="+heure+"h:"+min+"m");
        System.out.println("durée="+duree);
        System.out.println("inscrit dans tab "+heure);



        tb_tv1[heure].setBackgroundColor(0xffF44336);
        tb_tv1[heure].setText(s.titre);
        for(int i=1; i<duree; i++)
        {
            System.out.println("dure +1");
            tb_tv1[heure+i].setBackgroundColor(0xffF44336);
        }


        /*
        F44336

        03A9F4

        CDDC39
         */
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
