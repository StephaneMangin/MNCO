/*
 * Created on 31 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.istic.mnco.TP6.biglang;

/**
 * Gestion des messages d'erreur BigLang : lexical, syntaxique et s�mantique
 */
public class BigLangException extends Exception {
    public static final int BL_LEX = 0;
    public static final int BL_LEX_UNITE_NON_RECONNUE = 0;

    public static final int BL_SYNT = 1;
    public static final int BL_SYNT_GENERIQUE = 0;
    public static final int BL_SYNT_DEBUT_ATTENDU = 1;
    public static final int BL_SYNT_FIN_ATTENDU = 2;
    public static final int BL_SYNT_DECL_ATTENDUE = 3;
    public static final int BL_SYNT_PT_VIRG_ATTENDU = 4;
    public static final int BL_SYNT_ENTIER_ATTENDU = 5;
    public static final int BL_SYNT_IDF_ATTENDU = 6;
    public static final int BL_SYNT_BOOLEEN_ATTENDU = 7;
    public static final int BL_SYNT_VIRG_ATTENDU = 8;
    public static final int BL_SYNT_LIRE_ATTENDU = 9;
    public static final int BL_SYNT_ECRIRE_ATTENDU = 10;
    public static final int BL_SYNT_NBRE_ATTENDU = 11;
    public static final int BL_SYNT_PAR_OUV_ATTENDU = 12;
    public static final int BL_SYNT_PAR_FER_ATTENDU = 13;
    public static final int BL_SYNT_NON_ATTENDU = 14;
    public static final int BL_SYNT_MULT_ATTENDU = 15;
    public static final int BL_SYNT_DIV_ATTENDU = 16;
    public static final int BL_SYNT_PLUS_ATTENDU = 17;
    public static final int BL_SYNT_MOINS_ATTENDU = 18;
    public static final int BL_SYNT_INF_ATTENDU = 19;
    public static final int BL_SYNT_EG_ATTENDU = 20;
    public static final int BL_SYNT_ET_ATTENDU = 21;
    public static final int BL_SYNT_OU_ATTENDU = 22;
    public static final int BL_SYNT_AFF_ATTENDU = 23;
    public static final int BL_SYNT_SI_ATTENDU = 24;
    public static final int BL_SYNT_ALORS_ATTENDU = 25;
    public static final int BL_SYNT_SINON_ATTENDU = 26;
    public static final int BL_SYNT_FSI_ATTENDU = 27;

    public static final int BL_SEM = 2;
    public static final int BL_SEM_GENERIQUE = 0;
    public static final int BL_SEM_DLE_DECL = 1;
    public static final int BL_SEM_IDF_NON_DECL = 2;
    public static final int BL_SEM_TYPE_BOOL_ATTENDU = 3;
    public static final int BL_SEM_TYPE_GAUCHE_NON_CONFORME = 4;
    public static final int BL_SEM_TYPE_DROIT_NON_CONFORME = 5;
    public static final int BL_SEM_VAL_A_GAUCHE_AFF = 6;
    public static final int BL_SEM_TYPES_NON_CONFORMES = 7;


    public static final int BL_GEN = 3;
    public static final int BL_GEN_GENERIQUE = 0;
    public static final int BL_GEN_TAMPON_DEBORDEMENT = 1;

    public static final int BL_COM = 4;
    public static final int BL_COM_FICHIER_INEXISTANT = 0;
    private static String[] type = {"AnLex ", "AnSynt ", "AnSem", "Gen", "Commandes"};
    private static String[][] messages = {
            {// BL_LEX : analyse lexicale
                    "unite lexicale non reconnue"
            },
            {// BL_SYNT : analyse syntaxique
                    "on attend un des terminaux suivants : ",
                    "mot-cl� debut attendu",
                    "mot-cl� fin attendu",
                    "d�claration attendue",
                    "symbole ; attendu",
                    "mot-cl� entier attendu",
                    "identificateur attendu",
                    "mot-cl� booleen attendu",
                    "symbole , attendu",
                    "mot-cl� lire attendu",
                    "mot-cl� ecrire attendu",
                    "nombre attendu",
                    "symbole ( attendu",
                    "symbole ) attendu",
                    "mot-cl� non attendu",
                    "symbole * attendu",
                    "symbole / attendu",
                    "symbole + attendu",
                    "symbole - attendu",
                    "symbole < attendu",
                    "symbole = attendu",
                    "mot-cl� et attendu",
                    "mot-cl� ou attendu",
                    "symbole := attendu",
                    "mot-cl� si attendu",
                    "mot-cl� alors attendu",
                    "mot-cl� sinon attendu",
                    "mot-cl� fsi attendu"
            },
            {// BL_SEM : analyse s�mantique
                    "erreur analyse s�mantique",
                    "double d�claration",
                    "identificateur non d�clar�",
                    "type bool�en attendu",
                    "type de l'op�rande gauche non conforme",
                    "type de l'op�rande droit non conforme",
                    "valeur en partie gauche d'affectation",
                    "types non conformes"
            },
            {// BL_GEN : g�n�ration
                    "erreur de g�n�ration",
                    "d�bordement du tampon de production"
            },
            {// BL_COM : interpr�teur commandes
                    "fichier non existant"
            }
    };

    public BigLangException(int t, int num) {
        super("---- Exception BigLang <" + t + ", " + num + ">" + type[t] + messages[t][num]);
        System.out.println("\n---- Exception BigLang " + num + ": " + messages[t][num]);
    }

    public BigLangException(String ctx, int t, int num) {
        // super ("---- Exception BigLang <"+t+", "+num+">"+type[t]+"["+ctx+"]"+messages[t][num]) ;
        System.out.println("\n---- Exception BigLang <" + t + ", " + num + "> " + type[t] + " [" + ctx + "] " + messages[t][num]);
    }

    public BigLangException(String ctx, String e) {
        super("---- Exception BigLang <" + BL_SYNT + ", " + BL_SYNT_GENERIQUE + "> " + type[BL_SYNT] + "[" + ctx + "]" + messages[BL_SYNT][BL_SYNT_GENERIQUE] + e);
        System.out.println("\n---- Exception BigLang <" + BL_SYNT + ", " + BL_SYNT_GENERIQUE + "> " + type[BL_SYNT] + "[" + ctx + "]" + messages[BL_SYNT][BL_SYNT_GENERIQUE] + e);
    }

}
