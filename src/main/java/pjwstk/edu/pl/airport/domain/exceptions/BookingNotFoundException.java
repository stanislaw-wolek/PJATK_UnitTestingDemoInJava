package pjwstk.edu.pl.airport.domain.exceptions;

public class BookingNotFoundException extends RuntimeException {
	public BookingNotFoundException(String bookingReference) {
		super(String.format("Booking %s not found", bookingReference));
	}
}
