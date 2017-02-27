package io.robusta.birthday.implementations;

import io.robusta.birthday.interfaces.IPeople;
import io.robusta.birthday.interfaces.IPeopleCollection;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nicolas Zozol on 04/10/2016.
 */
public class PeopleCollection extends ArrayList<People>implements IPeopleCollection<People> {

	public PeopleCollection() {

	}

	public PeopleCollection(int size) {
		super(size);
		for (int i = 0; i < size; i++) {
			People people = new People();
			Random random = new Random();
			int date = random.nextInt(365) + 1;
			people.setBirthday(date);
			this.add(people);
		}
	}

	@Override
	public boolean hasSame() {
		int size = this.size();
		for(int p = 0; p<size-1; p++){
			for(int pComp = p + 1; pComp < size; pComp++){
				if(this.get(p).equals(this.get(pComp))){
					return true;
				}
			}
		}
		
		return false;
	}

}
