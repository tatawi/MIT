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
    public Context pContext;
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
    public boolean isNotification(C_Participant me)
    {
        for(C_Sujet sujet:liste_sujets)
        {
            if(sujet.isNotification(me))
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
        List<C_Sujet> list_sujets=new ArrayList<C_Sujet>();
        DAO_Sujet daoSujet = new DAO_Sujet(this.pContext);
        String[] parts;

        //gestion liste sujets
        parts = this.sujetsToString.split(";");
        for(int i = 0; i < parts.length; i++)
        {
            list_sujets.add(daoSujet.getSujetById(parts[i]));
        }
    }





//---------------------------------------------------------------------------------------
//	FONCTIONS PRIVATES 
//---------------------------------------------------------------------------------------
    /**
     *Convert class list in string (separated with ";") in order to store data in DAO bdd
     */
    private void listeToString()
    {
        this.sujetsToString="";

        for(C_Sujet s:this.liste_sujets)
        {
            if (sujetsToString=="")
            {
                sujetsToString = s.id;
            }
            else
            {
                sujetsToString = sujetsToString + ";" + s.id;
            }
        }
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
