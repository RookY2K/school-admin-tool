package edu.uwm.owyh.model;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class UserScheduleElement {
	String days;
	double startTime;
	double endTime;
	String room;
	String title;

	public UserScheduleElement(String scheduleString, String room, String title)
			throws IllegalArgumentException {
		try {
			days = parseDays(scheduleString);
			List<String> times = parseTimes(scheduleString);
			startTime = parseTime(times.get(0));
			endTime = parseTime(times.get(1));

			if (room != null && room.isEmpty() == false) {
				this.room = room;
			} else
				throw new IllegalArgumentException("Room must have a value");

			if (title != null && title.isEmpty() == false) {
				this.title = title;
			} else
				throw new IllegalArgumentException("Title must have a value");
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			throw e;
		}
	}

	public UserScheduleElement(String days, String startTime, String endTime,
			String room, String title) {
		try {
			this.days = days;
			this.startTime = parseTime(startTime);
			this.endTime = parseTime(endTime);
			this.room = room;
			this.title = title;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private double parseTime(String time) {
		List<String> splitTime = Arrays.asList(time.split(":"));

		double hours = Double.valueOf(splitTime.get(0));
		double mins = Double.valueOf(splitTime.get(1).substring(0, 2)) / 60;

		if (parseAmPm(splitTime.get(1)).charAt(0) == 'P') {
			/* PM */
			if (hours != 12)
				hours += 12;
		} else {
			/* AM */
			if (hours == 0)
				hours += 12;
		}

		return hours + mins;
	}

	private String parseAmPm(String time) {
		return time.substring(2);
	}

	private String parseDays(String dayString) {
		if (!StringUtils.isBlank(dayString)) {
			return dayString.substring(0, dayString.indexOf(" "));
		}
		throw new IllegalArgumentException("Day String must have a value");
	}

	private List<String> parseTimes(String dayString) {
		String times = dayString.substring(dayString.indexOf(" "));

		return Arrays.asList(times.split("-"));
	}

	public String getDays() {
		return days;
	}

	public double getStartTime() {
		return startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public String getRoom() {
		return room;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * Checks to determine if the given time period is part of this element.
	 * 
	 * @param day
	 * @param startOfRange
	 * @param endOfRange
	 * @return
	 */
	public boolean isPartOfElement(CharSequence day, String startOfRange,
			String endOfRange) {

		double startOfRangeVal = parseTime(startOfRange);
		double endOfRangeVal = parseTime(endOfRange);

		if (days.contains(day)) {

			if (isElementInsideRange(startOfRangeVal, endOfRangeVal))
				return true;

			if (doesElementStartInRange(startOfRangeVal, endOfRangeVal))
				return true;

			if (doesElementEndInRange(startOfRangeVal, endOfRangeVal))
				return true;

			if (isRangeInsideElement(startOfRangeVal, endOfRangeVal))
				return true;
		}

		return false;

	}

	private boolean isElementInsideRange(double startOfRange, double endOfRange) {
		if (startTime > startOfRange && endTime < endOfRange)
			return true;

		return false;
	}

	private boolean isRangeInsideElement(double startOfRange, double endOfRange) {
		if (startTime < startOfRange && endTime > endOfRange)
			return true;

		return false;
	}

	private boolean doesElementStartInRange(double startOfRange,
			double endOfRange) {
		if (startTime >= startOfRange && startTime < endOfRange)
			return true;

		return false;
	}

	private boolean doesElementEndInRange(double startOfRange, double endOfRange) {
		if (endTime >= startOfRange && endTime < endOfRange)
			return true;

		return false;
	}
}