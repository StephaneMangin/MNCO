package org.istic.mnco.TP6.biglang.syntaxique;

import org.istic.mnco.TP6.biglang.BigLangException;
import org.istic.mnco.TP6.biglang.generation.Generation;
import org.istic.mnco.TP6.biglang.lexical.AnalyseLexicale;
import org.istic.mnco.TP6.biglang.lexical.AnalyseLexicale.TeteDeLecture;
import org.istic.mnco.TP6.biglang.lexical.CategoriesLexicales;
/*
 * Créé le 7 juin 2005
 *
 */

/**
 * Analyseur syntaxique pour BigLang
 *
 * @author syntaxique.FaireAnalyseurSyntaxique
 */
public class AnalyseSyntaxique {
    public static TeteDeLecture tete;
    static AnalyseLexicale lex = new AnalyseLexicale();
    static ArbreSyntaxique a;

    static void initTete(String f) {
        lex.initTete(f + ".big");
        tete = lex.lexLu;
    }

    static void AvTete() {
        try {
            lex.lancer();
            tete = lex.lexLu;
        } catch (Exception e) {
            tete.categorie = CategoriesLexicales.LEX_ERR;
        }
    }

    static void franchir(int elt, int err) throws BigLangException {
        if (tete.categorie != elt) {
            throw new BigLangException(BigLangException.BL_SYNT, err);
        }
        a.texte(tete);
        AvTete();
    }

    /**
     * $debut Prog $fin -> Decls $finDefVar debut Insts fin
     */
    public static void Prog() throws BigLangException {
        Generation.debut();
        a.baliseO("Prog");
        if (tete.categorie == CategoriesLexicales.LEX_ENTIER ||
                tete.categorie == CategoriesLexicales.LEX_BOOLEEN) {
            Decls();
            Generation.finDefVar();
            franchir(CategoriesLexicales.LEX_DEBUT, BigLangException.BL_SYNT_DEBUT_ATTENDU);
            Insts();
            franchir(CategoriesLexicales.LEX_FIN, BigLangException.BL_SYNT_FIN_ATTENDU);
        } else {
            throw new BigLangException("Prog", "[entier, booleen]");
        }
        a.baliseF("Prog");
        Generation.fin();
    }

    /**
     * Decls -> Decl SuiteDecls | vide
     */
    public static void Decls() throws BigLangException {
        a.baliseO("Decls");
        if (tete.categorie == CategoriesLexicales.LEX_ENTIER ||
                tete.categorie == CategoriesLexicales.LEX_BOOLEEN) {
            Decl();
            SuiteDecls();
        } else if (tete.categorie == CategoriesLexicales.LEX_DEBUT) {
            // rien
        } else {
            throw new BigLangException("Decls", "[entier, booleen, debut]");
        }
        a.baliseF("Decls");
    }

    /**
     * SuiteDecls -> ; Decls SuiteDecls | vide
     */
    public static void SuiteDecls() throws BigLangException {
        a.baliseO("SuiteDecls");
        if (tete.categorie == CategoriesLexicales.LEX_PT_VIRG) {
            franchir(CategoriesLexicales.LEX_PT_VIRG, BigLangException.BL_SYNT_PT_VIRG_ATTENDU);
            Decls();
            SuiteDecls();
        } else if (tete.categorie == CategoriesLexicales.LEX_DEBUT) {
            // rien
        } else {
            throw new BigLangException("SuiteDecls", "[debut, ;]");
        }
        a.baliseF("SuiteDecls");
    }

