package edu.uwm.owyh.model;
import edu.uwm.owyh.library.StringHelper;

public class CellObject{
	UserScheduleElement element;
	String cellType;
	String spanType;
	String rowSpan;
	
	public CellObject(UserScheduleElement e, String type, String span, double rowS)
	{
		element = e;
		cellType = type;
		spanType = span;
		rowSpan = Double.toString(rowS * 2);
	}
	
	public String getTitle()
	{
		return element.getTitle();
	}
	
	public String getRoom()
	{
		return element.getRoom();
	}
	
	public String getHours()
	{
		String hours;
		String sTime = StringHelper.timeToString(element.getStartTime());
		String eTime = StringHelper.timeToString(element.getEndTime());
		if(sTime.equals("") && eTime.equals("")) return "";
		hours = sTime + " - " + eTime;
		return hours;
	}
	
	public String getType()
	{
		return cellType;
	}
	
	public String getSpanType()
	{
		return spanType;
	}
	
	public String getRowSpan()
	{
		return rowSpan;
	}
	
	public CellObject[][] configure(CellObject[][] array)
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
