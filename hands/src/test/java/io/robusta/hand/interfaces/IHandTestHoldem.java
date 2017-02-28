package io.robusta.hand.interfaces;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.TreeSet;

import io.robusta.hand.Card;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.robusta.hand.HandClassifier;
import io.robusta.hand.PokerTest;
import io.robusta.hand.solution.DeckGenerator;
import io.robusta.hand.solution.Hand;
import io.robusta.hand.solution.HandHoldem;

public class IHandTestHoldem extends PokerTest {

	
	IDeck deck;

	@Before
	public void setUp() throws Exception {
		DeckGenerator generator = new DeckGenerator();
		this.deck = generator.generate();
	}

	@After
	public void tearDown() throws Exception {
	}

	public IHand newHand(String cards) {
		HandHoldem h = new HandHoldem();
		h.addAll(generateCards(cards));
		return h;
	}

	public IHand randomHand(String cards) {
		return deck.giveHandHoldem();
	}

	@Test
	public void testChangeCards() {
		HandHoldem h = deck.giveHandHoldem();
		Set<Card> cards;
		
		cards = h.changeCards(deck, new TreeSet<>());
		assertTrue(cards.size()==5);
		
		cards = h.changeCards(deck, null);
		assertTrue(cards.size()==5);

		cards = h.changeCards(deck, new TreeSet<>(h));
		System.out.println("before: "+h+"\nafter: "+cards);
		assertFalse(h.equals(cards));
		assertTrue(cards.size()==5);
		
	}

	@Test
	public void testNumber() {
		IHand hand = newHand("4c 4s 2s 3s 6h");
		assertTrue(hand.number(4) ==2);
		
		hand = newHand("4c 3s 3h 3c 6h");
		assertTrue(hand.number(3) ==3);
		
		hand = newHand("4c 4s 2s 3s 6h");
		assertTrue(hand.number(Card.AS_VALUE) ==0);
		
		hand = newHand("4c 4s 2s As Ah");
		assertTrue(hand.number(Card.AS_VALUE) ==2);
	}

	@Test
	public void testGroup() {
		IHand hand = newHand("4c 4s 2s As Ah");
		assertTrue(hand.group().get(4).size()==2);
		assertTrue(hand.group().get(Card.AS_VALUE).size()==2);
		assertTrue(hand.group().get(12)==null);
		assertTrue(hand.group().get(2).size()==1);
	}

	@Test
	public void testIsStraight() {
		IHand hand = newHand("4c 5c 2s 3s 6h 6d As");
		assertTrue(hand.toString(), hand.isStraight());		
	}
	
	@Test
	public void testIsStraightWithAce() {
		
		IHand hand = newHand("4c 5c 2s 3s Ah Qh Qd");
		hand.isStraight();
		assertTrue(hand.toString(), hand.isStraight());
		
		hand = newHand("Tc Jc As Qs Kh 2s Qd");
		assertTrue(hand.toString(), hand.isStraight());
	}

	@Test
	public void testIsFlush() {
		IHand hand = newHand("4c 5c 2d 3c Qc Kc Tc");
		assertTrue(hand.toString(), hand.isFlush());
		hand = newHand("4c 5c 2c 3c Qs Kd Ah");
		assertFalse(hand.toString(), hand.isFlush());
	}
	
	

	@Test
	public void testTwoPair() {
		IHand hand = newHand("4c 4h 2c 2s Qc");
		assertEquals(HandClassifier.TWO_PAIR, hand.getClassifier());
		
		hand = newHand("4c 5c 2c 3c Qc");
		assertTrue(hand.toString(), hand.isFlush());
	}
	
	@Test
	public void testPair() {
		IHand hand = newHand("4c Kh 2c 2s Qc");
		assertTrue(hand.getClassifier() == HandClassifier.PAIR);
	}
	
	@Test
	public void testStraightFlush() {
		IHand hand = newHand("2c 3c 4c 5c 6c");
//		System.out.println(hand.isStraightFlush());
		assertTrue(hand.getClassifier() == HandClassifier.STRAIGHT_FLUSH);
	}
	
	@Test
	public void testBeats() {
		IHand hand1 = newHand("4c Kh 2c 2s Qc");
		IHand hand2 = newHand("2c 3c 4c 5s 6c");
		assertTrue(hand2.beats(hand1));
		
		hand1 = newHand("2d 2h 2c 2s Qc");
		hand2 = newHand("2c 3c 4c 5s 6c");
		assertTrue(hand1.beats(hand2));
		
		hand1 = newHand("Td Th Kc 2s Qc");
		hand2 = newHand("Tc Ts 4c 5s 6c");
		assertTrue(hand1.beats(hand2));
		
		hand1 = newHand("Td Th Kc Ks Qc");
		hand2 = newHand("Tc Ts Kh Kd 6c");
		assertTrue(hand1.beats(hand2));
	}
	
	@Test
	public void testHasCardValue(){
		IHand hand = newHand("4c Kh 2c 2s Qc");
		assertTrue(hand.hasCardValue(13));
		assertFalse(hand.hasCardValue(1));
	}
	
	@Test
	public void testHasAce(){
		IHand hand = newHand("4c Kh 2c 2s Qc");
		assertFalse(hand.hasAce());
		
		hand = newHand("Ac Kh 2c 2s Qc");
		assertTrue(hand.hasAce());
	}
	
	@Test
	public void testHighestValue(){
		IHand hand = newHand("4c Kh 2c 2s Qc");
		assertEquals(13, hand.highestValue());
		
		hand = newHand("Ac Kh 2c 1s Qc");
		assertEquals(14, hand.highestValue());
	}

}
