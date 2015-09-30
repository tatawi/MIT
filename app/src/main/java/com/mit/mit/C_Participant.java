package com.mit.mit;

/**
 *C_Participant : Represent an user
 *
 *@author Maxime BAUDRAIS
 *@version 0.1
 */
public class C_Participant {
    public int id;
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
    public C_Participant(int id, String nom, String prenom, String mail, String mdp) {
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
        this.id = this.setID(nom);
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.mdp = mdp;
    }



    private int setID(String stringID)
    {
        int i=0;
        int ascii=0;
        while( i < stringID.length())
        {
            char character = stringID.charAt(i);
            ascii =ascii+ (int) character;
            i++;
        }
        return ascii;
    }

}
