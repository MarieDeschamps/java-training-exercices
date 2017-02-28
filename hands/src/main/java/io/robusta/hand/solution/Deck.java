package io.robusta.hand.solution;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;

import io.robusta.hand.Card;
import io.robusta.hand.CardColor;
import io.robusta.hand.interfaces.IDeck;

public class Deck extends LinkedList<Card> implements IDeck{

	
	private static final long serialVersionUID = -4686285366508321800L;
	
	public Deck() {

	}
	
	@Override
	public Card pick() {
		int size = this.size();
		Random random = new Random();
		int cardNumber = random.nextInt(size);
		Card pickedCard = this.get(cardNumber);
		this.remove(pickedCard);
		
		return pickedCard;
	}


	

	@Override
	public TreeSet<Card> pick(int number) {
		TreeSet<Card> pickedCards = new TreeSet<>();
		for(int i = 0; i<number;i++){
			pickedCards.add(this.pick());
		}
		return pickedCards;
	}

	@Override
	public Hand giveHand() {
		Hand hand = new Hand();
		hand.addAll(this.pick(5));
		
		return hand;
	}
	
	public HandHoldem giveHandHoldem() {
		HandHoldem handHoldem = new HandHoldem();
		handHoldem.addAll(this.pick(7));
		
		return handHoldem;
	}
	
	
}
