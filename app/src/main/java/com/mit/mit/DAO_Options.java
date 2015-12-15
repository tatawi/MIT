package com.mit.mit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by baudraim on 15/12/2015.
 */
public class DAO_Options extends DAO_Bdd {

//---------------------------------------------------------------------------------------
//	VARIABLES
//---------------------------------------------------------------------------------------
    public static final String TABLE = "OPTIONS";
    public static final String ATTR_ID = "_id";
    public static final String ATTR_CURRENTUSER = "currentuser";
   // public static final String ATTR_ONLINE = "online";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_CURRENTUSER + " TEXT);";
                   // + ATTR_ONLINE + " TEXT);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE + ";";

//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    /**
     *Application builder
     *@param pContext			Context
     */
    public DAO_Options(Context pContext) {
        super(pContext);
    }



    public void ajouterCurrentUser(C_Participant p)
    {
        this.open();
        ContentValues value = new ContentValues();
        value.put(ATTR_CURRENTUSER, p.mail);
        bdd.insert(TABLE, null, value);
        this.close();
    }





    /**
     *Delete an day in bdd
     *@param p			day'id to delete
     */
    public void supprimerCurrentUser(C_Participant p)
    {
        try
        {
            //LOCAL
            this.open();
            bdd.delete(
                    TABLE,
                    ATTR_CURRENTUSER + " = ?",
                    new String[]{p.mail}
            );
            this.close();
        }
        catch (Exception ex)
        {System.out.println("[ERROR] : " +ex.getMessage());}
    }

    public void supprimerUser()
    {
            //LOCAL
            this.open();
            bdd.delete(
                    TABLE,
                    null,
                    null
            );
            this.close();
    }


    public String getSavedUser()
    {
        this.open();
        String iduser="";
        Cursor cursor = bdd.query(
                TABLE,
                new String[]{ATTR_ID, ATTR_CURRENTUSER},
                null,
                null,
                null,
                null,
                null,
                null
        );
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            iduser=cursor.getString(cursor.getColumnIndex(ATTR_CURRENTUSER));
        }

        this.close();
        cursor.close();

        return iduser;
    }



}
