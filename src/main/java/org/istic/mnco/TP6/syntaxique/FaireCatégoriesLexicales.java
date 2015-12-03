/*
 * Created on 31 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package syntaxique;

import java.util.HashSet;
import java.util.Hashtable;

import es.Ecriture;

import service.Service;


/**
 * @author herman
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FaireCatégoriesLexicales {
	public static class Unité {
		String valeur ;
		String nom ;
		Unité (String v, String n) {
			valeur = v ;
			nom = n ;
		}
	}
	static Unité [] lesUnités = {
			new Unité (":=",  "AFF"),
			new Unité (";",  "PT_VIRG"),
			new Unité (",",  "VIRG"),
			new Unité (".",  "PT"),
			new Unité ("(",  "PAR_OUV"),
			new Unité (")",  "PAR_FER"),
			new Unité ("+",  "PLUS"),
			new Unité ("-",  "MOINS"),
			new Unité ("*",  "MULT"),
			new Unité ("/",  "DIV"),
			new Unité (":=",  "AFF_ADD"),
			new Unité ("=",  "EG"),
			new Unité ("<",  "INF"),
			new Unité ("&lt;",  "INF"),
			new Unité ("<=",  "INF_EG"),
			new Unité (">",  "SUP"),
			new Unité (">=",  "SUP_EG"),
			
			new Unité ("erreur",  "ERR"),
			new Unité ("nbre",  "NBRE"),
			new Unité ("idf", "IDF"),
			
			new Unité ("si",  "SI"),
			new Unité ("alors",  "ALORS"),
			new Unité ("sinon",  "SINON"),
			new Unité ("fsi",  "FSI"),
			
			new Unité ("debut",  "DEBUT"),
			new Unité ("fin",  "FIN"),
			
			new Unité ("entier",  "ENTIER"),
			new Unité ("booleen",  "BOOLEEN"),
			
			new Unité ("lire",  "LIRE"),
			new Unité ("ecrire",  "ECRIRE"),
			
			new Unité ("et",  "ET"),
			new Unité ("ou",  "OU"),			
			new Unité ("non",  "NON")
			
	};
	public static Unité quelleUnité (String enClair) {
		for (int i = 0; i < lesUnités.length; i++) {
			if (lesUnités[i].valeur.equals(enClair)) {return lesUnités[i];}			
		}
		return null ;
	}
	public static int quelNuméro (String enClair) {
		for (int i = 0; i < lesUnités.length; i++) {
			if (lesUnités[i].valeur.equals(enClair)) {return i;}			
		}
		return -1 ;
	}
	public static String quelNom (String enClair) {
		for (int i = 0; i < lesUnités.length; i++) {
			if (lesUnités[i].valeur.equals(enClair)) {return lesUnités[i].nom;}			
		}
		return null ;
	}
	public static String quelleValeur (String nom) {
		for (int i = 0; i < lesUnités.length; i++) {
			if (lesUnités[i].nom.equals(nom)) {return lesUnités[i].valeur;}			
		}
		return null ;
	}
	public static void main (String [] args) {
		HashSet déjàVu = new HashSet () ;
		Ecriture as = new Ecriture ("TP6/lexical/CatégoriesLexicales.java");
		as.ecrire("\n"+"package TP6.lexical;") ;
		as.ecrire("\n"+"import java.util.Hashtable;");
		as.ecrire("\n/**");
		as.ecrire("\n * BigLang : catégories lexicales et crible");
		as.ecrire("\n * @author syntaxique.FaireCatégoriesLexicales");
		as.ecrire("\n *");
		as.ecrire("\n */");
		as.ecrire("\n"+"public class CatégoriesLexicales {") ;
		
		for (int i = 0; i < lesUnités.length; i++) {
			if (!déjàVu.contains(lesUnités[i].nom)) {
				as.ecrire("\n"+"    public static final int LEX_"+Service.compléterADroite(lesUnités[i].nom, 15)+" = "+i+" ;");
				déjàVu.add(lesUnités[i].nom);
			}
			
			
		}
		as.ecrire("\n"+"    static Hashtable créerCrible () {");
		as.ecrire("\n"+"        Hashtable c = new Hashtable () ;");
		for (int i = 0; i < lesUnités.length; i++) {
			String motif = lesUnités[i].valeur ;
			if (motif.matches("[a-zA-Z]+") && !motif.equals("erreur") && !motif.equals("nbre") && !motif.equals("idf")) {
				as.ecrire("\n"+"        c.put (\""+motif+"\", new Integer(LEX_"+lesUnités[i].nom+")) ;");
			}			
		}
		as.ecrire("\n"+"        return c;");
		as.ecrire("\n"+"    }");
		as.ecrire("\n"+"    public static Hashtable crible = créerCrible() ;");	
		
		as.ecrire("\n"+"    public static String [] enClair = {");
		as.ecrire("\n        \""+lesUnités[0].nom+"\"");
		for (int i = 1; i < lesUnités.length; i++) {as.ecrire(",\n        \""+lesUnités[i].nom+"\"");}
		as.ecrire("\n"+"");
		as.ecrire("\n"+"    };");
		
		as.ecrire("\n}\n") ;	
		as.fermer();
	}
}
