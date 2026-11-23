package pjwstk.edu.pl.airport.service;

import org.springframework.stereotype.Service;
import pjwstk.edu.pl.airport.domain.BoardingPass;
import pjwstk.edu.pl.airport.domain.Booking;
import pjwstk.edu.pl.airport.domain.Flight;
import pjwstk.edu.pl.airport.domain.exceptions.AlreadyCheckedInException;
import pjwstk.edu.pl.airport.domain.exceptions.BookingNotFoundException;
import pjwstk.edu.pl.airport.domain.exceptions.CheckInClosedException;
import pjwstk.edu.pl.airport.repository.BookingRepository;
import pjwstk.edu.pl.airport.repository.FlightRepository;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class CheckInService {
	private final BookingRepository bookingRepository;
	private final FlightRepository flightRepository;
	private final BoardingPassSender boardingPassSender;
	private final Clock clock;

	private static final int CHECK_IN_CLOSES_MINUTES_BEFORE = 40;

	public CheckInService(BookingRepository bookingRepository, FlightRepository flightRepository, BoardingPassSender boardingPassSender, Clock clock) {
		this.bookingRepository = bookingRepository;
		this.flightRepository = flightRepository;
		this.boardingPassSender = boardingPassSender;
		this.clock = clock;
	}

	public BoardingPass checkIn(String bookingReference) {
		Booking booking = bookingRepository.findByReference(bookingReference)
				.orElseThrow(() -> new BookingNotFoundException(bookingReference));
		if (booking.isCheckedIn()) {
			throw new AlreadyCheckedInException(bookingReference);
		}
		Flight flight = flightRepository.findByNumber(booking.getFlightNumber())
				.orElseThrow(() -> new IllegalStateException("Flight " + booking.getFlightNumber() + " not found"));
		LocalDateTime now = LocalDateTime.now(clock);
		if (now.isAfter(flight.getDepartureTime().minusMinutes(CHECK_IN_CLOSES_MINUTES_BEFORE))) {
			throw new CheckInClosedException(bookingReference);
		}
		int seat = flight.assignNextSeat();
		booking.markCheckedIn();
		flightRepository.save(flight);
		bookingRepository.save(booking);
		BoardingPass pass = new BoardingPass(bookingReference, flight.getNumber(), seat);
		boardingPassSender.send(pass);
		return pass;
	}
}
