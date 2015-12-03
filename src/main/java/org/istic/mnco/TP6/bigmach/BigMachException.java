/*
 * Created on 29 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.istic.mnco.TP6.bigmach;

/**
 * @author herman
 *         <p>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class BigMachException extends Exception {
    public static final int BM_DEBORDEMENT_HAUT = 0;
    public static final int BM_DEBORDEMENT_BAS = 1;
    public static final int BM_ACCES_HORS_INSTS = 2;
    public static final int BM_INSTRUCTION_INEXISTANTE = 3;
    private static String[] messages = {
            "d�bordement par le haut de la pile de calcul",
            "d�bordement par le bas de la pile de calcul",
            "acc�s hors de la m�moire des instructions",
            "instruction inexistante"
    };

    public BigMachException(int num) {
        super("**** Exception BigMach " + num + ": " + messages[num]);
        System.out.println("**** Exception BigMach " + num + ": " + messages[num]);
    }

}
