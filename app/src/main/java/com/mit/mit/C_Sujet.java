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
    public int id;
    public String idSujet;
    public String titre;
    public String description;
    public String type;
    public String localisation;
    public String localisation2;
    public Date heure;
    public int duree;
    public boolean auFeeling;
    public int areaSize;
    public double prix;
    public boolean valide;

    public String messagesToString;
    public String personnesAyantAccepteToString;
    public String participentToString;
    public String quiApayeToString;
    public String combienApayeToString;

    public transient List<C_Participant> personnesAyantAccepte;
    public transient List<C_Message> liste_messages;
    public transient List<C_Participant> liste_participent;
    public transient List<C_Participant> liste_quiApaye;
    public transient List<Double> liste_combienApaye;


//---------------------------------------------------------------------------------------
//	CONSTRUCTEURS
//---------------------------------------------------------------------------------------

    /**
     *Application builder - create a new subject
     *@param titre				Subject's title
     *@param description			Subject's description
     *@param type				Subject's type
     *@param localisation		Subject's localisation
     *@param heure				Subject's start time
     *@param duree				Subject's duration
     *@param auFeeling			If it's a feeling subject
     *@param prix				Subject's price
     */
    public C_Sujet( String titre, String description, String type, String localisation, String localisation2, Date heure, int duree, boolean auFeeling, double prix)
    {
        super();
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.localisation = localisation;
        this.localisation2=localisation2;
        this.heure = heure;
        this.duree = duree;
        this.auFeeling=auFeeling;
        this.areaSize=0;
        this.prix=prix;
        this.valide=false;
        this.messagesToString="";
        this.personnesAyantAccepteToString="";
        this.participentToString="";
        this.quiApayeToString="";
        this.combienApayeToString="";
        this.personnesAyantAccepte = new ArrayList<C_Participant>();
        this.liste_messages= new ArrayList<C_Message>();
        this.liste_participent= new ArrayList<C_Participant>();
        this.liste_quiApaye= new ArrayList<C_Participant>();
        this.liste_combienApaye= new ArrayList<Double>();
    }

    /**
     *DAO builder - load a subject from bdd
     *@param id								Id of the day containing the subject
     *@param idSujet						Id of the day containing the subject
     *@param titre							Subject's title
     *@param description					Subject's description
     *@param type							Subject's type
     *@param localisation					Subject's localisation
     *@param heure							Subject's start time
     *@param duree							Subject's duration
     *@param auFeeling						If it's a feeling subject
     *@param prix							Subject's price
     *@param messagesToString				List of all C_message's id in string format (separated with ";")
     *@param personnesAyantAccepteToString	List of all accepted member's id in string format (separated with ";")
     */
    public C_Sujet( int id, String idSujet, String titre, String description, String type, String localisation, String localisation2, Date heure, int duree, String auFeeling, int v_area, double prix, String messagesToString, String personnesAyantAccepteToString, String valide, String neParticipentPasToString, String quiApayeToString, String combienApayeToString)
    {
        super();
        boolean v_feel=false;
        boolean v_valide=false;

        //gestion feeling
        if (auFeeling.equals("true"))
        {
            v_feel=true;
        }

        this.id = id;
        this.idSujet = idSujet;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.localisation = localisation;
        this.localisation2 = localisation2;
        this.heure = heure;
        this.duree = duree;
        this.auFeeling=v_feel;
        this.areaSize=v_area;
        this.prix=prix;
        this.messagesToString=messagesToString;
        this.personnesAyantAccepteToString=personnesAyantAccepteToString;
        this.participentToString=neParticipentPasToString;
        this.quiApayeToString=quiApayeToString;
        this.combienApayeToString=combienApayeToString;

        //gestion bool valide
        if (valide.equals("true"))
        {
            v_valide=true;
        }
        this.valide=v_valide;
    }

    /**
     *Empty builder
     */
    public C_Sujet() {

        super();
        this.idSujet="";
        this.titre="";
        this.description="";
        this.type="";
        this.localisation="";
        this.localisation2="";
        this.heure=new Date();
        this.duree=0;
        this.auFeeling=false;
        this.areaSize=0;
        this.prix=0;
        this.valide=false;

        this.messagesToString="";
        this.personnesAyantAccepteToString="";
        this.participentToString="";
        this.quiApayeToString="";
        this.combienApayeToString="";

        this.personnesAyantAccepte = new ArrayList<C_Participant>();
        this.liste_messages= new ArrayList<C_Message>();
        this.liste_participent= new ArrayList<C_Participant>();
        this.liste_quiApaye= new ArrayList<C_Participant>();
        this.liste_combienApaye= new ArrayList<Double>();
    }

