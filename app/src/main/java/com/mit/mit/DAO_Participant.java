package com.mit.mit;

        import java.util.ArrayList;
        import java.util.List;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;

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
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_NOM + " TEXT, "
                    + ATTR_PRENOM + " TEXT);";
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
    public void ajouter(C_Participant p) {
        this.open();

        ContentValues value = new ContentValues();
        value.put(ATTR_ID, p.id);
        value.put(ATTR_NOM, p.nom);
        value.put(ATTR_PRENOM, p.prenom);
        bdd.insert(TABLE, null, value);

        this.close();
    }

    /**
     *Delete an user in bdd
     *@param id			User'id to delete
     */
    public void supprimer(Integer id) {
        this.open();

        bdd.delete(
                TABLE,
                ATTR_ID + " = ?",
                new String[] { String.valueOf(id) }
        );

        this.close();
    }

    /**
     *Delete an user in bdd
     *@param p			User to edit
     */
    public void modifier(C_Participant p) {
        this.open();

        ContentValues value = new ContentValues();
        value.put(ATTR_NOM, p.nom);
        value.put(ATTR_PRENOM, p.prenom);

        bdd.update(
                TABLE,
                value,
                ATTR_ID + " = ?",
                new String[] { String.valueOf(p.id) }
        );

        this.close();
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
                new String[] { ATTR_ID, ATTR_NOM, ATTR_PRENOM },
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
                            cursor.getString(cursor.getColumnIndex(ATTR_ID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_NOM)),
                            cursor.getString(cursor.getColumnIndex(ATTR_PRENOM))
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
                new String[] { ATTR_ID, ATTR_NOM, ATTR_PRENOM },
                ATTR_ID + " = ?",
                new String[] { id },
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext()) {

            p= new C_Participant(
                    cursor.getString(cursor.getColumnIndex(ATTR_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_NOM)),
                    cursor.getString(cursor.getColumnIndex(ATTR_PRENOM))
            );
        }
        this.close();
        cursor.close();
        return p;
    }

}
