/*
 * Created on 29 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package bigmach;
import service.* ;
/**
 * @author herman
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JeuDInstructions {

	public interface Action {
		public void executer ()throws BigMachException ;
	}
	public static class Instruction {
		public String nom ;
		public int format ;
		public Action action ;
		public String descr ;
		public Instruction (String n, int f, Action a, String d){
			nom = n ;
			format = f ;
			action = a ;
			descr = d ;
		}
	}
	public static Instruction [] instructions = {
			// STOP
			new Instruction ("stop", 1, new Action () {
				public void executer () {
					BigMach.arrêt = true ;
				}
			},"Arrêt de la machine"),
			// OU
			new Instruction ("ou", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.ou() ;
				}
			},"Ou logique"),
			new Instruction ("et", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.et() ;
				}
			},"Et logique"),
			new Instruction ("non", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.non () ;
				}
			},"Non logique"),
			new Instruction ("plus", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.addition () ;
				}
			},"Addition"),
			new Instruction ("moins", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.soustraction () ;
				}
			},"soustraction"),
			new Instruction ("mult", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.multiplication () ;
				}
			},"Multiplication"),
			new Instruction ("div", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.division () ;
				}
			},"Division"),
			new Instruction ("dupl", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.dupliquer () ;
				}
			},"Duplication du sommet de pile"),
			new Instruction ("inf", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.inférieur () ;
				}
			},"Comparaison <"),
			new Instruction ("eg", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.égal () ;
				}
			},"Comparaison ="),
			new Instruction ("affecter", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.affectation () ;
				}
			},"Affectation"),
			new Instruction ("valeur", 1, new Action () {
				public void executer () throws BigMachException {
					UAL.valeur () ;
				}
			},"Affectation"),
			new Instruction ("empiler", 2, new Action () {
				public void executer () throws BigMachException {
					PileDeCalcul.empiler(MémoireInstructions.inst(BigMach.iCo)) ;
					BigMach.iCo++ ;
				}
			},"Empiler"),
			new Instruction ("dépiler", 1, new Action () {
				public void executer () throws BigMachException {
					PileDeCalcul.dépiler () ;
				}
			},"dépiler"),
			new Instruction ("sommeta", 2, new Action () {
				public void executer () throws BigMachException {
					PileDeCalcul.fixerSommet(MémoireInstructions.inst(BigMach.iCo)) ;
					BigMach.iCo++ ;
				}
			},"Fixe la valeur du sommet de pile"),
			new Instruction ("lire", 2, new Action () {
				public void executer () throws BigMachException {
					int n = MémoireInstructions.inst(BigMach.iCo) ;
					BigMach.iCo++ ;
					UAL.lire(Service.motToAscii(n)) ;
				}
			},"lecture clavier"),
			new Instruction ("écrire", 2, new Action () {
				public void executer () throws BigMachException {
					int n = MémoireInstructions.inst(BigMach.iCo) ;
					BigMach.iCo++ ;
					UAL.écrire (Service.motToAscii(n)) ;
				}
			},"affichage à l'écran"),
			new Instruction ("nop", 1, new Action () {
				public void executer () throws BigMachException {
				}
			},"Sans effet"),
			new Instruction ("bsf", 2, new Action () {
				public void executer () throws BigMachException {
					if (PileDeCalcul.sommet() == 0) {
						BigMach.iCo = MémoireInstructions.inst(BigMach.iCo) ;
					} else {
						BigMach.iCo++ ;
					}
					PileDeCalcul.dépiler () ;
				}
			},"Branchement si faux"),
			new Instruction ("bsv", 2, new Action () {
				public void executer () throws BigMachException {
					if (PileDeCalcul.sommet() == 1) {
						BigMach.iCo = MémoireInstructions.inst(BigMach.iCo) ;
					} else {
						BigMach.iCo++ ;
					}
					PileDeCalcul.dépiler () ;
				}
			},"Branchement si vrai"),
			new Instruction ("tsf", 2, new Action () {
				public void executer () throws BigMachException {
					if (PileDeCalcul.sommet() == 0) {
						BigMach.iCo = MémoireInstructions.inst(BigMach.iCo) ;
						PileDeCalcul.dépiler () ;
					} else {
						BigMach.iCo++ ;
					}
				}
			},"Transfert si faux"),
			new Instruction ("tsv", 2, new Action () {
				public void executer () throws BigMachException {
					if (PileDeCalcul.sommet() == 1) {
						BigMach.iCo = MémoireInstructions.inst(BigMach.iCo) ;
						PileDeCalcul.dépiler () ;
					} else {
						BigMach.iCo++ ;
					}
				}
			},"Transfert si vrai"),
			new Instruction ("br", 2, new Action () {
				public void executer () throws BigMachException {
					BigMach.iCo = MémoireInstructions.inst(BigMach.iCo) ;
				}
			},"Branchement toujours")
			
	};
	
	
	public static void main (String [] args) {
		System.out.println("package BigMach;") ;
		System.out.println("public class CodesInstructions {") ;
		
		for (int i = 0; i < instructions.length; i++) {
			System.out.println("    public static final int CODE_"+Service.compléterADroite(instructions[i].nom.toUpperCase(), 15)+" = "+i+" ;");
			
		}
		
		System.out.println("}") ;
		
	}
}
