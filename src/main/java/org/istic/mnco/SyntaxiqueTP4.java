package org.istic.mnco;
/*
 * SyntaxiqueTP4.java : analyseur syntaxique du TP 4
 */
//import es.*;

import org.istic.mnco.automate.Analex1;
import org.istic.mnco.lexical.constlex;

import java.util.GregorianCalendar;

/**
  * Reconnait des expressions PREFIXES
  * Produit la forme INFIXE
  * 
  * Grammaire :
  * <Axiome> 	-> <expr> .
  * <expr>		-> <op> <exp> <exp> | <entier> | <ident>
  * <op>		-> + | - | * | /
  */

public class SyntaxiqueTP4  {
	

	
	Analex1 monauto; // l'analyseur leical
	int lexlu;		  // type de l'unite lexicale courante
	String valeurlue; //valeurde l'unite lexicale courante
	String [] Pile ;
	int iSommet;
	
	/*
	 * Constructeur
	 */
	SyntaxiqueTP4(String nomfich){
		monauto = new Analex1();
		monauto.initTete(nomfich);
		AvanceTete(); // lire une premiere unite lexicale
        Ginit();
	}
	
	void AvanceTete(){
		monauto.lancer();
		lexlu = monauto.unitelue.typelex;
		valeurlue = monauto.unitelue.valeurlex;

		// System.out.println("J'ai lu"+lexlu+" "+valeurlue);
		}

	public void Axiome(){
		expr();
		if (! (lexlu == constlex.lexpoint) ) {System.out.println("Erreur ds axiome : . attendu");}
		else  {GenEcrire();}
	}

	public void expr() {
        switch (lexlu) {
            case constlex.lexident : GenEcrire(); AvanceTete(); break;
	        case constlex.lexentier : GenEcrire(); AvanceTete(); break;
	        default :GenEcrirePO();  op(); expr(); GenEcrireOP(depile()); expr(); GenEcrirePF();
	    }
	}

    public void op() {
        switch (lexlu) {
            case constlex.lexplus : empile("+"); AvanceTete() ;  break;
            case constlex.lexmoins : empile("-"); AvanceTete() ;  break;
            case constlex.lexmult : empile("*"); AvanceTete() ;  break;
            case constlex.lexdiv : empile("/"); AvanceTete() ;  break;
            default : System.out.println("Erreur ds op") ; break;
        }
    }
	/*
	 * POINTS DE GENERATION TP4 (partie 1)
	 */
	void GenEcrire(){
		System.out.print(valeurlue);
	}
	
	void GenEcrireOP(String op){
		System.out.print(op);
	}
	
	
	/*
	 * POINTS DE GENERATION TP4 (partie 2)
	 */
	
	
	
	// une pile d'op�rateurs 
	 
	void GenEcrirePO(){
		System.out.print("(");
	}

	void GenEcrirePF(){
		System.out.print(")");
	}
	
	String depile(){
		iSommet--;
        return Pile[iSommet+1];
	}
	
	void empile(String  op){
		iSommet++;Pile[iSommet]=op;
	}
	
	
	void Ginit(){
		Pile = new String[20];
		iSommet = -1;
		// ... etd'autres choses eventuellement
	}
	
	// R�diger ici les nouveaux points de generation
	// ...


	} // fin de classe SyntaxiqueTP4
	
