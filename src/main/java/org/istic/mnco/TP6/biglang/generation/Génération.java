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
public class Génération {
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
    public static Hashtable tableDesOpérateurs = initTableDesOpérateurs();
    public static Stack pileDesOpérateursVus = new Stack();
    public static Stack pileDesTypesVus = new Stack();
    public static Stack pileDesRepères = new Stack();
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
    static boolean cEstUnRepère;

    private static Hashtable initTableDesOpérateurs() {
        Hashtable laTable = new Hashtable();
        laTable.put(
                "non",
                new Opérateur(
                        CodesInstructions.CODE_NON,
                        TYPE_INEXISTANT, TYPE_BOOLEEN, TYPE_BOOLEEN,
                        true, true,
                        false,
                        "non")
        );
        laTable.put(
                "*",
                new Opérateur(
                        CodesInstructions.CODE_MULT,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
                        true, true,
                        false,
                        "*")
        );
        laTable.put(
                "/",
                new Opérateur(
                        CodesInstructions.CODE_DIV,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
                        true, true,
                        false,
                        "/")
        );
        laTable.put(
                "+",
                new Opérateur(
                        CodesInstructions.CODE_PLUS,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
                        true, true,
                        false,
                        "+")
        );
        laTable.put(
                "-",
                new Opérateur(
                        CodesInstructions.CODE_MOINS,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_ENTIER,
                        true, true,
                        false,
                        "-")
        );
        laTable.put(
                "<",
                new Opérateur(
                        CodesInstructions.CODE_INF,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_BOOLEEN,
                        true, true,
                        false,
                        "<")
        );
        laTable.put(
                "=",
                new Opérateur(
                        CodesInstructions.CODE_EG,
                        TYPE_ENTIER, TYPE_ENTIER, TYPE_BOOLEEN,
                        true, true,
                        false,
                        "=")
        );
        laTable.put(
                "ou",
                new Opérateur(
                        CodesInstructions.CODE_OU,
                        TYPE_BOOLEEN, TYPE_BOOLEEN, TYPE_BOOLEEN,
                        true, true,
                        false,
                        "ou")
        );
        laTable.put(
                "et",
                new Opérateur(
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
    public static void initGénération(String n) {
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
        if (tableDesSymboles.get(AnalyseSyntaxique.tête.unité) != null) {
            throw new BigLangException(AnalyseSyntaxique.tête.unité, BigLangException.BL_SEM, BigLangException.BL_SEM_DLE_DECL);
        }
        tableDesSymboles.put(
                AnalyseSyntaxique.tête.unité,
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
        Description d = (Description) tableDesSymboles.get(AnalyseSyntaxique.tête.unité);
        if (d == null) {
            throw new BigLangException(AnalyseSyntaxique.tête.unité, BigLangException.BL_SEM, BigLangException.BL_SEM_IDF_NON_DECL);
        }
        String enClair = AnalyseSyntaxique.tête.unité + "    ";
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
        cEstUnRepère = false;

    }

    public static void ecrire() throws BigLangException {
        Description d = (Description) tableDesSymboles.get(AnalyseSyntaxique.tête.unité);
        if (d == null) {
            throw new BigLangException(AnalyseSyntaxique.tête.unité, BigLangException.BL_SEM, BigLangException.BL_SEM_IDF_NON_DECL);
        }

        String enClair = AnalyseSyntaxique.tête.unité + "    ";
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
        objet.modifie(Ce, CodesInstructions.CODE_ÉCRIRE);
        Ce++;
        objet.modifie(Ce, enAscii);
        Ce++;

        typeCourant = d.type;
        cEstUnRepère = false;

    }

    //
    //
    // Primaires
    //
    //
    public static void neutraliser() throws BigLangException {
        if (typeCourant != TYPE_NEUTRE) {
            objet.modifie(Ce, CodesInstructions.CODE_DÉPILER);
            Ce++;
        }
        typeCourant = TYPE_NEUTRE;
    }

    public static void var() throws BigLangException {
        Description d = (Description) tableDesSymboles.get(AnalyseSyntaxique.tête.unité);
        if (d == null) {
            throw new BigLangException(AnalyseSyntaxique.tête.unité, BigLangException.BL_SEM, BigLangException.BL_SEM_IDF_NON_DECL);
        }
        cEstUnRepère = true;
        typeCourant = d.type;

        objet.modifie(Ce, CodesInstructions.CODE_EMPILER);
        Ce++;
        objet.modifie(Ce, d.adr);
        Ce++;

    }

    public static void nbre() throws BigLangException {
        cEstUnRepère = false;
        typeCourant = TYPE_ENTIER;

        objet.modifie(Ce, CodesInstructions.CODE_EMPILER);
        Ce++;
        objet.modifie(Ce, Integer.parseInt(AnalyseSyntaxique.tête.unité));
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
        objet.modifie(Ce, CodesInstructions.CODE_ÉCRIRE);
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
        if (cEstUnRepère) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
        }
        cEstUnRepère = false;
        if (typeCourant == TYPE_BOOLEEN) {
            objet.modifie(Ce, CodesInstructions.CODE_NON);
            Ce++;
        } else {
            throw new BigLangException(AnalyseSyntaxique.tête.unité, BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_BOOL_ATTENDU);
        }
    }

    public static void opérandeGauche() throws BigLangException {
        Opérateur o = (Opérateur) tableDesOpérateurs.get(AnalyseSyntaxique.tête.unité);
        if (o.typeGauche != TYPE_INEXISTANT && o.valeurAGauche && cEstUnRepère) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
            cEstUnRepère = false;
        }
        if (o.typeGauche != TYPE_INEXISTANT && o.typeGauche != typeCourant) {
            throw new BigLangException(o.enClair + ": " + typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_GAUCHE_NON_CONFORME);
        }
        pileDesOpérateursVus.push(o);
    }

    public static void opérandeDroit() throws BigLangException {
        Opérateur o = (Opérateur) pileDesOpérateursVus.pop();
        if (o.typeDroit != TYPE_INEXISTANT && o.valeurADroite && cEstUnRepère) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
            cEstUnRepère = false;
        }
        if (o.typeDroit != TYPE_INEXISTANT && o.typeDroit != typeCourant) {
            throw new BigLangException(o.enClair + ": " + typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_DROIT_NON_CONFORME);
        }

        objet.modifie(Ce, o.code);
        Ce++;

        cEstUnRepère = o.leRésultatEstUnRepère;
        typeCourant = o.typeRésultat;
    }

    public static void opérandeGaucheAff() throws BigLangException {
        if (!cEstUnRepère) {
            throw new BigLangException(BigLangException.BL_SEM, BigLangException.BL_SEM_VAL_A_GAUCHE_AFF);
        }
        pileDesTypesVus.push(new Integer(typeCourant));
    }

    public static void opérandeDroitAff() throws BigLangException {
        int typeAGauche = ((Integer) pileDesTypesVus.pop()).intValue();
        if (typeCourant != typeAGauche) {
            throw new BigLangException("affectation : " + typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_DROIT_NON_CONFORME);
        }
        if (cEstUnRepère) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
            cEstUnRepère = false;
        }
        objet.modifie(Ce, CodesInstructions.CODE_AFFECTER);
        Ce++;
        cEstUnRepère = false;
    }

