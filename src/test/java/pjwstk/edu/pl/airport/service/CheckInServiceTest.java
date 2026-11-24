package pjwstk.edu.pl.airport.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjwstk.edu.pl.airport.domain.BoardingPass;
import pjwstk.edu.pl.airport.domain.Booking;
import pjwstk.edu.pl.airport.domain.Flight;
import pjwstk.edu.pl.airport.domain.exceptions.AlreadyCheckedInException;
import pjwstk.edu.pl.airport.domain.exceptions.CheckInClosedException;
import pjwstk.edu.pl.airport.domain.exceptions.FlightFullException;
import pjwstk.edu.pl.airport.repository.BookingRepository;
import pjwstk.edu.pl.airport.repository.FlightRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInServiceTest {

	private static final LocalDateTime NOW = LocalDateTime.of(2026, 12, 1, 10, 0);

	@Mock
	BookingRepository bookingRepository;
	@Mock
	FlightRepository flightRepository;
	@Mock
	BoardingPassSender boardingPassSender;

	Clock clock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
	CheckInService service;

	@BeforeEach
	void setUp() {
		service = new CheckInService(bookingRepository, flightRepository, boardingPassSender, clock);
	}

	@Test
	void shouldAllowCheckIn_exactly40MinutesBeforeDeparture() {
		// arrange
		Booking booking = new Booking("ABC123", "Jan Kowalski", "XY100");
		Flight flight = new Flight("XY100", NOW.plusMinutes(40), 180);
		when(bookingRepository.findByReference("ABC123")).thenReturn(Optional.of(booking));
		when(flightRepository.findByNumber("XY100")).thenReturn(Optional.of(flight));

		// act
		BoardingPass pass = service.checkIn("ABC123");

		// assert
		assertThat(pass.seatNumber()).isEqualTo(1);
	}

	@Test
	void shouldRejectCheckIn_whenLessThan40MinutesBeforeDeparture() {
		// arrange
		Booking booking = new Booking("ABC123", "Jan Kowalski", "XY100");
		Flight flight = new Flight("XY100", NOW.plusMinutes(30), 180);
		when(bookingRepository.findByReference("ABC123")).thenReturn(Optional.of(booking));
		when(flightRepository.findByNumber("XY100")).thenReturn(Optional.of(flight));

		// act + assert
		assertThatThrownBy(() -> service.checkIn("ABC123"))
				.isInstanceOf(CheckInClosedException.class);

		verify(boardingPassSender, never()).send(any());
		verify(bookingRepository, never()).save(any());
		verify(flightRepository, never()).save(any());
	}

	@Test
	void shouldRejectCheckIn_whenPassengerAlreadyCheckedIn() {
		// arrange
		Booking booking = new Booking("ABC123", "Jan Kowalski", "XY100");
		booking.markCheckedIn();
		when(bookingRepository.findByReference("ABC123")).thenReturn(Optional.of(booking));
		// no stub on purpose for flightRepository: service doesn't reach call

		// act + assert
		assertThatThrownBy(() -> service.checkIn("ABC123"))
				.isInstanceOf(AlreadyCheckedInException.class);
		verifyNoInteractions(flightRepository, boardingPassSender);
	}

	@Test
	void shouldRejectCheckIn_whenFlightIsFull() {
		// arrange: flight with both seats occupied
		Booking booking = new Booking("ABC123", "Jan Kowalski", "XY100");
		Flight flight = new Flight("XY100", NOW.plusHours(2), 2);
		flight.assignNextSeat();
		flight.assignNextSeat();
		when(bookingRepository.findByReference("ABC123")).thenReturn(Optional.of(booking));
		when(flightRepository.findByNumber("XY100")).thenReturn(Optional.of(flight));

		// act + assert
		assertThatThrownBy(() -> service.checkIn("ABC123"))
				.isInstanceOf(FlightFullException.class);
		verify(boardingPassSender, never()).send(any());
	}
}