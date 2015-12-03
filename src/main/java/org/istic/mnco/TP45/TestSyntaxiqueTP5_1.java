package org.istic.mnco.TP45;

public class TestSyntaxiqueTP5_1 {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// 
		
		SyntaxiqueTP5_1 monanalyseur = new SyntaxiqueTP5_1(TestSyntaxiqueTP5_1.class.getResource("/TP45/testSyntaxique5_1.txt").getFile());
		monanalyseur.Axiome();
		
		
	}

}


