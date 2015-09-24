package com.mit.mit;

/**
 *C_Participant : Represent an user
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class C_Participant {
    public String id;
    public String nom;
    public String prenom;
    public String mail;
    public String mdp;

    /**
     *Empty builder
     */
    public C_Participant() {
        super();
    }

    /**
     *DAO builder
     *@param id			User's Id
     *@param nom		User's name
     *@param prenom		User's surname
     *@param mail		User's mail adress
     *@param mdp		User's password
     */
    public C_Participant(String id, String nom, String prenom, String mail, String mdp) {
        super();
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.mdp = mdp;
    }

    /**
     *Application builder
     *@param nom		User's name
     *@param prenom		User's surname
     *@param mail		User's mail adress
     *@param mdp		User's password
     */
    public C_Participant(String nom, String prenom, String mail, String mdp) {
        super();
        this.id = mail;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.mdp = mdp;
    }

    /**
     *Application builder
     *@param mail		User's name
     *@param mdp		User's surname
     */
    public C_Participant(String mail, String mdp) {
        super();
        this.id = mail;
        this.nom = "";
        this.prenom = "";
        this.mail = mail;
        this.mdp = mdp;
    }

}
