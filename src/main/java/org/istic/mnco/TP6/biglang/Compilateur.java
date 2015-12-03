package biglang;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import es.LectureFichierTexte;

import biglang.generation.CodeObjet;
import biglang.generation.Génération;
import biglang.lexical.AnalyseLexicale;
import biglang.lexical.CatégoriesLexicales;
import biglang.syntaxique.AnalyseSyntaxique;
import biglang.syntaxique.ArbreSyntaxique;

import service.ActionMenu;
import service.Menu;


/*
 * Created on 29 oct. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * <p>Compilateur BigLang : gestion des commandes du compilateur</p>
 * <ul>
 * <li>Compilation</li>
 * <li>&Eacute;léments lexicaux</li>
 * <li>Code objet</li>
 * <li>Programme source</li>
 * </ul>
 * <p>Lancement par appel de la méthode <code>main</code></p>
 */
public class Compilateur extends Menu {
	/**
	 * Menu proposant les commandes <code>c, l, o, s</code>. Toutes les commandes
	 * prennent un nom de fichier (sans extension) en paramètre. par défaut, le nom utilisé est
	 * <code>test</code>
	 *
	 */
	public Compilateur () {
		items = new Item [6];
		items[0] = new Item ("(C)ompilation", 'c', new ActionMenu () {
			public void executer (String [] args) { // compilation
				String fichier = "test" ;
				if (args.length > 1) {fichier = args[1];}
				try {
					LectureFichierTexte.ilExiste(fichier+".big") ;
					String [] t = {fichier} ;
					AnalyseSyntaxique.main(t) ;
				} catch (BigLangException e) {
				}
			}},
		"c [nom fichier sans l'extension .big]") ;
		items[1] = new Item ("(L)exicaux", 'l', new ActionMenu () {
			public void executer (String [] args) { // chargement d'un programme objet
				String fichier = "test" ;
				if (args.length > 1) {fichier = args[1];}
				try {
					LectureFichierTexte.ilExiste(fichier+".xml") ;
					ArbreSyntaxique a = new ArbreSyntaxique (fichier+".xml");
					a.lexicaux();
				} catch (BigLangException e) {
				}
			}},
		"l [nom fichier sans l'extension .big]") ;
		items[2] = new Item ("(A)rbre syntaxique", 'a', new ActionMenu () {
			public void executer (String [] args) { // chargement d'un programme objet
				String fichier = "test" ;
				String profondeur = "-1" ;
				if (args.length > 1 && !args[1].equals("-p")) {fichier = args[1];}
				for (int i = 0; i < args.length; i++) {
					if (args[i].equals("-p") && i+1 < args.length) {
						profondeur = args[i+1] ;
					}					
				}

					try {
						LectureFichierTexte.ilExiste(fichier+".xml") ;
						TransformerFactory fabrique = TransformerFactory.newInstance(); 
						File stylesheet = new File("xml2svg.xslt");
						StreamSource stylesource = new StreamSource(stylesheet);

						Transformer transformer = fabrique.newTransformer(stylesource);
						transformer.setOutputProperty(OutputKeys.INDENT, "yes");
						transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");  // cRéer un transformateur

						transformer.setParameter( "maxProf", profondeur); 
							
						transformer.transform( new StreamSource(fichier+".xml"), new StreamResult( new FileOutputStream(fichier+".svg")));

						System.out.println("l'arbre syntaxique est dans le fichier : "+fichier+".svg");
					} catch (TransformerConfigurationException e) {
					} catch (IllegalArgumentException e) {
					} catch (FileNotFoundException e) {
					} catch (BigLangException e) {
					} catch (TransformerFactoryConfigurationError e) {
					} catch (TransformerException e) {
					}
				
			}},
		"a [nom fichier sans l'extension .big] [-p profondeur]") ;
		items[3] = new Item ("code (O)bjet", 'o', new ActionMenu () {
			public void executer (String [] args) { // décompilation du code objet
				String fichier = "test" ;
				if (args.length > 1) {fichier = args[1];}
				try {
					LectureFichierTexte.ilExiste(fichier+".obj") ;
					CodeObjet o = new CodeObjet(fichier);
					o.lecture();
					System.out.println (o) ;
				} catch (BigLangException e) {
				}
			
			}},
	    "o") ;
		items[4] = new Item ("programme (S)ource", 's', new ActionMenu () {
			public void executer (String [] args) { // décompilation du code objet
				String fichier = "test" ;
				if (args.length > 1) {fichier = args[1] ;}
				try {
					LectureFichierTexte.ilExiste(fichier+".big") ;
					LectureFichierTexte codeSource = new LectureFichierTexte (fichier+ ".big") ;
					while (!codeSource.finDeFichier()) {
						System.out.println(codeSource.lireChaine("\n\r"));
					}
					codeSource.fermer() ;
				} catch (BigLangException e) {
				}
			}},
	    "s [nom fichier sans l'extension .big]") ;
		items[5] = quitter ;
		prompt = "**" ;
		}
			
   public static void main (String [] args) {
   	Compilateur le = new Compilateur () ;
   	try {
		le.jqaFini() ;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
	}
   }
}
