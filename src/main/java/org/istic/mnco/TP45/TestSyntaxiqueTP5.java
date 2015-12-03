package org.istic.mnco.TP45;

public class TestSyntaxiqueTP5  {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// 
		
		SyntaxiqueTP5 monanalyseur = new SyntaxiqueTP5(TestSyntaxiqueTP5.class.getResource("/TP45/testSyntaxique5.txt").getFile());
		monanalyseur.Axiome();
		
		
	}

}


