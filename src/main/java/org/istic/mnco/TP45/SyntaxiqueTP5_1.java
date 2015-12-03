package org.istic.mnco.TP45;

import org.istic.mnco.TP45.automate.Analex1;
import org.istic.mnco.TP45.lexical.constlex;
import org.istic.mnco.TP45.lexical.unitelex;

import java.util.HashMap;
import java.util.Map;
/*
 * SyntaxiqueTP5_1.java
 */


/**
 * ANALYSEUR POUR LES EXPRESSION INFIXES
 * GENERATION (et calcul) PAR MACHINE A PILE
 * (instructions PUSH POP ADD SOUS MULT DIV)
 * <p>
 * <Axiome> ::= <exp> .
 * <Exp>  ::= <Terme> <STerme>
 * <Terme> ::= <Facteur> <SFacteur>
 * <STerme> ::= + <Exp> | - <Exp> | vide
 * <Facteur> ::= Entier | ( <Exp> )
 * <SFacteur> ::= * <Terme> | / <Terme> | vide
 */

public class SyntaxiqueTP5_1 {


    Analex1 monauto; // l'analyseur leical
    int lexlu;          // type de l'unite lexicale courante
    String valeurlue; //valeurde l'unite lexicale courante
    int[] PileEval;
    int iSommet;

    /*
     * Constructeur
     */
    SyntaxiqueTP5_1(String nomfich) {
        monauto = new Analex1();
        Ginit();
        monauto.initTete(nomfich);
        AvanceTete(); // lire une premiere unite lexical
    }


    void AvanceTete() {
        monauto.lancer();
        lexlu = monauto.unitelue.typelex;
        valeurlue = monauto.unitelue.valeurlex;

        //System.out.println("J'ai lu " + valeurlue);
    }

    void Message(String ch1, String ch2) {
        System.out.println("Erreur syntaxique dans " + ch1 + " " + ch2);
    }

    public void Axiome() {
        // <Axiome> ::= <exp> .
        Exp();
        if (lexlu != constlex.lexpoint) {
            System.out.println("Erreur syntaxique, point manquant");
        }
        Gfin();
    } // fin Axiome()

    void Exp() {
        // <Exp>  ::= <Terme> <STerme>
        Terme();
        STerme();
    }// fin Exp()


    void Terme() {
        // <Terme> ::= <Facteur> <SFacteur>
        Facteur();
        SFacteur();
    } // fin Terme()

    void STerme() {
        // <STerme> ::= + <Exp> | - <Exp> | vide
        switch (lexlu) {
            case constlex.lexplus:
                AvanceTete();
                Exp();
                GPPlus();
                break;
            case constlex.lexmoins:
                AvanceTete();
                Exp();
                GPMoins();
                break;
            default:
                break;
        }
    } // fin Sterme()

    void Facteur() {
        // <Facteur> ::= Entier | ( <Exp> )
        switch (lexlu) {
            case constlex.lexentier:
                GPEntier();
                AvanceTete();
                break;
            case constlex.lexPO:
                AvanceTete();
                Exp();
                if (lexlu != constlex.lexPF) {
                    System.out.println("Erreur syntaxique");
                }
                AvanceTete();
                break;
            default:
                System.out.println("Erreur syntaxique");
        }
    } // fin Facteur()

    void SFacteur() {
        //<SFacteur> ::= * <Terme> | / <Terme> | vide
        switch (lexlu) {
            case constlex.lexmult:
                AvanceTete();
                Terme();
                GPMult();
                break;
            case constlex.lexdiv:
                AvanceTete();
                Terme();
                GPDiv();
                break;
            default:
                break;
        }
    } // fin SFacteur()

    // POINTS DE GENERATION
    void GPEntier() {
        System.out.println("PUSH " + valeurlue);
        empiler(Integer.parseInt(valeurlue)); // passer d'une String à un int...
    }

    void GPPlus() {
        System.out.println("PLUS");
        int v1 = depiler();
        int v2 = depiler();
        empiler(v1 + v2);
    }

    void GPMoins() {
        System.out.println("MOINS");
        int v1 = depiler();
        int v2 = depiler();
        empiler(v2 - v1);
    }

    void GPMult() {
        System.out.println("MULT");
        int v1 = depiler();
        int v2 = depiler();
        empiler(v1 * v2);
    }

    void GPDiv() {
        System.out.println("DIV");
        int v1 = depiler();
        int v2 = depiler();
        empiler(v2 / v1);
    }

    void Ginit() {
        PileEval = new int[20];
        iSommet = -1;
        // ... et d'autres choses peut-etre}

    }

    void Gfin() {
        if (iSommet == 0) {
            System.out.println("Valeur de calcul : " + depiler());
        } else {
            System.out.println("Pb de pile : " + iSommet);
        }
    } // Gfin

    void empiler(int v) {
        iSommet++;
        PileEval[iSommet] = v;
    }

    int depiler() {
        iSommet--;
        return PileEval[iSommet + 1];
    }

} // fin de classe SyntaxiqueTP5_1

