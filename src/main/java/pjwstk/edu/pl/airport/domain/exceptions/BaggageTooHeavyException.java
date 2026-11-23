package pjwstk.edu.pl.airport.domain.exceptions;

public class BaggageTooHeavyException extends RuntimeException {
	public BaggageTooHeavyException(int weightKg) {
		super(String.format("Baggage of %d kg exceeds the weight limit", weightKg));
	}
}
