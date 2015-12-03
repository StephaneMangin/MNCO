
package biglang.syntaxique;
import biglang.BigLangException;
import biglang.generation.G�n�ration;
import biglang.lexical.AnalyseLexicale;
import biglang.lexical.AnalyseLexicale.T�teDeLecture;
import biglang.lexical.Cat�goriesLexicales;
/*
 * Cr�� le 7 juin 2005
 *
 */

/**
 * Analyseur syntaxique pour BigLang
 * @author syntaxique.FaireAnalyseurSyntaxique
 *
 */
public class AnalyseSyntaxique {
	static AnalyseLexicale lex = new AnalyseLexicale () ;
	public static T�teDeLecture t�te ;
	static void initT�te (String f) {
			lex.initT�te(f+".big");
			t�te = lex.lexLu ;
	}
	static void AvT�te () {
		try {
			lex.lancer() ;
			t�te = lex.lexLu ;
			} catch (Exception e) {
			t�te.cat�gorie = Cat�goriesLexicales.LEX_ERR ;
		}
	}
	static void franchir (int elt, int err) throws BigLangException {
		if (t�te.cat�gorie != elt) {throw new BigLangException (BigLangException.BL_SYNT, err);}
		a.texte(t�te);
		AvT�te() ;
	}
	static ArbreSyntaxique a ;
	/**  $debut Prog $fin -> Decls $finDefVar debut Insts fin */
	public static void Prog () throws BigLangException {
		G�n�ration.debut () ;
		a.baliseO("Prog");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_ENTIER ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_BOOLEEN) {
			Decls () ;
			G�n�ration.finDefVar () ;
			franchir (Cat�goriesLexicales.LEX_DEBUT, BigLangException.BL_SYNT_DEBUT_ATTENDU) ;
			Insts () ;
			franchir (Cat�goriesLexicales.LEX_FIN, BigLangException.BL_SYNT_FIN_ATTENDU) ;
		}
		else {
			throw new BigLangException ("Prog", "[entier, booleen]");
		}
		a.baliseF("Prog");
		G�n�ration.fin () ;
	}
	
	/**  Decls -> Decl SuiteDecls | vide */
	public static void Decls () throws BigLangException {
		a.baliseO("Decls");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_ENTIER ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_BOOLEEN) {
			Decl () ;
			SuiteDecls () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_DEBUT) {
			// rien
		}
		else {
			throw new BigLangException ("Decls", "[entier, booleen, debut]");
		}
		a.baliseF("Decls");
	}
	
	/**  SuiteDecls -> ; Decls SuiteDecls | vide */
	public static void SuiteDecls () throws BigLangException {
		a.baliseO("SuiteDecls");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_PT_VIRG) {
			franchir (Cat�goriesLexicales.LEX_PT_VIRG, BigLangException.BL_SYNT_PT_VIRG_ATTENDU) ;
			Decls () ;
			SuiteDecls () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_DEBUT) {
			// rien
		}
		else {
			throw new BigLangException ("SuiteDecls", "[debut, ;]");
		}
		a.baliseF("SuiteDecls");
	}
	
	/**  Decl -> entier $defVarEnt idf Idents | booleen $defVarBool idf Idents | vide */
	public static void Decl () throws BigLangException {
		a.baliseO("Decl");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_ENTIER) {
			franchir (Cat�goriesLexicales.LEX_ENTIER, BigLangException.BL_SYNT_ENTIER_ATTENDU) ;
			G�n�ration.defVarEnt () ;
			franchir (Cat�goriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU) ;
			Idents () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_BOOLEEN) {
			franchir (Cat�goriesLexicales.LEX_BOOLEEN, BigLangException.BL_SYNT_BOOLEEN_ATTENDU) ;
			G�n�ration.defVarBool () ;
			franchir (Cat�goriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU) ;
			Idents () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_DEBUT) {
			// rien
		}
		else {
			throw new BigLangException ("Decl", "[entier, booleen, debut]");
		}
		a.baliseF("Decl");
	}
	
	/**  Idents -> , $defVar idf Idents | vide */
	public static void Idents () throws BigLangException {
		a.baliseO("Idents");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_VIRG) {
			franchir (Cat�goriesLexicales.LEX_VIRG, BigLangException.BL_SYNT_VIRG_ATTENDU) ;
			G�n�ration.defVar () ;
			franchir (Cat�goriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU) ;
			Idents () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_PT_VIRG ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_DEBUT) {
			// rien
		}
		else {
			throw new BigLangException ("Idents", "[debut, ;, ,]");
		}
		a.baliseF("Idents");
	}
	
	/**  Insts -> Instruction SuiteInsts */
	public static void Insts () throws BigLangException {
		a.baliseO("Insts");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_IDF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NBRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_OUV ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NON ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_LIRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_ECRIRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SI) {
			Instruction () ;
			SuiteInsts () ;
		}
		else {
			throw new BigLangException ("Insts", "[non, (, idf, si, ecrire, nbre, lire]");
		}
		a.baliseF("Insts");
	}
	
	/**  SuiteInsts -> $neutraliser ; Insts | vide */
	public static void SuiteInsts () throws BigLangException {
		a.baliseO("SuiteInsts");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_PT_VIRG) {
			G�n�ration.neutraliser () ;
			franchir (Cat�goriesLexicales.LEX_PT_VIRG, BigLangException.BL_SYNT_PT_VIRG_ATTENDU) ;
			Insts () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_FIN ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FSI ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SINON) {
			// rien
		}
		else {
			throw new BigLangException ("SuiteInsts", "[sinon, fin, fsi, ;]");
		}
		a.baliseF("SuiteInsts");
	}
	
	/**  Instruction -> Exp | lire $lire idf | ecrire $ecrire idf */
	public static void Instruction () throws BigLangException {
		a.baliseO("Instruction");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_IDF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NBRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_OUV ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NON ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SI) {
			Exp () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_LIRE) {
			franchir (Cat�goriesLexicales.LEX_LIRE, BigLangException.BL_SYNT_LIRE_ATTENDU) ;
			G�n�ration.lire () ;
			franchir (Cat�goriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU) ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_ECRIRE) {
			franchir (Cat�goriesLexicales.LEX_ECRIRE, BigLangException.BL_SYNT_ECRIRE_ATTENDU) ;
			G�n�ration.ecrire () ;
			franchir (Cat�goriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU) ;
		}
		else {
			throw new BigLangException ("Instruction", "[non, (, idf, ecrire, si, nbre, lire]");
		}
		a.baliseF("Instruction");
	}
	
	/**  Exp -> SansAff RExp */
	public static void Exp () throws BigLangException {
		a.baliseO("Exp");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_IDF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NBRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_OUV ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NON ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SI) {
			SansAff () ;
			RExp () ;
		}
		else {
			throw new BigLangException ("Exp", "[non, (, idf, si, nbre]");
		}
		a.baliseF("Exp");
	}
	
	/**  RExp -> $op�randeGaucheAff := Exp $op�randeDroitAff | vide */
	public static void RExp () throws BigLangException {
		a.baliseO("RExp");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_AFF) {
			G�n�ration.op�randeGaucheAff () ;
			franchir (Cat�goriesLexicales.LEX_AFF, BigLangException.BL_SYNT_AFF_ATTENDU) ;
			Exp () ;
			G�n�ration.op�randeDroitAff () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_FIN ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PT_VIRG ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_FER ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_ALORS ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FSI ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SINON) {
			// rien
		}
		else {
			throw new BigLangException ("RExp", "[), :=, sinon, alors, fin, fsi, ;]");
		}
		a.baliseF("RExp");
	}
	
	/**  SansAff -> Disjonction RDisjonction */
	public static void SansAff () throws BigLangException {
		a.baliseO("SansAff");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_IDF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NBRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_OUV ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NON ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SI) {
			Disjonction () ;
			RDisjonction () ;
		}
		else {
			throw new BigLangException ("SansAff", "[non, (, idf, si, nbre]");
		}
		a.baliseF("SansAff");
	}
	
	/**  RDisjonction -> $op�randeGauche ou Disjonction $op�randeDroit RDisjonction | vide */
	public static void RDisjonction () throws BigLangException {
		a.baliseO("RDisjonction");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_OU) {
			G�n�ration.op�randeGauche () ;
			franchir (Cat�goriesLexicales.LEX_OU, BigLangException.BL_SYNT_OU_ATTENDU) ;
			Disjonction () ;
			G�n�ration.op�randeDroit () ;
			RDisjonction () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_FER ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PT_VIRG ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FIN ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_AFF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_ALORS ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FSI ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SINON) {
			// rien
		}
		else {
			throw new BigLangException ("RDisjonction", "[:=, ), ou, sinon, alors, fin, fsi, ;]");
		}
		a.baliseF("RDisjonction");
	}
	
	/**  Disjonction -> Conjonction RConjonction */
	public static void Disjonction () throws BigLangException {
		a.baliseO("Disjonction");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_IDF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NBRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_OUV ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NON ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SI) {
			Conjonction () ;
			RConjonction () ;
		}
		else {
			throw new BigLangException ("Disjonction", "[non, (, idf, si, nbre]");
		}
		a.baliseF("Disjonction");
	}
	
	/**  RConjonction -> $op�randeGauche et Conjonction $op�randeDroit RConjonction | vide */
	public static void RConjonction () throws BigLangException {
		a.baliseO("RConjonction");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_ET) {
			G�n�ration.op�randeGauche () ;
			franchir (Cat�goriesLexicales.LEX_ET, BigLangException.BL_SYNT_ET_ATTENDU) ;
			Conjonction () ;
			G�n�ration.op�randeDroit () ;
			RConjonction () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_OU ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_FER ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PT_VIRG ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FIN ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_AFF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_ALORS ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FSI ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SINON) {
			// rien
		}
		else {
			throw new BigLangException ("RConjonction", "[:=, ), ou, et, sinon, alors, fin, fsi, ;]");
		}
		a.baliseF("RConjonction");
	}
	
	/**  Conjonction -> Relation RRelation */
	public static void Conjonction () throws BigLangException {
		a.baliseO("Conjonction");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_IDF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NBRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_OUV ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NON ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SI) {
			Relation () ;
			RRelation () ;
		}
		else {
			throw new BigLangException ("Conjonction", "[non, (, idf, si, nbre]");
		}
		a.baliseF("Conjonction");
	}
	
	/**  RRelation -> $op�randeGauche &lt; Relation $op�randeDroit RRelation | $op�randeGauche = Relation $op�randeDroit RRelation | vide */
	public static void RRelation () throws BigLangException {
		a.baliseO("RRelation");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_INF) {
			G�n�ration.op�randeGauche () ;
			franchir (Cat�goriesLexicales.LEX_INF, BigLangException.BL_SYNT_INF_ATTENDU) ;
			Relation () ;
			G�n�ration.op�randeDroit () ;
			RRelation () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_EG) {
			G�n�ration.op�randeGauche () ;
			franchir (Cat�goriesLexicales.LEX_EG, BigLangException.BL_SYNT_EG_ATTENDU) ;
			Relation () ;
			G�n�ration.op�randeDroit () ;
			RRelation () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_OU ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_ET ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_FER ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PT_VIRG ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FIN ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_AFF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_ALORS ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FSI ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SINON) {
			// rien
		}
		else {
			throw new BigLangException ("RRelation", "[:=, ), et, ou, sinon, <, alors, fin, =, fsi, ;]");
		}
		a.baliseF("RRelation");
	}
	
	/**  Relation -> Terme RTerme */
	public static void Relation () throws BigLangException {
		a.baliseO("Relation");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_IDF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NBRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_OUV ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NON ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SI) {
			Terme () ;
			RTerme () ;
		}
		else {
			throw new BigLangException ("Relation", "[non, (, idf, si, nbre]");
		}
		a.baliseF("Relation");
	}
	
	/**  RTerme -> $op�randeGauche + Terme $op�randeDroit RTerme | $op�randeGauche - Terme $op�randeDroit RTerme | vide */
	public static void RTerme () throws BigLangException {
		a.baliseO("RTerme");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_PLUS) {
			G�n�ration.op�randeGauche () ;
			franchir (Cat�goriesLexicales.LEX_PLUS, BigLangException.BL_SYNT_PLUS_ATTENDU) ;
			Terme () ;
			G�n�ration.op�randeDroit () ;
			RTerme () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_MOINS) {
			G�n�ration.op�randeGauche () ;
			franchir (Cat�goriesLexicales.LEX_MOINS, BigLangException.BL_SYNT_MOINS_ATTENDU) ;
			Terme () ;
			G�n�ration.op�randeDroit () ;
			RTerme () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_OU ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_ET ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_INF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_EG ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_FER ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PT_VIRG ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FIN ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_AFF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_ALORS ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FSI ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SINON) {
			// rien
		}
		else {
			throw new BigLangException ("RTerme", "[ou, :=, +, ;, -, et, ), sinon, <, =, fin, alors, fsi]");
		}
		a.baliseF("RTerme");
	}
	
	/**  Terme -> Facteur RFacteur */
	public static void Terme () throws BigLangException {
		a.baliseO("Terme");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_IDF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NBRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_OUV ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NON ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SI) {
			Facteur () ;
			RFacteur () ;
		}
		else {
			throw new BigLangException ("Terme", "[non, (, idf, nbre, si]");
		}
		a.baliseF("Terme");
	}
	
	/**  RFacteur -> $op�randeGauche * Facteur $op�randeDroit RFacteur | $op�randeGauche / Facteur $op�randeDroit RFacteur | vide */
	public static void RFacteur () throws BigLangException {
		a.baliseO("RFacteur");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_MULT) {
			G�n�ration.op�randeGauche () ;
			franchir (Cat�goriesLexicales.LEX_MULT, BigLangException.BL_SYNT_MULT_ATTENDU) ;
			Facteur () ;
			G�n�ration.op�randeDroit () ;
			RFacteur () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_DIV) {
			G�n�ration.op�randeGauche () ;
			franchir (Cat�goriesLexicales.LEX_DIV, BigLangException.BL_SYNT_DIV_ATTENDU) ;
			Facteur () ;
			G�n�ration.op�randeDroit () ;
			RFacteur () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_OU ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_ET ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_INF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_EG ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PLUS ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_MOINS ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_FER ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PT_VIRG ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FIN ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_AFF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_ALORS ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_FSI ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SINON) {
			// rien
		}
		else {
			throw new BigLangException ("RFacteur", "[/, :=, ou, +, ;, -, ), et, sinon, <, alors, fin, =, fsi, *]");
		}
		a.baliseF("RFacteur");
	}
	
	/**  Facteur -> $op�randeGauche non Facteur $op�randeDroit | Primaire */
	public static void Facteur () throws BigLangException {
		a.baliseO("Facteur");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_NON) {
			G�n�ration.op�randeGauche () ;
			franchir (Cat�goriesLexicales.LEX_NON, BigLangException.BL_SYNT_NON_ATTENDU) ;
			Facteur () ;
			G�n�ration.op�randeDroit () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_IDF ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_NBRE ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_OUV ||
			t�te.cat�gorie == Cat�goriesLexicales.LEX_SI) {
			Primaire () ;
		}
		else {
			throw new BigLangException ("Facteur", "[non, (, idf, nbre, si]");
		}
		a.baliseF("Facteur");
	}
	
	/**  Primaire -> $var idf | $nbre nbre | ( Exp ) | si Exp $finCondition alors Insts RPrimaire fsi */
	public static void Primaire () throws BigLangException {
		a.baliseO("Primaire");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_IDF) {
			G�n�ration.var () ;
			franchir (Cat�goriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU) ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_NBRE) {
			G�n�ration.nbre () ;
			franchir (Cat�goriesLexicales.LEX_NBRE, BigLangException.BL_SYNT_NBRE_ATTENDU) ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_PAR_OUV) {
			franchir (Cat�goriesLexicales.LEX_PAR_OUV, BigLangException.BL_SYNT_PAR_OUV_ATTENDU) ;
			Exp () ;
			franchir (Cat�goriesLexicales.LEX_PAR_FER, BigLangException.BL_SYNT_PAR_FER_ATTENDU) ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_SI) {
			franchir (Cat�goriesLexicales.LEX_SI, BigLangException.BL_SYNT_SI_ATTENDU) ;
			Exp () ;
			G�n�ration.finCondition () ;
			franchir (Cat�goriesLexicales.LEX_ALORS, BigLangException.BL_SYNT_ALORS_ATTENDU) ;
			Insts () ;
			RPrimaire () ;
			franchir (Cat�goriesLexicales.LEX_FSI, BigLangException.BL_SYNT_FSI_ATTENDU) ;
		}
		else {
			throw new BigLangException ("Primaire", "[(, idf, nbre, si]");
		}
		a.baliseF("Primaire");
	}
	
	/**  RPrimaire -> $finAlors sinon Insts $finSinon | $fsiSansSinon vide */
	public static void RPrimaire () throws BigLangException {
		a.baliseO("RPrimaire");
		if      (t�te.cat�gorie == Cat�goriesLexicales.LEX_SINON) {
			G�n�ration.finAlors () ;
			franchir (Cat�goriesLexicales.LEX_SINON, BigLangException.BL_SYNT_SINON_ATTENDU) ;
			Insts () ;
			G�n�ration.finSinon () ;
		}
		else if (t�te.cat�gorie == Cat�goriesLexicales.LEX_FSI) {
			G�n�ration.fsiSansSinon () ;
			// rien
		}
		else {
			throw new BigLangException ("RPrimaire", "[sinon, fsi]");
		}
		a.baliseF("RPrimaire");
	}
	
	public static void main (String [] args) {
		a = new ArbreSyntaxique (args[0]+".xml", "Prog") ;
		lex = new AnalyseLexicale () ;
		initT�te(args[0]);
		G�n�ration.initG�n�ration(args[0]) ;
		AvT�te() ;
		try {
			Prog() ;
			} catch (BigLangException e) {
			// e.printStackTrace();
		}
		G�n�ration.objet.sortie();
		a.clore();
	}
}