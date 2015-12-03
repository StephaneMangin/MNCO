/*
 * Cree le 26 septembre 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * Automate g&eacute;n&eacute;rique &agrave; nombre fini d'&eacute;tats
 */
package org.istic.mnco.TP45.automate;


import org.istic.mnco.TP45.es.LectureFichierTexte;

/**
 * Moteur pour un automate M e nombre fini d'etat.
 * <code>M = (ascii, 0..nbEtats, transitions, 0, estFinal)</code>
 * Une table <code>actions</code> est associee aux <code>transitions</code>
 * <p>
 * <code>nbEtats</code>, <code>transitions</code>, <code>actions</code>,
 * <code>estFinal</code> doivent ete initialises dans une classe derivee
 * qui definit un automate particulier
 * </p><p>
 * @author vorch (e partir du package automate de herman)
 * </p>
 */
public class Automate {
	
	

	
	/**
	 * Bande d'entree de l'automate
	 */
	public LectureFichierTexte bande ;
	
	/**
	 * Tete de lecture
	 */
	protected int tete ;
	
	/**
	 * donne_tete : utilitaire de trace
	 * @return
	 */
	public int donne_tete(){return tete;}
	
	
	/**
	 * iniTete : ouvre un fichier en lecture et le lie à la bande de lecture ; 
	 * lit un premier caractere (si possible)
	 * @param f : nom du fichier source
	 */
	public void initTete (String f) {
		
		//if (f.equals("clavier")) { bande = new LectureFichierTexte (System.in,'#') ;}
		//else { 
			
			bande = new LectureFichierTexte (f) ; 
//System.out.println("initTete : EOF :"+ bande.finDeFichier()+" Fini :"+fini());
		

		// if (!bande.finDeFichier() && ! fini()) {
			if (!bande.finDeFichier() ) {	
			tete = bande.lireUnCar();
//System.out.println(" InitTete EOF2:"+ bande.finDeFichier()+" Fini2:"+fini());
			// System.out.print((char)tete); // pb pour sortie le code source !!!!
		} else {tete = -10;}
		
		// System.out.println("InitTete tete"+tete);
	}
	/**
	 * Deplacement force de la tete de lecture.
	 * Modifie <code>tete</code>.
	 * Si la <code>bande</code> d'entree est epuisee, <code>tete</code> est positionne e -1.
	 */
	public void forcerTete () {
		if (!bande.finDeFichier()) {tete = bande.lireUnCar();} else {tete = -15;}
	}
	/**
	 * Reinitialisation de la tete de lecture.
	 * @deprecated
	 */
	public void reInitTete () {
		// rien e faire, semble-t'il
	}
	/**
	 * Deplacement normal (ie. si possible et permis) de la tete de lecture.
	 * Modifie <code>tete</code>.
	 */
	public void avancerTete () {
		if (!bande.finDeFichier() && !fini()){
			tete = bande.lireUnCar();
			// System.out.print((char)tete); // pb pour sortie le code source !!!!
		}
	}
	
	
	/**
	 * Nombre d'etats de l'automate (numérotes de 0 a NbEtats-1). 0 est l'etat initial.
	 */
	protected int NbEtats ;
	/**
	 * Pour chaque etat, indique s'il est ou non final.
	 */
	protected boolean [] tab_etats_finals ;
	/**
	 * Table des transitions <code>[nbEtats][tailleVocabulaire]</code>
	 */
	protected int [] [] table_transitions ;
	/**
	 * Table des actions <code>[nbEtats][tailleVocabulaire]</code>
	 */
	protected int [] [] table_actions ;
	
	/**
	 * Quelques constantes caractere
	 */
	static final int c_code_min = 0;
	static final int c_code_max = 127;
	
	static  final char c_char0 =  '0'; // caractere zero
	static  final int c_codeNL =  10; // saut de ligne
	static  final int c_codeRC =  13; // retour chariot
	static  final int c_code0 =  (int)c_char0; // saut de ligne
	
	
	static  final char c_charNL =  (char)c_codeNL; // saut de ligne
	static  final char c_charRC =  (char)c_codeRC; // retour chariot
	
	static  final char c_charEspace = ' ';
	