    public static void finCondition() throws BigLangException {
        if (typeCourant != TYPE_BOOLEEN) {
            throw new BigLangException("si : " + typesEnClair[typeCourant], BigLangException.BL_SEM, BigLangException.BL_SEM_TYPE_BOOL_ATTENDU);
        }
        if (cEstUnRepère) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
            cEstUnRepère = false;
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
        pileDesRepères.push(new Boolean(cEstUnRepère));
    }
    //
    //
    // Conditionnelles
    //
    //

    public static void finSinon() throws BigLangException {
        boolean alorsEstUnRep = ((Boolean) pileDesRepères.pop()).booleanValue();
        int typeAlors = ((Integer) pileDesTypesVus.pop()).intValue();
        int adFinAlors = ((Integer) pileDesReprises.pop()).intValue();

        if (alorsEstUnRep && !cEstUnRepère) {
            objet.modifie(adFinAlors - 2, CodesInstructions.CODE_VALEUR);
        }
        if (!alorsEstUnRep && cEstUnRepère) {
            objet.modifie(Ce, CodesInstructions.CODE_VALEUR);
            Ce++;
            cEstUnRepère = false;
        }

        switch (typeAlors) {
            case TYPE_NEUTRE:                    // neutraliser le sinon
                if (typeCourant != TYPE_NEUTRE) {
                    objet.modifie(Ce, CodesInstructions.CODE_DÉPILER);
                    Ce++;
                    typeCourant = TYPE_NEUTRE;
                    cEstUnRepère = false;
                }
                break;
            case TYPE_BOOLEEN:
                switch (typeCourant) {
                    case TYPE_NEUTRE:                // neutraliser le alors
                        objet.modifie(adFinAlors - 2, CodesInstructions.CODE_DÉPILER);
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
                        objet.modifie(adFinAlors - 2, CodesInstructions.CODE_DÉPILER);
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
            objet.modifie(Ce, CodesInstructions.CODE_DÉPILER);
            Ce++;
            typeCourant = TYPE_NEUTRE;
            cEstUnRepère = false;
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

    static class Opérateur {
        int code;
        int typeGauche;
        int typeDroit;
        int typeRésultat;
        boolean valeurAGauche;
        boolean valeurADroite;
        boolean leRésultatEstUnRepère;

        String enClair;

        public Opérateur(int c, int g, int d, int r, boolean vg, boolean vd, boolean rr, String o) {
            this.code = c;
            this.typeGauche = g;
            this.typeDroit = d;
            this.typeRésultat = r;
            this.valeurAGauche = vg;
            this.valeurADroite = vd;
            this.leRésultatEstUnRepère = rr;
            this.enClair = o;
        }
    }
}
