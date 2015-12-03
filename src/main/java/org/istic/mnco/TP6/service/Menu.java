/*
 * Created on 29 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package service;


import es.Lecture;


/**
 * @author herman
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Menu {
	public static class Item {
		public String message ;
		public char abbrev ;
		public ActionMenu action ;
		public String explication ;
		public Item (String m, char a, ActionMenu o, String expl) {
			message = m ;
			abbrev = a ;
			action = o ;
			explication = expl ;
		}
	}

	public Item [] items ;
	public boolean fini ;
	public String prompt ;
	
	private int leMenu (char c){
		for (int i = 0; i < items.length ; i++) {if (items[i].abbrev == c || Character.toUpperCase(items[i].abbrev) == c){return i;}}
		return -1 ;
	}
	
	private void affiche () {
		System.out.print(prompt + " " + items[0].message) ;
		for (int i = 1; i < items.length; i++) {
			System.out.print (", " + items[i].message) ;
		}
		System.out.print(":") ;
	}
	public void explications (String mess) {
		System.out.println (mess);
		for (int j = 0 ; j < items.length; j++) {System.out.println ("!!   " + items[j].explication);}
	}
	
	public void uneFois () throws Exception {
		affiche();
		String ligneCmde = Lecture.chaine("\n\r") ;
		String [] cmde = ligneCmde.split("\\s+") ;
		char c = cmde[0].charAt(0) ;
		int i = leMenu(c) ;
		if (i == -1) {
			explications("!! commandes valides : ");		
		} else {
			try {
				items[i].action.executer(cmde) ;
			} catch (NumberFormatException e) {
				explications ("!! arguments incorrects : " + ligneCmde) ;
			}			
		}
	}
	public void jqaFini ()throws Exception {		
		
		try {
			while (true) {uneFois ();} 
		} catch (Exception e) {
			// e.printStackTrace(System.out) ;
			String [] vide = {""};
			System.out.println ("--");
			
			System.out.println ("--");
		}
	}
	
	public Item quitter = new Item ("(Q)uitter", 'q', new ActionMenu () {
		public void executer (String [] args) throws Exception {
			throw new Exception ("quitter") ;
		}},
		"q") ;
	public static void main (String [] args){
		
	}

}
