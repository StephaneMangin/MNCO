/*
 * Created on 29 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package bigmach;

/**
 * @author herman
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BigMach {
  public static int iCo ;
  public static boolean arr�t ;
  private static int rInst ;
  
  public static void lancer () throws BigMachException{
  	arr�t = false ;
  	iCo = 0;
  	while (!arr�t) {
  		rInst = M�moireInstructions.inst(iCo) ;
  		if (rInst < 0 || rInst >= JeuDInstructions.instructions.length) {throw (new BigMachException (BigMachException.BM_INSTRUCTION_INEXISTANTE)) ; }
  		iCo++ ;
  		// pr�-incr�mentation du CO : iCo d�signe l'instruction suivante dans les cas standard
  		JeuDInstructions.instructions[rInst].action.executer() ;
  		// System.out.println(PileDeCalcul.�tatPile ()) ;
  	}
  	System.out.println(PileDeCalcul.�tatPile ()) ;
  }
}
