package com.mit.mit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
    public static final String ATTR_VILLE = "ville";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_NOMJOUR + " TEXT, "
                    + ATTR_DATE + " TEXT, "
                    + ATTR_PRIXJOURNEE + " FLOAT, "
                    + ATTR_SUJETS + " TEXT, "
                    + ATTR_VILLE + " TEXT);";
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
    public void ajouter(C_Jour j, boolean SaveOnline)
    {
        //LOCAL
        this.open();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ContentValues value = new ContentValues();
        value.put(ATTR_NOMJOUR, j.nomJour);
        value.put(ATTR_DATE, sdf.format(j.jour));
        value.put(ATTR_PRIXJOURNEE, j.prixJournee);
        value.put(ATTR_SUJETS, j.sujetsToString);
        value.put(ATTR_VILLE, j.ville);

        bdd.insert(TABLE, null, value);
        this.close();

        //ONLINE
        if (SaveOnline) {
            ParseObject Jour = new ParseObject("Jour");
            Jour.put("nomJour", j.nomJour);
            Jour.put("date", sdf.format(j.jour));
            Jour.put("prixJournee", j.prixJournee);
            Jour.put("sujetsToString", j.sujetsToString);
            Jour.put("ville", j.ville);
            Jour.saveInBackground();
        }
    }


    /**
     *Delete an day in bdd
     *@param id			day'id to delete
     */
    public void supprimer(String id, boolean SaveOnline)
    {
        try
        {
            //LOCAL
            this.open();
            bdd.delete(
                    TABLE,
                    ATTR_NOMJOUR + " = ?",
                    new String[]{id}
            );
            this.close();


            //ONLINE
            if (SaveOnline) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Jour");
                query.whereEqualTo("nomJour", id);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject Jour, ParseException e) {
                        try {
                            if (e == null) {
                                Jour.delete();
                                Jour.saveInBackground();
                            }
                        } catch (ParseException ex) {
                            System.out.println("[PARSE ERROR] : " + ex.getMessage());
                        }
                    }
                });
            }
        }
        catch (Exception ex)
        {System.out.println("[ERROR BDD] : " +ex.getMessage());}
    }


    /**
     *Delete an day in bdd
     *@param j			day to edit
     */
    public void modifier(C_Jour j, boolean SaveOnline)
    {
        //LOCAL
        this.open();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        ContentValues value = new ContentValues();
        value.put(ATTR_NOMJOUR, j.nomJour);
        value.put(ATTR_DATE, formatter.format(j.jour));
        value.put(ATTR_PRIXJOURNEE, j.prixJournee);
        value.put(ATTR_SUJETS, j.sujetsToString);
        value.put(ATTR_VILLE, j.ville);

        bdd.update(
                TABLE,
                value,
                ATTR_NOMJOUR + " = ?",
                new String[]{String.valueOf(j.nomJour)}
        );
        this.close();


        //ONLINE
        if (SaveOnline) {
            final C_Jour jr = j;
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Jour");
            query.whereEqualTo("nomJour", j.nomJour);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject Jour, ParseException e) {
                    if (e == null) {
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.
                        Jour.put("nomJour", jr.nomJour);
                        Jour.put("date", sdf.format(jr.jour));
                        Jour.put("prixJournee", jr.prixJournee);
                        Jour.put("sujetsToString", jr.sujetsToString);
                        Jour.put("ville", jr.ville);
                        Jour.saveInBackground();
                    }
                }
            });
        }
    }


    /**
     *Add or modify a day if it already exists
     *@param j			day to edit
     */
    public void ajouterOUmodifier(C_Jour j, boolean SaveOnline) {
        C_Jour dao_j = this.getJourById(j.nomJour);
        if (dao_j != null) {
            this.modifier(j, SaveOnline);
        } else {
            this.ajouter(j, false);
        }
    }


                /**
                 *Return all days in the bdd
                 *@return return a list of C_Jour containing all days
                 */
                public List<C_Jour> getJours() {
                    this.open();
                    List<C_Jour> liste_jours = new ArrayList<C_Jour>();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date day = new Date();
                    Cursor cursor = bdd.query(
                            TABLE,
                            new String[]{ATTR_ID, ATTR_NOMJOUR, ATTR_DATE, ATTR_PRIXJOURNEE, ATTR_SUJETS, ATTR_VILLE},
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        try {
                            day = formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_DATE)));
                        } catch (Exception e) {
                            System.out.println("))ERROR warking with date: " + cursor.getString(cursor.getColumnIndex(ATTR_DATE)));
                        }

                        liste_jours.add(
                                new C_Jour(
                                        cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                                        cursor.getString(cursor.getColumnIndex(ATTR_NOMJOUR)),
                                        day,
                                        cursor.getFloat(cursor.getColumnIndex(ATTR_PRIXJOURNEE)),
                                        cursor.getString(cursor.getColumnIndex(ATTR_SUJETS)),
                                        cursor.getString(cursor.getColumnIndex(ATTR_VILLE))
                                )
                        );
                    }
                    this.close();
                    cursor.close();
                    return liste_jours;
                }


                /**
                 *Get one day from the bdd
                 *@param id            day's id to got
                 *@return return the day with the id
                 */
                public C_Jour getJourById(String id) {
                    this.open();
                    C_Jour p = null;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date day = new Date();
                    Cursor cursor = bdd.query(
                            TABLE,
                            new String[]{ATTR_ID, ATTR_NOMJOUR, ATTR_DATE, ATTR_PRIXJOURNEE, ATTR_SUJETS, ATTR_VILLE},
                            ATTR_NOMJOUR + " = ?",
                            new String[]{id},
                            null,
                            null,
                            null,
                            null
                    );

                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        try {
                            day = formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_DATE)));
                        } catch (Exception e) {
                            System.out.println("))ERROR warking with date: " + cursor.getString(cursor.getColumnIndex(ATTR_DATE)));
                        }

                        p = new C_Jour(
                                cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                                cursor.getString(cursor.getColumnIndex(ATTR_NOMJOUR)),
                                day,
                                cursor.getFloat(cursor.getColumnIndex(ATTR_PRIXJOURNEE)),
                                cursor.getString(cursor.getColumnIndex(ATTR_SUJETS)),
                                cursor.getString(cursor.getColumnIndex(ATTR_VILLE))
                        );
                    }
                    this.close();
                    cursor.close();
                    return p;
                }


            }











