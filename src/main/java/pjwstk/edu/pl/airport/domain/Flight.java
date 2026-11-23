package pjwstk.edu.pl.airport.domain;

import java.time.LocalDateTime;

public class Flight {

	private final String number;
	private final LocalDateTime departureTime;
	private final int capacity;
	private int checkedInCount = 0;

	public Flight(String number, LocalDateTime departureTime, int capacity) {
		this.number = number;
		this.departureTime = departureTime;
		this.capacity = capacity;
	}

	public String getNumber() {
		return number;
	}

	public LocalDateTime getDepartureTime() {
		return departureTime;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getCheckedInCount() {
		return checkedInCount;
	}

	public int assignNextSeat() {
		return ++this.checkedInCount;
	}
}
