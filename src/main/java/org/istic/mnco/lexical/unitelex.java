package org.istic.mnco.lexical;

public class unitelex {
	
	public int typelex ;
	public String valeurlex;

	public unitelex(int codelex, String ch){
	typelex = codelex ;
	 valeurlex = ch;
	}
	
	public String unitelex_to_String(){
			return("<"+ constlex.constlex_to_String(typelex)+
					","+
					valeurlex+">");
			
		}
}
