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
public class FaireCategoriesLexicales {
    static Unite[] lesUnites = {
            new Unite(":=", "AFF"),
            new Unite(";", "PT_VIRG"),
            new Unite(",", "VIRG"),
            new Unite(".", "PT"),
            new Unite("(", "PAR_OUV"),
            new Unite(")", "PAR_FER"),
            new Unite("+", "PLUS"),
            new Unite("-", "MOINS"),
            new Unite("*", "MULT"),
            new Unite("/", "DIV"),
            new Unite(":=", "AFF_ADD"),
            new Unite("=", "EG"),
            new Unite("<", "INF"),
            new Unite("&lt;", "INF"),
            new Unite("<=", "INF_EG"),
            new Unite(">", "SUP"),
            new Unite(">=", "SUP_EG"),

            new Unite("erreur", "ERR"),
            new Unite("nbre", "NBRE"),
            new Unite("idf", "IDF"),

            new Unite("si", "SI"),
            new Unite("alors", "ALORS"),
            new Unite("sinon", "SINON"),
            new Unite("fsi", "FSI"),

            new Unite("debut", "DEBUT"),
            new Unite("fin", "FIN"),

            new Unite("entier", "ENTIER"),
            new Unite("booleen", "BOOLEEN"),

            new Unite("lire", "LIRE"),
            new Unite("ecrire", "ECRIRE"),

            new Unite("et", "ET"),
            new Unite("ou", "OU"),
            new Unite("non", "NON")

    };

    public static Unite quelleUnite(String enClair) {
        for (int i = 0; i < lesUnites.length; i++) {
            if (lesUnites[i].valeur.equals(enClair)) {
                return lesUnites[i];
            }
        }
        return null;
    }

    public static int quelNumero(String enClair) {
        for (int i = 0; i < lesUnites.length; i++) {
            if (lesUnites[i].valeur.equals(enClair)) {
                return i;
            }
        }
        return -1;
    }

    public static String quelNom(String enClair) {
        for (int i = 0; i < lesUnites.length; i++) {
            if (lesUnites[i].valeur.equals(enClair)) {
                return lesUnites[i].nom;
            }
        }
        return null;
    }

    public static String quelleValeur(String nom) {
        for (int i = 0; i < lesUnites.length; i++) {
            if (lesUnites[i].nom.equals(nom)) {
                return lesUnites[i].valeur;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        HashSet dejaVu = new HashSet();
        Ecriture as = new Ecriture("TP6/lexical/CategoriesLexicales.java");
        as.ecrire("\n" + "package TP6.lexical;");
        as.ecrire("\n" + "import java.util.Hashtable;");
        as.ecrire("\n/**");
        as.ecrire("\n * BigLang : catégories lexicales et crible");
        as.ecrire("\n * @author syntaxique.FaireCategoriesLexicales");
        as.ecrire("\n *");
        as.ecrire("\n */");
        as.ecrire("\n" + "public class CategoriesLexicales {");

        for (int i = 0; i < lesUnites.length; i++) {
            if (!dejaVu.contains(lesUnites[i].nom)) {
                as.ecrire("\n" + "    public static final int LEX_" + Service.completerADroite(lesUnites[i].nom, 15) + " = " + i + " ;");
                dejaVu.add(lesUnites[i].nom);
            }


        }
        as.ecrire("\n" + "    static Hashtable créerCrible () {");
        as.ecrire("\n" + "        Hashtable c = new Hashtable () ;");
        for (int i = 0; i < lesUnites.length; i++) {
            String motif = lesUnites[i].valeur;
            if (motif.matches("[a-zA-Z]+") && !motif.equals("erreur") && !motif.equals("nbre") && !motif.equals("idf")) {
                as.ecrire("\n" + "        c.put (\"" + motif + "\", new Integer(LEX_" + lesUnites[i].nom + ")) ;");
            }
        }
        as.ecrire("\n" + "        return c;");
        as.ecrire("\n" + "    }");
        as.ecrire("\n" + "    public static Hashtable crible = créerCrible() ;");

        as.ecrire("\n" + "    public static String [] enClair = {");
        as.ecrire("\n        \"" + lesUnites[0].nom + "\"");
        for (int i = 1; i < lesUnites.length; i++) {
            as.ecrire(",\n        \"" + lesUnites[i].nom + "\"");
        }
        as.ecrire("\n" + "");
        as.ecrire("\n" + "    };");

        as.ecrire("\n}\n");
        as.fermer();
    }

    public static class Unite {
        String valeur;
        String nom;

        Unite(String v, String n) {
            valeur = v;
            nom = n;
        }
    }
}
