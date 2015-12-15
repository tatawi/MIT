package com.mit.mit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *BaseSQLite : SQLiteOpenHelper
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class BaseSQLite extends SQLiteOpenHelper {

    public BaseSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL(DAO_Participant.TABLE_CREATE);
        db.execSQL(DAO_Projet.TABLE_CREATE);
        db.execSQL(DAO_Jour.TABLE_CREATE);
        db.execSQL(DAO_Sujet.TABLE_CREATE);
        db.execSQL(DAO_Message.TABLE_CREATE);
        db.execSQL(DAO_Options.TABLE_CREATE);

        // Si la table est vide => création d'un user "admin" "admin"
        //db.execSQL(Constante.UTILISATEUR_INITIALIZE);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        db.execSQL(DAO_Participant.TABLE_DROP);
        db.execSQL(DAO_Projet.TABLE_DROP);
        db.execSQL(DAO_Jour.TABLE_DROP);
        db.execSQL(DAO_Sujet.TABLE_DROP);
        db.execSQL(DAO_Message.TABLE_DROP);
        db.execSQL(DAO_Options.TABLE_DROP);

        onCreate(db);
    }

}
