package com.mit.mit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class A_projet_Equilibre extends MainActivity
{


    class Depense
    {
        public C_Participant payeur;
        public C_Participant receveur;
        public double montant;
    };





    private C_Participant part;
    private C_Options options;
    private C_Projet projet;

    private List<Depense> liste_depenses;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_projet__equilibre);

        //initialisations
        liste_depenses=new ArrayList<>();


        //récupération du projet
        this.options=daoOptions.getOptionByUserId();
        System.out.println("IS ONLINE ="+options.online);
        this.part=daoparticipant.getParticipantById(options.userid);
        this.projet=daoProjet.getProjetByName(options.projetid);
        this.projet.creerLesListes(daoJour, daoparticipant);
        setTitle(this.projet.nom);


        populateListeDepenses();
    }


//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------











//---------------------------------------------------------------------------------------
//	FONCIONS
//---------------------------------------------------------------------------------------

    public void populateListeDepenses()
    {
        liste_depenses.clear();

        //pour tous les jours
        for(C_Jour j : projet.liste_jours)
        {
            System.out.println("****Work with jour : "+j.nomJour);
            //pour tous les sujets
            j.creerLesListes(daoSujet);
            for (C_Sujet sujet : j.liste_sujets)
            {
                System.out.println("****Work with sujet : "+sujet.idSujet);
                sujet.creerLesListes(daoMessage, daoparticipant);

                System.out.println("> Cout : " + sujet.prix);
                System.out.println("> Payeurs : "+sujet.quiApayeToString );
                System.out.println("> Montant : "+sujet.combienApayeToString );

                //pour tous les participants du sujet
                for (C_Participant payeur:sujet.liste_participent)
                {
                    //si j'ai payé
                    int positionPayeur = aPaye(payeur, sujet);
                    if (positionPayeur!=-1)
                    {
                        //combien chacun va devoir me rembourser
                        double montant=montantPaye(positionPayeur, sujet);

                        //on ne rajoute pas les dépenses nulles
                        if(montant>0)
                        {
                            double montantApartager = montant/sujet.liste_participent.size();


                            //création des dettes pour chacun
                            for (C_Participant receveur : sujet.liste_participent)
                            {
                                Depense dep= new Depense();
                                dep.payeur=payeur;
                                dep.receveur=receveur;
                                dep.montant=montantApartager;
                                liste_depenses.add(dep);
                                System.out.println("==>"+payeur.prenom+" a avancé "+ montantApartager + " à "+receveur.prenom);
                            }
                        }



                    }//fin if j'ai payé
                }//fin for les participants du sujet
            }//fin for tous les sujets du jour
        }//fin for tous les jours du projet

    }//fin calculer les depenses




    // si j'ai payé
    public int aPaye(C_Participant p, C_Sujet s)
    {
        int parcours=0;
        for(C_Participant payeur : s.liste_quiApaye)
        {
            if(p.mail.equals(payeur.mail))
            {
                return parcours;
            }
            parcours++;
        }

        return -1;
    }

    //combien j'ai payé
    public double montantPaye (int position, C_Sujet s)
    {
        int parcours=0;
        for (double montant : s.liste_combienApaye)
        {
            if (parcours==position)
            {
                return montant;
            }
            parcours++;
        }
        return -1;
    }











//---------------------------------------------------------------------------------------
//	MENU
//---------------------------------------------------------------------------------------



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a_projet__equilibre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
