package pjwstk.edu.pl.airport.domain.exceptions;

public class AlreadyCheckedInException extends RuntimeException {
	public AlreadyCheckedInException(String bookingReference) {
		super(String.format("Booking %s is already checked in", bookingReference));
	}
}
