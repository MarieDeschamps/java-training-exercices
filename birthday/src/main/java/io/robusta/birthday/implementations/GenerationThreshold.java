package io.robusta.birthday.implementations;

import io.robusta.birthday.interfaces.IGenerationThreshold;
import io.robusta.birthday.interfaces.IPeopleCollection;

import java.util.ArrayList;

/**
 * Created by Nicolas Zozol on 04/10/2016.
 */
public class GenerationThreshold implements IGenerationThreshold{

	static int numberOfCollections = 10000;

    public GenerationThreshold() {

    }


    @Override
    public int getSmallNumber() {
        return 20;
    }

    @Override
    public int getBigNumber() {
        return 30;
    }

    @Override
//    public int findSmallestNumberOfPeopleRequiredToHave50() throws Exception {
//        double probaObj = 0.5;
//        boolean found = false;
//        int nbPeople = this.getSmallNumber();
//        
//        while(!found &&  nbPeople< this.getBigNumber()){
//        	if(this.calculateProbabilityOfSame(nbPeople)>probaObj)
//        		found = true;
//        	else
//        		nbPeople++;
//        }
//        
//        if(!found)
//        	 throw new Exception("Value not in range : use a smaller getSmallNumber and/or a bigger getbigNumber" );
//        else
//        	return nbPeople;
//       
//    }
//    
    public int findSmallestNumberOfPeopleRequiredToHave50() throws Exception {
        double probaObj = 0.5;
        int nbPeopleMin = this.getSmallNumber();
        int nbPeopleMax = this.getBigNumber();
        
        if(!(this.calculateProbabilityOfSame(nbPeopleMin)<=0.5 && this.calculateProbabilityOfSame(nbPeopleMax)>=0.5)){
        	throw new Exception("Value not in range : use a smaller getSmallNumber and/or a bigger getbigNumber" );
        }else if(this.calculateProbabilityOfSame(nbPeopleMin)==0.5){
        	return nbPeopleMin;
        }else if(this.calculateProbabilityOfSame(nbPeopleMax)==0.5){
        	return nbPeopleMax;
        }
        
        while(nbPeopleMin < nbPeopleMax-1){
        	int nbPeopleMid = (nbPeopleMax + nbPeopleMin)/2;
        	if(this.calculateProbabilityOfSame(nbPeopleMid)>probaObj)
        		nbPeopleMax=nbPeopleMid;
        	else
        		nbPeopleMin=nbPeopleMid;
        }
        
        	return nbPeopleMax;
    }


	@Override
	public float calculateProbabilityOfSame(int size) {
		Generation generation = new Generation(numberOfCollections, size);
		int nbCollections = generation.collections.size();
    	float nbSame = (float) generation.getNumberOfCollectionsThatHasTwoPeopleWithSameBirthday();

    	return nbSame/nbCollections;
	}
}
