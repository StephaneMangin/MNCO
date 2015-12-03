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
public class PileDeCalcul {
    public static final int taillePile = 1000;
    private static int[] tPile = new int[taillePile];
    private static int iPile = -1;


    public static void empiler(int n) throws BigMachException {
        if (iPile >= taillePile - 1) {
            throw (new BigMachException(BigMachException.BM_DEBORDEMENT_HAUT));
        }
        iPile++;
        tPile[iPile] = n;
    }

    public static int depiler() throws BigMachException {
        if (iPile < 0) {
            throw (new BigMachException(BigMachException.BM_DEBORDEMENT_BAS));
        }
        iPile--;
        return tPile[iPile + 1];
    }

    public static void fixerSommet(int a) throws BigMachException {
        if (a < 0) {
            throw (new BigMachException(BigMachException.BM_DEBORDEMENT_BAS));
        }
        if (a >= taillePile - 1) {
            throw (new BigMachException(BigMachException.BM_DEBORDEMENT_HAUT));
        }
        iPile = a;
    }

    public static void fixerMot(int a, int v) throws BigMachException {
        if (a < 0) {
            throw (new BigMachException(BigMachException.BM_DEBORDEMENT_BAS));
        }
        if (a >= taillePile - 1) {
            throw (new BigMachException(BigMachException.BM_DEBORDEMENT_HAUT));
        }
        tPile[a] = v;
    }

    public static int sommet() throws BigMachException {
        if (iPile < 0) {
            throw (new BigMachException(BigMachException.BM_DEBORDEMENT_BAS));
        }
        if (iPile >= taillePile - 1) {
            throw (new BigMachException(BigMachException.BM_DEBORDEMENT_HAUT));
        }
        return tPile[iPile];
    }

    public static int valeurMot(int a) throws BigMachException {
        if (a < 0) {
            throw (new BigMachException(BigMachException.BM_DEBORDEMENT_BAS));
        }
        if (a >= taillePile - 1) {
            throw (new BigMachException(BigMachException.BM_DEBORDEMENT_HAUT));
        }
        return tPile[a];
    }

    public static boolean estVide() {
        return iPile == -1;
    }

    public static boolean estPleine() {
        return iPile == taillePile - 1;
    }

    public static String etatPile() {
        String r = "  -----------------------\n           |            |\n";
        for (int k = iPile; k >= 0; k--) {
            r = r + "  " + Service.formaterUnEntier(k, 6) + ":  | " + Service.formaterUnEntier(tPile[k], 10) + " |\n";
        }
        return r + "  -----------------------";
    }

}
