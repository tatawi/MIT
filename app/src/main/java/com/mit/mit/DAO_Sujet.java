package com.mit.mit;

        import java.util.ArrayList;
        import java.util.List;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import java.text.SimpleDateFormat;
        import java.util.Date;

/**
 *DAO_Sujet : DAO for C_Sujet class
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class DAO_Sujet extends DAO_Bdd {
    //---------------------------------------------------------------------------------------
//	VARIABLES
//---------------------------------------------------------------------------------------
    public static final String TABLE = " SUJET";
    public static final String ATTR_ID = "_id";
    public static final String ATTR_IDSUJET = "idSujet";
    public static final String ATTR_TITRE = "titre";
    public static final String ATTR_DESCRIPTION = "description";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_LOC = "localisation";
    public static final String ATTR_HEURE = "heure";
    public static final String ATTR_DUREE = "duree";
    public static final String ATTR_FEELLING = "auFelling";
    public static final String ATTR_PRIX = "prix";
    public static final String ATTR_VALIDE = "valide";
    public static final String ATTR_MESSAGES = "messages";
    public static final String ATTR_PERSONNESOK = "personnesAyantAccepte";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_IDSUJET + " TEXT, "
                    + ATTR_TITRE + " TEXT, "
                    + ATTR_DESCRIPTION + " TEXT, "
                    + ATTR_TYPE + " TEXT, "
                    + ATTR_LOC + " TEXT, "
                    + ATTR_HEURE + " TEXT, "
                    + ATTR_DUREE + " INTEGER, "
                    + ATTR_FEELLING + " TEXT, "
                    + ATTR_PRIX + " FLOAT, "
                    + ATTR_VALIDE + " TEXT, "
                    + ATTR_MESSAGES + " TEXT, "
                    + ATTR_PERSONNESOK + " TEXT);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE + ";";

//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------
    /**
     *Application builder
     *@param pContext			Context
     */
    public DAO_Sujet(Context pContext) {
        super(pContext);
    }

    /**
     *Add an subject in bdd
     *@param s			subject to add
     */
    public void ajouter(C_Sujet s) {
        this.open();

        ContentValues value = new ContentValues();
        value.put(ATTR_IDSUJET, s.idSujet);
        value.put(ATTR_TITRE, s.titre);
        value.put(ATTR_DESCRIPTION, s.description);
        value.put(ATTR_TYPE, s.type);
        value.put(ATTR_LOC, s.localisation);
        value.put(ATTR_HEURE, s.heure.toString());
        value.put(ATTR_DUREE, s.duree);
        value.put(ATTR_FEELLING, s.auFeeling);
        value.put(ATTR_PRIX, s.prix);
        value.put(ATTR_VALIDE, s.valide);
        value.put(ATTR_MESSAGES, s.messagesToString);
        value.put(ATTR_PERSONNESOK, s.personnesAyantAccepteToString);

        bdd.insert(TABLE, null, value);
        this.close();
    }

    /**
     *Delete an subject in bdd
     *@param id			subject'id to delete
     */
    public void supprimer(String id) {
        this.open();

        bdd.delete(
                TABLE,
                ATTR_IDSUJET + " = ?",
                new String[] { id }
        );

        this.close();
    }

    /**
     *Delete an subject in bdd
     *@param s			subject to edit
     */
    public void modifier(C_Sujet s) {
        this.open();

        ContentValues value = new ContentValues();
        value.put(ATTR_TITRE, s.titre);
        value.put(ATTR_DESCRIPTION, s.description);
        value.put(ATTR_TYPE, s.type);
        value.put(ATTR_LOC, s.localisation);
        value.put(ATTR_HEURE, s.heure.toString());
        value.put(ATTR_DUREE, s.duree);
        value.put(ATTR_FEELLING, s.auFeeling);
        value.put(ATTR_PRIX, s.prix);
        value.put(ATTR_VALIDE, s.valide);
        value.put(ATTR_MESSAGES, s.messagesToString);
        value.put(ATTR_PERSONNESOK, s.personnesAyantAccepteToString);

        bdd.update(
                TABLE,
                value,
                ATTR_IDSUJET + " = ?",
                new String[]{String.valueOf(s.idSujet)}
        );

        this.close();
    }

    /**
     *Return all subjects in the bdd
     *@return 		return a list of C_Participants containing all subjects
     */
    public List<C_Sujet> getSujets() {
        this.open();
        List<C_Sujet>list_sujets=new ArrayList<C_Sujet>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date lheure=new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_IDSUJET, ATTR_TITRE, ATTR_DESCRIPTION, ATTR_TYPE, ATTR_LOC, ATTR_HEURE, ATTR_DUREE, ATTR_FEELLING, ATTR_PRIX, ATTR_VALIDE, ATTR_MESSAGES, ATTR_PERSONNESOK},
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
                lheure = formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_HEURE)));
            }
            catch(Exception e)
            {

            }
            list_sujets.add(
                    new C_Sujet(
                            cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_IDSUJET)),
                            cursor.getString(cursor.getColumnIndex(ATTR_TITRE)),
                            cursor.getString(cursor.getColumnIndex(ATTR_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(ATTR_TYPE)),
                            cursor.getString(cursor.getColumnIndex(ATTR_LOC)),
                            lheure,
                            cursor.getInt(cursor.getColumnIndex(ATTR_DUREE)),
                            cursor.getString(cursor.getColumnIndex(ATTR_FEELLING)),
                            cursor.getDouble(cursor.getColumnIndex(ATTR_PRIX)),
                            cursor.getString(cursor.getColumnIndex(ATTR_MESSAGES)),
                            cursor.getString(cursor.getColumnIndex(ATTR_PERSONNESOK)),
                            cursor.getString(cursor.getColumnIndex(ATTR_VALIDE))
                    )
            );
        }

        this.close();
        cursor.close();
        return list_sujets;
    }

    /**
     *Get one subject from the bdd
     *@param id			subject's id to got
     *@return 			return the participant with the id
     */
    public C_Sujet getSujetById(String id) {
        this.open();
        C_Sujet p = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date lheure=new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_IDSUJET, ATTR_TITRE, ATTR_DESCRIPTION, ATTR_TYPE, ATTR_LOC, ATTR_HEURE, ATTR_DUREE, ATTR_FEELLING, ATTR_PRIX, ATTR_VALIDE, ATTR_MESSAGES, ATTR_PERSONNESOK},
                ATTR_IDSUJET + " = ?",
                new String[] { id },
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext())
        {
            try {
                lheure = formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_HEURE)));
            }
            catch(Exception e)
            {

            }

            p=new C_Sujet(
                    cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_IDSUJET)),
                    cursor.getString(cursor.getColumnIndex(ATTR_TITRE)),
                    cursor.getString(cursor.getColumnIndex(ATTR_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(ATTR_TYPE)),
                    cursor.getString(cursor.getColumnIndex(ATTR_LOC)),
                    lheure,
                    cursor.getInt(cursor.getColumnIndex(ATTR_DUREE)),
                    cursor.getString(cursor.getColumnIndex(ATTR_FEELLING)),
                    cursor.getDouble(cursor.getColumnIndex(ATTR_PRIX)),
                    cursor.getString(cursor.getColumnIndex(ATTR_MESSAGES)),
                    cursor.getString(cursor.getColumnIndex(ATTR_PERSONNESOK)),
                    cursor.getString(cursor.getColumnIndex(ATTR_VALIDE))
            );
        }
        this.close();
        cursor.close();
        return p;
    }






}










