package com.mit.mit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 *DAO_Bdd : DAO main class
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class DAO_Bdd {

    // Si mise à jour de la base de données => incrémenter VERSION
    protected final static int VERSION = 2;

    protected final static String NOM = "database.db";
    protected SQLiteDatabase bdd = null;
    protected BaseSQLite sqlHandler = null;

    public DAO_Bdd(Context pContext)
    {
        this.sqlHandler = new BaseSQLite(pContext, NOM, null, VERSION);
    }

    public void open()
    {
        bdd = sqlHandler.getWritableDatabase();
    }

    public void close()
    {
        bdd.close();
    }

    public SQLiteDatabase getDb()
    {
        return bdd;
    }

}
