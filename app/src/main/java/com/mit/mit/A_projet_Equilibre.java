package com.mit.mit;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private List<Depense> liste_ontPayes;



    private LinearLayout llDep;
    private LinearLayout llEq;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_projet__equilibre);

        //initialisations
        liste_depenses=new ArrayList<>();

        //récupération du projet
        this.options=daoOptions.getOptionByUserId();
        System.out.println("IS ONLINE =" + options.online);
        this.part=daoparticipant.getParticipantById(options.userid);
        this.projet=daoProjet.getProjetByName(options.projetid);
        this.projet.creerLesListes(daoJour, daoparticipant);
        setTitle("Equilibrage "+this.projet.nom);


        //variables de la page
        llDep = (LinearLayout) findViewById(R.id.projet_equilibre_llDep);
        llEq = (LinearLayout) findViewById(R.id.projet_equilibre_llEq);



        populateListeDepenses();

        System.out.println("__BRUT___________________________");
        ecrireListeDep();

        System.out.println("__REGROUPEMENT___________________________");
        liste_depenses=equilibrage_regroupement(liste_depenses);
        ecrireListeDep();

        System.out.println("__SEPARATION___________________________");
        separerDeuxListes(liste_depenses);
        ecrireListeDep();

        System.out.println("__REGROUPEMENT PAYEUR RECEVEUR___________________________");
        liste_depenses=equilibrage_payeurReceveur(liste_depenses);
        ecrireListeDep();


        majInterface();

    }


//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------











