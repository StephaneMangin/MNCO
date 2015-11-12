package org.istic.mnco;


public class TestSyntaxiqueTP4  {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// 
		
		SyntaxiqueTP4 monanalyseur = new SyntaxiqueTP4(TestSyntaxiqueTP4.class.getResource("/testSyntaxique4.txt").getFile());
		monanalyseur.Axiome();
		
		
	}

}
