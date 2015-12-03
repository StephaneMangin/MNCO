/*
 * Created on 31 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.istic.mnco.TP6.syntaxique;

import org.istic.mnco.TP6.es.Ecriture;
import org.istic.mnco.TP6.service.Service;

import java.util.HashSet;


/**
 * @author herman
 *         <p>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class FaireCat�goriesLexicales {
    static Unit�[] lesUnit�s = {
            new Unit�(":=", "AFF"),
            new Unit�(";", "PT_VIRG"),
            new Unit�(",", "VIRG"),
            new Unit�(".", "PT"),
            new Unit�("(", "PAR_OUV"),
            new Unit�(")", "PAR_FER"),
            new Unit�("+", "PLUS"),
            new Unit�("-", "MOINS"),
            new Unit�("*", "MULT"),
            new Unit�("/", "DIV"),
            new Unit�(":=", "AFF_ADD"),
            new Unit�("=", "EG"),
            new Unit�("<", "INF"),
            new Unit�("&lt;", "INF"),
            new Unit�("<=", "INF_EG"),
            new Unit�(">", "SUP"),
            new Unit�(">=", "SUP_EG"),

            new Unit�("erreur", "ERR"),
            new Unit�("nbre", "NBRE"),
            new Unit�("idf", "IDF"),

            new Unit�("si", "SI"),
            new Unit�("alors", "ALORS"),
            new Unit�("sinon", "SINON"),
            new Unit�("fsi", "FSI"),

            new Unit�("debut", "DEBUT"),
            new Unit�("fin", "FIN"),

            new Unit�("entier", "ENTIER"),
            new Unit�("booleen", "BOOLEEN"),

            new Unit�("lire", "LIRE"),
            new Unit�("ecrire", "ECRIRE"),

            new Unit�("et", "ET"),
            new Unit�("ou", "OU"),
            new Unit�("non", "NON")

    };

    public static Unit� quelleUnit�(String enClair) {
        for (int i = 0; i < lesUnit�s.length; i++) {
            if (lesUnit�s[i].valeur.equals(enClair)) {
                return lesUnit�s[i];
            }
        }
        return null;
    }

    public static int quelNum�ro(String enClair) {
        for (int i = 0; i < lesUnit�s.length; i++) {
            if (lesUnit�s[i].valeur.equals(enClair)) {
                return i;
            }
        }
        return -1;
    }

    public static String quelNom(String enClair) {
        for (int i = 0; i < lesUnit�s.length; i++) {
            if (lesUnit�s[i].valeur.equals(enClair)) {
                return lesUnit�s[i].nom;
            }
        }
        return null;
    }

    public static String quelleValeur(String nom) {
        for (int i = 0; i < lesUnit�s.length; i++) {
            if (lesUnit�s[i].nom.equals(nom)) {
                return lesUnit�s[i].valeur;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        HashSet d�j�Vu = new HashSet();
        Ecriture as = new Ecriture("TP6/lexical/Cat�goriesLexicales.java");
        as.ecrire("\n" + "package TP6.lexical;");
        as.ecrire("\n" + "import java.util.Hashtable;");
        as.ecrire("\n/**");
        as.ecrire("\n * BigLang : cat�gories lexicales et crible");
        as.ecrire("\n * @author syntaxique.FaireCat�goriesLexicales");
        as.ecrire("\n *");
        as.ecrire("\n */");
        as.ecrire("\n" + "public class Cat�goriesLexicales {");

        for (int i = 0; i < lesUnit�s.length; i++) {
            if (!d�j�Vu.contains(lesUnit�s[i].nom)) {
                as.ecrire("\n" + "    public static final int LEX_" + Service.completerADroite(lesUnit�s[i].nom, 15) + " = " + i + " ;");
                d�j�Vu.add(lesUnit�s[i].nom);
            }


        }
        as.ecrire("\n" + "    static Hashtable cr�erCrible () {");
        as.ecrire("\n" + "        Hashtable c = new Hashtable () ;");
        for (int i = 0; i < lesUnit�s.length; i++) {
            String motif = lesUnit�s[i].valeur;
            if (motif.matches("[a-zA-Z]+") && !motif.equals("erreur") && !motif.equals("nbre") && !motif.equals("idf")) {
                as.ecrire("\n" + "        c.put (\"" + motif + "\", new Integer(LEX_" + lesUnit�s[i].nom + ")) ;");
            }
        }
        as.ecrire("\n" + "        return c;");
        as.ecrire("\n" + "    }");
        as.ecrire("\n" + "    public static Hashtable crible = cr�erCrible() ;");

        as.ecrire("\n" + "    public static String [] enClair = {");
        as.ecrire("\n        \"" + lesUnit�s[0].nom + "\"");
        for (int i = 1; i < lesUnit�s.length; i++) {
            as.ecrire(",\n        \"" + lesUnit�s[i].nom + "\"");
        }
        as.ecrire("\n" + "");
        as.ecrire("\n" + "    };");

        as.ecrire("\n}\n");
        as.fermer();
    }

    public static class Unit� {
        String valeur;
        String nom;

        Unit�(String v, String n) {
            valeur = v;
            nom = n;
        }
    }
}
