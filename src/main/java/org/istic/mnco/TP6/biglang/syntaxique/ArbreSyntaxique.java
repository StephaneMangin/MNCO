/*
 * Created on 5 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * Outils de production et relecture d'un fichier <code>monmFichier.xml</code>
 * représentant l'arbre syntaxique du fichier <code>monmFichier.big</code>
 */
package org.istic.mnco.TP6.biglang.syntaxique;

import org.istic.mnco.TP6.biglang.lexical.AnalyseLexicale;
import org.istic.mnco.TP6.es.Ecriture;
import org.istic.mnco.TP6.es.LectureFichierTexte;

/**
 * @author herman
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ArbreSyntaxique {
    Ecriture a;
    LectureFichierTexte b;
    String nom;

    public ArbreSyntaxique(String n, String t) {
        nom = n;
        a = new Ecriture(n);
        a.ecrire("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        a.ecrire("<!DOCTYPE " + t + ">");
    }

    public ArbreSyntaxique(String n) {
        nom = n;
    }

    public void baliseO(String b) {
        a.ecrire("<" + b + ">\n");
    }

    public void baliseF(String b) {
        a.ecrire("</" + b + ">\n");
    }

    public void texte(AnalyseLexicale.TeteDeLecture b) {
        a.ecrire(b + "\n");
    }

    public void clore() {
        a.fermer();
    }

    public void lexicaux() {
        b = new LectureFichierTexte(nom);
        while (!b.finDeFichier()) {
            String ligne = b.lireChaine("\n\r");
            if (ligne.charAt(0) == '[') {
                System.out.println(ligne);
            }
        }
        b.fermer();

    }
}
