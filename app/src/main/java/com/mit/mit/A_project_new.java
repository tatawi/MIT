package com.mit.mit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class A_project_new extends MainActivity {

    //Objets de la page
    private EditText tb_nom;
    private EditText tb_description;
    private EditText tb_dateDebut;
    private ImageButton btn_dateDebut;
    private EditText tb_dateFin;
    private ImageButton btn_dateFin;
    private EditText tb_participant;
    private ImageButton btn_creer;
    private ImageButton btn_adduser;
    private TextView lb_erreurAdd;
    private ListView lv_listViewPart;
    private ImageButton btn_couleur_noir;
    private ImageButton btn_couleur_rouge;
    private ImageButton btn_couleur_bleu;
    private ImageButton btn_couleur_jaune;
    private ImageButton btn_couleur_vert;
    private ImageButton btn_couleur_violet;

    //variables
    private String v_nom;
    private String v_description;
    private Date v_dateDebut;
    private Date v_dateFin;
    private float v_prixSejour=0;
    private String v_couleur="noir";
    private transient List<C_Jour> v_liste_jours;
    private transient List<C_Participant> v_liste_participantsProjet;
    private String userID;
    private transient List<C_Participant> v_liste_allParticipants;
    private C_Participant part;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //initialisations
        setContentView(R.layout.activity_a_project_new);
        tb_nom = (EditText) findViewById(R.id.newProject_tb_nom);
        tb_description = (EditText) findViewById(R.id.newProject_tb_description);
        tb_dateDebut = (EditText) findViewById(R.id.newProject_tb_debut);
        btn_dateDebut = (ImageButton) findViewById(R.id.newProject_btn_dateDebut);
        tb_dateFin = (EditText) findViewById(R.id.newProject_tb_fin);
        btn_dateFin = (ImageButton) findViewById(R.id.newProject_btn_dateFin);
        tb_participant = (EditText) findViewById(R.id.newProject_tb_participants);
        btn_creer = (ImageButton) findViewById(R.id.newProject_btn_addProject);
        btn_adduser = (ImageButton) findViewById(R.id.newProject_btn_addParticipant);
        lv_listViewPart = (ListView) findViewById(R.id.newProject_lv_listView);
        btn_couleur_noir= (ImageButton) findViewById(R.id.newProject_btn_color_noir);
        btn_couleur_rouge= (ImageButton) findViewById(R.id.newProject_btn_color_rouge);
        btn_couleur_bleu= (ImageButton) findViewById(R.id.newProject_btn_color_bleu);
        btn_couleur_jaune= (ImageButton) findViewById(R.id.newProject_btn_color_jaune);
        btn_couleur_vert= (ImageButton) findViewById(R.id.newProject_btn_color_vert);
        btn_couleur_violet= (ImageButton) findViewById(R.id.newProject_btn_color_violet);
        lb_erreurAdd = (TextView) findViewById(R.id.newProject_lb_texteAjoutPart);

        //listeners
        btn_dateDebut.setOnClickListener(onClickDebutDate);
        btn_dateFin.setOnClickListener(onClickFinDate);
        btn_adduser.setOnClickListener(onAddUser);
        btn_creer.setOnClickListener(onCreateProjet);
        btn_couleur_noir.setOnClickListener(onColorClick);
        btn_couleur_rouge.setOnClickListener(onColorClick);
        btn_couleur_bleu.setOnClickListener(onColorClick);
        btn_couleur_jaune.setOnClickListener(onColorClick);
        btn_couleur_vert.setOnClickListener(onColorClick);
        btn_couleur_violet.setOnClickListener(onColorClick);



        // ini
        v_liste_jours=new ArrayList<C_Jour>();
        v_liste_participantsProjet=new ArrayList<C_Participant>();
        v_liste_allParticipants=new ArrayList<C_Participant>();
        v_liste_allParticipants=daoparticipant.getParticipants();
        part=new C_Participant();
        lb_erreurAdd.setVisibility(View.GONE);

        //getRessources
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.userID = extras.getString("userID");
            part=daoparticipant.getParticipantById(userID);

            //ajout participant actuelle au projet
            v_liste_participantsProjet.add(part);
            majListView();
        }
    }






