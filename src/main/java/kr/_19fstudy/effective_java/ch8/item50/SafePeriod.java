package kr._19fstudy.effective_java.ch8.item50;

import java.util.Date;

public class SafePeriod {

	private final Date start;
	private final Date end;

	public SafePeriod(Date start, Date end) {
		this.start = new Date(start.getTime());
		this.end = new Date(end.getTime());

		if (start.compareTo(end) > 0) {
			throw new IllegalArgumentException();
		}
	}

	public Date start() {
		return new Date(start.getTime());
	}

	public Date end() {
		return new Date(end.getTime());
	}

	public static void main(String[] args) {
		Date start = new Date();
		Date end = new Date();
		Period p = new Period(start, end);
	}
}