    /**
     * Decl -> entier $defVarEnt idf Idents | booleen $defVarBool idf Idents | vide
     */
    public static void Decl() throws BigLangException {
        a.baliseO("Decl");
        if (tete.categorie == CategoriesLexicales.LEX_ENTIER) {
            franchir(CategoriesLexicales.LEX_ENTIER, BigLangException.BL_SYNT_ENTIER_ATTENDU);
            Generation.defVarEnt();
            franchir(CategoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
            Idents();
        } else if (tete.categorie == CategoriesLexicales.LEX_BOOLEEN) {
            franchir(CategoriesLexicales.LEX_BOOLEEN, BigLangException.BL_SYNT_BOOLEEN_ATTENDU);
            Generation.defVarBool();
            franchir(CategoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
            Idents();
        } else if (tete.categorie == CategoriesLexicales.LEX_DEBUT) {
            // rien
        } else {
            throw new BigLangException("Decl", "[entier, booleen, debut]");
        }
        a.baliseF("Decl");
    }

    /**
     * Idents -> , $defVar idf Idents | vide
     */
    public static void Idents() throws BigLangException {
        a.baliseO("Idents");
        if (tete.categorie == CategoriesLexicales.LEX_VIRG) {
            franchir(CategoriesLexicales.LEX_VIRG, BigLangException.BL_SYNT_VIRG_ATTENDU);
            Generation.defVar();
            franchir(CategoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
            Idents();
        } else if (tete.categorie == CategoriesLexicales.LEX_PT_VIRG ||
                tete.categorie == CategoriesLexicales.LEX_DEBUT) {
            // rien
        } else {
            throw new BigLangException("Idents", "[debut, ;, ,]");
        }
        a.baliseF("Idents");
    }

    /**
     * Insts -> Instruction SuiteInsts
     */
    public static void Insts() throws BigLangException {
        a.baliseO("Insts");
        if (tete.categorie == CategoriesLexicales.LEX_IDF ||
                tete.categorie == CategoriesLexicales.LEX_NBRE ||
                tete.categorie == CategoriesLexicales.LEX_PAR_OUV ||
                tete.categorie == CategoriesLexicales.LEX_NON ||
                tete.categorie == CategoriesLexicales.LEX_LIRE ||
                tete.categorie == CategoriesLexicales.LEX_ECRIRE ||
                tete.categorie == CategoriesLexicales.LEX_SI) {
            Instruction();
            SuiteInsts();
        } else {
            throw new BigLangException("Insts", "[non, (, idf, si, ecrire, nbre, lire]");
        }
        a.baliseF("Insts");
    }

    /**
     * SuiteInsts -> $neutraliser ; Insts | vide
     */
    public static void SuiteInsts() throws BigLangException {
        a.baliseO("SuiteInsts");
        if (tete.categorie == CategoriesLexicales.LEX_PT_VIRG) {
            Generation.neutraliser();
            franchir(CategoriesLexicales.LEX_PT_VIRG, BigLangException.BL_SYNT_PT_VIRG_ATTENDU);
            Insts();
        } else if (tete.categorie == CategoriesLexicales.LEX_FIN ||
                tete.categorie == CategoriesLexicales.LEX_FSI ||
                tete.categorie == CategoriesLexicales.LEX_SINON) {
            // rien
        } else {
            throw new BigLangException("SuiteInsts", "[sinon, fin, fsi, ;]");
        }
        a.baliseF("SuiteInsts");
    }

    /**
     * Instruction -> Exp | lire $lire idf | ecrire $ecrire idf
     */
    public static void Instruction() throws BigLangException {
        a.baliseO("Instruction");
        if (tete.categorie == CategoriesLexicales.LEX_IDF ||
                tete.categorie == CategoriesLexicales.LEX_NBRE ||
                tete.categorie == CategoriesLexicales.LEX_PAR_OUV ||
                tete.categorie == CategoriesLexicales.LEX_NON ||
                tete.categorie == CategoriesLexicales.LEX_SI) {
            Exp();
        } else if (tete.categorie == CategoriesLexicales.LEX_LIRE) {
            franchir(CategoriesLexicales.LEX_LIRE, BigLangException.BL_SYNT_LIRE_ATTENDU);
            Generation.lire();
            franchir(CategoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
        } else if (tete.categorie == CategoriesLexicales.LEX_ECRIRE) {
            franchir(CategoriesLexicales.LEX_ECRIRE, BigLangException.BL_SYNT_ECRIRE_ATTENDU);
            Generation.ecrire();
            franchir(CategoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
        } else {
            throw new BigLangException("Instruction", "[non, (, idf, ecrire, si, nbre, lire]");
        }
        a.baliseF("Instruction");
    }

    /**
     * Exp -> SansAff RExp
     */
    public static void Exp() throws BigLangException {
        a.baliseO("Exp");
        if (tete.categorie == CategoriesLexicales.LEX_IDF ||
                tete.categorie == CategoriesLexicales.LEX_NBRE ||
                tete.categorie == CategoriesLexicales.LEX_PAR_OUV ||
                tete.categorie == CategoriesLexicales.LEX_NON ||
                tete.categorie == CategoriesLexicales.LEX_SI) {
            SansAff();
            RExp();
        } else {
            throw new BigLangException("Exp", "[non, (, idf, si, nbre]");
        }
        a.baliseF("Exp");
    }

    /**
     * RExp -> $operandeGaucheAff := Exp $operandeDroitAff | vide
     */
    public static void RExp() throws BigLangException {
        a.baliseO("RExp");
        if (tete.categorie == CategoriesLexicales.LEX_AFF) {
            Generation.operandeGaucheAff();
            franchir(CategoriesLexicales.LEX_AFF, BigLangException.BL_SYNT_AFF_ATTENDU);
            Exp();
            Generation.operandeDroitAff();
        } else if (tete.categorie == CategoriesLexicales.LEX_FIN ||
                tete.categorie == CategoriesLexicales.LEX_PT_VIRG ||
                tete.categorie == CategoriesLexicales.LEX_PAR_FER ||
                tete.categorie == CategoriesLexicales.LEX_ALORS ||
                tete.categorie == CategoriesLexicales.LEX_FSI ||
                tete.categorie == CategoriesLexicales.LEX_SINON) {
            // rien
        } else {
            throw new BigLangException("RExp", "[), :=, sinon, alors, fin, fsi, ;]");
        }
        a.baliseF("RExp");
    }

    /**
     * SansAff -> Disjonction RDisjonction
     */
    public static void SansAff() throws BigLangException {
        a.baliseO("SansAff");
        if (tete.categorie == CategoriesLexicales.LEX_IDF ||
                tete.categorie == CategoriesLexicales.LEX_NBRE ||
                tete.categorie == CategoriesLexicales.LEX_PAR_OUV ||
                tete.categorie == CategoriesLexicales.LEX_NON ||
                tete.categorie == CategoriesLexicales.LEX_SI) {
            Disjonction();
            RDisjonction();
        } else {
            throw new BigLangException("SansAff", "[non, (, idf, si, nbre]");
        }
        a.baliseF("SansAff");
    }

    /**
     * RDisjonction -> $operandeGauche ou Disjonction $operandeDroit RDisjonction | vide
     */
    public static void RDisjonction() throws BigLangException {
        a.baliseO("RDisjonction");
        if (tete.categorie == CategoriesLexicales.LEX_OU) {
            Generation.operandeGauche();
            franchir(CategoriesLexicales.LEX_OU, BigLangException.BL_SYNT_OU_ATTENDU);
            Disjonction();
            Generation.operandeDroit();
            RDisjonction();
        } else if (tete.categorie == CategoriesLexicales.LEX_PAR_FER ||
                tete.categorie == CategoriesLexicales.LEX_PT_VIRG ||
                tete.categorie == CategoriesLexicales.LEX_FIN ||
                tete.categorie == CategoriesLexicales.LEX_AFF ||
                tete.categorie == CategoriesLexicales.LEX_ALORS ||
                tete.categorie == CategoriesLexicales.LEX_FSI ||
                tete.categorie == CategoriesLexicales.LEX_SINON) {
            // rien
        } else {
            throw new BigLangException("RDisjonction", "[:=, ), ou, sinon, alors, fin, fsi, ;]");
        }
        a.baliseF("RDisjonction");
    }

    /**
     * Disjonction -> Conjonction RConjonction
     */
    public static void Disjonction() throws BigLangException {
        a.baliseO("Disjonction");
        if (tete.categorie == CategoriesLexicales.LEX_IDF ||
                tete.categorie == CategoriesLexicales.LEX_NBRE ||
                tete.categorie == CategoriesLexicales.LEX_PAR_OUV ||
                tete.categorie == CategoriesLexicales.LEX_NON ||
                tete.categorie == CategoriesLexicales.LEX_SI) {
            Conjonction();
            RConjonction();
        } else {
            throw new BigLangException("Disjonction", "[non, (, idf, si, nbre]");
        }
        a.baliseF("Disjonction");
    }

    /**
     * RConjonction -> $operandeGauche et Conjonction $operandeDroit RConjonction | vide
     */
    public static void RConjonction() throws BigLangException {
        a.baliseO("RConjonction");
        if (tete.categorie == CategoriesLexicales.LEX_ET) {
            Generation.operandeGauche();
            franchir(CategoriesLexicales.LEX_ET, BigLangException.BL_SYNT_ET_ATTENDU);
            Conjonction();
            Generation.operandeDroit();
            RConjonction();
        } else if (tete.categorie == CategoriesLexicales.LEX_OU ||
                tete.categorie == CategoriesLexicales.LEX_PAR_FER ||
                tete.categorie == CategoriesLexicales.LEX_PT_VIRG ||
                tete.categorie == CategoriesLexicales.LEX_FIN ||
                tete.categorie == CategoriesLexicales.LEX_AFF ||
                tete.categorie == CategoriesLexicales.LEX_ALORS ||
                tete.categorie == CategoriesLexicales.LEX_FSI ||
                tete.categorie == CategoriesLexicales.LEX_SINON) {
            // rien
        } else {
            throw new BigLangException("RConjonction", "[:=, ), ou, et, sinon, alors, fin, fsi, ;]");
        }
        a.baliseF("RConjonction");
    }

    /**
     * Conjonction -> Relation RRelation
     */
    public static void Conjonction() throws BigLangException {
        a.baliseO("Conjonction");
        if (tete.categorie == CategoriesLexicales.LEX_IDF ||
                tete.categorie == CategoriesLexicales.LEX_NBRE ||
                tete.categorie == CategoriesLexicales.LEX_PAR_OUV ||
                tete.categorie == CategoriesLexicales.LEX_NON ||
                tete.categorie == CategoriesLexicales.LEX_SI) {
            Relation();
            RRelation();
        } else {
            throw new BigLangException("Conjonction", "[non, (, idf, si, nbre]");
        }
        a.baliseF("Conjonction");
    }

    /**
     * RRelation -> $operandeGauche &lt; Relation $operandeDroit RRelation | $operandeGauche = Relation $operandeDroit RRelation | vide
     */
    public static void RRelation() throws BigLangException {
        a.baliseO("RRelation");
        if (tete.categorie == CategoriesLexicales.LEX_INF) {
            Generation.operandeGauche();
            franchir(CategoriesLexicales.LEX_INF, BigLangException.BL_SYNT_INF_ATTENDU);
            Relation();
            Generation.operandeDroit();
            RRelation();
        } else if (tete.categorie == CategoriesLexicales.LEX_EG) {
            Generation.operandeGauche();
            franchir(CategoriesLexicales.LEX_EG, BigLangException.BL_SYNT_EG_ATTENDU);
            Relation();
            Generation.operandeDroit();
            RRelation();
        } else if (tete.categorie == CategoriesLexicales.LEX_OU ||
                tete.categorie == CategoriesLexicales.LEX_ET ||
                tete.categorie == CategoriesLexicales.LEX_PAR_FER ||
                tete.categorie == CategoriesLexicales.LEX_PT_VIRG ||
                tete.categorie == CategoriesLexicales.LEX_FIN ||
                tete.categorie == CategoriesLexicales.LEX_AFF ||
                tete.categorie == CategoriesLexicales.LEX_ALORS ||
                tete.categorie == CategoriesLexicales.LEX_FSI ||
                tete.categorie == CategoriesLexicales.LEX_SINON) {
            // rien
        } else {
            throw new BigLangException("RRelation", "[:=, ), et, ou, sinon, <, alors, fin, =, fsi, ;]");
        }
        a.baliseF("RRelation");
    }

    /**
     * Relation -> Terme RTerme
     */
    public static void Relation() throws BigLangException {
        a.baliseO("Relation");
        if (tete.categorie == CategoriesLexicales.LEX_IDF ||
                tete.categorie == CategoriesLexicales.LEX_NBRE ||
                tete.categorie == CategoriesLexicales.LEX_PAR_OUV ||
                tete.categorie == CategoriesLexicales.LEX_NON ||
                tete.categorie == CategoriesLexicales.LEX_SI) {
            Terme();
            RTerme();
        } else {
            throw new BigLangException("Relation", "[non, (, idf, si, nbre]");
        }
        a.baliseF("Relation");
    }

    /**
     * RTerme -> $operandeGauche + Terme $operandeDroit RTerme | $operandeGauche - Terme $operandeDroit RTerme | vide
     */
    public static void RTerme() throws BigLangException {
        a.baliseO("RTerme");
        if (tete.categorie == CategoriesLexicales.LEX_PLUS) {
            Generation.operandeGauche();
            franchir(CategoriesLexicales.LEX_PLUS, BigLangException.BL_SYNT_PLUS_ATTENDU);
            Terme();
            Generation.operandeDroit();
            RTerme();
        } else if (tete.categorie == CategoriesLexicales.LEX_MOINS) {
            Generation.operandeGauche();
            franchir(CategoriesLexicales.LEX_MOINS, BigLangException.BL_SYNT_MOINS_ATTENDU);
            Terme();
            Generation.operandeDroit();
            RTerme();
        } else if (tete.categorie == CategoriesLexicales.LEX_OU ||
                tete.categorie == CategoriesLexicales.LEX_ET ||
                tete.categorie == CategoriesLexicales.LEX_INF ||
                tete.categorie == CategoriesLexicales.LEX_EG ||
                tete.categorie == CategoriesLexicales.LEX_PAR_FER ||
                tete.categorie == CategoriesLexicales.LEX_PT_VIRG ||
                tete.categorie == CategoriesLexicales.LEX_FIN ||
                tete.categorie == CategoriesLexicales.LEX_AFF ||
                tete.categorie == CategoriesLexicales.LEX_ALORS ||
                tete.categorie == CategoriesLexicales.LEX_FSI ||
                tete.categorie == CategoriesLexicales.LEX_SINON) {
            // rien
        } else {
            throw new BigLangException("RTerme", "[ou, :=, +, ;, -, et, ), sinon, <, =, fin, alors, fsi]");
        }
        a.baliseF("RTerme");
    }

    /**
     * Terme -> Facteur RFacteur
     */
    public static void Terme() throws BigLangException {
        a.baliseO("Terme");
        if (tete.categorie == CategoriesLexicales.LEX_IDF ||
                tete.categorie == CategoriesLexicales.LEX_NBRE ||
                tete.categorie == CategoriesLexicales.LEX_PAR_OUV ||
                tete.categorie == CategoriesLexicales.LEX_NON ||
                tete.categorie == CategoriesLexicales.LEX_SI) {
            Facteur();
            RFacteur();
        } else {
            throw new BigLangException("Terme", "[non, (, idf, nbre, si]");
        }
        a.baliseF("Terme");
    }

    /**
     * RFacteur -> $operandeGauche * Facteur $operandeDroit RFacteur | $operandeGauche / Facteur $operandeDroit RFacteur | vide
     */
    public static void RFacteur() throws BigLangException {
        a.baliseO("RFacteur");
        if (tete.categorie == CategoriesLexicales.LEX_MULT) {
            Generation.operandeGauche();
            franchir(CategoriesLexicales.LEX_MULT, BigLangException.BL_SYNT_MULT_ATTENDU);
            Facteur();
            Generation.operandeDroit();
            RFacteur();
        } else if (tete.categorie == CategoriesLexicales.LEX_DIV) {
            Generation.operandeGauche();
            franchir(CategoriesLexicales.LEX_DIV, BigLangException.BL_SYNT_DIV_ATTENDU);
            Facteur();
            Generation.operandeDroit();
            RFacteur();
        } else if (tete.categorie == CategoriesLexicales.LEX_OU ||
                tete.categorie == CategoriesLexicales.LEX_ET ||
                tete.categorie == CategoriesLexicales.LEX_INF ||
                tete.categorie == CategoriesLexicales.LEX_EG ||
                tete.categorie == CategoriesLexicales.LEX_PLUS ||
                tete.categorie == CategoriesLexicales.LEX_MOINS ||
                tete.categorie == CategoriesLexicales.LEX_PAR_FER ||
                tete.categorie == CategoriesLexicales.LEX_PT_VIRG ||
                tete.categorie == CategoriesLexicales.LEX_FIN ||
                tete.categorie == CategoriesLexicales.LEX_AFF ||
                tete.categorie == CategoriesLexicales.LEX_ALORS ||
                tete.categorie == CategoriesLexicales.LEX_FSI ||
                tete.categorie == CategoriesLexicales.LEX_SINON) {
            // rien
        } else {
            throw new BigLangException("RFacteur", "[/, :=, ou, +, ;, -, ), et, sinon, <, alors, fin, =, fsi, *]");
        }
        a.baliseF("RFacteur");
    }

    /**
     * Facteur -> $operandeGauche non Facteur $operandeDroit | Primaire
     */
    public static void Facteur() throws BigLangException {
        a.baliseO("Facteur");
        if (tete.categorie == CategoriesLexicales.LEX_NON) {
            Generation.operandeGauche();
            franchir(CategoriesLexicales.LEX_NON, BigLangException.BL_SYNT_NON_ATTENDU);
            Facteur();
            Generation.operandeDroit();
        } else if (tete.categorie == CategoriesLexicales.LEX_IDF ||
                tete.categorie == CategoriesLexicales.LEX_NBRE ||
                tete.categorie == CategoriesLexicales.LEX_PAR_OUV ||
                tete.categorie == CategoriesLexicales.LEX_SI) {
            Primaire();
        } else {
            throw new BigLangException("Facteur", "[non, (, idf, nbre, si]");
        }
        a.baliseF("Facteur");
    }

    /**
     * Primaire -> $var idf | $nbre nbre | ( Exp ) | si Exp $finCondition alors Insts RPrimaire fsi
     */
    public static void Primaire() throws BigLangException {
        a.baliseO("Primaire");
        if (tete.categorie == CategoriesLexicales.LEX_IDF) {
            Generation.var();
            franchir(CategoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
        } else if (tete.categorie == CategoriesLexicales.LEX_NBRE) {
            Generation.nbre();
            franchir(CategoriesLexicales.LEX_NBRE, BigLangException.BL_SYNT_NBRE_ATTENDU);
        } else if (tete.categorie == CategoriesLexicales.LEX_PAR_OUV) {
            franchir(CategoriesLexicales.LEX_PAR_OUV, BigLangException.BL_SYNT_PAR_OUV_ATTENDU);
            Exp();
            franchir(CategoriesLexicales.LEX_PAR_FER, BigLangException.BL_SYNT_PAR_FER_ATTENDU);
        } else if (tete.categorie == CategoriesLexicales.LEX_SI) {
            franchir(CategoriesLexicales.LEX_SI, BigLangException.BL_SYNT_SI_ATTENDU);
            Exp();
            Generation.finCondition();
            franchir(CategoriesLexicales.LEX_ALORS, BigLangException.BL_SYNT_ALORS_ATTENDU);
            Insts();
            RPrimaire();
            franchir(CategoriesLexicales.LEX_FSI, BigLangException.BL_SYNT_FSI_ATTENDU);
        } else {
            throw new BigLangException("Primaire", "[(, idf, nbre, si]");
        }
        a.baliseF("Primaire");
    }

    /**
     * RPrimaire -> $finAlors sinon Insts $finSinon | $fsiSansSinon vide
     */
    public static void RPrimaire() throws BigLangException {
        a.baliseO("RPrimaire");
        if (tete.categorie == CategoriesLexicales.LEX_SINON) {
            Generation.finAlors();
            franchir(CategoriesLexicales.LEX_SINON, BigLangException.BL_SYNT_SINON_ATTENDU);
            Insts();
            Generation.finSinon();
        } else if (tete.categorie == CategoriesLexicales.LEX_FSI) {
            Generation.fsiSansSinon();
            // rien
        } else {
            throw new BigLangException("RPrimaire", "[sinon, fsi]");
        }
        a.baliseF("RPrimaire");
    }

    public static void main(String[] args) {
        a = new ArbreSyntaxique(args[0] + ".xml", "Prog");
        lex = new AnalyseLexicale();
        initTete(args[0]);
        Generation.initGeneration(args[0]);
        AvTete();
        try {
            Prog();
        } catch (BigLangException e) {
            // e.printStackTrace();
        }
        Generation.objet.sortie();
        a.clore();
    }
}