//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //BOUTTON creer
    View.OnClickListener onCreateProjet = new View.OnClickListener() {
        public void onClick(View v) {
            //RECUPERATIONS DES INFOS POUR VARIABLES PROJET
            v_nom=tb_nom.getText().toString();
            v_description=tb_description.getText().toString();

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                v_dateDebut = formatter.parse(tb_dateDebut.getText().toString());
                v_dateFin = formatter.parse(tb_dateFin.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            v_prixSejour=0;
            v_liste_jours=creerListeJours(v_dateDebut, v_dateFin);



            //CREATION DU PROJET
            C_Projet newprojet=new C_Projet(v_nom, v_description, v_dateDebut, v_dateFin,v_couleur);
            newprojet.setListe_jours(v_liste_jours);
            newprojet.setListe_participants(v_liste_participantsProjet);
            newprojet.listeToString();


            //SAUVEGARDE
            daoProjet.ajouter(newprojet, true);

            for (C_Jour j:v_liste_jours)
            {
                daoJour.ajouter(j, true);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");


            }




            //RETOUR PAGE PRECEDENTE
            Intent intent = new Intent(A_project_new.this, A_projets.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }
    };




    //bouton ajouter date debut
    View.OnClickListener onClickDebutDate = new View.OnClickListener() {
        public void onClick(View v) {
            DialogFragment newFragment = new D_DatePickerFragment("newproject_debut");
            newFragment.show(getFragmentManager(), "datePicker");
            //tb_dateDebut.setText(lb_date.getText());
        }
    };




    //bouton ajouter date fin
    View.OnClickListener onClickFinDate = new View.OnClickListener() {
        public void onClick(View v) {
            DialogFragment newFragment = new D_DatePickerFragment("newproject_fin");
            newFragment.show(getFragmentManager(), "datePicker");
            //tb_dateFin.setText(lb_date.getText());
        }
    };



    //bouton ajouter participant
    View.OnClickListener onAddUser = new View.OnClickListener() {
        public void onClick(View v) {

            try
            {
                v_liste_participantsProjet.add(daoparticipant.getParticipantById(tb_participant.getText().toString()));
            }
            catch (Exception e)
            {
                lb_erreurAdd.setVisibility(View.VISIBLE);
                lb_erreurAdd.setText(e.getMessage());
            }
            majListView();
            tb_participant.setText("");

            /*for(C_Participant p:v_liste_allParticipants)
            {
                if(p.mail.equals(tb_participant.getText().toString()))
                {
                    v_liste_participantsProjet.add(p);
                    majListView();
                    tb_participant.setText("");

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(pContext);
                    alertDialogBuilder.setTitle(" "+v_liste_participants.size()+ " "+p.mail+" "+tb_participant.getText().toString());
                    alertDialogBuilder
                            .setCancelable(false)
                            .setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            }*/



        }
    };



    //BUTONS COLORS
    View.OnClickListener onColorClick = new View.OnClickListener() {
        public void onClick(View v) {
            ImageButton btn = (ImageButton) v;

            if (btn.equals(btn_couleur_bleu))
            {
                System.out.println("bleu");
                v_couleur="bleu";
                btn_couleur_bleu.setImageResource(R.drawable.ic_circle_full_bleu);
                btn_couleur_noir.setImageResource(R.drawable.ic_circle_noir);
                btn_couleur_rouge.setImageResource(R.drawable.ic_circle_rouge);
                btn_couleur_jaune.setImageResource(R.drawable.ic_circle_jaune);
                btn_couleur_vert.setImageResource(R.drawable.ic_circle_vert);
                btn_couleur_violet.setImageResource(R.drawable.ic_circle_violet);
            }

            if (btn.equals(btn_couleur_noir))
            {
                System.out.println("noir");
                v_couleur="noir";
                btn_couleur_bleu.setImageResource(R.drawable.ic_circle_bleu);
                btn_couleur_noir.setImageResource(R.drawable.ic_circle_full_noir);
                btn_couleur_rouge.setImageResource(R.drawable.ic_circle_rouge);
                btn_couleur_jaune.setImageResource(R.drawable.ic_circle_jaune);
                btn_couleur_vert.setImageResource(R.drawable.ic_circle_vert);
                btn_couleur_violet.setImageResource(R.drawable.ic_circle_violet);
            }

            if (btn.equals(btn_couleur_rouge))
            {
                System.out.println("rouge");
                v_couleur="rouge";
                btn_couleur_bleu.setImageResource(R.drawable.ic_circle_bleu);
                btn_couleur_noir.setImageResource(R.drawable.ic_circle_noir);
                btn_couleur_rouge.setImageResource(R.drawable.ic_circle_full_rouge);
                btn_couleur_jaune.setImageResource(R.drawable.ic_circle_jaune);
                btn_couleur_vert.setImageResource(R.drawable.ic_circle_vert);
                btn_couleur_violet.setImageResource(R.drawable.ic_circle_violet);
            }

            if (btn.equals(btn_couleur_jaune))
            {
                System.out.println("jaune");
                v_couleur="jaune";
                btn_couleur_bleu.setImageResource(R.drawable.ic_circle_bleu);
                btn_couleur_noir.setImageResource(R.drawable.ic_circle_noir);
                btn_couleur_rouge.setImageResource(R.drawable.ic_circle_rouge);
                btn_couleur_jaune.setImageResource(R.drawable.ic_circle_full_jaune);
                btn_couleur_vert.setImageResource(R.drawable.ic_circle_vert);
                btn_couleur_violet.setImageResource(R.drawable.ic_circle_violet);
            }

            if (btn.equals(btn_couleur_vert))
            {
                System.out.println("vert");
                v_couleur="vert";
                btn_couleur_bleu.setImageResource(R.drawable.ic_circle_bleu);
                btn_couleur_noir.setImageResource(R.drawable.ic_circle_noir);
                btn_couleur_rouge.setImageResource(R.drawable.ic_circle_rouge);
                btn_couleur_jaune.setImageResource(R.drawable.ic_circle_jaune);
                btn_couleur_vert.setImageResource(R.drawable.ic_circle_full_vert);
                btn_couleur_violet.setImageResource(R.drawable.ic_circle_violet);
            }

            if (btn.equals(btn_couleur_violet))
            {
                System.out.println("violet");
                v_couleur="violet";
                btn_couleur_bleu.setImageResource(R.drawable.ic_circle_bleu);
                btn_couleur_noir.setImageResource(R.drawable.ic_circle_noir);
                btn_couleur_rouge.setImageResource(R.drawable.ic_circle_rouge);
                btn_couleur_jaune.setImageResource(R.drawable.ic_circle_jaune);
                btn_couleur_vert.setImageResource(R.drawable.ic_circle_vert);
                btn_couleur_violet.setImageResource(R.drawable.ic_circle_full_violet);
            }

        }
    };



//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    private void majListView()
    {
        //String[] listeStrings = {""};
        List<String> listeStringParticipants = new ArrayList<String>();
        int i=0;
        for(C_Participant p:v_liste_participantsProjet)
        {
            //listeStrings[i]=p.prenom+" "+p.nom;
            listeStringParticipants.add(p.prenom+" "+p.nom);
        }

        lv_listViewPart.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeStringParticipants));
    }


    private List<C_Jour> creerListeJours(Date debut, Date fin)
    {
        List<C_Jour>maListeJours=new ArrayList<C_Jour>();
        Date jourEnCour=debut;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

        for(int i=0; i<getDaysBetweenDates(debut, fin); i++)
        {
            //sdf = new SimpleDateFormat("dd-MM");
            C_Jour jour = new C_Jour(v_nom+"_"+sdf.format(jourEnCour), jourEnCour);
            maListeJours.add(jour);
            System.out.print("Add day : "+jour.nomJour+" | "+sdf.format(jour.jour));
            System.out.println();

            //INCREMENT DAY
            Calendar cal = Calendar.getInstance();
            cal.setTime(jourEnCour);
            cal.add(Calendar.DATE, 1);

            jourEnCour = cal.getTime();
        }

        //public C_Jour(String nomJour, Date jour)



        return maListeJours;
    }

    private static double getDaysBetweenDates(Date theEarlierDate, Date theLaterDate) {
        double result = Double.POSITIVE_INFINITY;
        if (theEarlierDate != null && theLaterDate != null) {
            final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
            Calendar aCal = Calendar.getInstance();
            aCal.setTime(theEarlierDate);
            long aFromOffset = aCal.get(Calendar.DST_OFFSET);
            aCal.setTime(theLaterDate);
            long aToOffset = aCal.get(Calendar.DST_OFFSET);
            long aDayDiffInMili = (theLaterDate.getTime() + aToOffset) - (theEarlierDate.getTime() + aFromOffset);
            result = ((double) aDayDiffInMili / MILLISECONDS_PER_DAY);
        }
        return result;
    }








//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_new_project, menu);
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
