package com.mit.mit;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class A_conversation extends MainActivity {



    //objets de la page
    private LinearLayout container_globalLayout;
    private LinearLayout container_send;
    private ImageButton btn_send;
    private EditText tb_message;

    //variables
    private C_Sujet sujet;
    //private List<C_Message> liste_messages;
    private SimpleDateFormat sdf;
    private String userID;
    private C_Participant part;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialisations objets page
        setContentView(R.layout.activity_a_conversation);
        container_globalLayout = (LinearLayout) findViewById(R.id.conv_globalLayout);
        container_send= (LinearLayout) findViewById(R.id.conv_layoutMessage);
        btn_send=(ImageButton) findViewById(R.id.conv_btn_send);
        tb_message=(EditText) findViewById(R.id.conv_tb_message);

        //listener
        btn_send.setOnClickListener(onSendMessage);

        //variables
        //liste_messages= new ArrayList<C_Message>();

        //récupération du sujet
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.userID = extras.getString("userID");
            part=daoparticipant.getParticipantById(userID);

            //chargement des données du sujet
            this.sujet = daoSujet.getSujetById(extras.getString("idEntry"));
            this.sujet.creerLesListes(daoMessage, daoparticipant);
            System.out.println("string messages : "+ this.sujet.messagesToString);
            setTitle("[" + this.sujet.type + "] " + this.sujet.titre);
        }

        //scroll.fullScroll(View.FOCUS_DOWN) also should work.
        affichage();
    }


    public void affichage() {
        System.out.println("-*-*-*-*-*-*-**-**-*-*-*-*-*-*-*-*-***-*-*-*-");
        System.out.println("Go affichage");
        System.out.println("nb messages : "+this.sujet.liste_messages.size());
        container_globalLayout.removeAllViews();
        container_globalLayout.setOrientation(LinearLayout.VERTICAL);
        sdf = new SimpleDateFormat("dd/mm/yy HH:mm");
        String titreDescription = "";
        for (C_Message m : this.sujet.liste_messages) {
            //panel global
            LinearLayout LLglobal = new LinearLayout(this);
            LLglobal.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LLglobal.setLayoutParams(LLParams);
            LLglobal.setPadding(0, 3, 3, 0);

            //panel textes
            LinearLayout LLText = new LinearLayout(this);
            LLText.setOrientation(LinearLayout.VERTICAL);
            LLText.setLayoutParams(LLParams);

            //panel Nom
            LinearLayout LLup = new LinearLayout(this);
            LLup.setOrientation(LinearLayout.HORIZONTAL);
            LLup.setLayoutParams(LLParams);

            //déclarations champs
            TextView name = new TextView(this);
            TextView date = new TextView(this);
            TextView text = new TextView(this);

            //initialisation champs
            name.setText(daoparticipant.getParticipantById(m.id_participantEmetteur).prenom);
            name.setTextSize(18);
            text.setText(m.message);
            date.setText(sdf.format(m.heure));
            date.setPadding(10, 0, 0, 0);

            //configuration messages selon users
            if (!part.mail.equals(m.id_participantEmetteur)) {
                ImageButton imgLine = new ImageButton(this);
                imgLine.setImageResource(R.drawable.ic_project_conv_line);
                imgLine.setPadding(100, 0, 20, 0);
                imgLine.setBackgroundColor(Color.TRANSPARENT);

                LLglobal.addView(imgLine);
                //ic_project_conv_line

            }
            else
            {
                name.setTextColor(Color.parseColor("#ac035d"));
                text.setTextColor(Color.parseColor("#ac035d"));
                date.setTextColor(Color.parseColor("#ac035d"));
            }


            //ajout des champs aux pannels
            LLup.addView(name);
            LLup.addView(date);
            LLText.addView(LLup);
            LLText.addView(text);

            LLglobal.addView(LLText);

            container_globalLayout.addView(LLglobal);
        }
        container_globalLayout.addView(container_send);




    }



    public void majNotifications()
    {
        for (C_Message m : this.sujet.liste_messages)
        {


        }
    }










//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------


    //bouton send
    View.OnClickListener onSendMessage = new View.OnClickListener() {
        public void onClick(View v)
        {
/*       public C_Message(String idSujet, Date heure, String message, C_Participant emetteur)*/




            C_Message message = new C_Message(sujet.idSujet,new Date(), tb_message.getText().toString(), part);
            daoMessage.ajouter(message, true);


            sujet.liste_messages.add(message);
            sujet.listeToString();

            daoSujet.modifier(sujet);

            tb_message.setText("");
            affichage();



        }
    };



//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------




//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_conversation, menu);
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
