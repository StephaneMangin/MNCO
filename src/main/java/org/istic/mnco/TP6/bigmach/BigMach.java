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
  public static boolean arrêt ;
  private static int rInst ;
  
  public static void lancer () throws BigMachException{
  	arrêt = false ;
  	iCo = 0;
  	while (!arrêt) {
  		rInst = MémoireInstructions.inst(iCo) ;
  		if (rInst < 0 || rInst >= JeuDInstructions.instructions.length) {throw (new BigMachException (BigMachException.BM_INSTRUCTION_INEXISTANTE)) ; }
  		iCo++ ;
  		// pré-incrémentation du CO : iCo désigne l'instruction suivante dans les cas standard
  		JeuDInstructions.instructions[rInst].action.executer() ;
  		// System.out.println(PileDeCalcul.étatPile ()) ;
  	}
  	System.out.println(PileDeCalcul.étatPile ()) ;
  }
}
