package com.avanderbeck.september.character;

import java.util.Comparator;

public class MapUnitComparator implements Comparator<MapUnit> {

	@Override
	public int compare(MapUnit arg0, MapUnit arg1) {
		
		int retval = arg0.getUnitType().compareToIgnoreCase(arg1.getUnitType());
		
		if(retval == 0)
			retval = arg0.getName().compareToIgnoreCase(arg1.getName());
		
		return retval;
	}

}
