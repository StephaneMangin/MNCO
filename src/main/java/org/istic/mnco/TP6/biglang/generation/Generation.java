/*
 * Created on 14 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.istic.mnco.TP6.biglang.generation;

import org.istic.mnco.TP6.biglang.BigLangException;
import org.istic.mnco.TP6.biglang.syntaxique.AnalyseSyntaxique;
import org.istic.mnco.TP6.bigmach.CodesInstructions;

import java.util.Hashtable;
import java.util.Stack;


/**
 * BigLang : analyse sémantique et génération
 */
public class Generation {
    static final int TYPE_ENTIER = 0;
    static final int TYPE_BOOLEEN = 1;
    static final int TYPE_NEUTRE = 2;
    static final int TYPE_INEXISTANT = 3;
    static final String[] typesEnClair = {"entier", "booléen", "neutre", "inexistant"};
    /**
     * tampon de production du code objet
     */
    public static CodeObjet objet;
    /**
     * table des symboles : les éléments sont des <code>Description</code>
     */
    public static Hashtable tableDesSymboles;
    public static Hashtable tableDesOperateurs = initTableDesOperateurs();
    public static Stack pileDesOperateursVus = new Stack();
    public static Stack pileDesTypesVus = new Stack();
    public static Stack pileDesReperes = new Stack();
    public static Stack pileDesReprises = new Stack();
    /**
     * Compteur d'emplacements. désigne le prochain emplacement libre dans le code objet.
     */
    static int Ce;
    /**
     * Compteur de réservation des variables. Désigne la prochaine adresse libre pour implanter une variable.
     */
    static int adrVariables;
    /**
     * type de la constrauction en cours de traitement
     */
    static int typeCourant;
    /**
     * indique si la construction courante est un repère
     */
    static boolean cEstUnRepere;

    private static Hashtable initTableDesOperateurs() {
        Hashtable laTable = new Hashtable();
        laTable.put(
                "non",
                new Operateur(
                        CodesInstructions.CODE_NON,
                        TYPE_INEXISTANT, TYPE_BOOLEEN, TYPE_BOOLEEN,
                        true, true,
                        false,
                        "non")
        );
        laTable.put(
                "*",
                new Operateur(
                        CodesInstructions.CODE_MULT,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
                        true, true,
                        false,
                        "*")
        );
        laTable.put(
                "/",
                new Operateur(
                        CodesInstructions.CODE_DIV,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
                        true, true,
                        false,
                        "/")
        );
        laTable.put(
                "+",
                new Operateur(
                        CodesInstructions.CODE_PLUS,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
                        true, true,
                        false,
                        "+")
        );
        laTable.put(
                "-",
                new Operateur(
                        CodesInstructions.CODE_MOINS,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
                        true, true,
                        false,
                        "-")
        );
        laTable.put(
                "<",
                new Operateur(
                        CodesInstructions.CODE_INF,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_BOOLEEN,
                        true, true,
                        false,
                        "<")
        );
        laTable.put(
                "=",
                new Operateur(
                        CodesInstructions.CODE_EG,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_BOOLEEN,
                        true, true,
                        false,
                        "=")
        );
        laTable.put(
                "ou",
                new Operateur(
                        CodesInstructions.CODE_OU,
                        TYPE_BOOLEEN, TYPE_BOOLEEN, TYPE_BOOLEEN,
                        true, true,
                        false,
                        "ou")
        );
        laTable.put(
                "et",
                new Operateur(
                        CodesInstructions.CODE_ET,
                        TYPE_BOOLEEN, TYPE_BOOLEEN, TYPE_BOOLEEN,
                        true, true,
                        false,
                        "et")
        );
        return laTable;
    }

    /**
     * @param n : nom du fichier objet (sans l'extension)
     */
    public static void initGeneration(String n) {
        Ce = 0;
        adrVariables = 0;
        objet = new CodeObjet(n);
        tableDesSymboles = new Hashtable();
    }

