
package biglang.lexical;
import java.util.Hashtable;
/**
 * BigLang : catégories lexicales et crible
 * @author syntaxique.FaireCatégoriesLexicales
 *
 */
public class CatégoriesLexicales {
    public static final int LEX_AFF             = 0 ;
    public static final int LEX_PT_VIRG         = 1 ;
    public static final int LEX_VIRG            = 2 ;
    public static final int LEX_PT              = 3 ;
    public static final int LEX_PAR_OUV         = 4 ;
    public static final int LEX_PAR_FER         = 5 ;
    public static final int LEX_PLUS            = 6 ;
    public static final int LEX_MOINS           = 7 ;
    public static final int LEX_MULT            = 8 ;
    public static final int LEX_DIV             = 9 ;
    public static final int LEX_AFF_ADD         = 10 ;
    public static final int LEX_EG              = 11 ;
    public static final int LEX_INF             = 12 ;
    public static final int LEX_INF_EG          = 14 ;
    public static final int LEX_SUP             = 15 ;
    public static final int LEX_SUP_EG          = 16 ;
    public static final int LEX_ERR             = 17 ;
    public static final int LEX_NBRE            = 18 ;
    public static final int LEX_IDF             = 19 ;
    public static final int LEX_SI              = 20 ;
    public static final int LEX_ALORS           = 21 ;
    public static final int LEX_SINON           = 22 ;
    public static final int LEX_FSI             = 23 ;
    public static final int LEX_DEBUT           = 24 ;
    public static final int LEX_FIN             = 25 ;
    public static final int LEX_ENTIER          = 26 ;
    public static final int LEX_BOOLEEN         = 27 ;
    public static final int LEX_LIRE            = 28 ;
    public static final int LEX_ECRIRE          = 29 ;
    public static final int LEX_ET              = 30 ;
    public static final int LEX_OU              = 31 ;
    public static final int LEX_NON             = 32 ;
    // ajouté
    public static final int LEX_TANTQUE              = 33 ;
    public static final int LEX_FINTANTQUE            = 34 ;
    
    static Hashtable créerCrible () {
        Hashtable c = new Hashtable () ;
        c.put ("si", new Integer(LEX_SI)) ;
        c.put ("alors", new Integer(LEX_ALORS)) ;
        c.put ("sinon", new Integer(LEX_SINON)) ;
        c.put ("fsi", new Integer(LEX_FSI)) ;
        c.put ("debut", new Integer(LEX_DEBUT)) ;
        c.put ("fin", new Integer(LEX_FIN)) ;
        c.put ("entier", new Integer(LEX_ENTIER)) ;
        c.put ("booleen", new Integer(LEX_BOOLEEN)) ;
        c.put ("lire", new Integer(LEX_LIRE)) ;
        c.put ("ecrire", new Integer(LEX_ECRIRE)) ;
        c.put ("et", new Integer(LEX_ET)) ;
        c.put ("ou", new Integer(LEX_OU)) ;
        c.put ("non", new Integer(LEX_NON)) ;
        //ajouté
        c.put ("tantque", new Integer(LEX_TANTQUE)) ;
        c.put ("fintantque", new Integer(LEX_FINTANTQUE)) ;
        return c;
    }
    public static Hashtable crible = créerCrible() ;
    public static String [] enClair = {
        "AFF",
        "PT_VIRG",
        "VIRG",
        "PT",
        "PAR_OUV",
        "PAR_FER",
        "PLUS",
        "MOINS",
        "MULT",
        "DIV",
        "AFF_ADD",
        "EG",
        "INF",
        "INF",
        "INF_EG",
        "SUP",
        "SUP_EG",
        "ERR",
        "NBRE",
        "IDF",
        "SI",
        "ALORS",
        "SINON",
        "FSI",
        "DEBUT",
        "FIN",
        "ENTIER",
        "BOOLEEN",
        "LIRE",
        "ECRIRE",
        "ET",
        "OU",
        "NON",
		"TANTQUE",
		"FINTANTQUE"

    };
}
