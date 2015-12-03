/*
 * Created on 31 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * Automate g&eacute;n&eacute;rique &agrave; nombre fini d'&eacute;tats
 */
package org.istic.mnco.TP6.automate;


import org.istic.mnco.TP45.es.LectureFichierTexte;

/**
 * Moteur pour un automate M � nombre fini d'�tat.
 * <code>M = (ascii, 0..nbEtats, transitions, 0, estFinal)</code>
 * Une table <code>actions</code> est associ�e aux <code>transitions</code>
 * <p>
 * <code>nbEtats</code>, <code>transitions</code>, <code>actions</code>,
 * <code>estFinal</code> doivent �te initialis�s dans une classe d�riv�e
 * qui d�finit un automate particulier
 * </p><p>
 *
 * @author herman
 *         </p>
 */
public class Automate {
    /**
     * Bande d'entr�e de l'automate
     */
    public LectureFichierTexte bande;
    /**
     * Permet de fixer une condition d'arret suppl�mentaire (en sus de la fin de bande).
     * Utilis� lorsqu'on souhaite obtenir des r�sultats avant la fin
     * de la bande d'entr�e.
     */
    public ConditionDArret cEst;
    /**
     * T�te de lecture
     */
    protected int tete;
    /**
     * Table des etats
     */
    protected int[] etats;
    /**
     * Pour chaque �tat, indique s'il est ou non final.
     */
    protected boolean[] estFinal;
    /**
     * Table des transitions <code>[nbEtats][tailleVocabulaire]</code>
     */
    protected int[][] transitions;
    /**
     * Table des actions <code>[nbEtats][tailleVocabulaire]</code>
     */
    protected Action[][] actions;
    /**
     * Action appel�e lors de l'arret de l'automate.
     */
    protected Action actionFinale;
    /**
     * Indique quel est l'�tat courant.
     */
    protected int etatCourant;

    /**
     * Initialisation de la <code>bande</code> d'entr�e et de la <code>tete</code> de lecture
     *
     * @param f nom du fichier qui sert de bande d'entr�e. Le nom <code>"clavier"</code> d�signe justement le clavier !
     */
    public void initTete(String f) {
        bande = new LectureFichierTexte(f);
        if (!bande.finDeFichier() && !cEst.fini()) {
            tete = bande.lireUnCar();
            System.out.print((char) tete); // pb pour sortie le code source !!!!
        } else {
            tete = -1;
        }
    }

    /**
     * D�placement forc� de la tete de lecture.
     * Modifie <code>tete</code>.
     * Si la <code>bande</code> d'entr�e est �puis�e, <code>tete</code> est positionn� � -1.
     */
    public void forcerTete() {
        if (!bande.finDeFichier()) {
            tete = bande.lireUnCar();
        } else {
            tete = -1;
        }
    }

    /**
     * R�initialisation de la tete de lecture.
     *
     * @deprecated
     */
    public void reInitTete() {
        // rien � faire, semble-t'il
    }

    /**
     * D�placement normal (ie. si possible et permis) de la tete de lecture.
     * Modifie <code>tete</code>.
     */
    public void avancerTete() {
        if (!bande.finDeFichier() && !cEst.fini()) {
            tete = bande.lireUnCar();
            System.out.print((char) tete); // pb pour sortie le code source !!!!
        }
    }

    /**
     * Moteur de l'automate.
     *
     * @throws Exception toutes les actions peuvent poster une exception
     */
    public void lancer() throws Exception {
        etatCourant = 0;
        while (!bande.finDeFichier() && !cEst.fini()) {
            char c = (char) tete;
            actions[etatCourant][tete].executer();
            etatCourant = transitions[etatCourant][tete];
            avancerTete();
        }
        actionFinale.executer();
    }
    /**
     * Permet d'affecter et d'ex�cuter une fonction bool�enne permettant
     * de param�trer la condition d'arret de l'automate.
     * Utilisation typique :
     * <pre>
     * 		ConditionDArret cEst;
     * 		...
     * 		cEst = new ConditionDArret ... ;
     * 		...
     * 		if (cEst.fini()) ...
     *  </pre>
     */
    public interface ConditionDArret {
        boolean fini();
    }

    /**
     * Permet d'affecter et d'ex�cuter une proc�dure sans param�tre ni r�sultat.
     * Permet de param�trer les actions de l'automate (table et action finale).
     * Utilisation typique :
     * <pre>
     * 		actions [i][j] = new Action ...;
     * 		...
     * 		// ou encore
     * 		Action bonneAction = new Action ... ;
     * 		...
     * 		actions [i][j] = bonneAction ;
     *  </pre>
     */
    public interface Action {
        void executer() throws Exception;
    }

    /**
     * Action qui ne fait rien (utile pour les initialisations par d�faut).
     */
    public class ActionVide implements Action {
        public void executer() {

        }
    }

}
