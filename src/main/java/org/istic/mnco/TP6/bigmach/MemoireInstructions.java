/*
 * Created on 29 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.istic.mnco.TP6.bigmach;


import org.istic.mnco.TP6.es.LectureFichierTexte;
import org.istic.mnco.TP6.service.Service;

/**
 * @author herman
 *         <p>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class MemoireInstructions {
    public static final int tailleInsts = 1000;
    private static int[] tInsts = new int[tailleInsts];

    public static int inst(int ou) throws BigMachException {
        if (ou < 0 || ou > tailleInsts - 1) {
            throw (new BigMachException(BigMachException.BM_ACCES_HORS_INSTS));
        }
        return tInsts[ou];

    }

    public static void chargerCodeObjet(String f, int bourrage) {
        int i = 0;
        LectureFichierTexte codeObjet = new LectureFichierTexte(f);
        while (i < tailleInsts && !codeObjet.finDeFichier()) {
            tInsts[i] = codeObjet.lireUnEntier();
            i++;
        }
        while (i < tailleInsts) {
            tInsts[i] = bourrage;
            i++;
        }
    }

    public static void decompiler(int de, int a) {
        int i = de;
        while (i <= a && i < tailleInsts) {
            if (tInsts[i] < 0 || tInsts[i] >= JeuDInstructions.instructions.length) {
                System.out.println(Service.formaterUnEntier(i, 6) + ": " + "instruction inexistante");
            } else {
                System.out.println(Service.formaterUnEntier(i, 6) + ": " + JeuDInstructions.instructions[tInsts[i]].nom);
                if (JeuDInstructions.instructions[tInsts[i]].format == 2 && i + 1 < tailleInsts) {
                    i++;
                    System.out.println(Service.formaterUnEntier(i, 6) + ": " + tInsts[i]);
                }
            }
            i++;
        }


    }

}
