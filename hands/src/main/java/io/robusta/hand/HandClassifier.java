package io.robusta.hand;

public enum HandClassifier {

	HIGH_CARD, PAIR, TWO_PAIR, TRIPS, STRAIGHT, 
	FLUSH, FULL, FOUR_OF_A_KIND, STRAIGHT_FLUSH;
	//Carte, paire, double paire, brelan, suite, couleur, full, carre, quinte flush
	
	public int getValue(){
		return this.ordinal();
	}
	
}
