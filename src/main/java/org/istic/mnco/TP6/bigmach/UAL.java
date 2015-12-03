/*
 * Created on 29 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package bigmach;

import es.Lecture;

/**
 * @author herman
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UAL {
	private static int droit ;
	private static int gauche ;
	
	public static void ou () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		gauche = PileDeCalcul.d�piler() ;
		if (droit == 1) {PileDeCalcul.empiler(1);} else {PileDeCalcul.empiler(gauche);}
	}
	public static  void et () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		gauche = PileDeCalcul.d�piler() ;
		if (droit == 0) {PileDeCalcul.empiler(0);} else {PileDeCalcul.empiler(gauche);}
	}
	public static  void addition () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		gauche = PileDeCalcul.d�piler() ;
		PileDeCalcul.empiler(gauche + droit);
	}
	public static  void soustraction () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		gauche = PileDeCalcul.d�piler() ;
		PileDeCalcul.empiler(gauche - droit);
	}
	public static  void multiplication () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		gauche = PileDeCalcul.d�piler() ;
		PileDeCalcul.empiler(gauche * droit);
	}
	public static  void division () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		gauche = PileDeCalcul.d�piler() ;
		PileDeCalcul.empiler(gauche / droit);
	}
	public static  void inf�rieur () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		gauche = PileDeCalcul.d�piler() ;
		if (gauche < droit) {PileDeCalcul.empiler(1);} else {PileDeCalcul.empiler(0);}
	}
	public static  void �gal () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		gauche = PileDeCalcul.d�piler() ;
		if (gauche == droit) {PileDeCalcul.empiler(1);} else {PileDeCalcul.empiler(0);}
	}
	public static  void non () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		PileDeCalcul.empiler((droit + 1) % 2);
	}
	public static  void dupliquer () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		PileDeCalcul.empiler(droit);
		PileDeCalcul.empiler(droit);
	}
	public static  void affectation () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		gauche = PileDeCalcul.d�piler() ;
		PileDeCalcul.fixerMot(gauche, droit) ;
		PileDeCalcul.empiler(droit) ;
	}
	public static  void valeur () throws BigMachException{
		droit = PileDeCalcul.d�piler() ;
		PileDeCalcul.empiler(PileDeCalcul.valeurMot(droit)) ;
	}
	public static  void lire (String s) throws BigMachException{
		System.out.print (s+"? ");
		int v = Lecture.unEntier() ;
		PileDeCalcul.empiler(v) ;
	}
	public static  void �crire (String s) throws BigMachException{
		System.out.println (s+": "+PileDeCalcul.sommet());
	}
}
