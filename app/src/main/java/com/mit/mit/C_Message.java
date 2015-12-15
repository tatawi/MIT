package com.mit.mit;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;



/**
 *C_Projet : Regroup all informations about a message
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class C_Message {

    //---------------------------------------------------------------------------------------
//	VARIABLES
//---------------------------------------------------------------------------------------
    public Context pContext;
    public String id;
    public Date heure;
    public String message;
    public String id_participantEmetteur;
    public String personnesAyantVuesToString;

    public C_Participant emetteur;
    public transient List<C_Participant> liste_personnesAyantVues;


//---------------------------------------------------------------------------------------
//	CONSTRUCTEURS
//---------------------------------------------------------------------------------------

    /**
     *Application builder - create a new message
     *@param heure			Message's time
     *@param message			Message's body
     *@param emetteur		User who has send the message
     */
    public C_Message(String idSujet, Date heure, String message, C_Participant emetteur)
    {
        super();
        Date today = new Date();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd-MM-yy-HH-MM-SS");

        this.id = idSujet+sdf.format(heure);
        this.heure = heure;
        this.message = message;
        this.emetteur = emetteur;
        this.id_participantEmetteur = emetteur.mail;
        this.personnesAyantVuesToString = emetteur.mail;
        this.liste_personnesAyantVues= new ArrayList<C_Participant>();

    }

    /**
     *DAB builder - load a message from bdd
     *@param id							Message's Id
     *@param heure						Message's time
     *@param message						Message's body
     *@param id_participantEmetteur		String id of the user who has sent the message
     *@param personnesAyantVuesToString	string with users id (separated with ";") who has seen the message
     */
    public C_Message( String id, Date heure, String message, String id_participantEmetteur, String personnesAyantVuesToString)
    {
        super();
        Date today = new Date();

        this.id = id;

        this.id_participantEmetteur = id_participantEmetteur;
        this.message = message;
        this.heure = heure;
        this.personnesAyantVuesToString=personnesAyantVuesToString;
        this.liste_personnesAyantVues= new ArrayList<C_Participant>();
    }

    /**
     *Empty builder
     */
    public C_Message() {
        super();
    }

//---------------------------------------------------------------------------------------
//	FONCTIONS 
//---------------------------------------------------------------------------------------


    /**
     *Verify if the user has seen the message
     *@param me		User
     *@return true if the user has seen the message. Else if not
     */
    public boolean aiJeVu(C_Participant me)
    {
        try
        {
            for(C_Participant p:this.liste_personnesAyantVues)
            {
                if(me.mail.equals(p.mail))
                {
                    return true;
                }
            }
            return false;
        }
        catch(Exception ex)
        {
            System.out.println("aiJeVu : ERROR : "+ex.getMessage() );
            return false;
        }
    }

    /**
     *Spefify is the user has seen the message
     *@param p      	User
     */
    public void vuPar(C_Participant p)
    {
        this.liste_personnesAyantVues.add(p);
    }

    public void creerLesListes(DAO_Participant daopart)
    {
        liste_personnesAyantVues=new ArrayList<C_Participant>();
        String[] parts;

        if(personnesAyantVuesToString.length()>1) {
            //gestion liste sujets
            parts = this.personnesAyantVuesToString.split(";");
            for (int i = 0; i < parts.length; i++) {
                liste_personnesAyantVues.add(daopart.getParticipantById(parts[i]));
            }
        }

    }



//---------------------------------------------------------------------------------------
//	FONCTIONS PRIVATES 
//---------------------------------------------------------------------------------------

    /**
     *Convert class list in string (separated with ";") in order to store data in DAO bdd
     */
    public void listeToString()
    {
        System.out.println("message liste to string");
        System.out.println("taille : " + this.liste_personnesAyantVues.size());
        this.personnesAyantVuesToString="";
        for(C_Participant p:this.liste_personnesAyantVues)
        {
            System.out.println("l1 : " + this.personnesAyantVuesToString);
            System.out.println("part : " + p.mail);
            if (personnesAyantVuesToString=="")
            {
                personnesAyantVuesToString = p.mail;
            }
            else
            {
                personnesAyantVuesToString = liste_personnesAyantVues + ";" + p.mail;
            }
            System.out.println("l2 : " + this.personnesAyantVuesToString);
        }
    }





}

