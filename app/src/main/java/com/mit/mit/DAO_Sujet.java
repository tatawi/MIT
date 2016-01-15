package com.mit.mit;

        import java.util.ArrayList;
        import java.util.List;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;

        import com.parse.GetCallback;
        import com.parse.ParseException;
        import com.parse.ParseObject;
        import com.parse.ParseQuery;

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
    public static final String ATTR_LOC2 = "localisation2";
    public static final String ATTR_HEURE = "heure";
    public static final String ATTR_DUREE = "duree";
    public static final String ATTR_FEELLING = "auFelling";
    public static final String ATTR_PRIX = "prix";
    public static final String ATTR_VALIDE = "valide";
    public static final String ATTR_MESSAGES = "messages";
    public static final String ATTR_PERSONNESOK = "personnesAyantAccepte";
    public static final String ATTR_PARTICIPENT = "personnesParticipant";
    public static final String ATTR_QUIAPAYE = "personnesAyantPaye";
    public static final String ATTR_COMBIENAPAYE = "montantPaye";




    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_IDSUJET + " TEXT, "
                    + ATTR_TITRE + " TEXT, "
                    + ATTR_DESCRIPTION + " TEXT, "
                    + ATTR_TYPE + " TEXT, "
                    + ATTR_LOC + " TEXT, "
                    + ATTR_LOC2 + " TEXT, "
                    + ATTR_HEURE + " TEXT, "
                    + ATTR_DUREE + " INTEGER, "
                    + ATTR_FEELLING + " TEXT, "
                    + ATTR_PRIX + " FLOAT, "
                    + ATTR_VALIDE + " TEXT, "
                    + ATTR_MESSAGES + " TEXT, "
                    + ATTR_PERSONNESOK + " TEXT, "
                    + ATTR_PARTICIPENT + " TEXT, "
                    + ATTR_QUIAPAYE + " TEXT, "
                    + ATTR_COMBIENAPAYE + " TEXT);";
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
    public void ajouter(C_Sujet s, boolean SaveOnline)
    {
        System.out.println("SAVE online = "+SaveOnline);

        //LOCAL
        this.open();
        String valide ="false";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy HH:mm");
        if(s.valide)
        {
            valide="true";
        }
        ContentValues value = new ContentValues();
        value.put(ATTR_IDSUJET, s.idSujet);
        value.put(ATTR_TITRE, s.titre);
        value.put(ATTR_DESCRIPTION, s.description);
        value.put(ATTR_TYPE, s.type);
        value.put(ATTR_LOC, s.localisation);
        value.put(ATTR_LOC2, s.localisation2);
        value.put(ATTR_HEURE, sdf.format(s.heure));
        value.put(ATTR_DUREE, s.duree);
        value.put(ATTR_FEELLING, s.auFeeling);
        value.put(ATTR_PRIX, s.prix);
        value.put(ATTR_MESSAGES, s.messagesToString);
        value.put(ATTR_PERSONNESOK, s.personnesAyantAccepteToString);
        value.put(ATTR_VALIDE, valide);
        value.put(ATTR_PARTICIPENT, s.participentToString);
        value.put(ATTR_QUIAPAYE, s.quiApayeToString);
        value.put(ATTR_COMBIENAPAYE, s.combienApayeToString);

        bdd.insert(TABLE, null, value);
        this.close();

        //ONLINE
        if (SaveOnline) {

            ParseObject Sujet = new ParseObject("Sujet");
            Sujet.put("idSujet", s.idSujet);
            Sujet.put("titre", s.titre);
            Sujet.put("description", s.description);
            Sujet.put("type", s.type);
            Sujet.put("localisation", s.localisation);
            Sujet.put("localisation2", s.localisation2);
            Sujet.put("heure", sdf.format(s.heure));
            Sujet.put("duree", s.duree);
            Sujet.put("auFeeling", s.auFeeling);
            Sujet.put("prix", s.prix);
            Sujet.put("messagesToString", s.messagesToString);
            Sujet.put("personnesAyantAccepteToString", s.personnesAyantAccepteToString);
            Sujet.put("valide", valide);
            Sujet.put("personnesParticipant", s.participentToString);
            Sujet.put("personnesAyantPaye", s.quiApayeToString);
            Sujet.put("montantPaye", s.combienApayeToString);
            Sujet.saveInBackground();
        }
    }

    /**
     *Delete an subject in bdd
     *@param id			subject'id to delete
     */
    public void supprimer(String id, boolean SaveOnline)
    {
        try
        {
            //LOCAL
            this.open();

            bdd.delete(
                    TABLE,
                    ATTR_IDSUJET + " = ?",
                    new String[] { id }
            );

            this.close();

            //ONLINE
            if (SaveOnline) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Sujet");
                query.whereEqualTo("idSujet", id);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject Sujet, ParseException e) {
                        try {
                            if (e == null) {
                                Sujet.delete();
                                Sujet.saveInBackground();
                            }
                        } catch (ParseException ex) {
                            System.out.println("[PARSE ERROR] : " + ex.getMessage());
                        }
                    }
                });
            }
        }
        catch (Exception ex)
        {System.out.println("[ERROR] : " +ex.getMessage());}
    }


    /**
     *Add or modify a subject if exists
     *@param s			subject to add or modify
     */
    public void ajouterOUmodifier(C_Sujet s, boolean SaveOnline)
    {
        C_Sujet dao_s = this.getSujetById(s.idSujet);
        if (dao_s != null) {
            this.modifier(s, SaveOnline);
        } else {
            this.ajouter(s, SaveOnline);
        }
    }

    /**
     *Delete an subject in bdd
     *@param s			subject to edit
     */
    public void modifier(C_Sujet s, boolean SaveOnline)
    {
        //LOCAL
        this.open();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy HH:mm");
        String valide ="false";
        if(s.valide)
        {
            valide="true";
        }
        ContentValues value = new ContentValues();
        value.put(ATTR_TITRE, s.titre);
        value.put(ATTR_DESCRIPTION, s.description);
        value.put(ATTR_TYPE, s.type);
        value.put(ATTR_LOC, s.localisation);
        value.put(ATTR_LOC2, s.localisation2);
        value.put(ATTR_HEURE, sdf.format(s.heure));
        value.put(ATTR_DUREE, s.duree);
        value.put(ATTR_FEELLING, s.auFeeling);
        value.put(ATTR_PRIX, s.prix);
        value.put(ATTR_MESSAGES, s.messagesToString);
        value.put(ATTR_PERSONNESOK, s.personnesAyantAccepteToString);
        value.put(ATTR_VALIDE, valide);
        value.put(ATTR_PARTICIPENT, s.participentToString);
        value.put(ATTR_QUIAPAYE, s.quiApayeToString);
        value.put(ATTR_COMBIENAPAYE, s.combienApayeToString);

        bdd.update(
                TABLE,
                value,
                ATTR_IDSUJET + " = ?",
                new String[]{String.valueOf(s.idSujet)}
        );
        this.close();

        //ONLINE
        if (SaveOnline) {
            final C_Sujet st = s;
            final SimpleDateFormat sdff = new SimpleDateFormat("dd/mm/yy HH:mm:ss");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Sujet");
            query.whereEqualTo("idSujet", s.idSujet);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject Sujet, ParseException e) {
                    if (e == null) {
                        Sujet.put("idSujet", st.idSujet);
                        Sujet.put("titre", st.titre);
                        Sujet.put("description", st.description);
                        Sujet.put("type", st.type);
                        Sujet.put("localisation", st.localisation);
                        Sujet.put("localisation2", st.localisation2);
                        Sujet.put("heure", sdff.format(st.heure));
                        Sujet.put("duree", st.duree);
                        Sujet.put("auFeeling", st.auFeeling);
                        Sujet.put("prix", st.prix);
                        Sujet.put("messagesToString", st.messagesToString);
                        Sujet.put("personnesAyantAccepteToString", st.personnesAyantAccepteToString);
                        Sujet.put("personnesParticipant", st.participentToString);
                        Sujet.put("personnesAyantPaye", st.quiApayeToString);
                        Sujet.put("montantPaye", st.combienApayeToString);
                        if (st.valide) {
                            Sujet.put("valide", "true");
                        } else {
                            Sujet.put("valide", "false");
                        }
                        Sujet.saveInBackground();
                    }
                }
            });
        }
    }




    /**
     *Return all subjects in the bdd
     *@return 		return a list of C_Participants containing all subjects
     */
    public List<C_Sujet> getSujets() {
        this.open();
        List<C_Sujet>list_sujets=new ArrayList<C_Sujet>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yy HH:mm");
        Date lheure=new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_IDSUJET, ATTR_TITRE, ATTR_DESCRIPTION, ATTR_TYPE, ATTR_LOC, ATTR_LOC2, ATTR_HEURE, ATTR_DUREE,
                        ATTR_FEELLING, ATTR_PRIX, ATTR_MESSAGES, ATTR_PERSONNESOK, ATTR_VALIDE, ATTR_PARTICIPENT, ATTR_QUIAPAYE, ATTR_COMBIENAPAYE},
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
                            cursor.getString(cursor.getColumnIndex(ATTR_LOC2)),
                            lheure,
                            cursor.getInt(cursor.getColumnIndex(ATTR_DUREE)),
                            cursor.getString(cursor.getColumnIndex(ATTR_FEELLING)),
                            cursor.getDouble(cursor.getColumnIndex(ATTR_PRIX)),
                            cursor.getString(cursor.getColumnIndex(ATTR_MESSAGES)),
                            cursor.getString(cursor.getColumnIndex(ATTR_PERSONNESOK)),
                            cursor.getString(cursor.getColumnIndex(ATTR_VALIDE)),
                            cursor.getString(cursor.getColumnIndex(ATTR_PARTICIPENT)),
                            cursor.getString(cursor.getColumnIndex(ATTR_QUIAPAYE)),
                            cursor.getString(cursor.getColumnIndex(ATTR_COMBIENAPAYE))
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yy HH:mm");
        Date lheure=new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_IDSUJET, ATTR_TITRE, ATTR_DESCRIPTION, ATTR_TYPE, ATTR_LOC, ATTR_LOC2, ATTR_HEURE, ATTR_DUREE,
                        ATTR_FEELLING, ATTR_PRIX, ATTR_MESSAGES, ATTR_PERSONNESOK, ATTR_VALIDE, ATTR_PARTICIPENT, ATTR_QUIAPAYE, ATTR_COMBIENAPAYE},
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
                System.out.println("DAO_SUJET time : "+lheure);
            }
            catch(Exception e)
            {
                System.out.println("[ERROR DAO_SUJET] : "+e.getMessage());
            }

            p=new C_Sujet(
                    cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_IDSUJET)),
                    cursor.getString(cursor.getColumnIndex(ATTR_TITRE)),
                    cursor.getString(cursor.getColumnIndex(ATTR_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(ATTR_TYPE)),
                    cursor.getString(cursor.getColumnIndex(ATTR_LOC)),
                    cursor.getString(cursor.getColumnIndex(ATTR_LOC2)),
                    lheure,
                    cursor.getInt(cursor.getColumnIndex(ATTR_DUREE)),
                    cursor.getString(cursor.getColumnIndex(ATTR_FEELLING)),
                    cursor.getDouble(cursor.getColumnIndex(ATTR_PRIX)),
                    cursor.getString(cursor.getColumnIndex(ATTR_MESSAGES)),
                    cursor.getString(cursor.getColumnIndex(ATTR_PERSONNESOK)),
                    cursor.getString(cursor.getColumnIndex(ATTR_VALIDE)),
                    cursor.getString(cursor.getColumnIndex(ATTR_PARTICIPENT)),
                    cursor.getString(cursor.getColumnIndex(ATTR_QUIAPAYE)),
                    cursor.getString(cursor.getColumnIndex(ATTR_COMBIENAPAYE))
            );
        }
        this.close();
        cursor.close();
        return p;
    }






}










