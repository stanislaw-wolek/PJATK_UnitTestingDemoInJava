package pjwstk.edu.pl.airport.repository;

import pjwstk.edu.pl.airport.domain.Flight;

import java.util.Optional;

public interface FlightRepository {

	Optional<Flight> findByNumber(String number);
	void save(Flight flight);
}
