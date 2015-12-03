
/*
 * Created on 4 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package syntaxique;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

import es.Ecriture;
import es.LectureFichierTexte;

/**
 * @author herman
 *
 * Production d'un analyseur syntaxique LL(1) � partir d'une grammaire
 * - l'analyseur produit est le code source <tt>AnalyseurSyntaxique.java</tt> dans
 * le package <tt>bigLang.syntaxique</tt>.
 * - la grammaire (fichier source <tt>grammaire.xml</tt>) est d�crite en xml.
 * On peut consulter la <a href="../../grammaire.dtd">dtd</a>.
 */
public class FaireAnalyseurSyntaxique {	
	static LectureFichierTexte g;
	static String s ;
	
	static Ecriture as = new Ecriture ("TP6/syntaxique/AnalyseSyntaxique.java");
	static int niveau = 0 ;
	static String buf = "" ;
	static LinkedList multiBuf ;
	static boolean diff�r� = false ;
	
	static String commentaire ;
	static String genAvant ;
	static String genApr�s ;
	
	static HashSet permis = new HashSet();
	static String nomPG ;
	static boolean pGVue ;
	static String axiome ;
	
	public static void initMultiBuf () {
		if (buf.indexOf("{") == -1 && buf.indexOf("}") != -1) {niveau--; buf = buf.substring(1);}
		//System.out.println (buf) ;
		as.ecrire("\n"+buf);
		if (buf.indexOf("{") != -1 && buf.indexOf("}") == -1) {niveau++;}
		buf = "" ;
		for (int i = 0; i < niveau ; i++) {buf = buf + "\t";}
		//buf = buf + "/** nnn */" ;
		multiBuf = new LinkedList () ;
		// multiBuf.add(0, (buf + "/** nnn */")) ;
		diff�r� = true ;
	}
	public static void viderMultiBuf () {
		aLaLigne("");
		ListIterator it = multiBuf.listIterator() ;
		while (it.hasNext()) {
			String b = (String) it.next() ;
			as.ecrire("\n"+b) ;
		}
		diff�r� = false ;
	}
	public static void sortie (String p) {
		buf = buf + p;
	}
	public static void aLaLigne (String p) {
		if (diff�r�) {
			if (buf.indexOf("{") == -1 && buf.indexOf("}") != -1) {niveau--; buf = buf.substring(1);}
			//System.out.println (buf) ;
			multiBuf.addLast(buf) ;
			if (buf.indexOf("{") != -1 && buf.indexOf("}") == -1) {niveau++;}
			buf = "" ;
			for (int i = 0; i < niveau ; i++) {buf = buf + "\t";}
			buf = buf + p ;				
		} else {
			if (buf.indexOf("{") == -1 && buf.indexOf("}") != -1) {niveau--; buf = buf.substring(1);}
			//System.out.println (buf) ;
			as.ecrire("\n"+buf);
			if (buf.indexOf("{") != -1 && buf.indexOf("}") == -1) {niveau++;}
			buf = "" ;
			for (int i = 0; i < niveau ; i++) {buf = buf + "\t";}
			buf = buf + p ;		
		}
	}
	public static void lexical () {
		permis.add(FaireCat�goriesLexicales.quelleValeur(s));
		sortie (" ||");
		aLaLigne("\tt�te.cat�gorie == Cat�goriesLexicales.LEX_"+s);
		avT�te();
	}
	public static void lexical1 () {
		permis.add(FaireCat�goriesLexicales.quelleValeur(s));
		sortie ("t�te.cat�gorie == Cat�goriesLexicales.LEX_"+s);
		avT�te();
	}
	public static void s�lection () {
		consommer("selection") ;
		lexical1 () ;
		while (!s.equals("/selection")){
			lexical () ;
		}
		sortie (") {");
		consommer("/selection") ;
	}
	public static void �l�ment () {
		commentaire = commentaire + s + " " ;
		if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
			// Non terminal
			aLaLigne(""+s+" () ;") ;
		}
		else if ((s.charAt(0) == '$'))  {
			// point de g�n�ration
			aLaLigne("G�n�ration."+s.substring(1)+" () ;") ;
		}
		else if (s.equals("vide"))  {
			// vide
			aLaLigne ("// rien") ;
		}
		else {
			// terminal
			String elt = FaireCat�goriesLexicales.quelNom(s) ;
			if (elt == null) { System.out.println ("PB avec le terminal: "+s);};
			aLaLigne("franchir (Cat�goriesLexicales.LEX_"+elt+", BigLangException.BL_SYNT_"+elt+"_ATTENDU) ;") ;
		}
		avT�te() ;
	}
	public static void �l�ment1 () {
		commentaire = commentaire + s + " " ;
		if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
			// Non terminal
			aLaLigne(""+s+" () ;") ;
		}
		else if ((s.charAt(0) == '$'))  {
			// point de g�n�ration
			aLaLigne("G�n�ration."+s.substring(1)+" () ;") ;
		}
		else if (s.equals("vide"))  {
			// vide
			aLaLigne ("// rien") ;
		}
		else {
			// terminal
			String elt = FaireCat�goriesLexicales.quelNom(s) ;
			if (elt == null) { System.out.println ("PB avec le terminal: "+s);};
			aLaLigne("franchir (Cat�goriesLexicales.LEX_"+elt+", BigLangException.BL_SYNT_"+elt+"_ATTENDU) ;") ;
		}
		avT�te() ;
	}
	public static void droite () {
		consommer("pd") ;
		commentaire = commentaire + "| " ;
		if (s.equals("selection")) {aLaLigne("else if (");s�lection();}
		consommer("corps");
		�l�ment1 () ;
		while (!s.equals("/corps")){
			�l�ment () ;
		}
		aLaLigne("}");
		consommer("/corps");
		consommer("/pd") ;
	}
	public static void droite1 () {
		consommer("pd") ;
		if (s.equals("selection")) {aLaLigne("if      (");s�lection();}
		consommer("corps");
		�l�ment1 () ;
		while (!s.equals("/corps")){
			�l�ment () ;
		}
		aLaLigne("}");
		consommer("/corps");
		consommer("/pd") ;
	}
	public static void droites () {
		droite () ;
		if (s.equals("pd")) {droites();}
	}
	public static void gauche () {
		if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
			// Non terminal
			if (nomPG == null) {axiome = s;}
			nomPG = s ;
			pGVue = true ;
			commentaire = commentaire + " " + nomPG ;
			aLaLigne("public static void "+s+" () throws BigLangException {");
		}
		else if ((s.charAt(0) == '$'))  {
			// point de g�n�ration
			if (!pGVue) {genAvant = "G�n�ration."+s.substring(1)+" () ;" ;}
			else {genApr�s = "G�n�ration."+s.substring(1)+" () ;" ;}
			commentaire = commentaire + " " + s ;
			
		}
	}
	public static void gauche1 () {
		consommer("gauche") ;
		pGVue = false ;
		if (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z') {
			// Non terminal
			if (nomPG == null) {axiome = s;}
			nomPG = s ;
			pGVue = true ;
			commentaire = commentaire + " " + nomPG ;
			aLaLigne("public static void "+ nomPG +" () throws BigLangException {");
		}
		else if ((s.charAt(0) == '$'))  {
			// point de g�n�ration
			genAvant = "G�n�ration."+s.substring(1)+" () ;" ;
			commentaire = commentaire + " " + s ;
		}

		avT�te() ;
		while (!s.equals("/gauche")){
			gauche () ;
			avT�te();
		}
		commentaire = commentaire + " -> " ;
		//aLaLigne("public static void "+ nomPG +" () throws BigLangException {");
		// avT�te();
		consommer("/gauche") ;
	}
	public static void r�gle () {
		consommer("regle") ;
		permis.clear();
		initMultiBuf() ;
		commentaire = "/** " ;
		genAvant = "" ;
		genApr�s = "" ;
		gauche1 () ;
		if (!genAvant.equals("")) {aLaLigne(genAvant); genAvant = "";}
		aLaLigne("a.baliseO(\""+nomPG+"\");");
		droite1 ();
		if (s.equals("pd")) {droites();}
		aLaLigne("else {");
		aLaLigne("throw new BigLangException (\""+nomPG+"\", \""+permis+"\");");
		aLaLigne("}");
		aLaLigne("a.baliseF(\""+nomPG+"\");");
		if (!genApr�s.equals("")) {aLaLigne(genApr�s); genApr�s = "";}
		consommer("/regle") ;
	}
	public static void r�gles () {
		r�gle () ;
		aLaLigne("}");
		commentaire = commentaire + "*/" ;
		multiBuf.set(0, (String) multiBuf.get(0) + commentaire);
		viderMultiBuf();
		if (s.equals("regle")) {r�gles();}
	}
	public static void avT�te () {
		s = g.lireChaine(" \n\r\t><") ;
		// System.out.println ("tete: "+s);
	}
	public static void consommer (String m) {
		if (!s.equals(m)) { System.out.println ("PB avec: "+s+" vs "+m); }
		avT�te() ;
	}
	public static void main (String []args){
		String [] lesMois = {"janvier", "f�vrier", "mars", "avril", "mai", "juin", "juillet", "ao�t", "septembre", "octobre", "novembre", "d�cembre"};
 		Calendar cal = new GregorianCalendar() ;
		g = new LectureFichierTexte("grammaire.xml") ;
		sortie ("package TP6.syntaxique;");
		aLaLigne ("import TP6.BigLangException;");
		aLaLigne ("import TP6.generation.G�n�ration;");
		aLaLigne ("import TP6.lexical.AnalyseLexicale;");
		aLaLigne ("import TP6.lexical.AnalyseLexicale.T�teDeLecture;");
		aLaLigne ("import TP6.lexical.Cat�goriesLexicales;");
		aLaLigne ("/*");
		aLaLigne (" * Cr�� le "+cal.get(Calendar.DAY_OF_MONTH)+" "+lesMois[cal.get(Calendar.MONTH)]+" "+cal.get(Calendar.YEAR));
		aLaLigne (" *");
		aLaLigne (" */");
		aLaLigne ("");
		aLaLigne ("/**");
		aLaLigne (" * Analyseur syntaxique pour BigLang");
		aLaLigne (" * @author syntaxique.FaireAnalyseurSyntaxique");
		aLaLigne (" *");
		aLaLigne (" */");
		aLaLigne ("public class AnalyseSyntaxique {");
		aLaLigne("static AnalyseLexicale lex = new AnalyseLexicale () ;");
		aLaLigne("public static T�teDeLecture t�te ;");
		aLaLigne("static void initT�te (String f) {");
		aLaLigne("	lex.initT�te(f+\".big\");");
		aLaLigne("	t�te = lex.lexLu ;");
		aLaLigne("}");
		aLaLigne("static void AvT�te () {");
		aLaLigne("try {");
		aLaLigne("lex.lancer() ;");
		aLaLigne("t�te = lex.lexLu ;");
		aLaLigne("} catch (Exception e) {");
		aLaLigne("t�te.cat�gorie = Cat�goriesLexicales.LEX_ERR ;");
		aLaLigne("}");
		aLaLigne("}");
		aLaLigne("static void franchir (int elt, int err) throws BigLangException {");
		aLaLigne("if (t�te.cat�gorie != elt) {throw new BigLangException (BigLangException.BL_SYNT, err);}");
		aLaLigne("a.texte(t�te);");
		aLaLigne("AvT�te() ;");
		aLaLigne("}");
		aLaLigne("static ArbreSyntaxique a ;");
		/*
		aLaLigne("public static void pr�lude (String fichier) {");
		aLaLigne("a = new ArbreSyntaxique (fichier+\".xml\") ;");
		aLaLigne("lex = new AnalyseLexicale () ;");
		aLaLigne("lex.initT�te(fichier+\".big\");");
		aLaLigne("AvT�te() ;");		
		aLaLigne("}");
		aLaLigne("public static void postlude () {");
		aLaLigne("a.clore();");
		aLaLigne("}");
		*/
		avT�te () ;
		while (!s.equals("grammaire")) {
			avT�te();
		}
		avT�te () ;
		while (!s.equals("grammaire")) {
			avT�te();
		}
		avT�te();
		r�gles () ;
		consommer ("/grammaire") ;
		aLaLigne("public static void main (String [] args) {");
		aLaLigne("a = new ArbreSyntaxique (args[0]+\".xml\", \""+axiome+"\") ;");
		aLaLigne("lex = new AnalyseLexicale () ;");
		aLaLigne("initT�te(args[0]);");
		aLaLigne("G�n�ration.initG�n�ration(args[0]) ;");
		aLaLigne("AvT�te() ;");
		aLaLigne("try {");
		aLaLigne(axiome+"() ;");
		aLaLigne("} catch (BigLangException e) {");
		aLaLigne("e.printStackTrace();");
		aLaLigne("}");
		aLaLigne("G�n�ration.objet.sortie();");
		aLaLigne("a.clore();");
		aLaLigne("}");
		aLaLigne("}");
		aLaLigne("");
		as.fermer();
	}
}
