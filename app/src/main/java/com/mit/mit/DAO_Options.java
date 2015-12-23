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
    public static final String ATTR_IDOPTION = "idoption";
    public static final String ATTR_USERID = "userid";
    public static final String ATTR_PROJETID = "projetid";
    public static final String ATTR_JOURID = "jourid";
    public static final String ATTR_SUJETID = "sujetid";
    public static final String ATTR_REMEMBERME = "rememberme";
    public static final String ATTR_ONLINE = "online";
    public static final String ATTR_USERSAVED = "usersaved";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE + "("
                    + ATTR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ATTR_IDOPTION + " TEXT, "
                    + ATTR_USERID + " TEXT, "
                    + ATTR_PROJETID + " TEXT, "
                    + ATTR_JOURID + " TEXT, "
                    + ATTR_SUJETID + " TEXT, "
                    + ATTR_REMEMBERME + " TEXT, "
                    + ATTR_ONLINE + " TEXT, "
                    + ATTR_USERSAVED + " TEXT);";
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
        System.out.println("C_Options ajouter");
        //LOCAL
        this.open();
        ContentValues value = new ContentValues();
        value.put(ATTR_IDOPTION, "opt");
        value.put(ATTR_USERID, o.userid);
        value.put(ATTR_PROJETID, o.projetid);
        value.put(ATTR_JOURID, o.jourid);
        value.put(ATTR_SUJETID, o.sujetid);

        if(o.rememberme)
        {
            value.put(ATTR_REMEMBERME, "true");
        }
        else
        {
            value.put(ATTR_REMEMBERME, "false");
        }

        if(o.online)
        {
            value.put(ATTR_ONLINE, "true");
        }
        else
        {
            value.put(ATTR_ONLINE, "false");
        }

        if(o.userSaved)
        {
            value.put(ATTR_USERSAVED, "true");
        }
        else
        {
            value.put(ATTR_USERSAVED, "false");
        }

        bdd.insert(TABLE, null, value);
        System.out.println("C_Options added");
        this.close();
    }



    public void supprimer()
    {
            //LOCAL
            this.open();
            bdd.delete(
                    TABLE,
                    ATTR_IDOPTION + " = ?",
                    new String[]{"opt"}
            );
            this.close();


    }


    public void supprimerALL()
    {
        //LOCAL
        this.open();
        bdd.execSQL("delete from " + TABLE);
        this.close();
    }


    public void modifier(C_Options o)
    {
        //LOCAL
        this.open();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        ContentValues value = new ContentValues();
        value.put(ATTR_IDOPTION, "opt");
        value.put(ATTR_USERID, o.userid);
        value.put(ATTR_PROJETID, o.projetid);
        value.put(ATTR_JOURID, o.jourid);
        value.put(ATTR_SUJETID, o.sujetid);
        //value.put(ATTR_REMEMBERME, o.rememberme);
        //value.put(ATTR_ONLINE, o.online);

        if(o.rememberme)
        {
            value.put(ATTR_REMEMBERME, "true");
        }
        else
        {
            value.put(ATTR_REMEMBERME, "false");
        }

        if(o.online)
        {
            value.put(ATTR_ONLINE, "true");
        }
        else
        {
            value.put(ATTR_ONLINE, "false");
        }

        if(o.userSaved)
        {
            value.put(ATTR_USERSAVED, "true");
        }
        else
        {
            value.put(ATTR_USERSAVED, "false");
        }

        bdd.update(
                TABLE,
                value,
                ATTR_USERID + " = ?",
                new String[]{String.valueOf(o.userid)}
        );
        this.close();



    }



    public void ajouterOUmodifier(C_Options o) {
        C_Options dao_o = this.getOptionByUserId();
        if (dao_o != null) {
            this.modifier(o);
        } else {
            this.ajouter(o);
        }
    }




    /**
     *Get one day from the bdd
     *@return return the day with the id
     */
    public C_Options getOptionByUserId() {
        System.out.println("GET OPTIONS");
        this.open();
        C_Options o = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date day = new Date();
        Cursor cursor = bdd.query(
                TABLE,
                new String[]{ATTR_ID, ATTR_IDOPTION, ATTR_USERID, ATTR_PROJETID, ATTR_JOURID, ATTR_SUJETID, ATTR_REMEMBERME, ATTR_ONLINE, ATTR_USERSAVED},
                ATTR_IDOPTION + " = ?",
                new String[]{"opt"},
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            System.out.println("cursor");
            boolean online;
            boolean remember;
            boolean userSaved;

            if(cursor.getString(cursor.getColumnIndex(ATTR_REMEMBERME)).equals("true")) {
                remember=true;
            } else {
                remember=false;
            }

            if(cursor.getString(cursor.getColumnIndex(ATTR_ONLINE)).equals("true")) {
                online=true;
            } else {
                online=false;
            }

            if(cursor.getString(cursor.getColumnIndex(ATTR_USERSAVED)).equals("true")) {
                userSaved=true;
            } else {
                userSaved=false;
            }


            o = new C_Options(
                    cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_USERID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_PROJETID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_JOURID)),
                    cursor.getString(cursor.getColumnIndex(ATTR_SUJETID)),
                    remember,
                    online,
                    userSaved
            );
        }
        this.close();
        cursor.close();
        return o;
    }


    /*public C_Options getOption() {
        System.out.println("GET OPTIONS");
        this.open();
        List<C_Options> liste_options = new ArrayList<C_Options>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        C_Options option = new C_Options();
        Cursor cursor = bdd.query(
                TABLE,
                new String[]{ATTR_ID, ATTR_IDOPTION, ATTR_USERID, ATTR_PROJETID, ATTR_JOURID, ATTR_SUJETID, ATTR_REMEMBERME, ATTR_ONLINE},
                null,
                null,
                null,
                null,
                null,
                null
        );

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            System.out.println("in cursor");
            boolean online;
            boolean remember;
            boolean userSaved;

            if(cursor.getString(cursor.getColumnIndex(ATTR_REMEMBERME)).equals("true")) {
                remember=true;
            } else {
                remember=false;
            }

            if(cursor.getString(cursor.getColumnIndex(ATTR_ONLINE)).equals("true")) {
                online=true;
            } else {
                online=false;
            }

            if(cursor.getString(cursor.getColumnIndex(ATTR_USERSAVED)).equals("true")) {
                userSaved=true;
            } else {
                userSaved=false;
            }


            System.out.println("DAO READ********************");
            System.out.println("----user : " + cursor.getString(cursor.getColumnIndex(ATTR_USERID)));
            System.out.println("----online : " + online);
            System.out.println("----userSaved : " + userSaved);

            liste_options.add(
                    new C_Options(
                            cursor.getInt(cursor.getColumnIndex(ATTR_ID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_USERID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_PROJETID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_JOURID)),
                            cursor.getString(cursor.getColumnIndex(ATTR_SUJETID)),
                            remember,
                            online,
                            userSaved
                    )
            );


        }
        this.close();
        cursor.close();
        System.out.println("close bdd");

        for (C_Options o: liste_options)
        {
            System.out.println("DAO READ LIST********************");
            System.out.println("----user : " + o.userid);
            System.out.println("----online : " + o.online);
            System.out.println("----userSaved : " + o.userSaved);
            option=o;
        }
        return option;
    }*/



}
