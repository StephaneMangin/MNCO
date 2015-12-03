package bigmach;
import java.io.IOException;

import service.ActionMenu;
import service.Menu;

/*
 * Created on 29 oct. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author herman
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Commandes extends Menu {
	
	public Commandes () {
		items = new Item [5];
		items[0] = new Item ("(C)harger un programme", 'c', new ActionMenu () {
			public void executer (String [] args) { // chargement d'un programme objet
				String fichier = "test.obj" ;
				if (args.length > 1) {fichier = args[1]+".obj";}
				MémoireInstructions.chargerCodeObjet(fichier, CodesInstructions.CODE_STOP) ;
			}},
			"c [nom fichier sans l'extension]") ;
		items[1] =	new Item ("(P)ile", 'p', new ActionMenu () {
				public void executer (String [] args) {
					System.out.println (PileDeCalcul.étatPile()) ;
				}},
				"p") ;
		items[2] =	new Item ("(D)écompiler", 'd', new ActionMenu () {
			public void executer (String [] args) { // rien à faire	
				int de = 0 ;
				int a = 30 ;
				if (args.length == 3) {de = Integer.parseInt(args[1]) ; a = Integer.parseInt(args[2]) ;}
				MémoireInstructions.décompiler(de, a) ;
			}},
			"d") ;
		items[3] =	new Item ("e(X)ecution", 'x', new ActionMenu () {
			public void executer (String [] args) throws BigMachException { // rien à faire	
				BigMach.lancer() ;
			}},
			"x") ;
		items[4] = quitter ;
		prompt = "**" ;
		}
			
   public static void main (String [] args) {
   	Commandes le = new Commandes () ;
   	try {
		le.jqaFini() ;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
}



