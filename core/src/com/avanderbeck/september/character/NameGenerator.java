package com.avanderbeck.september.character;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameGenerator {
	List<String> firstNames;
	List<String> lastNames;
	List<String> specialNames;
	List<String> usedNames;
	
	public NameGenerator()
	{
		firstNames = new ArrayList<String>();
		lastNames = new ArrayList<String>();
		specialNames = new ArrayList<String>();
		usedNames = new ArrayList<String>();
	}
	
	public String getNewName()
	{
		String retval;
		Random r = new Random();
		
		do{
			if(r.nextInt(100) == 99 && !specialNames.isEmpty())
				retval = getSpecialName();
			else
			{
				retval = firstNames.get(r.nextInt(firstNames.size()));
				retval += " ";
				retval += lastNames.get(r.nextInt(lastNames.size()));
			}
		}while(isUsed(retval));
		
		usedNames.add(retval);
		
		return retval;
	}
	
	public String getSpecialName()
	{
		String retval;
		Random r = new Random();
		retval = specialNames.get(r.nextInt(specialNames.size()));
		specialNames.remove(retval);
		return retval;
		
	}
	
	private boolean isUsed(String name)
	{
		return usedNames.contains(name);
	}
}
