/*
 * Created on 14 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package biglang.generation;

import java.util.Hashtable;
import java.util.Stack;

import biglang.BigLangException;
import biglang.syntaxique.AnalyseSyntaxique;
import bigmach.CodesInstructions;


/**
 * BigLang : analyse s�mantique et g�n�ration
 */
public class G�n�ration {
	static final int TYPE_ENTIER = 0 ;
	static final int TYPE_BOOLEEN = 1 ;
	static final int TYPE_NEUTRE = 2 ;
	static final int TYPE_INEXISTANT = 3 ;
	static final String [] typesEnClair = {"entier", "bool�en", "neutre", "inexistant"};
	/**
	 * Compteur d'emplacements. d�signe le prochain emplacement libre dans le code objet.
	 */
	static int Ce ;
	/**
	 * Compteur de r�servation des variables. D�signe la prochaine adresse libre pour implanter une variable.
	 */
	static int adrVariables ;
	/**
	 * tampon de production du code objet
	 */
	public static CodeObjet objet ;
	/**
	 * El�ment de table des symboles :
	 * <p>
	 * Pour chaque variable on m�morise :
	 * <ul><li>son type</li>
	 * <li>son adresse d'implantation</li></ul>
	 * </p>
	 * @author herman
	 */
	static class Description {
		/**
		 * @param type : type du symbole
		 * @param adr : adresse d'implantation
		 */
		public Description(int type, int adr) {
			this.type = type;
			this.adr = adr;
		}
		int type ;
		int adr ;
		public String toString () {
			String t ;
			switch (type) {
			case TYPE_ENTIER:
				t = "entier" ;
				break;
			case TYPE_BOOLEEN:
				t = "bool�en" ;
				break;
			default:
				t = "inconnu";
			break;
			}			
			return "<" + t + ", " + adr + ">" ;
		}
	}
	/**
	 * table des symboles : les �l�ments sont des <code>Description</code>
	 */
	public static Hashtable tableDesSymboles ;
	/**
	 * type de la constrauction en cours de traitement
	 */
	static int typeCourant ;
	/**
	 * indique si la construction courante est un rep�re
	 */
	static boolean cEstUnRep�re ;
	static class Op�rateur {
		int code ;
		int typeGauche ;
		int typeDroit ;
		int typeR�sultat ;
		boolean valeurAGauche ;
		boolean valeurADroite ;
		boolean leR�sultatEstUnRep�re ;
		