	/**
	 * UTILITAIRES DE CONSTRUCTION DES TABLES
	 */
	
	
	/**
	 * Pose un etat final
	 * @param P : etat source
	 */
	void put_final(int P){
		tab_etats_finals[P]=true;
	}
	/**
	 * 
	 * @param P : etat 
	 * @return   : P est-il un etat final ?
	 */
	boolean est_final(int P){
		return tab_etats_finals[P];
	}
	/**
	 * Pose une transition
	 * @param P : etat source
	 * @param c : caractere sous la tete de lecture
	 * @param Q : etat cible
	 */
	protected void unArcT(int P,char c,int Q){
		table_transitions[P] [(int)c] = Q;
	}
	
	
	/**
	 * Pose une action
	 * @param P : etat source
	 * @param c : caractere sous la tete de lecture
	 * @param A : action
	 */
	protected void unArcA(int P,char c,int A){
		table_actions[P][c]=A;
	}
	
	
	
	
	/**
	 * 
	 * @param P : etat cible
	 * @param c : caractere sous la tete de lecture
	 * @param Q : etat cible
	 * @param A : numero d'action
	 */
	protected void unArc(int P,char c,int Q,int A){
		unArcT(P,c,Q);
		unArcA(P,c,A);
	}
	/**
	 * Pose une transition pour un intervalle [c1..c2] de caracteres
	 * @param P : etat source
	 * @param c1 : premire car
	 * @param c2 : deuxieme car
	 * @param Q : etat cible
	 */
	protected void arcsPourIntervalleT(int P, char c1, char c2, int Q){
		for(int i =(int)c1; i<=(int)c2;i++){
			unArcT(P,(char)i,Q);
		}
		
	}
	
	/**
	 * Pose une action pour un intervalle [c1..c2] de caracteres
	 * @param P : etat source
	 * @param c1 : premire car
	 * @param c2 : deuxieme car
	 * @param A : action
	 */
	protected void arcsPourIntervalleA(int P, char c1, char c2, int A){
		for(int i =(int)c1; i<=(int)c2;i++){
			unArcA(P,(char)i,A);
		}
		
	}
	
	/**
	 * Pose une transition et une action pour un intervalle [c1..c2] de caracteres
	 * @param P : etat source
	 * @param c1 : premire car
	 * @param c2 : deuxieme car
	 * @param Q :  etat cible
	 * @param A : action
	 */
	protected void arcPourIntervalle(int P, char c1, char c2, int Q,int A){
		arcsPourIntervalleT(P,c1,c2,Q);
		arcsPourIntervalleA(P,c1,c2,A);
		
	}
	
	
	
	/**
	 * Pose une transition pour toutes les lettres minuscules
	 * @param P : etat source
	 * @param Q : etat cible
	 */
	protected void arcsPourLettresT(int P, int Q){
		arcsPourIntervalleT(P,'a','z',Q);
	}
	
	/**
	 * Pose une action  pour toutes les lettres minuscules
	 * @param P : etat source
	 * @param A : action
	 */
	protected void arcsPourLettresA(int P, int A){
		arcsPourIntervalleA(P,'a','z',A);
	}
	
	/**
	 * Pose une transition et une action pour toutes les lettes minuscules
	 * @param P : etat source
	 * @param Q : etat cible
	 * @param A : action
	 */
	protected void arcsPourLettres(int P, int Q, int A){
		arcsPourLettresT(P, Q);
		arcsPourLettresA(P, A);
	}
	
	/**
	 * Pose une transition pour tous les chiffres
	 * @param P : etat source
	 * @param Q : etat cible
	 */
	protected void arcsPourChiffresT(int P, int Q){
		arcsPourIntervalleT(P,'0','9',Q);
	}
	
	/**
	 * Pose une action  pour tous les chiffres
	 * @param P : etat source
	 * @param A : action
	 */
	protected void arcsPourChiffresA(int P, int A){
		arcsPourIntervalleA(P,'0','9',A);
	}
	
