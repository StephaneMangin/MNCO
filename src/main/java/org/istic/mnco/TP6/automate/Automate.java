/*
 * Created on 31 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * Automate g&eacute;n&eacute;rique &agrave; nombre fini d'&eacute;tats
 */
package automate;


import es.LectureFichierTexte;

/**
 * Moteur pour un automate M � nombre fini d'�tat.
 * <code>M = (ascii, 0..nbEtats, transitions, 0, estFinal)</code>
 * Une table <code>actions</code> est associ�e aux <code>transitions</code>
 * <p>
 * <code>nbEtats</code>, <code>transitions</code>, <code>actions</code>,
 * <code>estFinal</code> doivent �te initialis�s dans une classe d�riv�e
 * qui d�finit un automate particulier
 * </p><p>
 * @author herman
 * </p>
 */
public class Automate {
	/** 
	 *  Permet d'affecter et d'ex�cuter une fonction bool�enne permettant
	 *  de param�trer la condition d'arr�t de l'automate.
	 *  Utilisation typique :
	 *  <pre>
	 * 		ConditionDArret cEst;
	 * 		...
	 * 		cEst = new ConditionDArret ... ;
	 * 		...
	 * 		if (cEst.fini()) ...
	 *  </pre>
	 */
	public interface ConditionDArret {
		public boolean fini () ;
	}
	/** 
	 * Permet d'affecter et d'ex�cuter une proc�dure sans param�tre ni r�sultat.
	 * Permet de param�trer les actions de l'automate (table et action finale).
	 *  Utilisation typique :
	 *  <pre>
	 * 		actions [i][j] = new Action ...;
	 * 		...
	 * 		// ou encore
	 * 		Action bonneAction = new Action ... ;
	 * 		...
	 * 		actions [i][j] = bonneAction ;
	 *  </pre>
	 */
	public interface Action {
		public void executer () throws Exception ;
	}
	/** 
	 * Bande d'entr�e de l'automate
	 */
	public LectureFichierTexte bande ;
	/**
	 * T�te de lecture
	 */
	protected int t�te ;
	/**
	 * Permet de fixer une condition d'arr�t suppl�mentaire (en sus de la fin de bande).
	 * Utilis� lorsqu'on souhaite obtenir des r�sultats avant la fin
	 * de la bande d'entr�e.
	 */
	public ConditionDArret cEst ;
	/**
	 * Initialisation de la <code>bande</code> d'entr�e et de la <code>t�te</code> de lecture
	 * @param f nom du fichier qui sert de bande d'entr�e. Le nom <code>"clavier"</code> d�signe justement le clavier !
	 */
	public void initT�te (String f) {
		bande = new LectureFichierTexte (f) ;
		if (!bande.finDeFichier() && !cEst.fini()) {
			t�te = bande.lireUnCar();
			System.out.print((char)t�te); // pb pour sortie le code source !!!!
		} else {t�te = -1;}
	}
	/**
	 * D�placement forc� de la t�te de lecture.
	 * Modifie <code>t�te</code>.
	 * Si la <code>bande</code> d'entr�e est �puis�e, <code>t�te</code> est positionn� � -1.
	 */
	public void forcerT�te () {
		if (!bande.finDeFichier()) {t�te = bande.lireUnCar();} else {t�te = -1;}
	}
	/**
	 * R�initialisation de la t�te de lecture.
	 * @deprecated
	 */
	public void reInitT�te () {
		// rien � faire, semble-t'il
	}
	/**
	 * D�placement normal (ie. si possible et permis) de la t�te de lecture.
	 * Modifie <code>t�te</code>.
	 */
	public void avancerT�te () {
		if (!bande.finDeFichier() && !cEst.fini()) {
			t�te = bande.lireUnCar();
			System.out.print((char)t�te); // pb pour sortie le code source !!!!
		}
	}
	/**
	 * Table des �tats
	 */
	protected int [] �tats ;
	/**
	 * Pour chaque �tat, indique s'il est ou non final.
	 */
	protected boolean [] estFinal ;
	/**
	 * Table des transitions <code>[nbEtats][tailleVocabulaire]</code>
	 */
	protected int [] [] transitions ;
	/**
	 * Table des actions <code>[nbEtats][tailleVocabulaire]</code>
	 */
	protected Action [] [] actions ;
	/**
	 * Action appel�e lors de l'arr�t de l'automate.
	 */
	protected Action actionFinale ;
	/**
	 * Indique quel est l'�tat courant.
	 */
	protected int �tatCourant ;
	/**
	 * Moteur de l'automate.
	 * @throws Exception toutes les actions peuvent poster une exception 
	 */
	public void lancer () throws Exception {
		�tatCourant = 0 ;
		while (!bande.finDeFichier() && !cEst.fini()) {
			char c = (char) t�te ;
			actions[�tatCourant][t�te].executer() ;
			�tatCourant = transitions [�tatCourant][t�te] ;
			avancerT�te();
		}
		actionFinale.executer() ;
	}
	/**
	 *
	 * Action qui ne fait rien (utile pour les initialisations par d�faut).
	 * 	 */
	public class ActionVide implements Action{
		public void executer () {
			
		}
	}

}
