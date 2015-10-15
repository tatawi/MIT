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
        this.personnesAyantVuesToString = "";
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
     *Spefify is the user has seen the message
     *@param me		User
     */
    public void meRajouterAyantVu(C_Participant me)
    {
        liste_personnesAyantVues.add(me);
    }

    /**
     *Verify if the user has seen the message
     *@param me		User
     *@return true if the user has seen the message. Else if not
     */
    public boolean aiJeVu(C_Participant me)
    {
        for(C_Participant p:this.liste_personnesAyantVues)
        {
            if(me.id==p.id)
            {
                return true;
            }
        }
        return false;
    }


//---------------------------------------------------------------------------------------
//	BDD GESTION
//---------------------------------------------------------------------------------------

    /**
     *Add class to DAO bdd
     */
    public void BDDajouter()
    {
        DAO_Message daoMessage = new DAO_Message(this.pContext);
        listeToString();

        daoMessage.ajouter(this);
    }

    /**
     *Modify class in DAO bdd
     */
    public void BDDmodifier()
    {
        DAO_Message daoMessage = new DAO_Message(this.pContext);
        listeToString();

        daoMessage.modifier(this);
    }

    /**
     *Remove class from DAO bdd
     */
    public void BDDsuprimer()
    {
        DAO_Message daoMessage = new DAO_Message(this.pContext);
        daoMessage.supprimer(this.id);
    }

//---------------------------------------------------------------------------------------
//	FONCTIONS PRIVATES 
//---------------------------------------------------------------------------------------

    /**
     *Convert class list in string (separated with ";") in order to store data in DAO bdd
     */
    private void listeToString()
    {
        this.personnesAyantVuesToString="";

        for(C_Participant p:this.liste_personnesAyantVues)
        {
            if (personnesAyantVuesToString=="")
            {
                personnesAyantVuesToString = p.mail;
            }
            else
            {
                personnesAyantVuesToString = liste_personnesAyantVues + ";" + p.mail;
            }
        }
    }





}