	/**
	 * Pose une transition et une action pour tous les chiffres
	 * @param P : etat source
	 * @param Q : etat cible
	 * @param A : action
	 */
	protected void arcsPourChiffres(int P, int Q, int A){
		arcsPourChiffresT(P, Q);
		arcsPourChiffresA(P, A);
	}
	
	
	/**
	 * 
	 * @param Q etat cible
	 * pose une transition P->Q pour tous les �tats et tous les caracteres
	 */
	protected void arcs_defauts(int Q){
		for (int P=1;P<=NbEtats;P++)
		{  arcsPourIntervalleT(P, (char) c_code_min, (char) c_code_max, Q);
			
		}
	}
	/**
	 * creer_tables_vides
	 * @param Netats : nombre d'etats de l'automate (etat init = 1 ; etat puit = 0)
	 */
	protected void creertables_vides(int Netats){
		// etat 0 : puit ; etat 1 : initial
		NbEtats = Netats;
		tab_etats_finals = new boolean [Netats+1];
		table_transitions = new int[Netats+1][c_code_max+1];
		table_actions= new int[Netats+1][c_code_max+1];
		// ! tables préinitialisées à 0
		

	}
	
	/**
	 * char_en_clair (utilitaire)
	 * @param code ascci
	 * @return : le code et sa version "en clair"
	 */
	protected String char_en_clair(int code){
		return ""+(char)code+"("+code+")";
		
	}
	
	
	/**
	 * affiche_tables (utilitaire) : affiche le contenu des tables transition et action
	 *
	 */
	public void affiche_tables(){
		for(int i=1; i<=(NbEtats);i++){
			    for(int j=0; (j<=c_code_max);j++){
			    	
			    	if (table_transitions[i] [j] != 0){
System.out.println("Etat :"+i+"->"+(char)j+"->"+table_transitions[i] [j]+" ("+table_actions[i][j]+")");
			    	}
			    }
		}
			
		
	}
	
	/**
	 * Action appelee avant de lancer l'automate.
	 * Vide par défaut
	 */
	protected void actionInitiale(){
	} ;
	 
	

	protected void actionFinale2(){
		System.out.print("Fin fichier   :");System.out.println(bande.finDeFichier());
		System.out.print("Fini          :");System.out.println(fini());
		System.out.print("En etat final :");System.out.println(est_final(etatCourant));
		}
	
	/**
	 * Action appelee lors de l'arret de l'automate (on peut redéfinir)
	 */
	protected void actionFinale(){
	actionFinale2();}
	
	/** 
	 *  Permet ''executer une fonction booleenne permettant
	 *  de parametrer la condition d'arret de l'automate.
	 *  
	 */
	protected boolean fini(){return false;};
	
	/**
	 * executer une action
	 */
	protected void executeraction(int A, int tete){
		
		
	}
	/**
	 * ============  MOTEUR DE l'AUTOMATE
	 */
	
	/**
	 * Indique quel est l'etat courant. *
	 */
	protected int etatCourant;
	
	
	/**
	 * tracer : trace d'une transition et d'une action
	 * @param P : etat source
	 * @param tete : tete de lecture
	 * @param Q : etat cible
	 * @param A : numero d'action
	 */
	protected void tracer(int P, int tete, int Q, int A){
		if (ontrace) {
		System.out.println(" "+ P + " : "+ char_en_clair(tete) +" : "+Q+" : "+A ); }
	}
	
	protected boolean ontrace=false;
	
	/**
	 * trace_on : active la trace
	 *
	 */
	public void trace_on(){ontrace=true;};
	
	/**
	 * trace_off : desactive la trace
	 *
	 */
	public void trace_off(){ontrace=false;};
	
	
	/**
	 * Lancer() : lance l'automate
	 * @pre : IniTete a ete execute
	 */
	public void lancer (){ 
		
		//pré : IniTete a été execute
		
		etatCourant=1; // etat initial
		
		int etatcible;	
		int numaction;
		
			
		actionInitiale();
		
		while (!bande.finDeFichier() && ! fini()) {
			
		
	
			 
			etatcible=table_transitions [etatCourant][tete] ;
			numaction=table_actions[etatCourant][tete]; 
			
			tracer(etatCourant,tete,etatcible,numaction); 
			
			
			executeraction(numaction,tete);
			//actions[etatCourant][tete] ;
			etatCourant = etatcible;
			
			avancerTete();
		} // end while
		
		//bande.fermer();
		
		actionFinale() ;
	}
	
	 
	
	} // classe Automate


