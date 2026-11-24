package pjwstk.edu.pl.airport.domain.exceptions;

public class FlightFullException extends RuntimeException {
	public FlightFullException(String flightNumber) {
		super(String.format("Flight %s is full", flightNumber));
	}
}