//---------------------------------------------------------------------------------------
//	FONCIONS
//---------------------------------------------------------------------------------------

    //ecrire le contenu de la liste
    public void ecrireListeDep()
    {
        for (Depense d : liste_depenses)
        {
            System.out.println(">>"+d.payeur.prenom + " a payé " + d.montant+" a "+d.receveur.prenom);
        }
    }



    //CREATION DE LA LISTE ********************************

    //créer le liste des structures dépenses
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

    //****************************************************




    // 0 -séparer la liste des dettes et la liste des choses que les personnes ont payés
    public void separerDeuxListes (List<Depense> listeDep)
    {
        List<Depense> listeDettes = new ArrayList<>();
        List<Depense> listePaye = new ArrayList<>();

        //pour toutes les dépenses
        for (Depense d: listeDep)
        {
            if(d.payeur.mail.equals(d.receveur.mail))
            {
                listePaye.add(d);
            }
            else
            {
                listeDettes.add(d);
            }

        }

        this.liste_depenses=listeDettes;
        this.liste_ontPayes=listePaye;

    }

    //****************************************************




    // 1 - REGROUPEMENT DES STRUCTURES IDENTIQUES ********

    public List<Depense> equilibrage_regroupement (List<Depense> listeDep)
    {
        List<Depense> listeWork = new ArrayList<>();

        //pour tous les payeurs
        for (C_Participant payeur : projet.liste_participants)
        {
            //pour tous les receveurs
            for (C_Participant receveur : projet.liste_participants)
            {
                Depense maDep=new Depense();
                maDep.payeur=payeur;
                maDep.receveur=receveur;

                //pour toutes les dépenses
                for (Depense d: listeDep)
                {
                    //si je suis sur la bonne structure
                    if(d.payeur.mail.equals(payeur.mail) && d.receveur.mail.equals(receveur.mail))
                    {
                        maDep.montant=maDep.montant+d.montant;
                    }
                }
                listeWork.add(maDep);
            }
        }
        return listeWork;
    }

    //****************************************************




    // 2 - EQUILIBRE PAYEUR RECEVEUR SUR RECEVEUR PAYEUR ********

    public List<Depense> equilibrage_payeurReceveur (List<Depense> listeDep)
    {
        List<Depense> listeWork = new ArrayList<>();
        List<Depense> listeOpp = new ArrayList<>();

        for (Depense d : listeDep)
        {
            //System.out.println("--work with : ["+d.payeur.prenom+" a paye "+d.montant+" a "+d.receveur.prenom+"]");
            if(monOpposeExist(listeDep, d))
            {
                //System.out.println("----Opposé existe ");
                Depense dOpp = getMonOppose(listeDep, d);
                //System.out.println("---- = ["+dOpp.payeur.prenom+" a paye "+dOpp.montant+" a "+dOpp.receveur.prenom+"]");
                double val = d.montant-dOpp.montant;
                //System.out.println("----montant = "+ val);
                if(d.montant-dOpp.montant<0)
                {
                   // System.out.println("----négatif ");
                    dOpp.montant=dOpp.montant-d.montant;
                    if(!existInlist(listeOpp, dOpp))
                    {
                       // System.out.println("----Ajout ");
                        listeWork.add(dOpp);
                        listeOpp.add(d);
                    }
                }
                else
                {
                    //System.out.println("----positif ");
                    d.montant=d.montant-dOpp.montant;
                    if(!existInlist(listeOpp, d))
                    {
                       // System.out.println("----Ajout ");
                        listeWork.add(d);
                        listeOpp.add(dOpp);
                    }
                }
            }
            else
            {
                //System.out.println("----Opposé NON trouvé ");

                if(!existInlist(listeOpp, d))
                {
                    //System.out.println("----Ajout ");
                    listeWork.add(d);
                }
            }
        }

        return listeWork;
    }

    public boolean monOpposeExist(List<Depense> liste, Depense dep)
    {
        Depense deOpp = new Depense();
        deOpp.payeur=dep.receveur;
        deOpp.receveur=dep.payeur;
        deOpp.montant=0;

        for (Depense DepParcours : liste)
        {
            if(DepParcours.payeur.mail.equals(deOpp.payeur.mail) && DepParcours.receveur.mail.equals(deOpp.receveur.mail))
            {
                return true;
            }
        }
        return false;
    }

    public Depense getMonOppose(List<Depense> liste, Depense dep)
    {
        Depense deOpp = new Depense();
        deOpp.payeur=dep.receveur;
        deOpp.receveur=dep.payeur;
        deOpp.montant=0;

        for (Depense DepParcours : liste)
        {
            if(DepParcours.payeur.mail.equals(deOpp.payeur.mail) && DepParcours.receveur.mail.equals(deOpp.receveur.mail))
            {
                return DepParcours;
            }
        }
        return dep;
    }


    public boolean existInlist(List<Depense> liste, Depense dep)
    {
        System.out.println("*****Vérifie si existe déjà");
        for (Depense d : liste)
        {
            System.out.println("*****Recherche : ["+dep.payeur.prenom+" a paye "+dep.montant+" a "+dep.receveur.prenom+"] vs ["+d.payeur.prenom+" a paye "+d.montant+" a "+d.receveur.prenom+"]");
            if(d.payeur.mail.equals(dep.payeur.mail))
            {
                System.out.println("*****Payeur ok ");
                if(d.receveur.mail.equals(dep.receveur.mail))
                {
                    System.out.println("*****Receveurok ");
                    if(d.montant == dep.montant)
                    {
                        System.out.println("*****Montant ok ");
                        return true;
                    }
                }
            }
        }
        System.out.println("*****Non trouvé, ajout");
        return false;
    }

    //****************************************************





    // 3 - Regrouper les depenses restantes


    //****************************************************







    // INTERFACE

    public void majInterface()
    {
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for(Depense d : liste_ontPayes)
        {
            //layout global
            LinearLayout LLgen = new LinearLayout(this);
            LLgen.setOrientation(LinearLayout.HORIZONTAL);
            LLgen.setLayoutParams(LLParams);

            //image gauche
            ImageButton img = new ImageButton(this);
            img.setImageResource(R.drawable.ic_equilibre_dep);
            img.setBackgroundColor(Color.TRANSPARENT);

            //layout infos
            LinearLayout LLinfos = new LinearLayout(this);
            LLinfos.setOrientation(LinearLayout.VERTICAL);
            LLinfos.setLayoutParams(LLParams);


            // nom
            TextView txtNom = new TextView(this);
            txtNom.setText(""+d.payeur.prenom);
            txtNom.setTextSize(14);
            txtNom.setTextColor(Color.parseColor("#ac035d"));
            txtNom.setPadding(0, 20, 10, 0);

            // dep
            TextView txtDep = new TextView(this);
            txtDep.setText("A dépensé " + d.montant + " € pour ses activités");
            txtDep.setPadding(0, 0, 10, 0);

            LLinfos.addView(txtNom);
            LLinfos.addView(txtDep);

            LLgen.addView(img);
            LLgen.addView(LLinfos);


            llDep.addView(LLgen);
        }


        for (Depense d : liste_depenses)
        {
            //layout global
            LinearLayout LLgen = new LinearLayout(this);
            LLgen.setOrientation(LinearLayout.HORIZONTAL);
            LLgen.setLayoutParams(LLParams);

            //image gauche
            ImageButton img = new ImageButton(this);
            img.setImageResource(R.drawable.ic_equilibre);
            img.setBackgroundColor(Color.TRANSPARENT);

            //layout infos
            LinearLayout LLinfos = new LinearLayout(this);
            LLinfos.setOrientation(LinearLayout.VERTICAL);
            LLinfos.setLayoutParams(LLParams);


            // nom
            TextView txtNom = new TextView(this);
            txtNom.setText(""+d.receveur.prenom);
            txtNom.setTextSize(16);
            txtNom.setTextColor(Color.parseColor("#ac035d"));
            txtNom.setPadding(0, 20, 10, 0);

            // nom
            TextView txtDep = new TextView(this);
            txtDep.setText("doit " + d.montant + " € à " + d.payeur.prenom);
            txtDep.setTextSize(14);
            //txtDep.setTextColor(Color.parseColor("#ac035d"));
            txtDep.setPadding(0, 10, 10, 0);



            LLinfos.addView(txtNom);
            LLinfos.addView(txtDep);

            LLgen.addView(img);
            LLgen.addView(LLinfos);


            llEq.addView(LLgen);
        }



    }

    //****************************************************



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