//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    public boolean isValide(int nbTotal)
    {
        try
        {

            if(this.personnesAyantAccepte.size()/nbTotal>0.5)
            {
                return true;
            }
            return false;
        }
        catch(Exception e)
        {
            return false;
        }
    }

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
            if(me.mail==p.mail)
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
    public boolean isNotificationForMe(C_Participant me, DAO_Participant daopart)
    {
        try {
            for (C_Message msg : liste_messages)
            {
                msg.creerLesListes(daopart);

                if (!msg.aiJeVu(me))
                {
                    return true;
                }
            }
            return false;
        }
        catch(Exception ex)
        {
            System.out.println("isNotificationForMe : ERROR : "+ex.getMessage() );
            return false;
        }
    }



    public void creerLesListes(DAO_Message daoMessage, DAO_Participant daoParticipant)
    {
        this.liste_messages=new ArrayList<C_Message>();
        this.personnesAyantAccepte=new ArrayList<C_Participant>();
        this.liste_participent= new ArrayList<C_Participant>();
        this.liste_quiApaye= new ArrayList<C_Participant>();
        this.liste_combienApaye= new ArrayList<Double>();
        String[] parts;

        try{
            //gestion liste messages
            if(messagesToString.length()>1) {
                parts = this.messagesToString.split(";");
                for (int i = 0; i < parts.length; i++) {
                    liste_messages.add(daoMessage.getMessageById(parts[i]));
                }
            }

            //gestion liste participants
            if(personnesAyantAccepteToString.length()>1) {
                parts = this.personnesAyantAccepteToString.split(";");
                for (int i = 0; i < parts.length; i++) {
                    personnesAyantAccepte.add(daoParticipant.getParticipantById(parts[i]));
                }
            }

            //ne participent pas
            if(participentToString.length()>1) {
                parts = this.participentToString.split(";");
                for (int i = 0; i < parts.length; i++) {
                    liste_participent.add(daoParticipant.getParticipantById(parts[i]));
                }
            }

            //qui a payé
            if(quiApayeToString.length()>1) {
                parts = this.quiApayeToString.split(";");
                for (int i = 0; i < parts.length; i++) {
                    liste_quiApaye.add(daoParticipant.getParticipantById(parts[i]));
                }
            }

            //combien a payé
            if(combienApayeToString.length()>1) {
                parts = this.combienApayeToString.split(";");
                for (int i = 0; i < parts.length; i++) {
                    liste_combienApaye.add(Double.parseDouble(parts[i]));
                }
            }


        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
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

        this.messagesToString="";
        this.personnesAyantAccepteToString="";
        this.participentToString="";
        this.quiApayeToString="";
        this.combienApayeToString="";

        if(!liste_messages.isEmpty()) {
            for (C_Message m : this.liste_messages) {
                if (messagesToString == "") {
                    messagesToString = m.id;
                } else {
                    messagesToString = messagesToString + ";" + m.id;
                }
            }
        }

        if(!personnesAyantAccepte.isEmpty()) {
            for (C_Participant p : this.personnesAyantAccepte) {
                if (personnesAyantAccepteToString == "") {
                    personnesAyantAccepteToString = p.mail;
                } else {
                    personnesAyantAccepteToString = personnesAyantAccepteToString + ";" + p.mail;
                }
            }
        }

        if(!liste_participent.isEmpty()) {
            for (C_Participant p : this.liste_participent) {
                if (participentToString == "") {
                    participentToString = p.mail;
                } else {
                    participentToString = participentToString + ";" + p.mail;
                }
            }
        }

        if(!liste_quiApaye.isEmpty()) {
            for (C_Participant p : this.liste_quiApaye) {
                if (quiApayeToString == "") {
                    quiApayeToString = p.mail;
                } else {
                    quiApayeToString = quiApayeToString + ";" + p.mail;
                }
            }
        }

        if(!liste_combienApaye.isEmpty()) {
            for (Double p : this.liste_combienApaye) {
                if (combienApayeToString == "") {
                    combienApayeToString = p.toString();
                } else {
                    combienApayeToString = combienApayeToString + ";" + p.toString();
                }
            }
        }



    }




}
