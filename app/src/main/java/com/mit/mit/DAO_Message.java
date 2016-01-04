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
    public static final String ATTR_IDMESSAGE = "idMessage";
    public static final String ATTR_HEURE = "heure";
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_EMETTEUR = "id_participantEmetteur";
    public static final String ATTR_PERSONNESAYANTVUES = "personnesAyantVuesToString";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_IDMESSAGE + " TEXT, "
                    + ATTR_HEURE + " TEXT, "
                    + ATTR_MESSAGE + " TEXT, "
                    + ATTR_EMETTEUR + " TEXT, "
                    + ATTR_PERSONNESAYANTVUES + " TEXT);";
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
    public void ajouter(C_Message m, boolean SaveOnline)
    {
        //LOCAL
        this.open();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy HH:mm:ss");
        ContentValues value = new ContentValues();
        value.put(ATTR_IDMESSAGE, m.id);
        value.put(ATTR_HEURE, m.heure.toString());
        value.put(ATTR_MESSAGE, m.message);
        value.put(ATTR_EMETTEUR, m.id_participantEmetteur);
        value.put(ATTR_PERSONNESAYANTVUES, m.personnesAyantVuesToString);

        bdd.insert(TABLE, null, value);
        this.close();

        //ONLINE
        if (SaveOnline) {
            ParseObject Message = new ParseObject("Message");
            Message.put("id", m.id);
            Message.put("heure", sdf.format(m.heure));
            Message.put("message", m.message);
            Message.put("id_participantEmetteur", m.id_participantEmetteur);
            Message.put("personnesAyantVuesToString", m.personnesAyantVuesToString);
            Message.saveInBackground();
        }
    }


    /**
     *Delete an message in bdd
     *@param id			message'id to delete
     */
    public void supprimer(String id, boolean SaveOnline)
    {
        try
        {
            //LOCAL
            this.open();
            bdd.delete(
                    TABLE,
                    ATTR_IDMESSAGE + " = ?",
                    new String[]{id}
            );
            this.close();


            //ONLINE
            if (SaveOnline) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
                query.whereEqualTo("id", id);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject Message, ParseException e) {
                        try {
                            if (e == null) {
                                Message.delete();
                                Message.saveInBackground();
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
     *Delete an message in bdd
     *@param m			message to edit
     */
    public void modifier(C_Message m, boolean SaveOnline)
    {
        //LOCAL
        this.open();
        ContentValues value = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        value.put(ATTR_IDMESSAGE, m.id);
        value.put(ATTR_HEURE, sdf.format(m.heure));
        value.put(ATTR_MESSAGE, m.message);
        value.put(ATTR_EMETTEUR, m.id_participantEmetteur);
        value.put(ATTR_PERSONNESAYANTVUES, m.personnesAyantVuesToString);

        bdd.update(
                TABLE,
                value,
                ATTR_IDMESSAGE + " = ?",
                new String[]{String.valueOf(m.id)}
        );
        this.close();


        //ONLINE
        if (SaveOnline) {
            final C_Message ms = m;
            final SimpleDateFormat sdff = new SimpleDateFormat("dd/mm/yy HH:mm:ss");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
            query.whereEqualTo("id", m.id);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject Message, ParseException e) {
                    if (e == null) {
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.
                        Message.put("id", ms.id);
                        Message.put("heure", sdff.format(ms.heure));
                        Message.put("message", ms.message);
                        Message.put("id_participantEmetteur", ms.id_participantEmetteur);
                        Message.put("personnesAyantVuesToString", ms.personnesAyantVuesToString);
                        Message.saveInBackground();
                    }
                }
            });
        }
    }


    /**
     *Add a message or modify it if already exists
     *@param m			message to add or edit
     */
    public void ajouterOUmodifier(C_Message m, boolean SaveOnline) {
        C_Message dao_m = this.getMessageById(m.id);
        if (dao_m != null) {
            this.modifier(m, SaveOnline);
        } else {
            this.ajouter(m, false);
        }
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
                new String[] { ATTR_IDMESSAGE, ATTR_HEURE, ATTR_MESSAGE, ATTR_EMETTEUR, ATTR_PERSONNESAYANTVUES},
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
                            cursor.getString(cursor.getColumnIndex(ATTR_IDMESSAGE)),
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
                new String[] { ATTR_IDMESSAGE, ATTR_HEURE, ATTR_MESSAGE, ATTR_EMETTEUR, ATTR_PERSONNESAYANTVUES},
                ATTR_IDMESSAGE + " = ?",
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
                    cursor.getString(cursor.getColumnIndex(ATTR_IDMESSAGE)),
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










