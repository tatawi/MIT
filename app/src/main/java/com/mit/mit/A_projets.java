package com.mit.mit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class A_projets extends MainActivity {

    //objets de la page
    //private ListView lv_listeAffichage;
    private ImageButton btn_enPreparation;
    private ImageButton btn_projEnCours;
    private ImageButton btn_projFinis;
    private LinearLayout ll_center;

    //variables
    private List<C_Projet> list_projets;
    private C_Participant part;
    private SimpleDateFormat sdf;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //récupération de l'utilisateur
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.userID = extras.getString("userID");
            part=daoparticipant.getParticipantById(userID);
            System.out.println("liste projets : "+this.userID);
        }

        //initialiser objets de la page
        setContentView(R.layout.activity_a_projets);
        btn_enPreparation = (ImageButton)findViewById(R.id.projets_ibtn_enPreparation);
        btn_projEnCours = (ImageButton)findViewById(R.id.projets_ibtn_enCours);
        btn_projFinis = (ImageButton)findViewById(R.id.projets_ibtn_finis);
        ll_center = (LinearLayout)findViewById(R.id.projets_LinearLayoutCenter);

        //listeners
        btn_enPreparation.setOnClickListener(onPreparationProjets);
        btn_projEnCours.setOnClickListener(onEnCoursProjets);
        btn_projFinis.setOnClickListener(onTerminesProjets);
        ll_center.setOrientation(LinearLayout.VERTICAL);

        majAffichage();

    }



    public void majAffichage()
    {
        //trier les projets de l'user only
        list_projets=new ArrayList<C_Projet>();

        for (C_Projet p : daoProjet.getProjets())
        {
            //p.creerLesListes(daoJour, daoparticipant);

            if(p.participantsToString.contains(part.mail))
            //if(p.liste_participants.contains(part))
            {
                list_projets.add(p);
            }
        }

        if(!list_projets.isEmpty())
        {
            setAffichage("Preparation");
        }
    }


    public void setAffichage(String cas)
    {
        ll_center.removeAllViews();
        for(C_Projet p:list_projets)
        {
            if (p.statut.equals(cas))
            {
                //panel global
                LinearLayout LLglobal = new LinearLayout(this);
                LLglobal.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LLglobal.setLayoutParams(LLParams);
                LLglobal.setId(p.id);

                //panel button
                LinearLayout LLleft = new LinearLayout(this);
                LLleft.setOrientation(LinearLayout.HORIZONTAL);
                LLleft.setLayoutParams(LLParams);

                //panel messages
                LinearLayout LLright = new LinearLayout(this);
                LLright.setOrientation(LinearLayout.VERTICAL);
                LLright.setLayoutParams(LLParams);

                //panel description
                LinearLayout LLrightUp = new LinearLayout(this);
                LLrightUp.setOrientation(LinearLayout.HORIZONTAL);
                LLrightUp.setLayoutParams(LLParams);

                //panel heures
                LinearLayout LLrightDown = new LinearLayout(this);
                LLrightDown.setOrientation(LinearLayout.HORIZONTAL);
                LLrightDown.setLayoutParams(LLParams);


                //déclarations champs
                ImageButton img = new ImageButton(this);
                TextView desc = new TextView(this);
                TextView d1 = new TextView(this);
                TextView cout = new TextView(this);

                //personnalisation champs

                switch (p.couleur) {
                    case "noir":
                        desc.setTextColor(Color.parseColor("#5b5b5b"));
                        switch (p.statut) {
                            case "Preparation":
                                img.setImageResource(R.drawable.ic_proj_prep_noir);
                                break;
                            case "Actuel":
                                img.setImageResource(R.drawable.ic_proj_cours_noir);
                                break;
                            case "Fini":
                                img.setImageResource(R.drawable.ic_proj_fini_noir);
                                break;
                        }

                        break;

                    case "rouge":
                        desc.setTextColor(Color.parseColor("#e74c3c"));
                        switch (p.statut) {
                            case "Preparation":
                                img.setImageResource(R.drawable.ic_proj_prep_rouge);
                                break;
                            case "Actuel":
                                img.setImageResource(R.drawable.ic_proj_cours_rouge);
                                break;
                            case "Fini":
                                img.setImageResource(R.drawable.ic_proj_fini_rouge);
                                break;
                        }
                        break;

                    case "jaune":
                        desc.setTextColor(Color.parseColor("#f39c12"));
                        switch (p.statut) {
                            case "Preparation":
                                img.setImageResource(R.drawable.ic_proj_prep_jaune);
                                break;
                            case "Actuel":
                                img.setImageResource(R.drawable.ic_proj_cours_jaune);
                                break;
                            case "Fini":
                                img.setImageResource(R.drawable.ic_proj_fini_jaune);
                                break;
                        }
                        break;

                    case "bleu":
                        desc.setTextColor(Color.parseColor("#2980b9"));
                        switch (p.statut) {
                            case "Preparation":
                                img.setImageResource(R.drawable.ic_proj_prep_bleu);
                                break;
                            case "Actuel":
                                img.setImageResource(R.drawable.ic_proj_cours_bleu);
                                break;
                            case "Fini":
                                img.setImageResource(R.drawable.ic_proj_fini_bleu);
                                break;
                        }
                        break;

                    case "vert":
                        desc.setTextColor(Color.parseColor("#16a085"));
                        switch (p.statut) {
                            case "Preparation":
                                img.setImageResource(R.drawable.ic_proj_prep_vert);
                                break;
                            case "Actuel":
                                img.setImageResource(R.drawable.ic_proj_cours_vert);
                                break;
                            case "Fini":
                                img.setImageResource(R.drawable.ic_proj_fini_vert);
                                break;
                        }
                        break;

                    case "violet":
                        desc.setTextColor(Color.parseColor("#9b59b6"));
                        switch (p.statut) {
                            case "Preparation":
                                img.setImageResource(R.drawable.ic_proj_prep_violet);
                                break;
                            case "Actuel":
                                img.setImageResource(R.drawable.ic_proj_cours_violet);
                                break;
                            case "Fini":
                                img.setImageResource(R.drawable.ic_proj_fini_violet);
                                break;
                        }
                        break;


                }


                //initialisation champs
                sdf = new SimpleDateFormat("dd/MM/yy");
                img.setBackgroundColor(Color.TRANSPARENT);
                desc.setText(p.nom);
                desc.setTextSize(18);
                desc.setPadding(10, 10, 0, 0);
                d1.setText(sdf.format(p.dateDebut) + " - " + sdf.format(p.dateFin));
                d1.setPadding(10, 10, 10, 0);
                cout.setText(" " + p.prixSejour + " € ");
                cout.setPadding(10, 10, 10, 0);

                //ajout des champs aux pannels
                LLleft.addView(img);
                LLrightUp.addView(desc);
                LLrightDown.addView(d1);
                LLrightDown.addView(cout);

                //ajouts panels
                LLright.addView(LLrightUp);
                LLright.addView(LLrightDown);
                LLglobal.addView(LLleft);
                LLglobal.addView(LLright);
                LLglobal.setOnClickListener(onClickLayout);
                ll_center.addView(LLglobal);


            }//if cas
        }//for projets
    }//set affichage








