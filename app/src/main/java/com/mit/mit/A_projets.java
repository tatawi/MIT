package com.mit.mit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

public class A_projets extends MainActivity {

    //objets de la page
    private ListView lv_listeAffichage;
    private ImageButton btn_enPreparation;
    private ImageButton btn_projEnCours;
    private ImageButton btn_projFinis;

    //variables
    private List<C_Projet> list_projEnPreparation;
    private List<C_Projet> list_projEnCours;
    private List<C_Projet> list_projFinis;
    private String[] tab_listViewElements= {" "};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //initialiser objets de la page
        setContentView(R.layout.activity_a_projets);
        lv_listeAffichage = (ListView)findViewById(R.id.projets_listViw);
        btn_enPreparation = (ImageButton)findViewById(R.id.projets_ibtn_enPreparation);
        btn_projEnCours = (ImageButton)findViewById(R.id.projets_ibtn_enCours);
        btn_projFinis = (ImageButton)findViewById(R.id.projets_ibtn_finis);

        //listeners
        btn_enPreparation.setOnClickListener(onPreparationProjets);
        btn_projEnCours.setOnClickListener(onEnCoursProjets);
        btn_projFinis.setOnClickListener(onTerminesProjets);

    }




private void majAdapter()
{
    lv_listeAffichage.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tab_listViewElements));
}

private void viderTableau()
{
    for(String str:tab_listViewElements)
    {
        str=null;
    }

}


//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //click projets en préparation
    View.OnClickListener onPreparationProjets = new View.OnClickListener() {
        public void onClick(View v) {
            int i=0;
            viderTableau();
            list_projEnPreparation=daoProjet.getProjets();



            for(C_Projet p:list_projEnPreparation)
            {
                //if(p.xx=="preparation"){
                tab_listViewElements[i]=p.nom;
                i++;
            }
            if (tab_listViewElements[0]==null)
            {
                tab_listViewElements[0]="Pas de projets";
            }

            majAdapter();

        }
    };

    //click projets en cours
    View.OnClickListener onEnCoursProjets = new View.OnClickListener() {
        public void onClick(View v) {
            tab_listViewElements=null;
            tab_listViewElements[0]="Pas de projets";
            majAdapter();

        }
    };

    //click projets terminés
    View.OnClickListener onTerminesProjets = new View.OnClickListener() {
        public void onClick(View v) {
            tab_listViewElements=null;
            tab_listViewElements[0]="Pas de projets";
            majAdapter();

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
        if (id == R.id.project_new) {





        }

        return super.onOptionsItemSelected(item);
    }
}
