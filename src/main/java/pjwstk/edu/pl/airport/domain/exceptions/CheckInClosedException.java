package pjwstk.edu.pl.airport.domain.exceptions;

public class CheckInClosedException extends RuntimeException {
	public CheckInClosedException(String bookingReference) {
		super(String.format("Check-in for booking %s is closed", bookingReference));
	}
}
