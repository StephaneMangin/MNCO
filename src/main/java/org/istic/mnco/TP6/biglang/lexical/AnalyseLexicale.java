/*
 * Created on 31 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package biglang.lexical; 


import biglang.BigLangException;
import automate.Automate;

/**
 * Analyseur lexical de BigLang : �tend l'automate g�n�rique du
 * package <code>automate</code>
 */
public class AnalyseLexicale extends Automate {
	static final int nEtats = 14 ;
	static final int tVocabulaire = 256 ;
	static final int dernier = nEtats - 1 ;
	
	public class T�teDeLecture {
		public int cat�gorie ;
		public String unit� ;
		public String toString () {
			String enClair = unit� ;
			if (enClair.equals("<")) {enClair = "&lt;";}
			return "["+Cat�goriesLexicales.enClair[cat�gorie]+", "+enClair+"]" ;
		}
	}
	
	public T�teDeLecture lexLu ; 
	
	char [] s�parateurs = { ' ', '\n', '\r', '\t' } ;
	
	public AnalyseLexicale () {
		lexLu = new T�teDeLecture();
		�tats = new int [nEtats] ;
		transitions = new int [nEtats] [tVocabulaire] ;
		actions = new Action [nEtats] [tVocabulaire] ;

//		 transitions par d�faut		
		for (int i = 1 ; i < nEtats ; i ++) {
			for (int j = 0 ; j < tVocabulaire ; j++) {
				transitions[i][j] = dernier ;
				actions[i][j]= new ActionVide ();
			}
		}
//		 transitions � partir de 0, en erreur par d�faut		
		for (int j = 0 ; j < tVocabulaire ; j++) {
			transitions[0][j] = dernier ;
			actions[0][j]= new Action () {
				public void executer () throws BigLangException {
					throw new BigLangException (BigLangException.BL_LEX, BigLangException.BL_LEX_UNITE_NON_RECONNUE) ;
				}
			};	
		}		
// transitions sur les lettres
		for (int j = 'a' ; j <= 'z' ; j++) {
			transitions[0][j] = 1 ;
			actions[0][j]= new Action () {
				public void executer () {
					lexLu.cat�gorie = Cat�goriesLexicales.LEX_IDF ;
					lexLu.unit� = "" + (char) t�te ;
				}
			};
			transitions[1][j] = 1 ;
			actions[1][j]= new Action () {
				public void executer () {
					lexLu.unit� = lexLu.unit� + (char) t�te ;
				}
			};
		}
//		 transitions sur les chiffres
		for (int j = (int) '0' ; j <= (int) '9' ; j++) {
			transitions[1][j] = 1 ;
			actions[1][j]= new Action () {
				public void executer () {
					lexLu.unit� = lexLu.unit� + (char) t�te ;
				}
			};
			transitions[0][j] = 2 ;
			actions[0][j]= new Action () {
				public void executer () {
					lexLu.cat�gorie = Cat�goriesLexicales.LEX_NBRE ;
					lexLu.unit� = "" + (char) t�te ;
				}
			};
			transitions[2][j] = 2 ;
			actions[2][j]= new Action () {
				public void executer () {
					lexLu.unit� = lexLu.unit� + (char) t�te ;
				}
			};
		}
		//	transition sur :
		transitions[0][':'] = 3 ;
		actions[0][':']= new Action () {
			public void executer () {
				lexLu.unit� = "" + (char) t�te ;
			}
		};
//		 transitions � partir de 3, en erreur par d�faut		
		for (int j = 0 ; j < tVocabulaire ; j++) {
			transitions[3][j] = dernier ;
			actions[3][j]= new Action () {
				public void executer () throws BigLangException {
					throw new BigLangException (BigLangException.BL_LEX, BigLangException.BL_LEX_UNITE_NON_RECONNUE) ;
				}
			};	
		}	
		//		transitions sur =
		transitions[3]['='] = 4 ;
		actions[3]['=']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_AFF ;
				lexLu.unit� = lexLu.unit� + (char) t�te ;
			}
		};
		
		transitions[0]['='] = 5;
		actions[0]['=']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_EG ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
//		transitions sur ;
		transitions[0][';'] = 5 ;
		actions[0][';']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_PT_VIRG ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
//		transitions sur ,
		transitions[0][','] = 5 ;
		actions[0][',']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_VIRG ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
