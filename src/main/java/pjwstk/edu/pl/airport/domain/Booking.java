package pjwstk.edu.pl.airport.domain;

public class Booking {

	private final String reference;
	private final String passengerName;
	private final String flightNumber;
	private boolean checkedIn = false;

	public Booking(String reference, String passengerName, String flightNumber) {
		this.reference = reference;
		this.passengerName = passengerName;
		this.flightNumber = flightNumber;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public String getReference() {
		return reference;
	}

	public boolean isCheckedIn() {
		return this.checkedIn;
	}

	public void markCheckedIn() {
		this.checkedIn = true;
	}
}
