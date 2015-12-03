/*
 * Created on 29 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.istic.mnco.TP6.bigmach;

import org.istic.mnco.TP6.service.Service;

/**
 * @author herman
 *         <p>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class JeuDInstructions {

    public static Instruction[] instructions = {
            // STOP
            new Instruction("stop", 1, new Action() {
                public void executer() {
                    BigMach.arret = true;
                }
            }, "Arrêt de la machine"),
            // OU
            new Instruction("ou", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.ou();
                }
            }, "Ou logique"),
            new Instruction("et", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.et();
                }
            }, "Et logique"),
            new Instruction("non", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.non();
                }
            }, "Non logique"),
            new Instruction("plus", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.addition();
                }
            }, "Addition"),
            new Instruction("moins", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.soustraction();
                }
            }, "soustraction"),
            new Instruction("mult", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.multiplication();
                }
            }, "Multiplication"),
            new Instruction("div", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.division();
                }
            }, "Division"),
            new Instruction("dupl", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.dupliquer();
                }
            }, "Duplication du sommet de pile"),
            new Instruction("inf", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.inferieur();
                }
            }, "Comparaison <"),
            new Instruction("eg", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.egal();
                }
            }, "Comparaison ="),
            new Instruction("affecter", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.affectation();
                }
            }, "Affectation"),
            new Instruction("valeur", 1, new Action() {
                public void executer() throws BigMachException {
                    UAL.valeur();
                }
            }, "Affectation"),
            new Instruction("empiler", 2, new Action() {
                public void executer() throws BigMachException {
                    PileDeCalcul.empiler(MemoireInstructions.inst(BigMach.iCo));
                    BigMach.iCo++;
                }
            }, "Empiler"),
            new Instruction("depiler", 1, new Action() {
                public void executer() throws BigMachException {
                    PileDeCalcul.depiler();
                }
            }, "depiler"),
            new Instruction("sommeta", 2, new Action() {
                public void executer() throws BigMachException {
                    PileDeCalcul.fixerSommet(MemoireInstructions.inst(BigMach.iCo));
                    BigMach.iCo++;
                }
            }, "Fixe la valeur du sommet de pile"),
            new Instruction("lire", 2, new Action() {
                public void executer() throws BigMachException {
                    int n = MemoireInstructions.inst(BigMach.iCo);
                    BigMach.iCo++;
                    UAL.lire(Service.motToAscii(n));
                }
            }, "lecture clavier"),
            new Instruction("ecrire", 2, new Action() {
                public void executer() throws BigMachException {
                    int n = MemoireInstructions.inst(BigMach.iCo);
                    BigMach.iCo++;
                    UAL.ecrire(Service.motToAscii(n));
                }
            }, "affichage à l'écran"),
            new Instruction("nop", 1, new Action() {
                public void executer() throws BigMachException {
                }
            }, "Sans effet"),
            new Instruction("bsf", 2, new Action() {
                public void executer() throws BigMachException {
                    if (PileDeCalcul.sommet() == 0) {
                        BigMach.iCo = MemoireInstructions.inst(BigMach.iCo);
                    } else {
                        BigMach.iCo++;
                    }
                    PileDeCalcul.depiler();
                }
            }, "Branchement si faux"),
            new Instruction("bsv", 2, new Action() {
                public void executer() throws BigMachException {
                    if (PileDeCalcul.sommet() == 1) {
                        BigMach.iCo = MemoireInstructions.inst(BigMach.iCo);
                    } else {
                        BigMach.iCo++;
                    }
                    PileDeCalcul.depiler();
                }
            }, "Branchement si vrai"),
            new Instruction("tsf", 2, new Action() {
                public void executer() throws BigMachException {
                    if (PileDeCalcul.sommet() == 0) {
                        BigMach.iCo = MemoireInstructions.inst(BigMach.iCo);
                        PileDeCalcul.depiler();
                    } else {
                        BigMach.iCo++;
                    }
                }
            }, "Transfert si faux"),
            new Instruction("tsv", 2, new Action() {
                public void executer() throws BigMachException {
                    if (PileDeCalcul.sommet() == 1) {
                        BigMach.iCo = MemoireInstructions.inst(BigMach.iCo);
                        PileDeCalcul.depiler();
                    } else {
                        BigMach.iCo++;
                    }
                }
            }, "Transfert si vrai"),
            new Instruction("br", 2, new Action() {
                public void executer() throws BigMachException {
                    BigMach.iCo = MemoireInstructions.inst(BigMach.iCo);
                }
            }, "Branchement toujours")

    };

    public static void main(String[] args) {
        System.out.println("package BigMach;");
        System.out.println("public class CodesInstructions {");

        for (int i = 0; i < instructions.length; i++) {
            System.out.println("    public static final int CODE_" + Service.completerADroite(instructions[i].nom.toUpperCase(), 15) + " = " + i + " ;");

        }

        System.out.println("}");

    }

    public interface Action {
        void executer() throws BigMachException;
    }

    public static class Instruction {
        public String nom;
        public int format;
        public Action action;
        public String descr;

        public Instruction(String n, int f, Action a, String d) {
            nom = n;
            format = f;
            action = a;
            descr = d;
        }
    }
}
