
/*
 * SyntaxiqueTP4.java : analyseur syntaxique du TP 4
 */
//import es.*;

 /**
  * Reconnait des expressions PREFIXES
  * Produit la forme INFIXE
  * 
  * Grammaire :
  * <Axiome> 	-> <expr> .
  * <expr>		-> + <exp> <exp>
  * <expr>		-> - <exp> <exp>	
  * <expr>		-> * <exp> <exp>
  * <expr>		-> / <exp> <exp>	
  */
import automate.*; import lexical.*;

public class SyntaxiqueTP4  {
	

	
	Analex1 monauto; // l'analyseur leical
	int lexlu;		  // type de l'unite lexicale courante
	String valeurlue; //valeurde l'unite lexicale courante
	
	/*
	 * Constructeur
	 */
	SyntaxiqueTP4(String nomfich){
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
	
	public void Axiome(){
		expr(); 
		if (! (lexlu == constlex.lexpoint) ) {System.out.println("Ereur ds axiome : . attendu");}
		else  {GenEcrire();}
	}
	
	public void expr()
	{ switch (lexlu) {
	case constlex.lexident : GenEcrire(); AvanceTete(); break;
	case constlex.lexentier : GenEcrire(); AvanceTete(); break;
	case constlex.lexplus : GenEcrirePO() ; AvanceTete() ; 
							expr(); 
							GenEcrireOP("+");
	                         expr(); 
	                        GenEcrirePF();  break;
	                        
	case constlex.lexmoins : GenEcrirePO() ; AvanceTete() ; 
	expr(); 
	GenEcrireOP("-");
     expr(); 
    GenEcrirePF();  break;
    
	case constlex.lexmult : GenEcrirePO() ; AvanceTete() ; 
	expr(); 
	GenEcrireOP("*");
     expr(); 
    GenEcrirePF();  break;
    
	case constlex.lexdiv : GenEcrirePO() ; AvanceTete() ; 
	expr(); 
	GenEcrireOP("/");
     expr(); 
    GenEcrirePF();  break;
                         
	                        
	default : System.out.println("Ereur ds expr") ; break; 
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
	
	void GenEcrirePO(){
		System.out.print("(");
	}
	
	void GenEcrirePF(){
		System.out.print(")");
	}
	
	
	/*
	 * POINTS DE GENERATION TP4 (partie 2)
	 */
	
	
	
	// une pile d'opérateurs 
	 
	
	String [] Pile ; 
	int iSommet;  
	
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
	
	// Rédiger ici les nouveaux points de generation
	// ...
	
	} // fin de classe SyntaxiqueTP4
	
