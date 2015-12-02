package com.mit.mit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;


/**
 *DAO_Jour : DAO for C_jour class
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class DAO_Jour extends DAO_Bdd {

    //---------------------------------------------------------------------------------------
//	VARIABLES
//---------------------------------------------------------------------------------------
    public static final String TABLE = " JOUR";
    public static final String ATTR_ID = "_id";
    public static final String ATTR_NOMJOUR = "nomJour";
    public static final String ATTR_DATE = "jour";
    public static final String ATTR_PRIXJOURNEE = "prixJournee";
    public static final String ATTR_SUJETS = "sujets";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_NOMJOUR + " TEXT, "
                    + ATTR_DATE + " TEXT, "
                    + ATTR_PRIXJOURNEE + " FLOAT, "
                    + ATTR_SUJETS + " TEXT);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE + ";";

//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    /**
     *Application builder
     *@param pContext			Context
     */
    public DAO_Jour(Context pContext) {
        super(pContext);
    }


    /**
     *Add an day in bdd
     *@param j			day to add
     */
    public void ajouter(C_Jour j) {
        this.open();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ContentValues value = new ContentValues();
        value.put(ATTR_NOMJOUR, j.nomJour);
        value.put(ATTR_DATE, sdf.format(j.jour));
        value.put(ATTR_PRIXJOURNEE, j.prixJournee);
        value.put(ATTR_SUJETS, j.sujetsToString);

        bdd.insert(TABLE, null, value);
        this.close();


        //add on cloud
        ParseObject Jour = new ParseObject("Jour");
        Jour.put("nomJour", j.nomJour);
        Jour.put("date", sdf.format(j.jour));
        Jour.put("prixJournee", j.prixJournee);
        Jour.put("sujetsToString", j.sujetsToString);
        Jour.saveInBackground();
    }


    /**
     *Delete an day in bdd
     *@param id			day'id to delete
     */
    public void supprimer(String id) {
        this.open();

        bdd.delete(
                TABLE,
                ATTR_NOMJOUR + " = ?",
                new String[]{id}
        );
        this.close();
    }


    /**
     *Delete an day in bdd
     *@param j			day to edit
     */
    public void modifier(C_Jour j) {
        this.open();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        ContentValues value = new ContentValues();
        value.put(ATTR_ID, j.id);
        value.put(ATTR_NOMJOUR, j.nomJour);
        value.put(ATTR_DATE, formatter.format(j.jour));
        value.put(ATTR_PRIXJOURNEE, j.prixJournee);
        value.put(ATTR_SUJETS, j.sujetsToString);

        bdd.update(
                TABLE,
                value,
                ATTR_NOMJOUR + " = ?",
                new String[]{String.valueOf(j.nomJour)}
        );
        this.close();
    }

    public void ajouterOUmodifier(C_Jour j) {
        C_Jour dao_j = this.getJourById(j.nomJour);
        if (dao_j != null) {
            this.modifier(j);
        } else {
            this.ajouter(j);
        }
    }


    /**
     *Return all days in the bdd
     *@return 		return a list of C_Jour containing all days
     */
    public List<C_Jour> getJours() {
        this.open();
        List<C_Jour> liste_jours=new ArrayList<C_Jour>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date day = new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_NOMJOUR, ATTR_DATE, ATTR_PRIXJOURNEE, ATTR_SUJETS },
                null,
                null,
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext())
        {
            try {
                day = formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_DATE)));
            }
            catch(Exception e)
            {
                System.out.println("))ERROR warking with date: " +cursor.getString(cursor.getColumnIndex(ATTR_DATE)));
            }

            liste_jours.add(
                    new C_Jour(
                            cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_NOMJOUR)),
                            day,
                            cursor.getFloat(cursor.getColumnIndex(ATTR_PRIXJOURNEE)),
                            cursor.getString(cursor.getColumnIndex(ATTR_SUJETS))
                    )
            );
        }
        this.close();
        cursor.close();
        return liste_jours;
    }



    /**
     *Get one day from the bdd
     *@param id			day's id to got
     *@return 			return the day with the id
     */
    public C_Jour getJourById(String id) {
        this.open();
        C_Jour p = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date day = new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_NOMJOUR, ATTR_DATE, ATTR_PRIXJOURNEE, ATTR_SUJETS },
                ATTR_NOMJOUR + " = ?",
                new String[] { id },
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext())
        {
            try {
                day = formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_DATE)));
            }
            catch(Exception e)
            {
                System.out.println("))ERROR warking with date: " +cursor.getString(cursor.getColumnIndex(ATTR_DATE)));
            }

            p=new C_Jour(
                    cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_NOMJOUR)),
                    day,
                    cursor.getFloat(cursor.getColumnIndex(ATTR_PRIXJOURNEE)),
                    cursor.getString(cursor.getColumnIndex(ATTR_SUJETS))
            );
        }
        this.close();
        cursor.close();
        return p;
    }






}











