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
    public static final String ATTR_USERID = "userid";
    public static final String ATTR_PROJETID = "projetid";
    public static final String ATTR_JOURID = "jourid";
    public static final String ATTR_SUJETID = "sujetid";
    public static final String ATTR_ONLINE = "online";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_USERID + " TEXT, "
                    + ATTR_PROJETID + " TEXT, "
                    + ATTR_JOURID + " TEXT, "
                    + ATTR_SUJETID + " TEXT, "
                    + ATTR_ONLINE + " TEXT);";
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


    public void ajouter(C_Options o)
    {
        //LOCAL
        this.open();
        ContentValues value = new ContentValues();
        value.put(ATTR_USERID, o.userid);
        value.put(ATTR_PROJETID, o.projetid);
        value.put(ATTR_JOURID, o.jourid);
        value.put(ATTR_SUJETID, o.sujetid);
        value.put(ATTR_ONLINE, o.online);

        bdd.insert(TABLE, null, value);
        this.close();
    }



    public void supprimer(C_Options o)
    {
            //LOCAL
            this.open();
            bdd.delete(
                    TABLE,
                    ATTR_USERID + " = ?",
                    new String[]{o.userid}
            );
            this.close();
        }



    public void modifier(C_Options o)
    {
        //LOCAL
        this.open();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        ContentValues value = new ContentValues();
        value.put(ATTR_USERID, o.userid);
        value.put(ATTR_PROJETID, o.projetid);
        value.put(ATTR_JOURID, o.jourid);
        value.put(ATTR_SUJETID, o.sujetid);
        value.put(ATTR_ONLINE, o.online);

        bdd.update(
                TABLE,
                value,
                ATTR_USERID + " = ?",
                new String[]{String.valueOf(o.userid)}
        );
        this.close();



    }



    public void ajouterOUmodifier(C_Options o) {
        C_Options dao_o = this.getOptionByUserId(o.userid);
        if (dao_o != null) {
            this.modifier(o);
        } else {
            this.ajouter(o);
        }
    }




    /**
     *Get one day from the bdd
     *@param id            day's id to got
     *@return return the day with the id
     */
    public C_Options getOptionByUserId(String id) {
        this.open();
        C_Options o = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date day = new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[]{ATTR_ID, ATTR_USERID, ATTR_PROJETID, ATTR_JOURID, ATTR_SUJETID, ATTR_ONLINE},
                ATTR_USERID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            o = new C_Options(
                    cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_USERID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_PROJETID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_JOURID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_SUJETID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_ONLINE))
            );
        }
        this.close();
        cursor.close();
        return o;
    }


    public C_Options getOption() {
        this.open();
        List<C_Options> liste_options = new ArrayList<C_Options>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        C_Options option = new C_Options();
        Cursor cursor = bdd.query(
                TABLE,
                new String[]{ATTR_ID, ATTR_USERID, ATTR_PROJETID, ATTR_JOURID, ATTR_SUJETID, ATTR_ONLINE},
                null,
                null,
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {


            liste_options.add(
                    new C_Options(
                            cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_USERID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_PROJETID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_JOURID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_SUJETID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_ONLINE))
                    )
            );
        }
        this.close();
        cursor.close();

        for (C_Options o: liste_options)
        {
            option=o;
        }
        return option;
    }



}
