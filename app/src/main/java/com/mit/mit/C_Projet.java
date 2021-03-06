package com.mit.mit;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *C_Projet : Regroup all informations about a project
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class C_Projet {

    //---------------------------------------------------------------------------------------
//	VARIABLES
//---------------------------------------------------------------------------------------
    public Context pContext;
    public int id;
    public String nom;
    public String description;
    public Date dateDebut;
    public Date dateFin;
    public float prixSejour;
    public String statut;
    public String participantsToString;
    public String joursToString;
    public String couleur;

    public transient List<C_Jour> liste_jours;
    public transient List<C_Participant> liste_participants;

//---------------------------------------------------------------------------------------
//	CONSTRUCTEURS
//---------------------------------------------------------------------------------------
    /**
     *Application builder - create a new project
     *@param nom 			Project's name
     *@param description 	Project's description
     *@param dateDebut		Start date
     *@param dateFin			End date
     *@param couleur			couleur
     */
    public C_Projet( String nom, String description, Date dateDebut, Date dateFin, String couleur)
    {
        super();
        this.id=setID();
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixSejour=0;
        this.statut="Preparation";
        liste_jours=new ArrayList<C_Jour>();
        liste_participants=new ArrayList<C_Participant>();
        this.couleur=couleur;
    }

    /**
     *Application builder - create a new project
     *@param nom 			Project's name
     *@param description 	Project's description
     *@param dateDebut		Start date
     *@param dateFin			End date
     */
    public C_Projet( Context pContext, String nom, String description, Date dateDebut, Date dateFin)
    {
        super();
        this.pContext= pContext;
        this.id=setID();
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixSejour=0;
        this.statut="Preparation";
        liste_jours=new ArrayList<C_Jour>();
        liste_participants=new ArrayList<C_Participant>();
        couleur="noir";
    }

    /**
     *DAO builder - load a project from bdd
     *@param id 				Project's id
     *@param nom 			Project's name
     *@param description		Project's description
     *@param dateDebut		Start date
     *@param dateFin 		End date
     *@param prixSejour 		Project's cost
     *@param participants	List of project's ids members in string separated by ";"
     *@param jours			List of C_Jour ids in string separated by ";"
     */
    public C_Projet( int id, String nom, String description, Date dateDebut, Date dateFin, float prixSejour, String statut, String participants, String jours, String couleur)
    {
        super();

        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixSejour=prixSejour;
        this.joursToString=jours;
        this.statut=statut;
        this.participantsToString=participants;
        this.couleur=couleur;
    }


    /**
     *Empty builder
     */
    public C_Projet()
    {
        super();
    }


//---------------------------------------------------------------------------------------
//	FONCTIONS 
//---------------------------------------------------------------------------------------

    public void setContext(Context context)
    {
        this.pContext=context;
    }

    public void creerLesListes(DAO_Jour daoJour, DAO_Participant daoPart)
    {
        liste_participants=new ArrayList<C_Participant>();
        liste_jours=new ArrayList<C_Jour>();
        String[] parts;

        //gestion liste participants
        parts = this.participantsToString.split(";");
        for(int i = 0; i < parts.length; i++)
        {
            liste_participants.add(daoPart.getParticipantById(parts[i]));
        }
        //gestion liste jours
        parts = this.joursToString.split(";");
        for(int i = 0; i < parts.length; i++)
        {
            liste_jours.add(daoJour.getJourById(parts[i]));
        }
    }




    /**
     *Convert class list in string (separated with ";") in order to store data in DAO bdd
     */
    public void listeToString()
    {
        this.participantsToString="";
        this.joursToString="";

        if(!liste_jours.isEmpty())
        {
            for(C_Jour j:this.liste_jours)
            {
                if (joursToString.equals(""))
                {
                    joursToString = j.nomJour;
                }
                else
                {
                    joursToString = joursToString + ";" + j.nomJour;
                }
            }
        }

        if(!liste_participants.isEmpty())
        {
            for(C_Participant p:this.liste_participants)
            {
                if (participantsToString.equals(""))
                {
                    participantsToString = p.mail;
                }
                else
                {
                    participantsToString = participantsToString + ";" + p.mail;
                }
            }
        }


    }

    public void calculerPrixSejour()
    {
        this.prixSejour=0;
        for (C_Jour jour:liste_jours)
        {
            this.prixSejour=this.prixSejour+jour.prixJournee;
        }
    }


    public void setListe_jours(List<C_Jour> liste_jours) {
        this.liste_jours = liste_jours;
    }

    public void setListe_participants(List<C_Participant> liste_participants) {
        this.liste_participants = liste_participants;
    }





//---------------------------------------------------------------------------------------
//	FONCTIONS PRIVATES 
//---------------------------------------------------------------------------------------	



    private int setID()
    {
       /* int i=0;
        int ascii=0;
        while( i < stringID.length())
        {
            char character = stringID.charAt(i);
            ascii =ascii+ (int) character;
            i++;
        }
        return ascii;*/

        return (int) (new Date().getTime()/1000);

    }








}
