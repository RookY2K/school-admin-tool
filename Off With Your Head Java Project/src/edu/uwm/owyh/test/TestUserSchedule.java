package edu.uwm.owyh.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import edu.uwm.owyh.model.UserSchedule;
import edu.uwm.owyh.model.UserScheduleElement;

public class TestUserSchedule {

	@Test
	public void testEmptyConstructor_arraySizeIs0() {
		UserSchedule schedule = new UserSchedule();

		assertEquals(0, schedule.size());
	}

	@Test
	public void testElementConstructor_arraySizeIs1() {
		UserScheduleElement e = new UserScheduleElement("TR", "12:25PM",
				"1:15PM", "PHY 125", "This is a Class");

		UserSchedule schedule = new UserSchedule(e);

		assertEquals(1, schedule.size());
	}

	@Test
	public void testElementConstructor_elementMatches() {
		UserScheduleElement e = new UserScheduleElement("TR", "12:25PM",
				"1:15PM", "PHY 125", "This is a Class");

		UserSchedule schedule = new UserSchedule(e);

		assertSame(e, schedule.getElement(0));
	}

}
