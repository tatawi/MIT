package com.mit.mit;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;


/**
 *C_Projet : Regroup all informations about a day
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class C_Jour {

//---------------------------------------------------------------------------------------
//	VARIABLES
//---------------------------------------------------------------------------------------

    public int id;
    public String nomJour;
    public Date jour;
    public float prixJournee;
    public String sujetsToString;

    public transient List<C_Sujet> liste_sujets;


//---------------------------------------------------------------------------------------
//	CONSTRUCTEURS
//---------------------------------------------------------------------------------------
    /**
     *Application builder - create a new day
     *@param nomJour 		Project id containing the day
     *@param jour			Day in date format
     */
    public C_Jour(String nomJour, Date jour)
    {
        super();
        List<C_Sujet> liste_sujets=new ArrayList<C_Sujet>();
        this.id=setID(nomJour);
        this.nomJour = nomJour;
        this.jour = jour;
        this.prixJournee=0;
        this.sujetsToString="";
    }

    /**
     *DAO builder - load a day from bdd
     *@param id				Day's Id
     *@param nomJour		Project's day Id
     *@param jour			Day in date format
     *@param prixJournee		Day's current cost in float
     *@param sujets			String containing all C_Sujet's Is separated by ";"
     */
    public C_Jour( int id, String nomJour, Date jour, float prixJournee, String sujets)
    {
        super();
        this.id = id;
        this.nomJour = nomJour;
        this.jour = jour;
        this.prixJournee = prixJournee;
        this.sujetsToString=sujets;
        this.liste_sujets=new ArrayList<C_Sujet>();
    }

    /**
     *Empty builder
     *@param
     *@return
     */
    public C_Jour() {
        super();
    }

//---------------------------------------------------------------------------------------
//	FONCTIONS 
//---------------------------------------------------------------------------------------
    /**
     *Inform if the user have a notification for the this day
     *@param me			User using the application
     *@return 			True if user have a notification on this day. False if not.
     */
    public boolean isNotification(C_Participant me, DAO_Message daomessage, DAO_Participant daopart)
    {
        try {
            for (C_Sujet sujet : liste_sujets)
            {
                sujet.creerLesListes(daomessage, daopart);
                if (sujet.isNotificationForMe(me, daopart))
                {
                    return true;
                }
            }
            return false;
        }
        catch(Exception ex)
        {
            System.out.println("isNotification : ERROR : "+ex.getMessage() );
            return false;
        }
    }



    public void creerLesListes(DAO_Sujet daoSujet)
    {
        liste_sujets=new ArrayList<C_Sujet>();
        String[] parts;

        if(sujetsToString.length()>1) {
            //gestion liste sujets
            parts = this.sujetsToString.split(";");
            System.out.println("parts : "+parts.length);
            for (int i = 0; i < parts.length; i++) {
                liste_sujets.add(daoSujet.getSujetById(parts[i]));
            }
        }

    }


    public void calculerPrixJournee(int nbTotal)
    {
        this.prixJournee=0;
        for (C_Sujet sujet:liste_sujets)
        {
            if (sujet.valide)
            {
                this.prixJournee=this.prixJournee+(float)sujet.prix;
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
        System.out.println("---JOUR LISTE TO STRING----------------");
        this.sujetsToString="";
        System.out.println("taille : " + this.liste_sujets.size());
        for(C_Sujet s:this.liste_sujets)
        {
            System.out.println("Enter");
            if (sujetsToString=="")
            {
                sujetsToString = s.idSujet;
            }
            else
            {
                sujetsToString = sujetsToString + ";" + s.idSujet;
            }
            System.out.println(sujetsToString);
        }
        System.out.println("--------------------------------------");
    }


    private int setID(String stringID)
    {
        int i=0;
        int ascii=0;
        while( i < stringID.length())
        {
            char character = stringID.charAt(i);
            ascii =ascii+ (int) character;
            i++;
        }
        return ascii;
    }


}
