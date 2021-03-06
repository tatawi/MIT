package com.mit.mit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class A_gestionParticipants extends MainActivity {
    private ListView lvListe;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String[] listeStrings = {""};
        List<String> listeStringParticipants = new ArrayList<String>();
        List<C_Participant>list_parts=new ArrayList<C_Participant>();
        list_parts=daoparticipant.getParticipants();
        int i=0;


        setContentView(R.layout.activity_a_gestion_participants);
        lvListe = (ListView)findViewById(R.id.gestionPart_listView);

        for(C_Participant p:list_parts)
        {
            listeStringParticipants.add(p.mail);
        }



        //lvListe.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeStrings));
        lvListe.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listeStringParticipants));


    }




//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_gestion_participants, menu);
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
