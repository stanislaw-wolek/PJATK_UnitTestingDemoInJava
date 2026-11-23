package pjwstk.edu.pl.airport.repository;

import org.springframework.stereotype.Repository;
import pjwstk.edu.pl.airport.domain.Booking;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBookingRepository implements BookingRepository {

	private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

	public InMemoryBookingRepository() {
		save(new Booking("ABC123", "Jan Kowalski", "XY100"));
		save(new Booking("DEF456", "Anna Nowak", "XY100"));
		save(new Booking("GHI789", "Piotr Wiśniewski", "XY200"));
		save(new Booking("JKL012", "Ewa Zielińska", "XY300"));
		save(new Booking("MNO345", "Adam Lewandowski", "XY300"));
	}

	@Override
	public Optional<Booking> findByReference(String reference) {
		return Optional.ofNullable(bookings.get(reference));
	}

	@Override
	public void save(Booking booking) {
		bookings.put(booking.getReference(), booking);
	}
}
