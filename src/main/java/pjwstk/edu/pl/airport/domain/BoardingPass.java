package pjwstk.edu.pl.airport.domain;

public record BoardingPass(
		String bookingReference,
		String flightNumber,
		int seatNumber
) {}
