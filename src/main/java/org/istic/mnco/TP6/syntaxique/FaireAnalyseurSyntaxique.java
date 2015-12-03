/*
 * Created on 4 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.istic.mnco.TP6.syntaxique;

import org.istic.mnco.TP6.es.Ecriture;
import org.istic.mnco.TP6.es.LectureFichierTexte;

import java.util.*;

/**
 * @author herman
 *         <p>
 *         Production d'un analyseur syntaxique LL(1) à partir d'une grammaire
 *         - l'analyseur produit est le code source <tt>AnalyseurSyntaxique.java</tt> dans
 *         le package <tt>bigLang.syntaxique</tt>.
 *         - la grammaire (fichier source <tt>grammaire.xml</tt>) est décrite en xml.
 *         On peut consulter la <a href="../../grammaire.dtd">dtd</a>.
 */
public class FaireAnalyseurSyntaxique {
    static LectureFichierTexte g;
    static String s;

    static Ecriture as = new Ecriture("TP6/syntaxique/AnalyseSyntaxique.java");
    static int niveau = 0;
    static String buf = "";
    static LinkedList multiBuf;
    static boolean differe = false;

    static String commentaire;
    static String genAvant;
    static String genApres;

    static HashSet permis = new HashSet();
    static String nomPG;
    static boolean pGVue;
    static String axiome;

    public static void initMultiBuf() {
        if (buf.indexOf("{") == -1 && buf.indexOf("}") != -1) {
            niveau--;
            buf = buf.substring(1);
        }
        //System.out.println (buf) ;
        as.ecrire("\n" + buf);
        if (buf.indexOf("{") != -1 && buf.indexOf("}") == -1) {
            niveau++;
        }
        buf = "";
        for (int i = 0; i < niveau; i++) {
            buf = buf + "\t";
        }
        //buf = buf + "/** nnn */" ;
        multiBuf = new LinkedList();
        // multiBuf.add(0, (buf + "/** nnn */")) ;
        differe = true;
    }

    public static void viderMultiBuf() {
        aLaLigne("");
        ListIterator it = multiBuf.listIterator();
        while (it.hasNext()) {
            String b = (String) it.next();
            as.ecrire("\n" + b);
        }
        differe = false;
    }

    public static void sortie(String p) {
        buf = buf + p;
    }

    public static void aLaLigne(String p) {
        if (differe) {
            if (buf.indexOf("{") == -1 && buf.indexOf("}") != -1) {
                niveau--;
                buf = buf.substring(1);
            }
            //System.out.println (buf) ;
            multiBuf.addLast(buf);
            if (buf.indexOf("{") != -1 && buf.indexOf("}") == -1) {
                niveau++;
            }
            buf = "";
            for (int i = 0; i < niveau; i++) {
                buf = buf + "\t";
            }
            buf = buf + p;
        } else {
            if (buf.indexOf("{") == -1 && buf.indexOf("}") != -1) {
                niveau--;
                buf = buf.substring(1);
            }
            //System.out.println (buf) ;
            as.ecrire("\n" + buf);
            if (buf.indexOf("{") != -1 && buf.indexOf("}") == -1) {
                niveau++;
            }
            buf = "";
            for (int i = 0; i < niveau; i++) {
                buf = buf + "\t";
            }
            buf = buf + p;
        }
    }

    public static void lexical() {
        permis.add(FaireCategoriesLexicales.quelleValeur(s));
        sortie(" ||");
        aLaLigne("\ttete.categorie == CategoriesLexicales.LEX_" + s);
        avTete();
    }

    public static void lexical1() {
        permis.add(FaireCategoriesLexicales.quelleValeur(s));
        sortie("tete.categorie == CategoriesLexicales.LEX_" + s);
        avTete();
    }

    public static void selection() {
        consommer("selection");
        lexical1();
        while (!s.equals("/selection")) {
            lexical();
        }
        sortie(") {");
        consommer("/selection");
    }