		String enClair ;
		public Op�rateur(int c, int g, int d, int r, boolean vg, boolean vd, boolean rr, String o) {
			this.code = c;
			this.typeGauche = g;
			this.typeDroit = d;
			this.typeR�sultat = r;
			this.valeurAGauche = vg ;
			this.valeurADroite = vd ;
			this.leR�sultatEstUnRep�re = rr ;
			this.enClair = o;
		}
	}
	private static Hashtable initTableDesOp�rateurs () {
		Hashtable laTable = new Hashtable () ;
		laTable.put (
				"non",
				new Op�rateur (
						CodesInstructions.CODE_NON,
						TYPE_INEXISTANT, TYPE_BOOLEEN, TYPE_BOOLEEN,
						true, true,
						false,
						"non")
		);
		laTable.put (
				"*",
				new Op�rateur (
						CodesInstructions.CODE_MULT,
						TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
						true, true,
						false,
						"*")
		);
		laTable.put (
				"/",
				new Op�rateur (
						CodesInstructions.CODE_DIV,
						TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
						true, true,
						false,
						"/")
		);
		laTable.put (
				"+",
				new Op�rateur (
						CodesInstructions.CODE_PLUS,
						TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
						true, true,
						false,
						"+")
		);
		laTable.put (
				"-",
				new Op�rateur (
						CodesInstructions.CODE_MOINS,
						TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
						true, true,
						false,
						"-")
		);
		laTable.put (
				"<",
				new Op�rateur (
						CodesInstructions.CODE_INF,
						TYPE_ENTIER, TYPE_ENTIER, TYPE_BOOLEEN,
						true, true,
						false,
						"<")
		);
		laTable.put (
				"=",
				new Op�rateur (
						CodesInstructions.CODE_EG,
						TYPE_ENTIER, TYPE_ENTIER, TYPE_BOOLEEN,
						true, true,
						false,
						"=")
		);
		laTable.put (
				"ou",
				new Op�rateur (
						CodesInstructions.CODE_OU,
						TYPE_BOOLEEN, TYPE_BOOLEEN, TYPE_BOOLEEN,
						true, true,
						false,
						"ou")
		);
		laTable.put (
				"et",
				new Op�rateur (
						CodesInstructions.CODE_ET,
						TYPE_BOOLEEN, TYPE_BOOLEEN, TYPE_BOOLEEN,
						true, true,
						false,
						"et")
		);
		return laTable ;
	}
	public static Hashtable tableDesOp�rateurs = initTableDesOp�rateurs() ;
	public static Stack pileDesOp�rateursVus = new Stack () ;
	public static Stack pileDesTypesVus = new Stack () ;
	public static Stack pileDesRep�res = new Stack () ;
	public static Stack pileDesReprises = new Stack () ;
	/**
	 * 
	 * @param n : nom du fichier objet (sans l'extension)
	 */
	public static void initG�n�ration (String n) {
		Ce = 0 ;
		adrVariables = 0 ;
		objet = new CodeObjet (n) ;
		tableDesSymboles = new Hashtable () ;
	}
	//
	//
	// Analyse des d�clarations
	//
	//
	public static void defVar () throws BigLangException {
		if (tableDesSymboles.get(AnalyseSyntaxique.t�te.unit�)!= null) {
			throw new BigLangException (AnalyseSyntaxique.t�te.unit�, BigLangException.BL_SEM, BigLangException.BL_SEM_DLE_DECL);
		}
		tableDesSymboles.put(
				AnalyseSyntaxique.t�te.unit�,
				new Description(typeCourant, adrVariables)
		);
		adrVariables++ ;
	}
	public static void defVarEnt () throws BigLangException {
		typeCourant = TYPE_ENTIER ;
		defVar () ;
	}
	public static void defVarBool () throws BigLangException {
		typeCourant = TYPE_BOOLEEN ;
		defVar () ;
	}
	public static void finDefVar () throws BigLangException {
		objet.modifie(Ce, CodesInstructions.CODE_SOMMETA);
		Ce++ ;
		objet.modifie(Ce, adrVariables-1) ;
		Ce++ ;
		typeCourant = TYPE_NEUTRE ;
	}
	//
	//
	// Entr�es-sorties
	//
	//
	public static void lire () throws BigLangException {
		Description d = (Description) tableDesSymboles.get(AnalyseSyntaxique.t�te.unit�) ;
		if (d == null) {
			throw new BigLangException (AnalyseSyntaxique.t�te.unit�, BigLangException.BL_SEM, BigLangException.BL_SEM_IDF_NON_DECL);
		}
		String enClair = AnalyseSyntaxique.t�te.unit� + "    " ;
		int enAscii = 0 ;
		for (int i = 0; i < 4; i++) {
			enAscii = enAscii * 256 + enClair.charAt(i) ;			
		}
		objet.modifie(Ce, CodesInstructions.CODE_EMPILER) ; Ce++ ;
		objet.modifie(Ce, d.adr); Ce++ ;
		objet.modifie(Ce, CodesInstructions.CODE_LIRE) ; Ce++ ;
		objet.modifie(Ce, enAscii) ; Ce++ ;
		objet.modifie(Ce, CodesInstructions.CODE_AFFECTER) ;
		Ce++ ;
		
		typeCourant = d.type ;
		cEstUnRep�re = false ;
		
	}
	public static void ecrire () throws BigLangException {
		Description d = (Description) tableDesSymboles.get(AnalyseSyntaxique.t�te.unit�) ;
		if (d == null) {
			throw new BigLangException (AnalyseSyntaxique.t�te.unit�, BigLangException.BL_SEM, BigLangException.BL_SEM_IDF_NON_DECL);
		}
		
		String enClair = AnalyseSyntaxique.t�te.unit� + "    " ;
		int enAscii = 0 ;
		for (int i = 0; i < 4; i++) {
			enAscii = enAscii * 256 + enClair.charAt(i) ;			
		}
		
		objet.modifie(Ce, CodesInstructions.CODE_EMPILER) ; Ce++ ;
		objet.modifie(Ce, d.adr); Ce++ ;
		objet.modifie(Ce, CodesInstructions.CODE_VALEUR) ; Ce++ ;
		objet.modifie(Ce, CodesInstructions.CODE_�CRIRE) ; Ce++ ;
		objet.modifie(Ce, enAscii) ; Ce++ ;
		
		typeCourant = d.type ;
		cEstUnRep�re = false ;
	
	}
	//
	//
	// Primaires
	//
	//
	public static void neutraliser () throws BigLangException {
		if (typeCourant != TYPE_NEUTRE) {
			objet.modifie(Ce, CodesInstructions.CODE_D�PILER);
			Ce++ ;
		}
		typeCourant = TYPE_NEUTRE ;
	}
	public static void var () throws BigLangException {
		Description d = (Description) tableDesSymboles.get(AnalyseSyntaxique.t�te.unit�) ;
		if (d == null) {
			throw new BigLangException (AnalyseSyntaxique.t�te.unit�, BigLangException.BL_SEM, BigLangException.BL_SEM_IDF_NON_DECL);
		}
		cEstUnRep�re = true ;
		typeCourant = d.type ;
		
		objet.modifie(Ce, CodesInstructions.CODE_EMPILER) ;
		Ce++ ;
		objet.modifie(Ce, d.adr);
		Ce++ ;
		
	}
	public static void nbre () throws BigLangException {
		cEstUnRep�re = false ;
		typeCourant = TYPE_ENTIER ;
		
		objet.modifie(Ce, CodesInstructions.CODE_EMPILER) ;
		Ce++ ;
		objet.modifie(Ce, Integer.parseInt(AnalyseSyntaxique.t�te.unit�));
		Ce++ ;	
	}
	//
	//
	// Pr�lude et postlude
	//
	//
	public static void debut () throws BigLangException {
		
	}
	public static void fin () throws BigLangException {
		String fin = "fini" ;
		objet.modifie(Ce, CodesInstructions.CODE_�CRIRE) ;
		Ce++ ;
		objet.modifie(Ce, (('f'*256 + 'i')*256 + 'n')*256 + 'i');
		Ce++ ;
		objet.modifie(Ce, CodesInstructions.CODE_STOP) ;
		Ce++ ;
		objet.sortie();
	}
	//
	//
	// Op�rateurs
	//
	//
	public static void genNon () throws BigLangException {
		if (cEstUnRep�re) { objet.modifie(Ce, CodesInstructions.CODE_VALEUR) ; Ce++ ;}
		cEstUnRep�re = false ;
		if (typeCourant == TYPE_BOOLEEN) {
			objet.modifie(Ce, CodesInstructions.CODE_NON) ;
			Ce++ ;
		} else {
			throw new BigLangException (AnalyseSyntaxique.t�te.unit�, BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_BOOL_ATTENDU);
		}
	}
	public static void op�randeGauche () throws BigLangException {
		Op�rateur o = (Op�rateur) tableDesOp�rateurs.get(AnalyseSyntaxique.t�te.unit�);
		if (o.typeGauche != TYPE_INEXISTANT && o.valeurAGauche && cEstUnRep�re) {
			objet.modifie(Ce, CodesInstructions.CODE_VALEUR) ; Ce++ ;
			cEstUnRep�re = false ;
		}
		if (o.typeGauche != TYPE_INEXISTANT && o.typeGauche != typeCourant) {throw new BigLangException (o.enClair+": "+typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_GAUCHE_NON_CONFORME);};
		pileDesOp�rateursVus.push(o) ;
	}
	public static void op�randeDroit () throws BigLangException {
		Op�rateur o = (Op�rateur) pileDesOp�rateursVus.pop();
		if (o.typeDroit != TYPE_INEXISTANT && o.valeurADroite && cEstUnRep�re) {
			objet.modifie(Ce, CodesInstructions.CODE_VALEUR) ; Ce++ ;
			cEstUnRep�re = false ;
		}
		if (o.typeDroit != TYPE_INEXISTANT && o.typeDroit != typeCourant) {throw new BigLangException (o.enClair+": "+typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_DROIT_NON_CONFORME);};
		
		objet.modifie(Ce, o.code) ; Ce++ ;
		
		cEstUnRep�re = o.leR�sultatEstUnRep�re ;
		typeCourant = o.typeR�sultat ;
	}
	public static void op�randeGaucheAff () throws BigLangException {	
		if (!cEstUnRep�re) {throw new BigLangException(BigLangException.BL_SEM, BigLangException.BL_SEM_VAL_A_GAUCHE_AFF) ;};
		pileDesTypesVus.push(new Integer(typeCourant)) ;
	}
	public static void op�randeDroitAff () throws BigLangException {
		int typeAGauche = ((Integer) pileDesTypesVus.pop()).intValue();
		if (typeCourant != typeAGauche) {throw new BigLangException ("affectation : "+typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_DROIT_NON_CONFORME);};
		if (cEstUnRep�re) {
			objet.modifie(Ce, CodesInstructions.CODE_VALEUR) ; Ce++ ;
			cEstUnRep�re = false ;
		}		
		objet.modifie(Ce, CodesInstructions.CODE_AFFECTER) ; Ce++ ;		
		cEstUnRep�re = false ;
	}
	//
	//
	// Conditionnelles
	//
	//

