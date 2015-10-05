package com.mit.mit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class A_projets extends MainActivity {

    //objets de la page
    private ListView lv_listeAffichage;
    private ImageButton btn_enPreparation;
    private ImageButton btn_projEnCours;
    private ImageButton btn_projFinis;

    //variables
    private List<C_Projet> list_projets;

    private List<String> listeStringParticipants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //initialiser objets de la page
        setContentView(R.layout.activity_a_projets);
        lv_listeAffichage = (ListView)findViewById(R.id.projets_listViw);
        btn_enPreparation = (ImageButton)findViewById(R.id.projets_ibtn_enPreparation);
        btn_projEnCours = (ImageButton)findViewById(R.id.projets_ibtn_enCours);
        btn_projFinis = (ImageButton)findViewById(R.id.projets_ibtn_finis);

        listeStringParticipants= new ArrayList<String>();
        list_projets=daoProjet.getProjets();

        //listeners
        btn_enPreparation.setOnClickListener(onPreparationProjets);
        btn_projEnCours.setOnClickListener(onEnCoursProjets);
        btn_projFinis.setOnClickListener(onTerminesProjets);
        lv_listeAffichage.setOnItemClickListener(onListClick);

        //initialisation de la liste
        for(C_Projet p:list_projets)
        {
            if(p.statut.equals("Preparation"))
            {
                listeStringParticipants.add(p.nom);
            }
        }
        majListe();


    }





    private void majListe()
    {

        if(listeStringParticipants.isEmpty())
        {
            listeStringParticipants.add("Pas de projet");
        }

        lv_listeAffichage.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeStringParticipants));
    }


//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //click projets en préparation
    View.OnClickListener onPreparationProjets = new View.OnClickListener() {
        public void onClick(View v) {

            listeStringParticipants.clear();

            for(C_Projet p:list_projets)
            {
                if(p.statut.equals("Preparation"))
                {
                    listeStringParticipants.add(p.nom);
                }
            }
            majListe();


        }
    };

    //click projets en cours
    View.OnClickListener onEnCoursProjets = new View.OnClickListener() {
        public void onClick(View v) {
            listeStringParticipants.clear();

            for(C_Projet p:list_projets)
            {
                if(p.statut.equals("Actuel"))
                {
                    listeStringParticipants.add(p.nom);
                }
            }
            majListe();
        }
    };

    //click projets terminés
    View.OnClickListener onTerminesProjets = new View.OnClickListener() {
        public void onClick(View v) {

            listeStringParticipants.clear();

            for(C_Projet p:list_projets)
            {
                if(p.statut.equals("Fini"))
                {
                    listeStringParticipants.add(p.nom);
                }
            }
            majListe();
        }
    };

    //liste on click
    AdapterView.OnItemClickListener onListClick =new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            C_Projet p=daoProjet.getProjetByName(listeStringParticipants.get(position));

            Intent intent = new Intent(A_projets.this, A_projet_Preparation.class);
            intent.putExtra("idEntry", p.nom);
            startActivity(intent);

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


        }

    };








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
            Intent intent = new Intent(A_projets.this, A_newProject.class);
            startActivity(intent);




        }

        return super.onOptionsItemSelected(item);
    }
}
