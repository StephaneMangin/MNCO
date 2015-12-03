/*
 * Created on 30 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package service;

/**
 * @author herman
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Service {
	public static String compléterAGauche (String s, int n) {
		String r = "" ;
		for (int i = 0 ; i < n - s.length() ; i++) {r = r + " ";}
		return r + s ;
	}
	public static String compléterADroite (String s, int l) {
		String r = s ;
		for (int i = s.length() ; i < l ; i++) {r = r + " ";}
		return r ;
	}
	public static String formaterUnEntier (int k, int n) {
		return compléterAGauche(Integer.toString(k), n) ;
	}
	public static String motToAscii (int n) {
		String s = "" ;
		for (int i = 0; i < 4; i++) {
			char c = (char)(n % 256) ;
			s = s + c ;
			n = n / 256 ;}
		String r = "" ;
		for (int j = 3; j >= 0; j--) {r = r + s.charAt(j);}
		return r ;
	}


}
