package io.robusta.hand.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import io.robusta.hand.Card;
import io.robusta.hand.CardColor;
import io.robusta.hand.HandClassifier;
import io.robusta.hand.HandValue;
import io.robusta.hand.interfaces.IDeck;
import io.robusta.hand.interfaces.IHand;
import io.robusta.hand.interfaces.IHandResolver;

public class Hand extends TreeSet<Card> implements IHand {

	private static final long serialVersionUID = 7824823998655881611L;

	@Override
	public Set<Card> changeCards(IDeck deck, Set<Card> cards) {
		// For exemple remove three cards from this hand
		// , and get 3 new ones from the Deck
		// returns the new given cards
		Set<Card> finalCards = new TreeSet<>(this);
		
		if(cards !=null && cards.size()!=0){
			for(Card c : cards){
				finalCards.remove(c);
			}
			for(int i = 0; i<cards.size(); i++){
				finalCards.add(deck.pick());
			}
		}

		return finalCards;
	}

	@Override
	public boolean beats(IHand villain) {
		return this.getValue().compareTo(villain.getValue())>0;
	}

	@Override
	public IHand getHand() {
		return this;
	}

	@Override
	public HandClassifier getClassifier() {
		if (this.isStraightFlush()) {
			return HandClassifier.STRAIGHT_FLUSH;
		} else if (this.isFourOfAKind()) {
			return HandClassifier.FOUR_OF_A_KIND;
		} else if (this.isFull()) {
			return HandClassifier.FULL;
		} else if (this.isFlushOrMore()) {
			return HandClassifier.FLUSH;
		} else if (this.isStraightOrMore()) {
			return HandClassifier.STRAIGHT;
		} else if (this.isTripsOrMore()) {
			return HandClassifier.TRIPS;
		} else if (this.isDoublePairOrMore()) {
			return HandClassifier.TWO_PAIR;
		} else if (this.isPairOrMore()) {
			return HandClassifier.PAIR;
		}
		return HandClassifier.HIGH_CARD;
	}

	@Override
	public boolean isStraight() {
		if (this.isStraightFlush()) {
			return false;
		}
		return isStraightOrMore();
	}

	private boolean isStraightOrMore() {
		List<Card> cards = new ArrayList<>(this);
		for (int nbC = 1; nbC < cards.size(); nbC++) {
			if (cards.get(nbC).getValue() != cards.get(nbC-1).getValue() + 1) {
				boolean testAce = (nbC == cards.size()-1) && (cards.get(nbC).getValue() == 14) && (cards.get(0).getValue() == 2);
				if(!testAce){
					return false;
				}
			}
		}
		mainValue = this.last().getValue();
		remainings = new TreeSet<>();
		return true;
	}

	@Override
	public boolean isFlush() {
		if (this.isStraightFlush()) {
			return false;
		}
		return isFlushOrMore();
	}

	private boolean isFlushOrMore() {
		CardColor color = null;
		boolean colorSet = false;
		for (Card c : this) {
			if (!colorSet) {
				color = c.getColor();
				colorSet = true;
			}
			if (c.getColor().getValue() != color.getValue()) {
				return false;
			}
		}
		mainValue = this.last().getValue();
		remainings = new TreeSet<>(this);
		remainings.remove(this.last());
		return true;
	}

	/**
	 * Returns number of identical cards 5s5cAd2s3s has two cardValue of 5
	 */
	@Override
	public int number(int cardValue) {
		int result = 0;
		for (Card current : this) {
			if (current.getValue() == cardValue) {
				result++;
			}
		}
		return result;
	}

	/**
	 * The fundamental map Check the tests and README to understand
	 */
	@Override
	public HashMap<Integer, List<Card>> group() {
		HashMap<Integer, List<Card>> map = new HashMap<>();
		for (Card c : this) {
			List<Card> cards;
			if (!map.containsKey(c.getValue())) {
				cards = new ArrayList<>();
				cards.add(c);
				map.put(c.getValue(), cards);
			} else {
				cards = map.get(c.getValue());
				cards.add(c);
				map.put(c.getValue(), cards);
			}
		}
		return map;
	}

	// different states of the hand
	int mainValue;
//	int tripsValue;
//	int pairValue;
	int secondValue;
	TreeSet<Card> remainings;

	/**
	 * return all single cards not used to build the classifier
	 * 
	 * @param map
	 * @return
	 */
	TreeSet<Card> getGroupRemainingsCard(Map<Integer, List<Card>> map) {
		TreeSet<Card> groupRemaining = new TreeSet<>();
		// May be adapted at the end of project:
		// if straight or flush : return empty
		// If High card, return 4 cards

		for (List<Card> group : map.values()) {
			if (group.size() == 1) {
				groupRemaining.add(group.get(0));
			}
		}
		return groupRemaining;
	}

