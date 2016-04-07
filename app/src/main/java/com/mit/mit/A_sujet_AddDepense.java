package com.mit.mit;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class A_sujet_AddDepense extends MainActivity
{

    //objets de la page
    private ImageButton btn_ok;
    private TextView lb_montant;
    private TextView lb_montantRestant;
    private LinearLayout ll_dep;

    private TextView[] tb_lbNom;
    private EditText[] tb_tbPrix;
    //private SeekBar[] tb_sbPrix;

    //android:id="@+id/sujetPreparation_lb_description"
    //sujetPreparation_btn_type





    //variables
    private C_Options options;
    private C_Participant part;
    private C_Projet projet;
    private C_Jour jour;
    private C_Sujet sujet;


    private SimpleDateFormat sdf;

    private String nomJour;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        //initialisation objet page
        setContentView(R.layout.activity_a_sujet__add_depense);
        ll_dep = (LinearLayout) findViewById(R.id.sujetDep_ll);
        lb_montant = (TextView) findViewById(R.id.sujetDep_montant);
        btn_ok = (ImageButton) findViewById(R.id.sujetDep_btn_ok);
        lb_montantRestant = (TextView) findViewById(R.id.sujetDep_montantRestant);

        //listeners
       btn_ok.setOnClickListener(onBtnOk);




        //initialisation variables
        sdf = new SimpleDateFormat("HH:MM");

        this.options=daoOptions.getOptionByUserId();

        this.part=daoparticipant.getParticipantById(options.userid);


        this.projet = daoProjet.getProjetByName(options.projetid);
        this.projet.creerLesListes(daoJour, daoparticipant);

        this.sujet=daoSujet.getSujetById(options.sujetid);
        this.sujet.creerLesListes(daoMessage, daoparticipant);

        this.nomJour =projet.nom+"_"+this.sujet.idSujet.split("_")[1];
        this.jour=daoJour.getJourById(options.jourid);
        this.jour.creerLesListes(daoSujet);

        System.out.println("********************************");
        System.out.println("Add depenses");

        //affichage
        majInterface();



    }



//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //bouton creer
    View.OnClickListener onBtnOk = new View.OnClickListener() {
        public void onClick(View v) {

            System.out.println("VALIDATION");
            //Intent intent = new Intent(A_sujet_Work.this, A_sujet_Map_view.class);
            //startActivity(intent);

            for (int i=0; i<tb_tbPrix.length; i++)
            {
                C_Participant part;
                String nomCourant = tb_lbNom[i].getText().toString();
                Double montant = Double.parseDouble(tb_tbPrix[i].getText().toString());

                //ini listes
                sujet.liste_quiApaye.clear();
                sujet.liste_combienApaye.clear();

                for (C_Participant p :sujet.liste_participent)
                {
                    if(p.prenom.equals(nomCourant))
                    {
                        System.out.println("ajout de : "+nomCourant);
                        System.out.println("a payé : " +montant);
                        System.out.println("--------------");
                        sujet.liste_quiApaye.add(p);
                        sujet.liste_combienApaye.add(montant);
                    }
                }

                sujet.creerLesListes(daoMessage, daoparticipant);
                daoSujet.modifier(sujet, options.online);

            }

            Intent intent = new Intent(A_sujet_AddDepense.this, A_sujet_Work.class);
            startActivity(intent);

        }
    };


    TextWatcher onTextChange = new TextWatcher()
    {
        public void afterTextChanged(Editable s) {

            lb_montantRestant.setText(""+montantRestantApayer());


        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
};












//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------


    //MAJ de l'interface avec les données
    public void majInterface()
    {
        //ini tab
        tb_lbNom = new TextView[sujet.liste_participent.size()];
        tb_tbPrix = new EditText[sujet.liste_participent.size()];


        //Titre
        setTitle("[" + this.sujet.type + "] " + this.sujet.titre);

        //champs
        lb_montant.setText(""+this.sujet.prix);
        lb_montantRestant.setText(""+montantRestantApayer());



        //afficher layout participants
        ll_dep.removeAllViews();

        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int parcours=0;
        for (C_Participant p : sujet.liste_participent)
        {

            //layout global
            LinearLayout LLgen = new LinearLayout(this);
            LLgen.setOrientation(LinearLayout.HORIZONTAL);
            LLgen.setLayoutParams(LLParams);

                //image gauche
                ImageButton img = new ImageButton(this);
                img.setImageResource(R.drawable.ic_sujetdep_user);
                img.setBackgroundColor(Color.TRANSPARENT);

                //layout infos
                LinearLayout LLinfos = new LinearLayout(this);
                LLinfos.setOrientation(LinearLayout.VERTICAL);
                LLinfos.setLayoutParams(LLParams);

            //layout txt
            LinearLayout LLtxt = new LinearLayout(this);
            LLinfos.setOrientation(LinearLayout.HORIZONTAL);
            LLinfos.setLayoutParams(LLParams);

            //texte nom
            tb_lbNom[parcours] = new TextView(this);
            tb_lbNom[parcours].setText("" + p.prenom);
            tb_lbNom[parcours].setPadding(0, 0, 10, 0);

            //texte
            TextView txt = new TextView(this);
            txt.setText(" va payer :");
            txt.setPadding(0, 0, 10, 0);

            LLtxt.addView(tb_lbNom[parcours]);
            LLtxt.addView(txt);


            //edit text
            tb_tbPrix[parcours] = new EditText(this);
            tb_tbPrix[parcours].addTextChangedListener(onTextChange);
            tb_tbPrix[parcours].setInputType(InputType.TYPE_CLASS_NUMBER);
            tb_tbPrix[parcours].setText("0");

            LLinfos.addView(LLtxt);
            LLinfos.addView(tb_tbPrix[parcours]);

            LLgen.addView(img);
            LLgen.addView(LLinfos);

            ll_dep.addView(LLgen);
            parcours++;

            //bar
            /*SeekBar bar = new SeekBar(this);
            bar.setMax((int)sujet.prix);
            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // TODO Auto-generated method stub

                    tb_lbPrix[parcours].setText(""+bar.getProgress());
                }
            });*/






//sb_hour.setOnSeekBarChangeListener(new seekBarListener());

/*
            private class seekBarListener implements SeekBar.OnSeekBarChangeListener {

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    int hour = sb_hour.getProgress();
                    int min = sb_min.getProgress();
                    int duree = sb_duree.getProgress();

                    lb_time.setText(hour+"h"+min);

                    hour=duree/60;
                    min=duree-hour*60;
                    lb_duree.setText(hour+"h"+min);

                }

                public void onStartTrackingTouch(SeekBar seekBar) {}

                public void onStopTrackingTouch(SeekBar seekBar) {}

            }
*/



        }
    }




private double montantRestantApayer()
{
    double montantPaye=0;
    for (int i=0; i<tb_tbPrix.length; i++)
    {
        try {
            montantPaye = montantPaye + Integer.parseInt(tb_tbPrix[i].getText().toString());
        }
        catch (Exception ex)
        {

        }
    }

    return sujet.prix-montantPaye;



}













//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_sujet__add_depense, menu);
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
