package com.mit.mit;

/**
 * Created by baudraim on 17/12/2015.
 */
public class C_Options {



    public int id;
    public String userid;
    public String projetid;
    public String jourid;
    public String sujetid;
    public boolean rememberme;
    public boolean online;
    public boolean userSaved;

    /**
     *Empty builder
     */

    public C_Options() {
        super();
        this.userid = "";
        this.projetid = "null";
        this.jourid = "null";
        this.sujetid = "null";
        this.rememberme= false;
        this.online = true;
        this.userSaved=false;
    }

    public C_Options(C_Participant p, boolean online, boolean userSaved) {
        super();
        System.out.println("C_OPTIONS user : "+p.mail);
        this.userid = p.mail;
        this.projetid = "null";
        this.jourid = "null";
        this.sujetid = "null";
        this.rememberme= false;
        this.online = online;
        this.userSaved=userSaved;
    }

    /**
     *DAO builder
     *@param id			User's Id
     *@param userid		User's name
     *@param projetid		User's surname
     *@param jourid		User's mail adress
     *@param sujetid		User's password
     */
    public C_Options(int id, String userid, String projetid, String jourid, String sujetid, boolean remember, boolean online, boolean userSaved) {
        super();
        this.id = id;
        this.userid = userid;
        this.projetid = projetid;
        this.jourid = jourid;
        this.sujetid = sujetid;
        this.rememberme = remember;
        this.online = online;
        this.userSaved=userSaved;
    }












}
