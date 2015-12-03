/*
 * Created on 31 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.istic.mnco.TP6.biglang.lexical;


import org.istic.mnco.TP6.automate.Automate;
import org.istic.mnco.TP6.biglang.BigLangException;

/**
 * Analyseur lexical de BigLang : étend l'automate générique du
 * package <code>automate</code>
 */
public class AnalyseLexicale extends Automate {
    static final int nEtats = 14;
    static final int tVocabulaire = 256;
    static final int dernier = nEtats - 1;
    public TêteDeLecture lexLu;
    char[] séparateurs = {' ', '\n', '\r', '\t'};

    public AnalyseLexicale() {
        lexLu = new TêteDeLecture();
        états = new int[nEtats];
        transitions = new int[nEtats][tVocabulaire];
        actions = new Action[nEtats][tVocabulaire];

//		 transitions par défaut
        for (int i = 1; i < nEtats; i++) {
            for (int j = 0; j < tVocabulaire; j++) {
                transitions[i][j] = dernier;
                actions[i][j] = new ActionVide();
            }
        }
//		 transitions à partir de 0, en erreur par défaut
        for (int j = 0; j < tVocabulaire; j++) {
            transitions[0][j] = dernier;
            actions[0][j] = new Action() {
                public void executer() throws BigLangException {
                    throw new BigLangException(BigLangException.BL_LEX, BigLangException.BL_LEX_UNITE_NON_RECONNUE);
                }
            };
        }
// transitions sur les lettres
        for (int j = 'a'; j <= 'z'; j++) {
            transitions[0][j] = 1;
            actions[0][j] = new Action() {
                public void executer() {
                    lexLu.catégorie = CatégoriesLexicales.LEX_IDF;
                    lexLu.unité = "" + (char) tête;
                }
            };
            transitions[1][j] = 1;
            actions[1][j] = new Action() {
                public void executer() {
                    lexLu.unité = lexLu.unité + (char) tête;
                }
            };
        }
//		 transitions sur les chiffres
        for (int j = (int) '0'; j <= (int) '9'; j++) {
            transitions[1][j] = 1;
            actions[1][j] = new Action() {
                public void executer() {
                    lexLu.unité = lexLu.unité + (char) tête;
                }
            };
            transitions[0][j] = 2;
            actions[0][j] = new Action() {
                public void executer() {
                    lexLu.catégorie = CatégoriesLexicales.LEX_NBRE;
                    lexLu.unité = "" + (char) tête;
                }
            };
            transitions[2][j] = 2;
            actions[2][j] = new Action() {
                public void executer() {
                    lexLu.unité = lexLu.unité + (char) tête;
                }
            };
        }
        //	transition sur :
        transitions[0][':'] = 3;
        actions[0][':'] = new Action() {
            public void executer() {
                lexLu.unité = "" + (char) tête;
            }
        };
//		 transitions à partir de 3, en erreur par défaut
        for (int j = 0; j < tVocabulaire; j++) {
            transitions[3][j] = dernier;
            actions[3][j] = new Action() {
                public void executer() throws BigLangException {
                    throw new BigLangException(BigLangException.BL_LEX, BigLangException.BL_LEX_UNITE_NON_RECONNUE);
                }
            };
        }
        //		transitions sur =
        transitions[3]['='] = 4;
        actions[3]['='] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_AFF;
                lexLu.unité = lexLu.unité + (char) tête;
            }
        };

        transitions[0]['='] = 5;
        actions[0]['='] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_EG;
                lexLu.unité = "" + (char) tête;
            }
        };
//		transitions sur ;
        transitions[0][';'] = 5;
        actions[0][';'] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_PT_VIRG;
                lexLu.unité = "" + (char) tête;
            }
        };
//		transitions sur ,
        transitions[0][','] = 5;
        actions[0][','] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_VIRG;
                lexLu.unité = "" + (char) tête;
            }
        };
//		transitions sur .
        transitions[0]['.'] = 5;
        actions[0]['.'] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_PT;
                lexLu.unité = "" + (char) tête;
            }
        };
//		transitions sur (
        transitions[0]['('] = 5;
        actions[0]['('] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_PAR_OUV;
                lexLu.unité = "" + (char) tête;
            }
        };
