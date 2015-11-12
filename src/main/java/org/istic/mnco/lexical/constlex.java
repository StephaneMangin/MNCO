package org.istic.mnco.lexical;

public class constlex {
	
	
	public static final int lexerreur = 0;
	public static final int lexident = 2 ;
	public static final int lexentier = 3;
	
	public static final int lexplus = 4;
	public static final int lexmoins  = 5;
	public static final int lexmult = 6;
	public static final int lexdiv = 7;
	
	
	
	public static final int lexPO= 8; // parenthese ouvrante
	public static final int lexPF= 9; // parenthese fermante
	
	public static final int lexpoint = 10;
	public static final int lexpointvirgule = 11;
	public static final int lexdieze = 12;
	public static final int lexegal = 13;
	
	public static final int lexop= 14; //operateur (si on regroupe les 4 ops)
	
	
	
	
	
	static public String constlex_to_String(int i){
		switch(i){
		case 0 : return new String("lexerreur") ;
		case lexentier : return new String("lexentier") ;
		case lexident : return new String("lexident");
		
		case lexplus : return new String("lexplus") ;
		case lexmoins : return new String("lexmoins") ;
		case lexmult : return new String("lexmult") ;
		case lexdiv : return new String("lexdiv") ;
		
		case lexPO : return new String("lexPO") ;
		case lexPF : return new String("lexPF") ;
		case lexpoint : return new String("lexpoint") ;
		case lexpointvirgule : return new String("lexpointvirgule") ;
		
		case lexdieze : return new String("lexdieze") ;
		case lexegal : return new String("lexegal") ;
		
		case lexop : return new String("lexop") ;
		
		default : return new String("rien"); 
		}
	}
	}