	public static void finCondition () throws BigLangException {
		if (typeCourant != TYPE_BOOLEEN) {throw new BigLangException ("si : "+typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_BOOL_ATTENDU);};
		if (cEstUnRep�re) {
			objet.modifie(Ce, CodesInstructions.CODE_VALEUR) ; Ce++ ;
			cEstUnRep�re = false ;
		}
		objet.modifie(Ce, CodesInstructions.CODE_BSF) ; Ce++ ;
		pileDesReprises.push(new Integer(Ce));
		objet.modifie(Ce, -1) ; Ce++ ;		
	}
	public static void finAlors () throws BigLangException {
		int adFinCondition = ((Integer) pileDesReprises.pop()).intValue() ;
		
		objet.modifie(Ce, CodesInstructions.CODE_NOP) ; Ce++ ;	// pour �vent. netraliser ou d�rep�rer
		
		objet.modifie(Ce, CodesInstructions.CODE_BR) ; Ce++ ;
		pileDesReprises.push(new Integer(Ce));
		objet.modifie(Ce, -1) ; Ce++ ;	
		
		objet.modifie(adFinCondition, Ce) ;
		
		pileDesTypesVus.push(new Integer(typeCourant)) ;
		pileDesRep�res.push(new Boolean(cEstUnRep�re)) ;
	}
	public static void finSinon () throws BigLangException {
		boolean alorsEstUnRep = ((Boolean) pileDesRep�res.pop()).booleanValue();
		int typeAlors  = ((Integer) pileDesTypesVus.pop()).intValue() ;
		int adFinAlors = ((Integer) pileDesReprises.pop()).intValue() ;
		
		if (alorsEstUnRep && !cEstUnRep�re) {
			objet.modifie(adFinAlors - 2, CodesInstructions.CODE_VALEUR) ;
		}
		if (!alorsEstUnRep && cEstUnRep�re) {
			objet.modifie(Ce, CodesInstructions.CODE_VALEUR) ; Ce++ ;
			cEstUnRep�re = false ;
		}
		
		switch (typeAlors) {
		case TYPE_NEUTRE:					// neutraliser le sinon
			if (typeCourant != TYPE_NEUTRE) {
				objet.modifie(Ce, CodesInstructions.CODE_D�PILER) ; Ce++ ;
				typeCourant = TYPE_NEUTRE ; cEstUnRep�re = false ;
			}
			break;
		case TYPE_BOOLEEN:
			switch (typeCourant) {
			case TYPE_NEUTRE:				// neutraliser le alors
				objet.modifie(adFinAlors - 2, CodesInstructions.CODE_D�PILER) ;
				break;
			case TYPE_BOOLEEN:
				break;							// types conformes et �quilibr�s				
			case TYPE_ENTIER:
				throw new BigLangException ("alors/sinon : "+typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPES_NON_CONFORMES);
			default:
				break;
			}			
			break;
		case TYPE_ENTIER:
			switch (typeAlors) {
			case TYPE_NEUTRE:				// neutraliser le alors
				objet.modifie(adFinAlors - 2, CodesInstructions.CODE_D�PILER) ;				
				break;
			case TYPE_BOOLEEN:
				throw new BigLangException ("alors/sinon : "+typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPES_NON_CONFORMES);
			case TYPE_ENTIER:				
				break;							// types conformes et �quilibr�s
			default:
				break;
			}			
			break;
		default:
			break;
		}
		
		objet.modifie(adFinAlors, Ce) ;
	}
	public static void fsiSansSinon () throws BigLangException {
		if (typeCourant != TYPE_NEUTRE) {
			objet.modifie(Ce, CodesInstructions.CODE_D�PILER) ; Ce++ ;
			typeCourant = TYPE_NEUTRE ; cEstUnRep�re = false ;
		}
		
		int adFinCondition = ((Integer) pileDesReprises.pop()).intValue() ;
		objet.modifie(adFinCondition, Ce) ;
	}
}
