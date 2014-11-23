package edu.uwm.owyh.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.uwm.owyh.model.UserScheduleElement;

public class UserScheduleElementTest {
	private UserScheduleElement userScheduleElement;

	@Test
	public void constructor_parseTuesdayThursdayNoonToThreeTwentyFivePmRoomTitle_tuesdayThursdayForDay() {
		String dayString = "TR 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertEquals("TR", userScheduleElement.getDays());
	}

	@Test
	public void constructor_parseMondayWednesdayNoonToThreeTwentyFivePm_mondayWednesdayForDay() {
		String dayString = "MW 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertEquals("MW", userScheduleElement.getDays());
	}

	@Test
	public void constructor_parseMondayThroughFridayNoonToThreeTwentyFivePm_mondayThroughFridayForDay() {
		String dayString = "MTWRF 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertEquals("MTWRF", userScheduleElement.getDays());
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor_parseEmptyString_exceptionThrown() {
		String dayString = "";
		String room = "EMS 940";
		String title = "Test Title";

		new UserScheduleElement(dayString, room, title);
	}

	@Test
	public void constructor_parseMondayThroughFridayNoonToThreeTwentyFivePm_noonInDoubleForStartTime() {
		String dayString = "MTWRF 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertEquals(12.0d, userScheduleElement.getStartTime(), 0);
	}

	@Test
	public void constructor_parseMondayThroughFridayNoonToThreeTwentyFivePm_threePointTwoFiveInDoubleForEndTime() {
		String dayString = "MTWRF 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertEquals((15d + (25d / 60d)), userScheduleElement.getEndTime(), 0);
	}

	@Test
	public void constructor_parseMondayThroughFridayNoonToThreeTwentyFivePm_ems940ForRoom() {
		String dayString = "MTWRF 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertEquals("EMS 940", userScheduleElement.getRoom());
	}

	@Test
	public void constructor_parseMondayThroughFridayNoonToThreeTwentyFivePm_testTitleForTitle() {
		String dayString = "MTWRF 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertEquals("Test Title", userScheduleElement.getTitle());
	}

	@Test
	public void elementInRange_elementInRange_true() {
		String dayString = "MTWRF 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertTrue(userScheduleElement.isPartOfElement("M", "8:00AM", "6:00PM"));
	}

	@Test
	public void elementInRange_rangeInElement_true() {
		String dayString = "MTWRF 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertTrue(userScheduleElement.isPartOfElement("M", "1:00PM", "3:00PM"));
	}

	@Test
	public void elementInRange_elementStartsInRange_true() {
		String dayString = "MW 12:25PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertTrue(userScheduleElement.isPartOfElement("W", "12:00PM",
				"12:30PM"));
	}

	@Test
	public void elementInRange_elementEndsInRange_true() {
		String dayString = "TR 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertTrue(userScheduleElement.isPartOfElement("R", "3:00PM", "3:30PM"));

	}

	@Test
	public void elementInRange_elementBeforeRange_false() {
		String dayString = "TR 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertFalse(userScheduleElement
				.isPartOfElement("T", "5:00PM", "6:30PM"));

	}

	@Test
	public void elementInRange_elementAfterRange_false() {
		String dayString = "MWF 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertFalse(userScheduleElement
				.isPartOfElement("F", "8:00AM", "9:30AM"));

	}

	@Test
	public void elementInRange_elementWrongDay_false() {
		String dayString = "MWF 12:00PM-3:25PM";
		String room = "EMS 940";
		String title = "Test Title";

		userScheduleElement = new UserScheduleElement(dayString, room, title);

		assertFalse(userScheduleElement
				.isPartOfElement("R", "8:00AM", "9:30AM"));

	}

}