package edu.uwm.owyh.library;

public final class Utility {

	private Utility() {
		//prevents instantiation
	}

	/**
	 * Buids an array of the 50 state abbreviations. Useful for building state select lists.
	 * @return an array of the 50 state abbreviations
	 */
	public static String[] getStates(){
		String stateString = "AL,AK,AZ,AR,CA,CO,CT,DE,FL,GA,HI,ID,IL,IN,IA,KS,KY,LA,ME,MD,MA,MI,MN,"
				+ "MO,MS,MT,NE,NV,NH,NJ,NM,NY,NC,ND,OH,OK,OR,PA,RI,SC,SD,TN,TX,UT,VT,VA,WA,WV,WI,WY";
		
		String delim = "[,]";
		
		String[] states = stateString.split(delim);
		
		return states;
	}

}
