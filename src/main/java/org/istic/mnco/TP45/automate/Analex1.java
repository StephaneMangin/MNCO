/**
 * ANALYSEUR LEXICAL POUR LES TPs 5 et 6
 */


package org.istic.mnco.TP45.automate;

import org.istic.mnco.TP45.lexical.constlex;
import org.istic.mnco.TP45.lexical.unitelex;

public class Analex1 extends Automate {
    
	//	 INITIALISATION DES TABLES 
    // Partie 1  à réaliser pour un automate particulier
	 public Analex1(){
		creertables_vides(13); // rappel : 13 est ici le nombre d'etats (1 etat initial ; 0 etat puit)
		initialiser_tables();
			
	}

	
	
	 
	 public void initialiser_tables(){
	 	
	 	 	//arcs_defauts(8);
			
			unArc(1,c_charEspace,1,0);
			unArc(1,c_charNL,1,0);
			unArc(1,c_charRC,1,0);
		
																																																																								

			arcsPourLettres(1,2,102);
			arcsPourLettres(2,2,202);															
			
			arcsPourChiffres(1,3,103);
			arcsPourChiffres(3,3,303);
			
			unArc(1,'+',4, constlex.lexplus);
			unArc(1,'-',5,constlex.lexmoins);
			unArc(1,'*',6,constlex.lexmult);
			unArc(1,'/',7,constlex.lexdiv);
			
			unArc(1,'(',8,constlex.lexPO);
			unArc(1,')',9,constlex.lexPF);
			
			unArc(1,'.',10,constlex.lexpoint);
			unArc(1,';',11,constlex.lexpointvirgule);	
			unArc(1,'#',12,constlex.lexdieze);
			unArc(1,'=',13,constlex.lexegal);
				
			
	}
	
	 // ici on redéfini fini()
	 	protected boolean fini(){return (etatCourant==0) ;}

	// PROGRAMMATION DES ACTIONS
	    // Partie 2  à réaliser pour un automate particulier
	 
	 	int lexlu ; // type de l'unite lue
	 	String chainelue;  // valeur de l'unite lue
	 	
	 	public unitelex unitelue ;
	 	
	 	protected void actionInitiale(){
	 		
	 		lexlu=constlex.lexerreur;
	 		chainelue="";}
	 	
	 	
	 	protected void actionFinale(){
	 		//actionFinale2();
	 		unitelue =new unitelex(lexlu,chainelue);
	 	
	 	}
	
	 	
	 	protected void executeraction(int A, int tete){
			switch (A) {
			case 102 : chainelue=chainelue+(char)tete; lexlu=constlex.lexident; break;
			case 202 : 	chainelue=chainelue+(char)tete; break;
						
			case 103 : chainelue=chainelue+(char)tete; lexlu=constlex.lexentier; break;
			case 303 : chainelue=chainelue+(char)tete; break;
			
			default :  // num axion = code du type lexical
				if (A != 0){ chainelue=chainelue+(char)tete; 
							lexlu = A;} break;
			}
	
			
			
		}
	 	
	 	
	 	
	 	
	 	

}
