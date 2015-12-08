package com.mit.mit;

        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;

        import com.parse.GetCallback;
        import com.parse.ParseException;
        import com.parse.ParseObject;
        import com.parse.ParseQuery;

        import java.text.SimpleDateFormat;

/**
 *DAO_Projet : DAO for C_Projet class
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class DAO_Projet extends DAO_Bdd {

    //---------------------------------------------------------------------------------------
//	VARIABLES
//---------------------------------------------------------------------------------------
    public static final String TABLE = " PROJET";
    public static final String ATTR_ID = "_id";
    public static final String ATTR_NOM = "nom";
    public static final String ATTR_DESCRIPTION = "description";
    public static final String ATTR_DATEDEBUT = "dateDebut";
    public static final String ATTR_DATEFIN = "dateFin";
    public static final String ATTR_PRIXSEJOUR = "prixSejour";
    public static final String ATTR_STATUT = "statut";
    public static final String ATTR_PARTICIPANTS = "participants";
    public static final String ATTR_JOURS = "jours";
    public static final String ATTR_COULEUR = "couleur";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_NOM + " TEXT, "
                    + ATTR_DESCRIPTION + " TEXT, "
                    + ATTR_DATEDEBUT + " TEXT, "
                    + ATTR_DATEFIN + " TEXT, "
                    + ATTR_PRIXSEJOUR + " FLOAT, "
                    + ATTR_STATUT + " TEXT, "
                    + ATTR_PARTICIPANTS + " TEXT, "
                    + ATTR_COULEUR + " TEXT, "
                    + ATTR_JOURS + " TEXT);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE + ";";

//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------
    /**
     *Application builder
     *@param pContext			Context
     */
    public DAO_Projet(Context pContext) {
        super(pContext);
    }


    /**
     *Add an project in bdd
     *@param p			project to add
     */
    public void ajouter(C_Projet p, boolean online) {
        this.open();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        ContentValues value = new ContentValues();
        value.put(ATTR_ID, p.id);
        value.put(ATTR_NOM, p.nom);
        value.put(ATTR_DESCRIPTION, p.description);
        value.put(ATTR_DATEDEBUT, sdf.format(p.dateDebut));
        value.put(ATTR_DATEFIN, sdf.format(p.dateFin));
        value.put(ATTR_PRIXSEJOUR, p.prixSejour);
        value.put(ATTR_STATUT, p.statut);
        value.put(ATTR_PARTICIPANTS, p.participantsToString);
        value.put(ATTR_JOURS, p.joursToString);
        value.put(ATTR_COULEUR, p.couleur);

        bdd.insert(TABLE, null, value);
        System.out.println("*- ajouté");
        this.close();

        if (online)
        {
            //add on cloud
            ParseObject Projet = new ParseObject("Projet");
            Projet.put("id", p.id);
            Projet.put("nom", p.nom);
            Projet.put("description", p.description);
            Projet.put("dateDebut", sdf.format(p.dateDebut));
            Projet.put("dateFin", sdf.format(p.dateFin));
            Projet.put("prixSejour", p.prixSejour);
            Projet.put("statut", p.statut);
            Projet.put("participantsToString", p.participantsToString);
            Projet.put("joursToString", p.joursToString);
            Projet.put("couleur", p.couleur);
            Projet.saveInBackground();
        }

    }


    /**
     *Delete an project in bdd
     *@param id			project'id to delete
     */
    public void supprimer(String id) {
        this.open();

        bdd.delete(
                TABLE,
                ATTR_ID + " = ?",
                new String[]{id}
        );
        this.close();

    }

    /**
     *Delete an project in bdd
     *@param p			project to edit
     */
    public void modifier(C_Projet p) {
        this.open();

        ContentValues value = new ContentValues();
        value.put(ATTR_NOM, p.nom);
        value.put(ATTR_DESCRIPTION, p.description);
        value.put(ATTR_DATEDEBUT, p.dateDebut.toString());
        value.put(ATTR_DATEFIN, p.dateFin.toString());
        value.put(ATTR_PRIXSEJOUR, p.prixSejour);
        value.put(ATTR_STATUT, p.statut);
        value.put(ATTR_PARTICIPANTS, p.participantsToString);
        value.put(ATTR_JOURS, p.joursToString);
        value.put(ATTR_COULEUR, p.couleur);

        bdd.update(
                TABLE,
                value,
                ATTR_ID + " = ?",
                new String[]{String.valueOf(p.id)}
        );
        this.close();

        //modif online
        final C_Projet pr = p;
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Projet");
        query.whereEqualTo("id", p.id);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject Projet, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    Projet.put("nom", pr.nom);
                    Projet.put("description", pr.description);
                    Projet.put("dateDebut", sdf.format(pr.dateDebut));
                    Projet.put("dateFin", sdf.format(pr.dateFin));
                    Projet.put("prixSejour", pr.prixSejour);
                    Projet.put("statut", pr.statut);
                    Projet.put("participantsToString", pr.participantsToString);
                    Projet.put("joursToString", pr.joursToString);
                    Projet.put("couleur", pr.couleur);
                    Projet.saveInBackground();
                }
            }
        });
    }

    public void ajouterOUmodifier(C_Projet p) {
        System.out.println("**working with :" + p.nom);
        C_Projet dao_p = this.getProjetByName(p.nom);
        if (dao_p != null) {
            System.out.println("**existe : modifications");
            System.out.println("**titre :" + dao_p.nom);
            System.out.println("**str :" + dao_p.toString());


            this.open();

            ContentValues value = new ContentValues();
            value.put(ATTR_NOM, p.nom);
            value.put(ATTR_DESCRIPTION, p.description);
            value.put(ATTR_DATEDEBUT, p.dateDebut.toString());
            value.put(ATTR_DATEFIN, p.dateFin.toString());
            value.put(ATTR_PRIXSEJOUR, p.prixSejour);
            value.put(ATTR_STATUT, p.statut);
            value.put(ATTR_PARTICIPANTS, p.participantsToString);
            value.put(ATTR_JOURS, p.joursToString);
            value.put(ATTR_COULEUR, p.couleur);

            bdd.update(
                    TABLE,
                    value,
                    ATTR_ID + " = ?",
                    new String[]{String.valueOf(p.nom)}
            );
            this.close();


        }
        else {
            System.out.println("**ajout dans bdd interne");
            this.ajouter(p, false);
        }
    }


    /**
     *Return all projects in the bdd
     *@return 		return a list of C_Projects containing all projects
     */
    public List<C_Projet> getProjets() {
        this.open();
        List<C_Projet>list_projets = new ArrayList<C_Projet>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date datedebut=new Date();
        Date datefin=new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[]{ATTR_ID, ATTR_NOM, ATTR_DESCRIPTION, ATTR_DATEDEBUT, ATTR_DATEFIN, ATTR_PRIXSEJOUR, ATTR_STATUT, ATTR_PARTICIPANTS, ATTR_JOURS, ATTR_COULEUR},
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
                datedebut = formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_DATEDEBUT)));
                datefin= formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_DATEFIN)));
            }
            catch(Exception e)
            {

            }
            list_projets.add(
                    new C_Projet(
                            cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_NOM)),
                            cursor.getString(cursor.getColumnIndex(ATTR_DESCRIPTION)),
                            datedebut,
                            datefin,
                            cursor.getFloat(cursor.getColumnIndex(ATTR_PRIXSEJOUR)),
                            cursor.getString(cursor.getColumnIndex(ATTR_STATUT)),
                            cursor.getString(cursor.getColumnIndex(ATTR_PARTICIPANTS)),
                            cursor.getString(cursor.getColumnIndex(ATTR_JOURS)),
                            cursor.getString(cursor.getColumnIndex(ATTR_COULEUR))
                    )
            );
        }

        this.close();
        cursor.close();
        return list_projets;
    }


    /**
     *Get one project from the bdd
     *@param id			project's id to got
     *@return 			return the project with the id
     */
    public C_Projet getProjetById(Integer id) {
        this.open();
        C_Projet p = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date datedebut=new Date();
        Date datefin=new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_NOM, ATTR_DESCRIPTION, ATTR_DATEDEBUT, ATTR_DATEFIN, ATTR_PRIXSEJOUR, ATTR_STATUT, ATTR_PARTICIPANTS, ATTR_JOURS, ATTR_COULEUR },
                ATTR_ID + " = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext())
        {
            try {
                datedebut = formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_DATEDEBUT)));
                datefin= formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_DATEFIN)));
            }
            catch(Exception e)
            {
                System.out.print("ERROR LOADING PROJECT DATE");
                System.out.println();
                System.out.print(""+cursor.getString(cursor.getColumnIndex(ATTR_DATEDEBUT)));
                System.out.println();
            }
            p=new C_Projet(
                    cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_NOM)),
                    cursor.getString(cursor.getColumnIndex(ATTR_DESCRIPTION)),
                    datedebut,
                    datefin,
                    cursor.getFloat(cursor.getColumnIndex(ATTR_PRIXSEJOUR)),
                    cursor.getString(cursor.getColumnIndex(ATTR_STATUT)),
                    cursor.getString(cursor.getColumnIndex(ATTR_PARTICIPANTS)),
                    cursor.getString(cursor.getColumnIndex(ATTR_JOURS)),
                    cursor.getString(cursor.getColumnIndex(ATTR_COULEUR))
            );
        }
        this.close();
        cursor.close();
        return p;
    }



    /**
     *Get one project from the bdd
     *@param nom			project's id to got
     *@return 			return the project with the id
     */
    public C_Projet getProjetByName(String nom) {
        this.open();
        C_Projet p = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date datedebut=new Date();
        Date datefin=new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_NOM, ATTR_DESCRIPTION, ATTR_DATEDEBUT, ATTR_DATEFIN, ATTR_PRIXSEJOUR, ATTR_STATUT, ATTR_PARTICIPANTS, ATTR_JOURS, ATTR_COULEUR },
                ATTR_NOM + " = ?",
                new String[] { nom },
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext())
        {
            try {
                datedebut = formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_DATEDEBUT)));
                datefin= formatter.parse(cursor.getString(cursor.getColumnIndex(ATTR_DATEFIN)));
            }
            catch(Exception e)
            {
                System.out.print("ERROR LOADING PROJECT DATE");
                System.out.println();
                System.out.print(""+cursor.getString(cursor.getColumnIndex(ATTR_DATEDEBUT)));
                System.out.println();
            }
            p=new C_Projet(
                    cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_NOM)),
                    cursor.getString(cursor.getColumnIndex(ATTR_DESCRIPTION)),
                    datedebut,
                    datefin,
                    cursor.getFloat(cursor.getColumnIndex(ATTR_PRIXSEJOUR)),
                    cursor.getString(cursor.getColumnIndex(ATTR_STATUT)),
                    cursor.getString(cursor.getColumnIndex(ATTR_PARTICIPANTS)),
                    cursor.getString(cursor.getColumnIndex(ATTR_JOURS)),
                    cursor.getString(cursor.getColumnIndex(ATTR_COULEUR))
            );
        }
        this.close();
        cursor.close();
        return p;
    }





}










