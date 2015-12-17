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
    public String online;

    /**
     *Empty builder
     */
    public C_Options(C_Participant p) {
        super();
        this.userid = p.mail;
        this.projetid = "null";
        this.jourid = "null";
        this.sujetid = "null";
        this.online = "null";
    }

    /**
     *DAO builder
     *@param id			User's Id
     *@param userid		User's name
     *@param projetid		User's surname
     *@param jourid		User's mail adress
     *@param sujetid		User's password
     */
    public C_Options(int id, String userid, String projetid, String jourid, String sujetid, String online) {
        super();
        this.id = id;
        this.userid = userid;
        this.projetid = projetid;
        this.jourid = jourid;
        this.sujetid = sujetid;
        this.online = online;
    }

    public C_Options() {
        super();

    }







}
