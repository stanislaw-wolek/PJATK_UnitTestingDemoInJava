package pjwstk.edu.pl.airport.repository;

import pjwstk.edu.pl.airport.domain.Booking;

import java.util.Optional;

public interface BookingRepository {

	Optional<Booking> findByReference(String reference);
	void save(Booking booking);
}
