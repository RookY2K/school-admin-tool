package edu.uwm.owyh.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Mike Laabs
 * 
 *         This class is simply a storage class for schedule information to make
 *         it easier to display when we want to write it to the calendar.
 *
 */
public class UserSchedule {

	List<UserScheduleElement> schedule;

	public UserSchedule(List<UserScheduleElement> list) {
		schedule = new ArrayList<UserScheduleElement>();

		for (UserScheduleElement e : list) {
			schedule.add(e);
		}
	}

	public UserSchedule(UserScheduleElement e) {
		schedule = new ArrayList<UserScheduleElement>();

		schedule.add(e);
	}

	public UserSchedule() {
		schedule = new ArrayList<UserScheduleElement>();
	}

	public void addElement(UserScheduleElement e) {
		schedule.add(e);
	}

	public int size() {
		return schedule.size();
	}
	
	public UserScheduleElement getElement(int index)
	{
		return schedule.get(index);
	}
}
