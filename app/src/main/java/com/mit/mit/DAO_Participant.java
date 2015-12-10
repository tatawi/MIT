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

/**
 *DAO_Bdd : DAO main class
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class DAO_Participant extends DAO_Bdd {

    //---------------------------------------------------------------------------------------
//	VARIABLES
//---------------------------------------------------------------------------------------
    public static final String TABLE = " PARTICIPANT";
    public static final String ATTR_ID = "_id";
    public static final String ATTR_NOM = "nom";
    public static final String ATTR_PRENOM = "prenom";
    public static final String ATTR_MAIL = "mail";
    public static final String ATTR_MDP = "mdp";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_NOM + " TEXT, "
                    + ATTR_PRENOM + " TEXT, "
                    + ATTR_MAIL + " TEXT, "
                    + ATTR_MDP + " TEXT);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE + ";";


//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    /**
     *Application builder
     *@param pContext			Context
     */
    public DAO_Participant(Context pContext) {
        super(pContext);
    }

    /**
     *Add an user in bdd
     *@param p			User to add
     */
    public void ajouter(C_Participant p, boolean online)
    {
        //LOCAL
        this.open();
        ContentValues value = new ContentValues();
        value.put(ATTR_ID, p.id);
        value.put(ATTR_NOM, p.nom);
        value.put(ATTR_PRENOM, p.prenom);
        value.put(ATTR_MAIL, p.mail);
        value.put(ATTR_MDP, p.mdp);
        bdd.insert(TABLE, null, value);
        this.close();

        //ONLINE
        if (online) {
            //add on cloud
            ParseObject Participant = new ParseObject("Participant");
            Participant.put("nom", p.nom);
            Participant.put("prenom", p.prenom);
            Participant.put("mail", p.mail);
            Participant.put("mdp", p.mdp);
            Participant.saveInBackground();
        }
    }

    /**
     *Delete an user in bdd
     *@param id			User'id to delete
     */
    public void supprimer(String id)
    {
        try
        {
            //LOCAL
            this.open();

            bdd.delete(
                    TABLE,
                    ATTR_MAIL + " = ?",
                    new String[]{id}
            );

            this.close();

            //ONLINE
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Participant");
            query.whereEqualTo("mail", id);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject Participant, ParseException e) {
                    try {
                        if (e == null) {
                            Participant.delete();
                            Participant.saveInBackground();
                        }
                    } catch (ParseException ex) {
                        System.out.println("[PARSE ERROR] : " + ex.getMessage());
                    }
                }
            });
        }
        catch (Exception ex)
        {System.out.println("[ERROR] : " +ex.getMessage());}
    }


    /**
     *Delete an user in bdd
     *@param p			User to edit
     */
    public void modifier(C_Participant p)
    {
        //LOCAL
        this.open();
        ContentValues value = new ContentValues();
        value.put(ATTR_NOM, p.nom);
        value.put(ATTR_PRENOM, p.prenom);
        value.put(ATTR_MAIL, p.mail);
        value.put(ATTR_MDP, p.mdp);

        bdd.update(
                TABLE,
                value,
                ATTR_ID + " = ?",
                new String[]{String.valueOf(p.id)}
        );

        this.close();

        //ONLINE
        final C_Participant pt = p;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Participant");
        query.whereEqualTo("mail", p.mail);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject Participant, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    Participant.put("nom", pt.nom);
                    Participant.put("prenom", pt.prenom);
                    Participant.put("mail", pt.mail);
                    Participant.put("mdp", pt.mdp);
                    Participant.saveInBackground();
                }
            }
        });

    }


    /**
     *Add a user or modify if not exists
     *@param p			User to add or modify
     */
    public void ajouterOUmodifier(C_Participant p){
        C_Participant dao_p = this.getParticipantById(p.mail);
        if(dao_p!=null)
        {
            this.modifier(p);
        }
        else
        {
            this.ajouter(p, false);
        }

    }


    /**
     *Return all users in the bdd
     *@return 		return a list of C_Participants containing all users
     */
    public List<C_Participant> getParticipants() {
        this.open();

        List<C_Participant> listParticipants = new ArrayList<C_Participant>();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_NOM, ATTR_PRENOM , ATTR_MAIL , ATTR_MDP },
                null,
                null,
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext()) {
            listParticipants.add(
                    new C_Participant(
                            cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_NOM)),
                            cursor.getString(cursor.getColumnIndex(ATTR_PRENOM)),
                            cursor.getString(cursor.getColumnIndex(ATTR_MAIL)),
                            cursor.getString(cursor.getColumnIndex(ATTR_MDP))
                    )
            );
        }

        this.close();
        cursor.close();
        return listParticipants;
    }


    /**
     *Get one user from the bdd
     *@param id			User's id to got
     *@return 			return the participant with the id
     */
    public C_Participant getParticipantById(String id) {
        this.open();

        C_Participant p = null;
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_NOM, ATTR_PRENOM , ATTR_MAIL , ATTR_MDP },
                ATTR_MAIL + " = ?",
                new String[] { id },
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext()) {

            p= new C_Participant(
                    cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_NOM)),
                    cursor.getString(cursor.getColumnIndex(ATTR_PRENOM)),
                    cursor.getString(cursor.getColumnIndex(ATTR_MAIL)),
                    cursor.getString(cursor.getColumnIndex(ATTR_MDP))
            );
        }
        this.close();
        cursor.close();
        return p;
    }

}
