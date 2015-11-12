package org.istic.mnco;

import org.istic.mnco.automate.Analex1;
import static org.istic.mnco.lexical.constlex.*;
/*
 * SyntaxiqueTP5.java
 */


/**
*  ANALYSEUR POUR LES EXPRESSION INFIXES
*  GENERATION (et calcul) PAR MACHINE A PILE
*  (instructions PUSH POP ADD SOUS MULT DIV)
* 
* <Axiome> ::= <exp> .
* <Exp>  ::= <Terme> <STerme>
* <Terme> ::= <Facteur> <SFacteur>
* <STerme> ::= + <Exp> | - <Exp> | vide
* <Facteur> ::= Entier | ( <Exp> )
* <SFacteur> ::= * <Terme> | / <Terme> | vide
* 
*/

public class SyntaxiqueTP5 {
	

	
	Analex1 monauto; // l'analyseur leical
	int lexlu;		  // type de l'unite lexicale courante
	String valeurlue; //valeurde l'unite lexicale courante
	
	/*
	 * Constructeur
	 */
	SyntaxiqueTP5(String nomfich){
		monauto = new Analex1();
		monauto.initTete(nomfich);
		AvanceTete(); // lire une premiere unite lexicale
	}
	
	
	void AvanceTete(){
		monauto.lancer();
		lexlu = monauto.unitelue.typelex;
		valeurlue = monauto.unitelue.valeurlex;
		
		// System.out.println("J'ai lu"+lexlu+" "+valeurlue);
		}
	
	void Message(String ch1 , String ch2){
		System.out.println("Erreur syntaxique dans "+ch1+" "+ch2);
	}
		
	
	public void Axiome() {
		   // <Axiome> ::= <exp> .
		  
		
		Ginit();
		
		// ... COMPLETER !!
		}; // fin Axiome()

		void Exp() {
			// <Exp>  ::= <Terme> <STerme>
		      
			Terme(); STerme(); AvanceTete();
			//. .. COMPLETER !!
	
		};// fin Exp()


		void Terme(){

			// <Terme> ::= <Facteur> <SFacteur>
			//			. .. COMPLETER !!
            Facteur(); SFacteur(); AvanceTete();


            } // fin Terme()
		
		void STerme() {
			// <STerme> ::= + <Exp> | - <Exp> | vide
            switch (lexlu) {
                case lexplus: Exp(); AvanceTete(); break;
                case lexmoins: Exp(); AvanceTete(); break;
                default: break;
            }

//			. .. COMPLETER !!
		     }; // fin Sterme()

		void Facteur()  { 
			// <Facteur> ::= Entier | ( <Exp> )
			
//			. .. COMPLETER !!
            switch (lexlu) {
                case lexentier: GPEntier(); AvanceTete(); break;
                case '(': Exp(); AvanceTete();
                    if (lexlu != lexPF) {
                        System.out.println("Erreur syntaxique");
                    }
                    break;
                    default: break;
            }
		
		} // fin Facteur()


		void SFacteur() {
		//<SFacteur> ::= * <Terme> | / <Terme> | vide
		
//			. .. COMPLETER !!
			switch (lexlu) {
                case lexmult:
            }

		}; // fin SFacteur()
		
	
		// POINTS DE GENERATION
	
	
	void GPEntier() {
		System.out.println("PUSH "+valeurlue);
		empiler(Integer.decode(valeurlue).intValue()); // passer d'une String à un int...
	}
	
	void GPPlus() {
		System.out.println("PLUS");
		int v1 = depiler() ;
		int v2 = depiler() ;
		empiler(v1+v2);
	}
	
	
	void GPMoins() {
		System.out.println("MOINS");
		int v1 = depiler() ;
		int v2 = depiler() ;
		empiler(v2-v1);
	}
	
	void GPMult() {
		System.out.println("MULT");
		int v1 = depiler() ;
		int v2 = depiler() ;
		empiler(v1*v2);
	}
	
	void GPDiv() {
		System.out.println("DIV");
		int v1 = depiler() ;
		int v2 = depiler() ;
		empiler(v2/v1);
	}
	
	
	void Ginit()
	{PileEval = new int[20] ; 
	 iSommet = -1;
	 // ... et d'autres choses peut-etre}
	
	}
	
	void Gfin(){
		if (iSommet==0)
		{System.out.println("Valeur calculéé"+depiler()) ; }
		else System.out.println("Pb de pile :"+iSommet);
	} // Gfin
	int [] PileEval ;
	int iSommet;
	
	void empiler(int v){
		iSommet++;
		PileEval[iSommet]=v;
	}
	
	int depiler(){
		iSommet=iSommet-1 ;
		return PileEval[iSommet+1];
	}
	
	
	
	} // fin de classe SyntaxiqueTP5
	
