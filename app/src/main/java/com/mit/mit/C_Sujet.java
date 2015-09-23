package com.mit.mit;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *C_Projet : Regroup all informations about a subject
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class C_Sujet {

    //---------------------------------------------------------------------------------------
//	VARIABLES
//---------------------------------------------------------------------------------------
    public Context pContext;
    public String id;
    public String titre;
    public String description;
    public String type;
    public String localisation;
    public Date heure; //in minutes
    public int duree;
    public boolean auFeeling;
    public float prix;

    public String messagesToString;
    public String personnesAyantAccepteToString;

    public transient List<C_Participant> personnesAyantAccepte;
    public transient List<C_Message> liste_messages;


//---------------------------------------------------------------------------------------
//	CONSTRUCTEURS
//---------------------------------------------------------------------------------------

    /**
     *Application builder - create a new subject
     *@param idJour				Id of the day containing the subject
     *@param titre				Subject's title
     *@param description			Subject's description
     *@param type				Subject's type
     *@param localisation		Subject's localisation
     *@param heure				Subject's start time
     *@param duree				Subject's duration
     *@param auFeeling			If it's a feeling subject
     *@param prix				Subject's price
     */
    public C_Sujet( String idJour, String titre, String description, String type, String localisation, Date heure, int duree, boolean auFeeling, float prix)
    {
        super();
        this.id =idJour + "_" + titre;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.localisation = localisation;
        this.heure = heure;
        this.duree = duree;
        this.auFeeling=auFeeling;
        this.prix=prix;
    }

    /**
     *DAO builder - load a subject from bdd
     *@param id								Id of the day containing the subject
     *@param titre							Subject's title
     *@param description						Subject's description
     *@param type							Subject's type
     *@param localisation					Subject's localisation
     *@param heure							Subject's start time
     *@param duree							Subject's duration
     *@param auFeeling						If it's a feeling subject
     *@param prix							Subject's price
     *@param messagesToString				List of all C_message's id in string format (separated with ";")
     *@param personnesAyantAccepteToString	List of all accepted member's id in string format (separated with ";")
     */
    public C_Sujet( String id, String titre, String description, String type, String localisation, Date heure, int duree, String auFeeling, float prix, String messagesToString, String personnesAyantAccepteToString)
    {
        super();
        boolean v_feel=false;

        //gestion feeling
        if (auFeeling=="oui")
        {
            v_feel=true;
        }

        this.id = id;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.localisation = localisation;
        this.heure = heure;
        this.duree = duree;
        this.auFeeling=v_feel;
        this.prix=prix;
        this.messagesToString=messagesToString;
        this.personnesAyantAccepteToString=personnesAyantAccepteToString;
    }

    /**
     *Empty builder
     */
    public C_Sujet() {
        super();
    }

//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    /**
     *Accept subject - Add a user in accepted member list
     *@param me			Member to add
     */
    public void accepterSujet(C_Participant me)
    {
        this.personnesAyantAccepte.add(me);
    }

    /**
     *Do I Have Accept Subject - To know if user have accepted the Subject
     *@param me			Member to verify
     */
    public boolean aiJeAccepteSujet(C_Participant me)
    {
        for(C_Participant p:this.personnesAyantAccepte)
        {
            if(me.id==p.id)
            {
                return true;
            }
        }
        return false;
    }

    /**
     *Inform if the user have a notification for the this day
     *@param me			User using the application
     *@return 			True if user have a notification on this day. False if not.
     */
    public boolean isNotification(C_Participant me)
    {
        for(C_Message msg:liste_messages)
        {
            if(!msg.aiJeVu(me))
            {
                return true;
            }
        }
        return false;
    }


    public void setContext(Context context)
    {
        this.pContext=context;
    }

    public void creerLesListes()
    {
        List<C_Message> list_messages=new ArrayList<C_Message>();
        List<C_Participant> list_part=new ArrayList<C_Participant>();
        DAO_Message daoMessage = new DAO_Message(this.pContext);
        DAO_Participant daoParticipant = new DAO_Participant(this.pContext);
        String[] parts;

        //gestion liste messages
        parts = this.messagesToString.split(";");
        for(int i = 0; i < parts.length; i++)
        {
            list_messages.add(daoMessage.getMessageById(parts[i]));
        }

        //gestion liste participants
        parts = this.personnesAyantAccepteToString.split(";");
        for(int i = 0; i < parts.length; i++)
        {
            list_part.add(daoParticipant.getParticipantById(parts[i]));
        }
    }


//---------------------------------------------------------------------------------------
//	BDD GESTION
//---------------------------------------------------------------------------------------

    /**
     *Add class to DAO bdd
     */
    public void BDDajouter()
    {
        DAO_Sujet daoSujet = new DAO_Sujet(this.pContext);
        listeToString();

        daoSujet.ajouter(this);
    }

    /**
     *Modify class in DAO bdd
     */
    public void BDDmodifier()
    {
        DAO_Sujet daoSujet = new DAO_Sujet(this.pContext);
        listeToString();

        daoSujet.modifier(this);
    }

    /**
     *Remove class from DAO bdd
     */
    public void BDDsuprimer()
    {
        DAO_Sujet daoSujet = new DAO_Sujet(this.pContext);
        daoSujet.supprimer(this.id);
    }

//---------------------------------------------------------------------------------------
//	FONCTIONS PRIVATES
//---------------------------------------------------------------------------------------

    /**
     *Convert class list in string (separated with ";") in order to store data in DAO bdd
     */
    private void listeToString()
    {
        this.messagesToString="";
        this.personnesAyantAccepteToString="";

        for(C_Participant p:this.personnesAyantAccepte)
        {
            if (messagesToString=="")
            {
                messagesToString = p.id;
            }
            else
            {
                messagesToString = messagesToString + ";" + p.id;
            }
        }

        for(C_Message m:this.liste_messages)
        {
            if (personnesAyantAccepteToString=="")
            {
                personnesAyantAccepteToString = m.id;
            }
            else
            {
                personnesAyantAccepteToString = personnesAyantAccepteToString + ";" + m.id;
            }
        }
    }




}