    //
    //
    // Analyse des déclarations
    //
    //
    public static void defVar() throws BigLangException {
        if (tableDesSymboles.get(AnalyseSyntaxique.tete.unite) != null) {
            throw new BigLangException(AnalyseSyntaxique.tete.unite, BigLangException.BL_SEM, BigLangException.BL_SEM_DLE_DECL);
        }
        tableDesSymboles.put(
                AnalyseSyntaxique.tete.unite,
                new Description(typeCourant, adrVariables)
        );
        adrVariables++;
    }

    public static void defVarEnt() throws BigLangException {
        typeCourant = TYPE_ENTIER;
        defVar();
    }

    public static void defVarBool() throws BigLangException {
        typeCourant = TYPE_BOOLEEN;
        defVar();
    }

    public static void finDefVar() throws BigLangException {
        objet.modifie(Ce, CodesInstructions.CODE_SOMMETA);
        Ce++;
        objet.modifie(Ce, adrVariables - 1);
        Ce++;
        typeCourant = TYPE_NEUTRE;
    }

    //
    //
    // Entrées-sorties
    //
    //
    public static void lire() throws BigLangException {
        Description d = (Description) tableDesSymboles.get(AnalyseSyntaxique.tete.unite);
        if (d == null) {
            throw new BigLangException(AnalyseSyntaxique.tete.unite, BigLangException.BL_SEM, BigLangException.BL_SEM_IDF_NON_DECL);
        }
        String enClair = AnalyseSyntaxique.tete.unite + "    ";
        int enAscii = 0;
        for (int i = 0; i < 4; i++) {
            enAscii = enAscii * 256 + enClair.charAt(i);
        }
        objet.modifie(Ce, CodesInstructions.CODE_EMPILER);
        Ce++;
        objet.modifie(Ce, d.adr);
        Ce++;
        objet.modifie(Ce, CodesInstructions.CODE_LIRE);
        Ce++;
        objet.modifie(Ce, enAscii);
        Ce++;
        objet.modifie(Ce, CodesInstructions.CODE_AFFECTER);
        Ce++;

        typeCourant = d.type;
        cEstUnRepere = false;

    }

    public static void ecrire() throws BigLangException {
        Description d = (Description) tableDesSymboles.get(AnalyseSyntaxique.tete.unite);
        if (d == null) {
            throw new BigLangException(AnalyseSyntaxique.tete.unite, BigLangException.BL_SEM, BigLangException.BL_SEM_IDF_NON_DECL);
        }

        String enClair = AnalyseSyntaxique.tete.unite + "    ";
        int enAscii = 0;
        for (int i = 0; i < 4; i++) {
            enAscii = enAscii * 256 + enClair.charAt(i);
        }

        objet.modifie(Ce, CodesInstructions.CODE_EMPILER);
        Ce++;
        objet.modifie(Ce, d.adr);
        Ce++;
        objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
        Ce++;
        objet.modifie(Ce, CodesInstructions.CODE_ECRIRE);
        Ce++;
        objet.modifie(Ce, enAscii);
        Ce++;

        typeCourant = d.type;
        cEstUnRepere = false;

    }

    //
    //
    // Primaires
    //
    //
    public static void neutraliser() throws BigLangException {
        if (typeCourant != TYPE_NEUTRE) {
            objet.modifie(Ce, CodesInstructions.CODE_DEPILER);
            Ce++;
        }
        typeCourant = TYPE_NEUTRE;
    }

    public static void var() throws BigLangException {
        Description d = (Description) tableDesSymboles.get(AnalyseSyntaxique.tete.unite);
        if (d == null) {
            throw new BigLangException(AnalyseSyntaxique.tete.unite, BigLangException.BL_SEM, BigLangException.BL_SEM_IDF_NON_DECL);
        }
        cEstUnRepere = true;
        typeCourant = d.type;

        objet.modifie(Ce, CodesInstructions.CODE_EMPILER);
        Ce++;
        objet.modifie(Ce, d.adr);
        Ce++;

    }

    public static void nbre() throws BigLangException {
        cEstUnRepere = false;
        typeCourant = TYPE_ENTIER;

        objet.modifie(Ce, CodesInstructions.CODE_EMPILER);
        Ce++;
        objet.modifie(Ce, Integer.parseInt(AnalyseSyntaxique.tete.unite));
        Ce++;
    }