	@Override
	public boolean isPair() {
		if (this.isFourOfAKind() || this.isFull() || this.isTrips() || this.isDoublePair()) {
			return false;
		}
		return isPairOrMore();
	}

	private boolean isPairOrMore() {
		HashMap<Integer, List<Card>> map = this.group();
		for (int i = 1; i <= 13; i++) {
			if (this.number(i) == 2) {
				mainValue = i;
				completeRemainings(map, i);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDoublePair() {
		if (this.isFourOfAKind() || this.isFull()) {
			return false;
		}
		return isDoublePairOrMore();
	}

	private boolean isDoublePairOrMore() {
		HashMap<Integer, List<Card>> map = group();
		int pairNumber = 0;
		mainValue = 0;
		for (int i = 1; i <= 13; i++) {
			if (this.number(i) == 2) {
				pairNumber++;
				if(i>mainValue){
					secondValue = mainValue;
					mainValue = i;
				}else{
					secondValue = i;
				}
				completeRemainings(map, i);
			}
		}
		return pairNumber == 2;
	}

	@Override
	public boolean isHighCard() {
		if (!this.isPair() && !this.isDoublePair() && !this.isTrips() && !this.isStraight() && !this.isFlush()
				&& !this.isFull() && !this.isFourOfAKind() && !this.isStraightFlush()) {
			mainValue = 0;
			for(Card c : this){
				if(c.getValue()>mainValue){
					mainValue = c.getValue();
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isTrips() {
		if (this.isFourOfAKind() || this.isFull()) {
			return false;
		}
		return isTripsOrMore();
	}

	private boolean isTripsOrMore() {
		HashMap<Integer, List<Card>> map = group();
		if (map.size() != 3) {
			return false;
		}
		Iterator<Entry<Integer, List<Card>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, List<Card>> mapLine = it.next();
			if (mapLine.getValue().size() == 3) {
				mainValue = mapLine.getKey();
				completeRemainings(mapLine);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isFourOfAKind() {
		HashMap<Integer, List<Card>> map = this.group();
		if (map.size() != 2) {
			return false;
		}
		Iterator<Entry<Integer, List<Card>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, List<Card>> mapLine = it.next();
			if (mapLine.getValue().size() == 4) {
				mainValue = mapLine.getKey();
				completeRemainings(mapLine);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isFull() {
		HashMap<Integer, List<Card>> map = group();
		if (map.size() != 2) {
			return false;
		}
		if (!this.isFourOfAKind()) {
			Iterator<Entry<Integer, List<Card>>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, List<Card>> mapLine = it.next();
				if (mapLine.getValue().size() == 3) {
					mainValue = mapLine.getKey();
				}else{
					secondValue = mapLine.getKey();
				}
				remainings = new TreeSet<>();
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isStraightFlush() {
		if(this.isStraightOrMore() && this.isFlushOrMore()){
			mainValue = this.last().getValue();
			remainings = new TreeSet<>();
			return true;
		}
		return false;
	}

	@Override
	public HandValue getValue() {
		HandValue handValue = new HandValue();

		handValue.setClassifier(this.getClassifier());
		handValue.setLevelValue(this.mainValue);
		handValue.setOtherCards(this.remainings); // or this.getRemainings()
		return handValue;
		
	}

	@Override
	public boolean hasCardValue(int level) {
		for (Card c : this) {
			if (c.getValue() == level) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasAce() {
		return hasCardValue(14);
	}

	@Override
	public int highestValue() {
		for (int i = 14; i > 1; i--) {
			if (hasCardValue(i)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int compareTo(IHandResolver o) {
		return this.getValue().compareTo(o.getValue());
	}
	
	private void completeRemainings(HashMap<Integer, List<Card>> map, int i) {
		Iterator<Entry<Integer, List<Card>>> it = map.entrySet().iterator();
		remainings = new TreeSet<>(this);
		while (it.hasNext()) {
			Entry<Integer, List<Card>> mapLine = it.next();
			if (mapLine.getKey()==i) {
				for(Card c : mapLine.getValue()){
					remainings.remove(c);
				}
			}
		}
	}
	
	private void completeRemainings(Entry<Integer, List<Card>> mapLine) {
		remainings = new TreeSet<>(this);
		for(Card c : mapLine.getValue()){
			remainings.remove(c);
		}
	}

}
