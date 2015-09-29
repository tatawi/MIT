package com.mit.mit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class A_newProject extends FragmentActivity {

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
    private ListView lv_listViewPart;

    //variables



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialisations
        setContentView(R.layout.activity_a_new_project);
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


        //listeners
        btn_dateDebut.setOnClickListener(onClickDebutDate);
        btn_dateFin.setOnClickListener(onClickFinDate);





    }






//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //click projets en cours
    View.OnClickListener onClickDebutDate = new View.OnClickListener() {
        public void onClick(View v) {
            DialogFragment newFragment = new D_DatePickerFragment("newproject_debut");
            newFragment.show(getFragmentManager(), "datePicker");
            //tb_dateDebut.setText(lb_date.getText());
        }
    };

    //click projets en cours
    View.OnClickListener onClickFinDate = new View.OnClickListener() {
        public void onClick(View v) {
            DialogFragment newFragment = new D_DatePickerFragment("newproject_fin");
            newFragment.show(getFragmentManager(), "datePicker");
            //tb_dateFin.setText(lb_date.getText());
        }
    };








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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