    //
    //
    // Prélude et postlude
    //
    //
    public static void debut() throws BigLangException {

    }

    public static void fin() throws BigLangException {
        String fin = "fini";
        objet.modifie(Ce, CodesInstructions.CODE_ECRIRE);
        Ce++;
        objet.modifie(Ce, (('f' * 256 + 'i') * 256 + 'n') * 256 + 'i');
        Ce++;
        objet.modifie(Ce, CodesInstructions.CODE_STOP);
        Ce++;
        objet.sortie();
    }

    //
    //
    // Opérateurs
    //
    //
    public static void genNon() throws BigLangException {
        if (cEstUnRepere) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
        }
        cEstUnRepere = false;
        if (typeCourant == TYPE_BOOLEEN) {
            objet.modifie(Ce, CodesInstructions.CODE_NON);
            Ce++;
        } else {
            throw new BigLangException(AnalyseSyntaxique.tete.unite, BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_BOOL_ATTENDU);
        }
    }

    public static void operandeGauche() throws BigLangException {
        Operateur o = (Operateur) tableDesOperateurs.get(AnalyseSyntaxique.tete.unite);
        if (o.typeGauche != TYPE_INEXISTANT && o.valeurAGauche && cEstUnRepere) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
            cEstUnRepere = false;
        }
        if (o.typeGauche != TYPE_INEXISTANT && o.typeGauche != typeCourant) {
            throw new BigLangException(o.enClair + ": " + typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_GAUCHE_NON_CONFORME);
        }
        pileDesOperateursVus.push(o);
    }

    public static void operandeDroit() throws BigLangException {
        Operateur o = (Operateur) pileDesOperateursVus.pop();
        if (o.typeDroit != TYPE_INEXISTANT && o.valeurADroite && cEstUnRepere) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
            cEstUnRepere = false;
        }
        if (o.typeDroit != TYPE_INEXISTANT && o.typeDroit != typeCourant) {
            throw new BigLangException(o.enClair + ": " + typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_DROIT_NON_CONFORME);
        }

        objet.modifie(Ce, o.code);
        Ce++;

        cEstUnRepere = o.leResultatEstUnRepere;
        typeCourant = o.typeResultat;
    }

    public static void operandeGaucheAff() throws BigLangException {
        if (!cEstUnRepere) {
            throw new BigLangException(BigLangException.BL_SEM, BigLangException.BL_SEM_VAL_A_GAUCHE_AFF);
        }
        pileDesTypesVus.push(new Integer(typeCourant));
    }

    public static void operandeDroitAff() throws BigLangException {
        int typeAGauche = ((Integer) pileDesTypesVus.pop()).intValue();
        if (typeCourant != typeAGauche) {
            throw new BigLangException("affectation : " + typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_DROIT_NON_CONFORME);
        }
        if (cEstUnRepere) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
            cEstUnRepere = false;
        }
        objet.modifie(Ce, CodesInstructions.CODE_AFFECTER);
        Ce++;
        cEstUnRepere = false;
    }

    public static void finCondition() throws BigLangException {
        if (typeCourant != TYPE_BOOLEEN) {
            throw new BigLangException("si : " + typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_BOOL_ATTENDU);
        }
        if (cEstUnRepere) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
            cEstUnRepere = false;
        }
        objet.modifie(Ce, CodesInstructions.CODE_BSF);
        Ce++;
        pileDesReprises.push(new Integer(Ce));
        objet.modifie(Ce, -1);
        Ce++;
    }

    public static void finAlors() throws BigLangException {
        int adFinCondition = ((Integer) pileDesReprises.pop()).intValue();

        objet.modifie(Ce, CodesInstructions.CODE_NOP);
        Ce++;    // pour évent. netraliser ou dérepérer

        objet.modifie(Ce, CodesInstructions.CODE_BR);
        Ce++;
        pileDesReprises.push(new Integer(Ce));
        objet.modifie(Ce, -1);
        Ce++;

        objet.modifie(adFinCondition, Ce);

        pileDesTypesVus.push(new Integer(typeCourant));
        pileDesReperes.push(new Boolean(cEstUnRepere));
    }
    //
    //
    // Conditionnelles
    //
    //

    public static void finSinon() throws BigLangException {
        boolean alorsEstUnRep = ((Boolean) pileDesReperes.pop()).booleanValue();
        int typeAlors = ((Integer) pileDesTypesVus.pop()).intValue();
        int adFinAlors = ((Integer) pileDesReprises.pop()).intValue();

        if (alorsEstUnRep && !cEstUnRepere) {
            objet.modifie(adFinAlors - 2, CodesInstructions.CODE_VALEUR);
        }
        if (!alorsEstUnRep && cEstUnRepere) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
            cEstUnRepere = false;
        }

        switch (typeAlors) {
            case TYPE_NEUTRE:                    // neutraliser le sinon
                if (typeCourant != TYPE_NEUTRE) {
                    objet.modifie(Ce, CodesInstructions.CODE_DEPILER);
                    Ce++;
                    typeCourant = TYPE_NEUTRE;
                    cEstUnRepere = false;
                }
                break;
            case TYPE_BOOLEEN:
                switch (typeCourant) {
                    case TYPE_NEUTRE:                // neutraliser le alors
                        objet.modifie(adFinAlors - 2, CodesInstructions.CODE_DEPILER);
                        break;
                    case TYPE_BOOLEEN:
                        break;                            // types conformes et équilibrés
                    case TYPE_ENTIER:
                        throw new BigLangException("alors/sinon : " + typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPES_NON_CONFORMES);
                    default:
                        break;
                }
                break;
            case TYPE_ENTIER:
                switch (typeAlors) {
                    case TYPE_NEUTRE:                // neutraliser le alors
                        objet.modifie(adFinAlors - 2, CodesInstructions.CODE_DEPILER);
                        break;
                    case TYPE_BOOLEEN:
                        throw new BigLangException("alors/sinon : " + typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPES_NON_CONFORMES);
                    case TYPE_ENTIER:
                        break;                            // types conformes et équilibrés
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        objet.modifie(adFinAlors, Ce);
    }

    public static void fsiSansSinon() throws BigLangException {
        if (typeCourant != TYPE_NEUTRE) {
            objet.modifie(Ce, CodesInstructions.CODE_DEPILER);
            Ce++;
            typeCourant = TYPE_NEUTRE;
            cEstUnRepere = false;
        }

        int adFinCondition = ((Integer) pileDesReprises.pop()).intValue();
        objet.modifie(adFinCondition, Ce);
    }

    /**
     * Elément de table des symboles :
     * <p>
     * Pour chaque variable on mémorise :
     * <ul><li>son type</li>
     * <li>son adresse d'implantation</li></ul>
     * </p>
     *
     * @author herman
     */
    static class Description {
        int type;
        int adr;
        /**
         * @param type : type du symbole
         * @param adr  : adresse d'implantation
         */
        public Description(int type, int adr) {
            this.type = type;
            this.adr = adr;
        }

        public String toString() {
            String t;
            switch (type) {
                case TYPE_ENTIER:
                    t = "entier";
                    break;
                case TYPE_BOOLEEN:
                    t = "booléen";
                    break;
                default:
                    t = "inconnu";
                    break;
            }
            return "<" + t + ", " + adr + ">";
        }
    }

    static class Operateur {
        int code;
        int typeGauche;
        int typeDroit;
        int typeResultat;
        boolean valeurAGauche;
        boolean valeurADroite;
        boolean leResultatEstUnRepere;

        String enClair;

        public Operateur(int c, int g, int d, int r, boolean vg, boolean vd, boolean rr, String o) {
            this.code = c;
            this.typeGauche = g;
            this.typeDroit = d;
            this.typeResultat = r;
            this.valeurAGauche = vg;
            this.valeurADroite = vd;
            this.leResultatEstUnRepere = rr;
            this.enClair = o;
        }
    }
}
