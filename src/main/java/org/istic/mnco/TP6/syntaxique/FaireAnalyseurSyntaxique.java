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
        permis.add(FaireCatégoriesLexicales.quelleValeur(s));
        sortie(" ||");
        aLaLigne("\ttête.catégorie == CatégoriesLexicales.LEX_" + s);
        avTête();
    }

    public static void lexical1() {
        permis.add(FaireCatégoriesLexicales.quelleValeur(s));
        sortie("tête.catégorie == CatégoriesLexicales.LEX_" + s);
        avTête();
    }

    public static void sélection() {
        consommer("selection");
        lexical1();
        while (!s.equals("/selection")) {
            lexical();
        }
        sortie(") {");
        consommer("/selection");
    }

    public static void élément() {
        commentaire = commentaire + s + " ";
        if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
            // Non terminal
            aLaLigne("" + s + " () ;");
        } else if ((s.charAt(0) == '$')) {
            // point de génération
            aLaLigne("Génération." + s.substring(1) + " () ;");
        } else if (s.equals("vide")) {
            // vide
            aLaLigne("// rien");
        } else {
            // terminal
            String elt = FaireCatégoriesLexicales.quelNom(s);
            if (elt == null) {
                System.out.println("PB avec le terminal: " + s);
            }
            aLaLigne("franchir (CatégoriesLexicales.LEX_" + elt + ", BigLangException.BL_SYNT_" + elt + "_ATTENDU) ;");
        }
        avTête();
    }

    public static void élément1() {
        commentaire = commentaire + s + " ";
        if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
            // Non terminal
            aLaLigne("" + s + " () ;");
        } else if ((s.charAt(0) == '$')) {
            // point de génération
            aLaLigne("Génération." + s.substring(1) + " () ;");
        } else if (s.equals("vide")) {
            // vide
            aLaLigne("// rien");
        } else {
            // terminal
            String elt = FaireCatégoriesLexicales.quelNom(s);
            if (elt == null) {
                System.out.println("PB avec le terminal: " + s);
            }
            aLaLigne("franchir (CatégoriesLexicales.LEX_" + elt + ", BigLangException.BL_SYNT_" + elt + "_ATTENDU) ;");
        }
        avTête();
    }

    public static void droite() {
        consommer("pd");
        commentaire = commentaire + "| ";
        if (s.equals("selection")) {
            aLaLigne("else if (");
            sélection();
        }
        consommer("corps");
        élément1();
        while (!s.equals("/corps")) {
            élément();
        }
        aLaLigne("}");
        consommer("/corps");
        consommer("/pd");
    }

    public static void droite1() {
        consommer("pd");
        if (s.equals("selection")) {
            aLaLigne("if      (");
            sélection();
        }
        consommer("corps");
        élément1();
        while (!s.equals("/corps")) {
            élément();
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
                genAvant = "Génération." + s.substring(1) + " () ;";
            } else {
                genApres = "Génération." + s.substring(1) + " () ;";
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
            genAvant = "Génération." + s.substring(1) + " () ;";
            commentaire = commentaire + " " + s;
        }

        avTête();
        while (!s.equals("/gauche")) {
            gauche();
            avTête();
        }
        commentaire = commentaire + " -> ";
        //aLaLigne("public static void "+ nomPG +" () throws BigLangException {");
        // avTête();
        consommer("/gauche");
    }

    public static void règle() {
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

    public static void règles() {
        règle();
        aLaLigne("}");
        commentaire = commentaire + "*/";
        multiBuf.set(0, multiBuf.get(0) + commentaire);
        viderMultiBuf();
        if (s.equals("regle")) {
            règles();
        }
    }

    public static void avTête() {
        s = g.lireChaine(" \n\r\t><");
        // System.out.println ("tete: "+s);
    }

    public static void consommer(String m) {
        if (!s.equals(m)) {
            System.out.println("PB avec: " + s + " vs " + m);
        }
        avTête();
    }

    public static void main(String[] args) {
        String[] lesMois = {"janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre", "décembre"};
        Calendar cal = new GregorianCalendar();
        g = new LectureFichierTexte("grammaire.xml");
        sortie("package TP6.syntaxique;");
        aLaLigne("import TP6.BigLangException;");
        aLaLigne("import TP6.generation.Génération;");
        aLaLigne("import TP6.lexical.AnalyseLexicale;");
        aLaLigne("import TP6.lexical.AnalyseLexicale.TêteDeLecture;");
        aLaLigne("import TP6.lexical.CatégoriesLexicales;");
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
        aLaLigne("public static TêteDeLecture tête ;");
        aLaLigne("static void initTête (String f) {");
        aLaLigne("	lex.initTête(f+\".big\");");
        aLaLigne("	tête = lex.lexLu ;");
        aLaLigne("}");
        aLaLigne("static void AvTête () {");
        aLaLigne("try {");
        aLaLigne("lex.lancer() ;");
        aLaLigne("tête = lex.lexLu ;");
        aLaLigne("} catch (Exception e) {");
        aLaLigne("tête.catégorie = CatégoriesLexicales.LEX_ERR ;");
        aLaLigne("}");
        aLaLigne("}");
        aLaLigne("static void franchir (int elt, int err) throws BigLangException {");
        aLaLigne("if (tête.catégorie != elt) {throw new BigLangException (BigLangException.BL_SYNT, err);}");
        aLaLigne("a.texte(tête);");
        aLaLigne("AvTête() ;");
        aLaLigne("}");
        aLaLigne("static ArbreSyntaxique a ;");
        /*
		aLaLigne("public static void prélude (String fichier) {");
		aLaLigne("a = new ArbreSyntaxique (fichier+\".xml\") ;");
		aLaLigne("lex = new AnalyseLexicale () ;");
		aLaLigne("lex.initTête(fichier+\".big\");");
		aLaLigne("AvTête() ;");		
		aLaLigne("}");
		aLaLigne("public static void postlude () {");
		aLaLigne("a.clore();");
		aLaLigne("}");
		*/
        avTête();
        while (!s.equals("grammaire")) {
            avTête();
        }
        avTête();
        while (!s.equals("grammaire")) {
            avTête();
        }
        avTête();
        règles();
        consommer("/grammaire");
        aLaLigne("public static void main (String [] args) {");
        aLaLigne("a = new ArbreSyntaxique (args[0]+\".xml\", \"" + axiome + "\") ;");
        aLaLigne("lex = new AnalyseLexicale () ;");
        aLaLigne("initTête(args[0]);");
        aLaLigne("Génération.initGénération(args[0]) ;");
        aLaLigne("AvTête() ;");
        aLaLigne("try {");
        aLaLigne(axiome + "() ;");
        aLaLigne("} catch (BigLangException e) {");
        aLaLigne("e.printStackTrace();");
        aLaLigne("}");
        aLaLigne("Génération.objet.sortie();");
        aLaLigne("a.clore();");
        aLaLigne("}");
        aLaLigne("}");
        aLaLigne("");
        as.fermer();
    }
}