//		transitions sur )
        transitions[0][')'] = 5;
        actions[0][')'] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_PAR_FER;
                lexLu.unité = "" + (char) tête;
            }
        };
//		transitions sur +
        transitions[0]['+'] = 6;
        actions[0]['+'] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_PLUS;
                lexLu.unité = "" + (char) tête;
            }
        };

        //poursuite par :=

        transitions[6][':'] = 7;
        actions[6][':'] = new Action() {
            public void executer() {

                lexLu.unité = lexLu.unité + (char) tête;
            }
        };

        //transitions à partirde 7 en erreur pardéfaut
        for (int j = 0; j < tVocabulaire; j++) {
            transitions[7][j] = dernier;
            actions[7][j] = new Action() {
                public void executer() throws BigLangException {
                    throw new BigLangException(BigLangException.BL_LEX, BigLangException.BL_LEX_UNITE_NON_RECONNUE);
                }
            };
        }
        transitions[7]['='] = 8;
        actions[7]['='] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_AFF_ADD;
                lexLu.unité = lexLu.unité + (char) tête;
            }
        };


//		transitions sur -
        transitions[0]['-'] = 5;
        actions[0]['-'] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_MOINS;
                lexLu.unité = "" + (char) tête;
            }
        };
//		transitions sur *
        transitions[0]['*'] = 5;
        actions[0]['*'] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_MULT;
                lexLu.unité = "" + (char) tête;
            }
        };
//		transitions sur /
        transitions[0]['/'] = 5;
        actions[0]['/'] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_DIV;
                lexLu.unité = "" + (char) tête;
            }
        };
//		transitions sur <
        transitions[0]['<'] = 11;
        actions[0]['<'] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_INF;
                lexLu.unité = "" + (char) tête;
            }
        };
// nouveau <= : transition sur = depuis 11
        transitions[11]['='] = 12;
        actions[11]['='] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_INF_EG;
                lexLu.unité = lexLu.unité + (char) tête;
            }
        };


//		nouveau transitions sur >
        transitions[0]['>'] = 9;
        actions[0]['>'] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_SUP;
                lexLu.unité = "" + (char) tête;
            }
        };
// nouveau >= : transition sur = depuis 8
        transitions[9]['='] = 10;
        actions[9]['='] = new Action() {
            public void executer() {
                lexLu.catégorie = CatégoriesLexicales.LEX_SUP_EG;
                lexLu.unité = lexLu.unité + (char) tête;
            }
        };

        //		transitions sur les séparateurs
        for (int j = 0; j < séparateurs.length; j++) {
            transitions[0][séparateurs[j]] = 0;
            actions[0][séparateurs[j]] = new ActionVide();
        }
        for (int i = 2; i < nEtats; i++) {
            if ((i != 3) && (i != 7)) {
                for (int j = 0; j < séparateurs.length; j++) {
                    transitions[i][séparateurs[j]] = dernier;
                    actions[i][séparateurs[j]] = new ActionVide();
                }
            }
        }

// états finaux
        estFinal = new boolean[nEtats];
        for (int i = 0; i < dernier; i++) {
            estFinal[i] = false;
        }
        estFinal[dernier] = true;
// condition spéciale d'arret
        cEst = new ConditionDArret() {
            public boolean fini() {
                return estFinal[étatCourant];
            }
        };
// action finale
        actionFinale = new Action() {
            public void executer() {
                if (estFinal[étatCourant]) {
                    // passage au crible
                    Integer n = (Integer) CatégoriesLexicales.crible.get(lexLu.unité);
                    if (n != null) {
                        lexLu.catégorie = n.intValue();
                    }
                    // System.out.println (lexLu) ;
                }
            }
        };
    }

    public static void main(String[] args) {
        AnalyseLexicale lex = new AnalyseLexicale();
        lex.initTête("clavier");
        try {
            lex.lancer();
            System.out.println(lex.lexLu.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (lex.lexLu.catégorie != CatégoriesLexicales.LEX_PT) {
            lex.reInitTête();
            try {
                lex.lancer();
                System.out.println(lex.lexLu.toString());

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public class TêteDeLecture {
        public int catégorie;
        public String unité;

        public String toString() {
            String enClair = unité;
            if (enClair.equals("<")) {
                enClair = "&lt;";
            }
            return "[" + CatégoriesLexicales.enClair[catégorie] + ", " + enClair + "]";
        }
    }
}
