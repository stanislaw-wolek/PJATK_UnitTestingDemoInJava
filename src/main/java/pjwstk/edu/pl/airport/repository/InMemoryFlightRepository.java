package pjwstk.edu.pl.airport.repository;

import org.springframework.stereotype.Repository;
import pjwstk.edu.pl.airport.domain.Flight;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryFlightRepository implements FlightRepository {

	private final Map<String, Flight> flights = new ConcurrentHashMap<>();

	public InMemoryFlightRepository(Clock clock) {
		LocalDateTime now = LocalDateTime.now(clock);
		save(new Flight("XY100", now.plusHours(3), 180));
		save(new Flight("XY200", now.plusMinutes(30), 120));
		save(new Flight("XY300", now.plusHours(2), 1));
	}

	@Override
	public Optional<Flight> findByNumber(String number) {
		return Optional.ofNullable(flights.get(number));
	}

	@Override
	public void save(Flight flight) {
		flights.put(flight.getNumber(), flight);
	}
}
