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
    public String id;
    public String nom;
    public String description;
    public Date dateDebut;
    public Date dateFin;
    public float prixSejour;
    public String participantsToString;
    public String joursToString;

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
     */
    public C_Projet( Context pContext, String nom, String description, Date dateDebut, Date dateFin)
    {
        super();
        this.pContext= pContext;
        this.id = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixSejour=0;
        liste_jours=new ArrayList<C_Jour>();
        liste_participants=new ArrayList<C_Participant>();
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
    public C_Projet( String id, String nom, String description, Date dateDebut, Date dateFin, float prixSejour, String participants, String jours)
    {
        super();
        DAO_Participant daoPart = new DAO_Participant(this.pContext);
        DAO_Jour daoJour = new DAO_Jour(this.pContext);
        List<C_Participant> list_participants=new ArrayList<C_Participant>();
        List<C_Jour> list_jours=new ArrayList<C_Jour>();
        String[] parts;

        //gestion liste participants
        parts = participants.split(";");
        for(int i = 0; i < parts.length; i++)
        {
            list_participants.add(daoPart.getParticipantById(parts[i]));
        }
        //gestion liste jours
        parts = jours.split(";");
        for(int i = 0; i < parts.length; i++)
        {
            list_jours.add(daoJour.getJourById(parts[i]));
        }

        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixSejour=prixSejour;
        this.liste_jours=list_jours;
        this.liste_participants=list_participants;
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







//---------------------------------------------------------------------------------------
//	BDD GESTION
//---------------------------------------------------------------------------------------
    /**
     *Add class to DAO bdd
     */
    public void BDDajouter()
    {
        DAO_Projet daoProj = new DAO_Projet(this.pContext);
        listeToString();
        daoProj.ajouter(this);
    }

    /**
     *Modify class in DAO bdd
     */
    public void BDDmodifier()
    {
        DAO_Projet daoProj = new DAO_Projet(this.pContext);
        listeToString();
        daoProj.modifier(this);
    }

    /**
     *Remove class from DAO bdd
     */
    public void BDDsuprimer()
    {
        DAO_Projet daoProj = new DAO_Projet(this.pContext);
        daoProj.supprimer(this.id);
    }


//---------------------------------------------------------------------------------------
//	FONCTIONS PRIVATES 
//---------------------------------------------------------------------------------------	
    /**
     *Convert class list in string (separated with ";") in order to store data in DAO bdd
     */
    private void listeToString()
    {
        this.participantsToString="";
        this.joursToString="";

        for(C_Jour j:this.liste_jours)
        {
            if (joursToString=="")
            {
                joursToString = j.id;
            }
            else
            {
                joursToString = joursToString + ";" + j.id;
            }
        }

        for(C_Participant p:this.liste_participants)
        {
            if (participantsToString=="")
            {
                participantsToString = p.id;
            }
            else
            {
                participantsToString = participantsToString + ";" + p.id;
            }
        }

    }









}