    public static void element() {
        commentaire = commentaire + s + " ";
        if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
            // Non terminal
            aLaLigne("" + s + " () ;");
        } else if ((s.charAt(0) == '$')) {
            // point de génération
            aLaLigne("Generation." + s.substring(1) + " () ;");
        } else if (s.equals("vide")) {
            // vide
            aLaLigne("// rien");
        } else {
            // terminal
            String elt = FaireCategoriesLexicales.quelNom(s);
            if (elt == null) {
                System.out.println("PB avec le terminal: " + s);
            }
            aLaLigne("franchir (CategoriesLexicales.LEX_" + elt + ", BigLangException.BL_SYNT_" + elt + "_ATTENDU) ;");
        }
        avTete();
    }

    public static void element1() {
        commentaire = commentaire + s + " ";
        if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
            // Non terminal
            aLaLigne("" + s + " () ;");
        } else if ((s.charAt(0) == '$')) {
            // point de génération
            aLaLigne("Generation." + s.substring(1) + " () ;");
        } else if (s.equals("vide")) {
            // vide
            aLaLigne("// rien");
        } else {
            // terminal
            String elt = FaireCategoriesLexicales.quelNom(s);
            if (elt == null) {
                System.out.println("PB avec le terminal: " + s);
            }
            aLaLigne("franchir (CategoriesLexicales.LEX_" + elt + ", BigLangException.BL_SYNT_" + elt + "_ATTENDU) ;");
        }
        avTete();
    }

    public static void droite() {
        consommer("pd");
        commentaire = commentaire + "| ";
        if (s.equals("selection")) {
            aLaLigne("else if (");
            selection();
        }
        consommer("corps");
        element1();
        while (!s.equals("/corps")) {
            element();
        }
        aLaLigne("}");
        consommer("/corps");
        consommer("/pd");
    }

    public static void droite1() {
        consommer("pd");
        if (s.equals("selection")) {
            aLaLigne("if      (");
            selection();
        }
        consommer("corps");
        element1();
        while (!s.equals("/corps")) {
            element();
        }
        aLaLigne("}");
        consommer("/corps");
        consommer("/pd");
    }

    public static void droites() {
        droite();
        if (s.equals("pd")) {
            droites();
        }
    }

    public static void gauche() {
        if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
            // Non terminal
            if (nomPG == null) {
                axiome = s;
            }
            nomPG = s;
            pGVue = true;
            commentaire = commentaire + " " + nomPG;
            aLaLigne("public static void " + s + " () throws BigLangException {");
        } else if ((s.charAt(0) == '$')) {
            // point de génération
            if (!pGVue) {
                genAvant = "Generation." + s.substring(1) + " () ;";
            } else {
                genApres = "Generation." + s.substring(1) + " () ;";
            }
            commentaire = commentaire + " " + s;

        }
    }

    public static void gauche1() {
        consommer("gauche");
        pGVue = false;
        if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
            // Non terminal
            if (nomPG == null) {
                axiome = s;
            }
            nomPG = s;
            pGVue = true;
            commentaire = commentaire + " " + nomPG;
            aLaLigne("public static void " + nomPG + " () throws BigLangException {");
        } else if ((s.charAt(0) == '$')) {
            // point de génération
            genAvant = "Generation." + s.substring(1) + " () ;";
            commentaire = commentaire + " " + s;
        }

        avTete();
        while (!s.equals("/gauche")) {
            gauche();
            avTete();
        }
        commentaire = commentaire + " -> ";
        //aLaLigne("public static void "+ nomPG +" () throws BigLangException {");
        // avTete();
        consommer("/gauche");
    }

    public static void regle() {
        consommer("regle");
        permis.clear();
        initMultiBuf();
        commentaire = "/** ";
        genAvant = "";
        genApres = "";
        gauche1();
        if (!genAvant.equals("")) {
            aLaLigne(genAvant);
            genAvant = "";
        }
        aLaLigne("a.baliseO(\"" + nomPG + "\");");
        droite1();
        if (s.equals("pd")) {
            droites();
        }
        aLaLigne("else {");
        aLaLigne("throw new BigLangException (\"" + nomPG + "\", \"" + permis + "\");");
        aLaLigne("}");
        aLaLigne("a.baliseF(\"" + nomPG + "\");");
        if (!genApres.equals("")) {
            aLaLigne(genApres);
            genApres = "";
        }
        consommer("/regle");
    }

    public static void regles() {
        regle();
        aLaLigne("}");
        commentaire = commentaire + "*/";
        multiBuf.set(0, multiBuf.get(0) + commentaire);
        viderMultiBuf();
        if (s.equals("regle")) {
            regles();
        }
    }

    public static void avTete() {
        s = g.lireChaine(" \n\r\t><");
        // System.out.println ("tete: "+s);
    }

    public static void consommer(String m) {
        if (!s.equals(m)) {
            System.out.println("PB avec: " + s + " vs " + m);
        }
        avTete();
    }

    public static void main(String[] args) {
        String[] lesMois = {"janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre", "décembre"};
        Calendar cal = new GregorianCalendar();
        g = new LectureFichierTexte("grammaire.xml");
        sortie("package TP6.syntaxique;");
        aLaLigne("import TP6.BigLangException;");
        aLaLigne("import TP6.generation.Generation;");
        aLaLigne("import TP6.lexical.AnalyseLexicale;");
        aLaLigne("import TP6.lexical.AnalyseLexicale.TeteDeLecture;");
        aLaLigne("import TP6.lexical.CategoriesLexicales;");
        aLaLigne("/*");
        aLaLigne(" * Créé le " + cal.get(Calendar.DAY_OF_MONTH) + " " + lesMois[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR));
        aLaLigne(" *");
        aLaLigne(" */");
        aLaLigne("");
        aLaLigne("/**");
        aLaLigne(" * Analyseur syntaxique pour BigLang");
        aLaLigne(" * @author syntaxique.FaireAnalyseurSyntaxique");
        aLaLigne(" *");
        aLaLigne(" */");
        aLaLigne("public class AnalyseSyntaxique {");
        aLaLigne("static AnalyseLexicale lex = new AnalyseLexicale () ;");
        aLaLigne("public static TeteDeLecture tete ;");
        aLaLigne("static void initTete (String f) {");
        aLaLigne("	lex.initTete(f+\".big\");");
        aLaLigne("	tete = lex.lexLu ;");
        aLaLigne("}");
        aLaLigne("static void AvTête () {");
        aLaLigne("try {");
        aLaLigne("lex.lancer() ;");
        aLaLigne("tete = lex.lexLu ;");
        aLaLigne("} catch (Exception e) {");
        aLaLigne("tete.categorie = CategoriesLexicales.LEX_ERR ;");
        aLaLigne("}");
        aLaLigne("}");
        aLaLigne("static void franchir (int elt, int err) throws BigLangException {");
        aLaLigne("if (tete.categorie != elt) {throw new BigLangException (BigLangException.BL_SYNT, err);}");
        aLaLigne("a.texte(tete);");
        aLaLigne("AvTête() ;");
        aLaLigne("}");
        aLaLigne("static ArbreSyntaxique a ;");
        /*
		aLaLigne("public static void prélude (String fichier) {");
		aLaLigne("a = new ArbreSyntaxique (fichier+\".xml\") ;");
		aLaLigne("lex = new AnalyseLexicale () ;");
		aLaLigne("lex.initTete(fichier+\".big\");");
		aLaLigne("AvTête() ;");		
		aLaLigne("}");
		aLaLigne("public static void postlude () {");
		aLaLigne("a.clore();");
		aLaLigne("}");
		*/
        avTete();
        while (!s.equals("grammaire")) {
            avTete();
        }
        avTete();
        while (!s.equals("grammaire")) {
            avTete();
        }
        avTete();
        regles();
        consommer("/grammaire");
        aLaLigne("public static void main (String [] args) {");
        aLaLigne("a = new ArbreSyntaxique (args[0]+\".xml\", \"" + axiome + "\") ;");
        aLaLigne("lex = new AnalyseLexicale () ;");
        aLaLigne("initTete(args[0]);");
        aLaLigne("Generation.initGeneration(args[0]) ;");
        aLaLigne("AvTête() ;");
        aLaLigne("try {");
        aLaLigne(axiome + "() ;");
        aLaLigne("} catch (BigLangException e) {");
        aLaLigne("e.printStackTrace();");
        aLaLigne("}");
        aLaLigne("Generation.objet.sortie();");
        aLaLigne("a.clore();");
        aLaLigne("}");
        aLaLigne("}");
        aLaLigne("");
        as.fermer();
    }
}
