package com.mit.mit;


        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import java.text.SimpleDateFormat;

/**
 *DAO_Message : DAO for C_Message class
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class DAO_Message extends DAO_Bdd {
    //---------------------------------------------------------------------------------------
//	VARIABLES
//---------------------------------------------------------------------------------------
    public static final String TABLE = " MESSAGE";
    public static final String ATTR_ID = "_id";
    public static final String ATTR_HEURE = "heure";
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_EMETTEUR = "id_participantEmetteur";
    public static final String ATTR_PERSONNESAYANTVUES = "personnesAyantVuesToString";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " TEXT PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_HEURE + " TEXT, "
                    + ATTR_MESSAGE + " TEXT, "
                    + ATTR_EMETTEUR + " TEXT, "
                    + ATTR_PERSONNESAYANTVUES + " TEXT;";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE + ";";

//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------
    /**
     *Application builder
     *@param pContext			Context
     */
    public DAO_Message(Context pContext) {
        super(pContext);
    }

    /**
     *Add an message in bdd
     *@param m			message to add
     */
    public void ajouter(C_Message m) {
        this.open();

        ContentValues value = new ContentValues();
        value.put(ATTR_ID, m.id);
        value.put(ATTR_HEURE, m.heure.toString());
        value.put(ATTR_MESSAGE, m.message);
        value.put(ATTR_EMETTEUR, m.id_participantEmetteur);
        value.put(ATTR_PERSONNESAYANTVUES, m.personnesAyantVuesToString);

        bdd.insert(TABLE, null, value);
        this.close();
    }

    /**
     *Delete an message in bdd
     *@param id			message'id to delete
     */
    public void supprimer(String id) {
        this.open();

        bdd.delete(
                TABLE,
                ATTR_ID + " = ?",
                new String[] { id }
        );

        this.close();
    }

    /**
     *Delete an message in bdd
     *@param m			message to edit
     */
    public void modifier(C_Message m) {
        this.open();

        ContentValues value = new ContentValues();
        value.put(ATTR_ID, m.id);
        value.put(ATTR_HEURE, m.heure.toString());
        value.put(ATTR_MESSAGE, m.message);
        value.put(ATTR_EMETTEUR, m.id_participantEmetteur);
        value.put(ATTR_PERSONNESAYANTVUES, m.personnesAyantVuesToString);

        bdd.update(
                TABLE,
                value,
                ATTR_ID + " = ?",
                new String[] { String.valueOf(m.id) }
        );
        this.close();
    }

    /**
     *Return all messages in the bdd
     *@return 		return a list of C_Participants containing all messages
     */
    public List<C_Message> getMessages() {
        this.open();
        List<C_Message> liste_messages=new ArrayList<C_Message>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date lheure = new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_HEURE, ATTR_MESSAGE, ATTR_EMETTEUR, ATTR_PERSONNESAYANTVUES},
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

            liste_messages.add(
                    new C_Message(
                            cursor.getString(cursor.getColumnIndex(ATTR_ID)),
                            lheure,
                            cursor.getString(cursor.getColumnIndex(ATTR_MESSAGE)),
                            cursor.getString(cursor.getColumnIndex(ATTR_EMETTEUR)),
                            cursor.getString(cursor.getColumnIndex(ATTR_PERSONNESAYANTVUES))
                            )
            );
        }
        this.close();
        cursor.close();
        return liste_messages;
    }

    /**
     *Get one message from the bdd
     *@param id			message's id to got
     *@return 			return the participant with the id
     */
    public C_Message getMessageById(String id) {
        this.open();
        C_Message p = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date lheure = new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[] { ATTR_ID, ATTR_HEURE, ATTR_MESSAGE, ATTR_EMETTEUR, ATTR_PERSONNESAYANTVUES},
                ATTR_ID + " = ?",
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

            p=new C_Message(
                    cursor.getString(cursor.getColumnIndex(ATTR_ID)),
                    lheure,
                    cursor.getString(cursor.getColumnIndex(ATTR_MESSAGE)),
                    cursor.getString(cursor.getColumnIndex(ATTR_EMETTEUR)),
                    cursor.getString(cursor.getColumnIndex(ATTR_PERSONNESAYANTVUES))
                    );
        }
        this.close();
        cursor.close();
        return p;
    }






}










