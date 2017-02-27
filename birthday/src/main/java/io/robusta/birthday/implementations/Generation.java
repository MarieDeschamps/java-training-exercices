package io.robusta.birthday.implementations;

import io.robusta.birthday.interfaces.IGeneration;
import io.robusta.birthday.interfaces.IPeopleCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Zozol on 04/10/2016.
 */
public class Generation implements IGeneration{

    List<PeopleCollection> collections;

    public Generation(){

    }

    public Generation(int n, int collectionSize) {
        this.collections = createAllRandom(n, collectionSize);
    }

    @Override
    public PeopleCollection createRandom(int size) {
    	return new PeopleCollection(size);
    }

    @Override
    public List<PeopleCollection> createAllRandom(int n, int size) {
        // call n times createRandom(size)
    	List<PeopleCollection> listPeopleCollection = new ArrayList<>();
    	for(int pCollection = 0; pCollection < n; pCollection++){
    		listPeopleCollection.add(createRandom(size));
    	}
        return listPeopleCollection;
    }

    @Override
    public List<PeopleCollection> getPeopleCollections() {
        return collections;
    }

    @Override
    public int getNumberOfCollectionsThatHasTwoPeopleWithSameBirthday() {
        int result = 0;
        
        for(PeopleCollection peopleCollection : collections){
        	if(peopleCollection.hasSame()){
        		result++;
        	}
        }
        	
    	return result;
    }

    @Override
    public boolean isLessThan50() {
    	int nbCollections = collections.size();
    	int nbSame = getNumberOfCollectionsThatHasTwoPeopleWithSameBirthday();

    	return nbSame * 100 < 50 * nbCollections;
    }


}
