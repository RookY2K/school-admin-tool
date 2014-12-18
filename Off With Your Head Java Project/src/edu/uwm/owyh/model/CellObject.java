package edu.uwm.owyh.model;
import edu.uwm.owyh.library.StringHelper;

/**
 * 
 * @author Mitchell
 *
 */

public class CellObject{
	UserScheduleElement element;
	String cellType;
	String spanType;
	String rowSpan;
	
	private CellObject(){
		//default constructor
	}
	
	private CellObject(UserScheduleElement e, String type, String span, double rowS)
	{
		element = e;
		cellType = type;
		spanType = span;
		rowSpan = Double.toString(rowS * 2);
	}
	
	/**
	 * 
	 * @return a new CellObject with empty fields.
	 */
	public static CellObject getCellObject(){
		return new CellObject();
	}
	
	/**
	 * 
	 * @param e
	 * @param type
	 * @param span
	 * @param rowS
	 * @return a new CellObject with given params.
	 */
	public static CellObject getCellObject(UserScheduleElement e, String type, String span, double rowS){
		return new CellObject(e, type, span, rowS);
	}
	
	/**
	 * 
	 * @return the title
	 */
	public String getTitle()
	{
		return element.getTitle();
	}
	
	/**
	 * 
	 * @return the room
	 */
	public String getRoom()
	{
		return element.getRoom();
	}
	
	/**
	 * 
	 * @return the hours
	 */
	public String getHours()
	{
		String hours;
		String sTime = StringHelper.timeToString(element.getStartTime());
		String eTime = StringHelper.timeToString(element.getEndTime());
		if(sTime.equals("") && eTime.equals("")) return "";
		hours = sTime + " - " + eTime;
		return hours;
	}
	
	/**
	 * 
	 * @return the type
	 */
	public String getType()
	{
		return cellType;
	}
	
	/**
	 * 
	 * @return the spanType
	 */
	public String getSpanType()
	{
		return spanType;
	}
	
	/**
	 * 
	 * @return the rowSpan
	 */
	public String getRowSpan()
	{
		return rowSpan;
	}
	
	/**
	 * Utility method to configure array using correct rowSpans from CellObjects.
	 * @param array
	 * @return A new array with correct elements in each index.
	 */
	public static CellObject[][] configure(CellObject[][] array)
	{
		CellObject[][] newArray = array.clone();
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 30; j++)
			{
				if(newArray[i][j] != null)
				{
					int length = (int) Double.parseDouble(newArray[i][j].rowSpan);
					if(length > 1)
					{
						for(int k = 1; k < length; k++)
						{
							newArray[i][j+k] = null;
						}
					}
				}
			}
		}
		return newArray;
	}
}
