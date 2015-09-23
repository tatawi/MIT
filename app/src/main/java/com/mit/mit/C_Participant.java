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

    /**
     *Empty builder
     */
    public C_Participant() {
        super();
    }

    /**
     *DAO builder
     *@param id			User's Id
     *@param nom			User's name
     *@param prenom		User's surname
     */
    public C_Participant(String id, String nom, String prenom) {
        super();
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     *Application builder
     *@param nom			User's name
     *@param prenom		User's surname
     */
    public C_Participant(String nom, String prenom) {
        super();
        this.id = nom+prenom;
        this.nom = nom;
        this.prenom = null;
    }

}
