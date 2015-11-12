import automate.*;

import     lexical.*;
 
public class TestAnalex1  {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// 
		
		Analex1 monauto;
		
	
		
		
		monauto = new Analex1();
		// monauto.trace_on();   // demande la trace des transitions
		
		//monauto.affiche_tables();   // afficher le contenu des tables
		
		monauto.initTete("testanalex.txt");
		
	
	
		do {
			monauto.lancer();
	System.out.println("tete apres lancer "+monauto.donne_tete());
		
	System.out.println("unite : "+monauto.unitelue.unitelex_to_String());
		
		
		}
	    while(monauto.unitelue.typelex != constlex.lexerreur);
		
		
	}

}
