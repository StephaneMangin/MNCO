package org.istic.mnco.TP6.biglang.syntaxique;

import org.istic.mnco.TP6.biglang.BigLangException;
import org.istic.mnco.TP6.biglang.generation.Génération;
import org.istic.mnco.TP6.biglang.lexical.AnalyseLexicale;
import org.istic.mnco.TP6.biglang.lexical.AnalyseLexicale.TêteDeLecture;
import org.istic.mnco.TP6.biglang.lexical.CatégoriesLexicales;
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
    public static TêteDeLecture tête;
    static AnalyseLexicale lex = new AnalyseLexicale();
    static ArbreSyntaxique a;

    static void initTête(String f) {
        lex.initTête(f + ".big");
        tête = lex.lexLu;
    }

    static void AvTête() {
        try {
            lex.lancer();
            tête = lex.lexLu;
        } catch (Exception e) {
            tête.catégorie = CatégoriesLexicales.LEX_ERR;
        }
    }

    static void franchir(int elt, int err) throws BigLangException {
        if (tête.catégorie != elt) {
            throw new BigLangException(BigLangException.BL_SYNT, err);
        }
        a.texte(tête);
        AvTête();
    }

    /**
     * $debut Prog $fin -> Decls $finDefVar debut Insts fin
     */
    public static void Prog() throws BigLangException {
        Génération.debut();
        a.baliseO("Prog");
        if (tête.catégorie == CatégoriesLexicales.LEX_ENTIER ||
                tête.catégorie == CatégoriesLexicales.LEX_BOOLEEN) {
            Decls();
            Génération.finDefVar();
            franchir(CatégoriesLexicales.LEX_DEBUT, BigLangException.BL_SYNT_DEBUT_ATTENDU);
            Insts();
            franchir(CatégoriesLexicales.LEX_FIN, BigLangException.BL_SYNT_FIN_ATTENDU);
        } else {
            throw new BigLangException("Prog", "[entier, booleen]");
        }
        a.baliseF("Prog");
        Génération.fin();
    }

    /**
     * Decls -> Decl SuiteDecls | vide
     */
    public static void Decls() throws BigLangException {
        a.baliseO("Decls");
        if (tête.catégorie == CatégoriesLexicales.LEX_ENTIER ||
                tête.catégorie == CatégoriesLexicales.LEX_BOOLEEN) {
            Decl();
            SuiteDecls();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_DEBUT) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_PT_VIRG) {
            franchir(CatégoriesLexicales.LEX_PT_VIRG, BigLangException.BL_SYNT_PT_VIRG_ATTENDU);
            Decls();
            SuiteDecls();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_DEBUT) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_ENTIER) {
            franchir(CatégoriesLexicales.LEX_ENTIER, BigLangException.BL_SYNT_ENTIER_ATTENDU);
            Génération.defVarEnt();
            franchir(CatégoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
            Idents();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_BOOLEEN) {
            franchir(CatégoriesLexicales.LEX_BOOLEEN, BigLangException.BL_SYNT_BOOLEEN_ATTENDU);
            Génération.defVarBool();
            franchir(CatégoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
            Idents();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_DEBUT) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_VIRG) {
            franchir(CatégoriesLexicales.LEX_VIRG, BigLangException.BL_SYNT_VIRG_ATTENDU);
            Génération.defVar();
            franchir(CatégoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
            Idents();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_PT_VIRG ||
                tête.catégorie == CatégoriesLexicales.LEX_DEBUT) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_IDF ||
                tête.catégorie == CatégoriesLexicales.LEX_NBRE ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_OUV ||
                tête.catégorie == CatégoriesLexicales.LEX_NON ||
                tête.catégorie == CatégoriesLexicales.LEX_LIRE ||
                tête.catégorie == CatégoriesLexicales.LEX_ECRIRE ||
                tête.catégorie == CatégoriesLexicales.LEX_SI) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_PT_VIRG) {
            Génération.neutraliser();
            franchir(CatégoriesLexicales.LEX_PT_VIRG, BigLangException.BL_SYNT_PT_VIRG_ATTENDU);
            Insts();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_FIN ||
                tête.catégorie == CatégoriesLexicales.LEX_FSI ||
                tête.catégorie == CatégoriesLexicales.LEX_SINON) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_IDF ||
                tête.catégorie == CatégoriesLexicales.LEX_NBRE ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_OUV ||
                tête.catégorie == CatégoriesLexicales.LEX_NON ||
                tête.catégorie == CatégoriesLexicales.LEX_SI) {
            Exp();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_LIRE) {
            franchir(CatégoriesLexicales.LEX_LIRE, BigLangException.BL_SYNT_LIRE_ATTENDU);
            Génération.lire();
            franchir(CatégoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
        } else if (tête.catégorie == CatégoriesLexicales.LEX_ECRIRE) {
            franchir(CatégoriesLexicales.LEX_ECRIRE, BigLangException.BL_SYNT_ECRIRE_ATTENDU);
            Génération.ecrire();
            franchir(CatégoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
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
        if (tête.catégorie == CatégoriesLexicales.LEX_IDF ||
                tête.catégorie == CatégoriesLexicales.LEX_NBRE ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_OUV ||
                tête.catégorie == CatégoriesLexicales.LEX_NON ||
                tête.catégorie == CatégoriesLexicales.LEX_SI) {
            SansAff();
            RExp();
        } else {
            throw new BigLangException("Exp", "[non, (, idf, si, nbre]");
        }
        a.baliseF("Exp");
    }

    /**
     * RExp -> $opérandeGaucheAff := Exp $opérandeDroitAff | vide
     */
    public static void RExp() throws BigLangException {
        a.baliseO("RExp");
        if (tête.catégorie == CatégoriesLexicales.LEX_AFF) {
            Génération.opérandeGaucheAff();
            franchir(CatégoriesLexicales.LEX_AFF, BigLangException.BL_SYNT_AFF_ATTENDU);
            Exp();
            Génération.opérandeDroitAff();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_FIN ||
                tête.catégorie == CatégoriesLexicales.LEX_PT_VIRG ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_FER ||
                tête.catégorie == CatégoriesLexicales.LEX_ALORS ||
                tête.catégorie == CatégoriesLexicales.LEX_FSI ||
                tête.catégorie == CatégoriesLexicales.LEX_SINON) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_IDF ||
                tête.catégorie == CatégoriesLexicales.LEX_NBRE ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_OUV ||
                tête.catégorie == CatégoriesLexicales.LEX_NON ||
                tête.catégorie == CatégoriesLexicales.LEX_SI) {
            Disjonction();
            RDisjonction();
        } else {
            throw new BigLangException("SansAff", "[non, (, idf, si, nbre]");
        }
        a.baliseF("SansAff");
    }

    /**
     * RDisjonction -> $opérandeGauche ou Disjonction $opérandeDroit RDisjonction | vide
     */
    public static void RDisjonction() throws BigLangException {
        a.baliseO("RDisjonction");
        if (tête.catégorie == CatégoriesLexicales.LEX_OU) {
            Génération.opérandeGauche();
            franchir(CatégoriesLexicales.LEX_OU, BigLangException.BL_SYNT_OU_ATTENDU);
            Disjonction();
            Génération.opérandeDroit();
            RDisjonction();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_PAR_FER ||
                tête.catégorie == CatégoriesLexicales.LEX_PT_VIRG ||
                tête.catégorie == CatégoriesLexicales.LEX_FIN ||
                tête.catégorie == CatégoriesLexicales.LEX_AFF ||
                tête.catégorie == CatégoriesLexicales.LEX_ALORS ||
                tête.catégorie == CatégoriesLexicales.LEX_FSI ||
                tête.catégorie == CatégoriesLexicales.LEX_SINON) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_IDF ||
                tête.catégorie == CatégoriesLexicales.LEX_NBRE ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_OUV ||
                tête.catégorie == CatégoriesLexicales.LEX_NON ||
                tête.catégorie == CatégoriesLexicales.LEX_SI) {
            Conjonction();
            RConjonction();
        } else {
            throw new BigLangException("Disjonction", "[non, (, idf, si, nbre]");
        }
        a.baliseF("Disjonction");
    }

    /**
     * RConjonction -> $opérandeGauche et Conjonction $opérandeDroit RConjonction | vide
     */
    public static void RConjonction() throws BigLangException {
        a.baliseO("RConjonction");
        if (tête.catégorie == CatégoriesLexicales.LEX_ET) {
            Génération.opérandeGauche();
            franchir(CatégoriesLexicales.LEX_ET, BigLangException.BL_SYNT_ET_ATTENDU);
            Conjonction();
            Génération.opérandeDroit();
            RConjonction();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_OU ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_FER ||
                tête.catégorie == CatégoriesLexicales.LEX_PT_VIRG ||
                tête.catégorie == CatégoriesLexicales.LEX_FIN ||
                tête.catégorie == CatégoriesLexicales.LEX_AFF ||
                tête.catégorie == CatégoriesLexicales.LEX_ALORS ||
                tête.catégorie == CatégoriesLexicales.LEX_FSI ||
                tête.catégorie == CatégoriesLexicales.LEX_SINON) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_IDF ||
                tête.catégorie == CatégoriesLexicales.LEX_NBRE ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_OUV ||
                tête.catégorie == CatégoriesLexicales.LEX_NON ||
                tête.catégorie == CatégoriesLexicales.LEX_SI) {
            Relation();
            RRelation();
        } else {
            throw new BigLangException("Conjonction", "[non, (, idf, si, nbre]");
        }
        a.baliseF("Conjonction");
    }

    /**
     * RRelation -> $opérandeGauche &lt; Relation $opérandeDroit RRelation | $opérandeGauche = Relation $opérandeDroit RRelation | vide
     */
    public static void RRelation() throws BigLangException {
        a.baliseO("RRelation");
        if (tête.catégorie == CatégoriesLexicales.LEX_INF) {
            Génération.opérandeGauche();
            franchir(CatégoriesLexicales.LEX_INF, BigLangException.BL_SYNT_INF_ATTENDU);
            Relation();
            Génération.opérandeDroit();
            RRelation();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_EG) {
            Génération.opérandeGauche();
            franchir(CatégoriesLexicales.LEX_EG, BigLangException.BL_SYNT_EG_ATTENDU);
            Relation();
            Génération.opérandeDroit();
            RRelation();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_OU ||
                tête.catégorie == CatégoriesLexicales.LEX_ET ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_FER ||
                tête.catégorie == CatégoriesLexicales.LEX_PT_VIRG ||
                tête.catégorie == CatégoriesLexicales.LEX_FIN ||
                tête.catégorie == CatégoriesLexicales.LEX_AFF ||
                tête.catégorie == CatégoriesLexicales.LEX_ALORS ||
                tête.catégorie == CatégoriesLexicales.LEX_FSI ||
                tête.catégorie == CatégoriesLexicales.LEX_SINON) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_IDF ||
                tête.catégorie == CatégoriesLexicales.LEX_NBRE ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_OUV ||
                tête.catégorie == CatégoriesLexicales.LEX_NON ||
                tête.catégorie == CatégoriesLexicales.LEX_SI) {
            Terme();
            RTerme();
        } else {
            throw new BigLangException("Relation", "[non, (, idf, si, nbre]");
        }
        a.baliseF("Relation");
    }

    /**
     * RTerme -> $opérandeGauche + Terme $opérandeDroit RTerme | $opérandeGauche - Terme $opérandeDroit RTerme | vide
     */
    public static void RTerme() throws BigLangException {
        a.baliseO("RTerme");
        if (tête.catégorie == CatégoriesLexicales.LEX_PLUS) {
            Génération.opérandeGauche();
            franchir(CatégoriesLexicales.LEX_PLUS, BigLangException.BL_SYNT_PLUS_ATTENDU);
            Terme();
            Génération.opérandeDroit();
            RTerme();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_MOINS) {
            Génération.opérandeGauche();
            franchir(CatégoriesLexicales.LEX_MOINS, BigLangException.BL_SYNT_MOINS_ATTENDU);
            Terme();
            Génération.opérandeDroit();
            RTerme();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_OU ||
                tête.catégorie == CatégoriesLexicales.LEX_ET ||
                tête.catégorie == CatégoriesLexicales.LEX_INF ||
                tête.catégorie == CatégoriesLexicales.LEX_EG ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_FER ||
                tête.catégorie == CatégoriesLexicales.LEX_PT_VIRG ||
                tête.catégorie == CatégoriesLexicales.LEX_FIN ||
                tête.catégorie == CatégoriesLexicales.LEX_AFF ||
                tête.catégorie == CatégoriesLexicales.LEX_ALORS ||
                tête.catégorie == CatégoriesLexicales.LEX_FSI ||
                tête.catégorie == CatégoriesLexicales.LEX_SINON) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_IDF ||
                tête.catégorie == CatégoriesLexicales.LEX_NBRE ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_OUV ||
                tête.catégorie == CatégoriesLexicales.LEX_NON ||
                tête.catégorie == CatégoriesLexicales.LEX_SI) {
            Facteur();
            RFacteur();
        } else {
            throw new BigLangException("Terme", "[non, (, idf, nbre, si]");
        }
        a.baliseF("Terme");
    }

    /**
     * RFacteur -> $opérandeGauche * Facteur $opérandeDroit RFacteur | $opérandeGauche / Facteur $opérandeDroit RFacteur | vide
     */
    public static void RFacteur() throws BigLangException {
        a.baliseO("RFacteur");
        if (tête.catégorie == CatégoriesLexicales.LEX_MULT) {
            Génération.opérandeGauche();
            franchir(CatégoriesLexicales.LEX_MULT, BigLangException.BL_SYNT_MULT_ATTENDU);
            Facteur();
            Génération.opérandeDroit();
            RFacteur();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_DIV) {
            Génération.opérandeGauche();
            franchir(CatégoriesLexicales.LEX_DIV, BigLangException.BL_SYNT_DIV_ATTENDU);
            Facteur();
            Génération.opérandeDroit();
            RFacteur();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_OU ||
                tête.catégorie == CatégoriesLexicales.LEX_ET ||
                tête.catégorie == CatégoriesLexicales.LEX_INF ||
                tête.catégorie == CatégoriesLexicales.LEX_EG ||
                tête.catégorie == CatégoriesLexicales.LEX_PLUS ||
                tête.catégorie == CatégoriesLexicales.LEX_MOINS ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_FER ||
                tête.catégorie == CatégoriesLexicales.LEX_PT_VIRG ||
                tête.catégorie == CatégoriesLexicales.LEX_FIN ||
                tête.catégorie == CatégoriesLexicales.LEX_AFF ||
                tête.catégorie == CatégoriesLexicales.LEX_ALORS ||
                tête.catégorie == CatégoriesLexicales.LEX_FSI ||
                tête.catégorie == CatégoriesLexicales.LEX_SINON) {
            // rien
        } else {
            throw new BigLangException("RFacteur", "[/, :=, ou, +, ;, -, ), et, sinon, <, alors, fin, =, fsi, *]");
        }
        a.baliseF("RFacteur");
    }

    /**
     * Facteur -> $opérandeGauche non Facteur $opérandeDroit | Primaire
     */
    public static void Facteur() throws BigLangException {
        a.baliseO("Facteur");
        if (tête.catégorie == CatégoriesLexicales.LEX_NON) {
            Génération.opérandeGauche();
            franchir(CatégoriesLexicales.LEX_NON, BigLangException.BL_SYNT_NON_ATTENDU);
            Facteur();
            Génération.opérandeDroit();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_IDF ||
                tête.catégorie == CatégoriesLexicales.LEX_NBRE ||
                tête.catégorie == CatégoriesLexicales.LEX_PAR_OUV ||
                tête.catégorie == CatégoriesLexicales.LEX_SI) {
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
        if (tête.catégorie == CatégoriesLexicales.LEX_IDF) {
            Génération.var();
            franchir(CatégoriesLexicales.LEX_IDF, BigLangException.BL_SYNT_IDF_ATTENDU);
        } else if (tête.catégorie == CatégoriesLexicales.LEX_NBRE) {
            Génération.nbre();
            franchir(CatégoriesLexicales.LEX_NBRE, BigLangException.BL_SYNT_NBRE_ATTENDU);
        } else if (tête.catégorie == CatégoriesLexicales.LEX_PAR_OUV) {
            franchir(CatégoriesLexicales.LEX_PAR_OUV, BigLangException.BL_SYNT_PAR_OUV_ATTENDU);
            Exp();
            franchir(CatégoriesLexicales.LEX_PAR_FER, BigLangException.BL_SYNT_PAR_FER_ATTENDU);
        } else if (tête.catégorie == CatégoriesLexicales.LEX_SI) {
            franchir(CatégoriesLexicales.LEX_SI, BigLangException.BL_SYNT_SI_ATTENDU);
            Exp();
            Génération.finCondition();
            franchir(CatégoriesLexicales.LEX_ALORS, BigLangException.BL_SYNT_ALORS_ATTENDU);
            Insts();
            RPrimaire();
            franchir(CatégoriesLexicales.LEX_FSI, BigLangException.BL_SYNT_FSI_ATTENDU);
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
        if (tête.catégorie == CatégoriesLexicales.LEX_SINON) {
            Génération.finAlors();
            franchir(CatégoriesLexicales.LEX_SINON, BigLangException.BL_SYNT_SINON_ATTENDU);
            Insts();
            Génération.finSinon();
        } else if (tête.catégorie == CatégoriesLexicales.LEX_FSI) {
            Génération.fsiSansSinon();
            // rien
        } else {
            throw new BigLangException("RPrimaire", "[sinon, fsi]");
        }
        a.baliseF("RPrimaire");
    }

    public static void main(String[] args) {
        a = new ArbreSyntaxique(args[0] + ".xml", "Prog");
        lex = new AnalyseLexicale();
        initTête(args[0]);
        Génération.initGénération(args[0]);
        AvTête();
        try {
            Prog();
        } catch (BigLangException e) {
            // e.printStackTrace();
        }
        Génération.objet.sortie();
        a.clore();
    }
}