//		transitions sur .
		transitions[0]['.'] = 5 ;
		actions[0]['.']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_PT ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
//		transitions sur (
		transitions[0]['('] = 5 ;
		actions[0]['(']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_PAR_OUV ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
//		transitions sur )
		transitions[0][')'] = 5 ;
		actions[0][')']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_PAR_FER ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
//		transitions sur +
		transitions[0]['+'] = 6 ;
		actions[0]['+']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_PLUS ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
		
		//poursuite par :=
		
		transitions[6][':'] = 7 ;
		actions[6][':']= new Action () {
			public void executer () {
				
				lexLu.unit� =  lexLu.unit�+ (char) t�te ;
			}
		};
		
		//transitions � partirde 7 en erreur pard�faut
		for (int j = 0 ; j < tVocabulaire ; j++) {
			transitions[7][j] = dernier ;
			actions[7][j]= new Action () {
				public void executer () throws BigLangException {
					throw new BigLangException (BigLangException.BL_LEX, BigLangException.BL_LEX_UNITE_NON_RECONNUE) ;
				}
			};	
		}		
		transitions[7]['='] = 8 ;
		actions[7]['=']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_AFF_ADD ;
				lexLu.unit� = lexLu.unit� + (char) t�te ;
			}
		};
		
		
//		transitions sur -
		transitions[0]['-'] = 5 ;
		actions[0]['-']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_MOINS ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
//		transitions sur *
		transitions[0]['*'] = 5 ;
		actions[0]['*']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_MULT ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
//		transitions sur /
		transitions[0]['/'] = 5 ;
		actions[0]['/']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_DIV ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
//		transitions sur <
		transitions[0]['<'] = 11 ;
		actions[0]['<']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_INF ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
// nouveau <= : transition sur = depuis 11
		transitions[11]['='] = 12 ;
		actions[11]['=']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_INF_EG ;
				lexLu.unit� = lexLu.unit� + (char) t�te ;
			}
		};

		
//		nouveau transitions sur >
		transitions[0]['>'] = 9 ;
		actions[0]['>']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_SUP ;
				lexLu.unit� = "" + (char) t�te ;
			}
		};
// nouveau >= : transition sur = depuis 8
		transitions[9]['='] = 10 ;
		actions[9]['=']= new Action () {
			public void executer () {
				lexLu.cat�gorie = Cat�goriesLexicales.LEX_SUP_EG ;
				lexLu.unit� = lexLu.unit� + (char) t�te ;
			}
		};
	
		//		transitions sur les s�parateurs
		for (int j = 0 ; j < s�parateurs.length ; j++) {
			transitions[0][s�parateurs[j]] = 0 ;
			actions[0][s�parateurs[j]]= new ActionVide () ;
		}
		for (int i = 2 ; i < nEtats ; i++) {
			if ((i != 3)&& (i != 7) ){
				for (int j = 0 ; j < s�parateurs.length ; j++) {				
					transitions[i][s�parateurs[j]] = dernier ;
					actions[i][s�parateurs[j]]= new ActionVide () ;
				}
			}
		}

// �tats finaux
		estFinal = new boolean [nEtats] ;
		for (int i = 0 ; i < dernier ; i++) {
			estFinal[i] = false ;
		}
		estFinal[dernier] = true ;
// condition sp�ciale d'arr�t
		cEst = new ConditionDArret () {
			public boolean fini () {
				return estFinal[�tatCourant] ;
			}
		};
// action finale
		actionFinale = new Action () {
			public void executer () {
				if (estFinal[�tatCourant]) {
					// passage au crible
					Integer n = (Integer)Cat�goriesLexicales.crible.get(lexLu.unit�);
					if (n != null) {lexLu.cat�gorie = n.intValue();}
					// System.out.println (lexLu) ;
				}
			}
		};
	}
	public static void main (String [] args) {
		AnalyseLexicale lex = new AnalyseLexicale () ;
		lex.initT�te("clavier");
		try {
			lex.lancer();
			System.out.println(lex.lexLu.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (lex.lexLu.cat�gorie != Cat�goriesLexicales.LEX_PT) {
			lex.reInitT�te() ;
			try {
				lex.lancer() ;
				System.out.println(lex.lexLu.toString());
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
