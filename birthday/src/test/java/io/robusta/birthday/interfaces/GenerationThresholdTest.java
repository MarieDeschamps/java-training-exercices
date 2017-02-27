package io.robusta.birthday.interfaces;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.robusta.birthday.implementations.Generation;
import io.robusta.birthday.implementations.GenerationThreshold;
import io.robusta.birthday.implementations.PeopleCollection;

public class GenerationThresholdTest {

    GenerationThreshold generationThreshold;

    @Before
    public void setUp() throws Exception {
    	generationThreshold = new GenerationThreshold();
    }
	
//    @Test
//    public void create() throws Exception {
//    	GenerationThreshold generationThresholdTest = new GenerationThreshold(1000);
//        assertTrue(generationThresholdTest.size() == 10);
//        assertTrue(generationThresholdTest.get(3).size() == 40);
//    }
    
	@Test
	public void testCalculateProbabilityOfSame() {
		 // one people : none should have same
        generationThreshold = new GenerationThreshold();
        assertTrue(generationThreshold.calculateProbabilityOfSame(1) == 0);

        // 366 people : all should have same
        generationThreshold = new GenerationThreshold();
        assertTrue(generationThreshold.calculateProbabilityOfSame(366) == 1);

        // 30 people : some should have same
        generationThreshold = new GenerationThreshold();
        assertTrue(generationThreshold.calculateProbabilityOfSame(30) > 0.01);

        assertTrue(generationThreshold.calculateProbabilityOfSame(30)< 1);
	}
	
	@Test
	public void testFindSmallestNumberOfPeopleRequiredToHave50() {
		generationThreshold = new GenerationThreshold();
		int nbPeople;
		try {
			nbPeople = generationThreshold.findSmallestNumberOfPeopleRequiredToHave50();
			System.out.println(nbPeople);
	        assertTrue(nbPeople==23);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		

	}

}