//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------


    //bouton ajouter date debut
    View.OnClickListener onClickLayout = new View.OnClickListener() {
        public void onClick(View v) {
            LinearLayout selectedLL = (LinearLayout) v;

            for (C_Projet p:list_projets)
            {
                if(p.id==selectedLL.getId())
                {
                    Intent intent = new Intent(A_projets.this, A_projet_Preparation.class);
                    intent.putExtra("idEntry", p.nom);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                }
            }

            //DialogFragment newFragment = new D_DatePickerFragment("newproject_debut");
            //newFragment.show(getFragmentManager(), "datePicker");
            //tb_dateDebut.setText(lb_date.getText());
        }
    };

    //click projets en préparation
    View.OnClickListener onPreparationProjets = new View.OnClickListener() {
        public void onClick(View v) {
            setAffichage("Preparation");
            btn_enPreparation.setImageResource(R.drawable.ic_project_prepare_full);
            btn_projEnCours.setImageResource(R.drawable.ic_project_current);
            btn_projFinis.setImageResource(R.drawable.ic_project_ended);

        }
    };

    //click projets en cours
    View.OnClickListener onEnCoursProjets = new View.OnClickListener() {
        public void onClick(View v) {
            setAffichage("Actuel");
            btn_enPreparation.setImageResource(R.drawable.ic_project_prepare);
            btn_projEnCours.setImageResource(R.drawable.ic_project_current_full);
            btn_projFinis.setImageResource(R.drawable.ic_project_ended);
        }
    };

    //click projets terminés
    View.OnClickListener onTerminesProjets = new View.OnClickListener() {
        public void onClick(View v) {
            setAffichage("Fini");
            btn_enPreparation.setImageResource(R.drawable.ic_project_prepare);
            btn_projEnCours.setImageResource(R.drawable.ic_project_current);
            btn_projFinis.setImageResource(R.drawable.ic_project_ended_full);
        }
    };



             /*       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(pContext);
                    alertDialogBuilder.setTitle(p.nom);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();*/

            //Intent inte = new Intent(A_projets.this, A_projet_Preparation.class);
            //startActivity(inte);





//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------



//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_projets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //CREATE A NEW PROJECT
        if (id == R.id.project_new)
        {
            Intent intent = new Intent(A_projets.this, A_project_new.class);
            intent.putExtra("userID", userID);
            startActivity(intent);




        }

        return super.onOptionsItemSelected(item);
    }
}
