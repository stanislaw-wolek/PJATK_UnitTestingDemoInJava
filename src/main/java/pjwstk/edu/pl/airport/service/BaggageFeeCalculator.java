package pjwstk.edu.pl.airport.service;

import pjwstk.edu.pl.airport.domain.exceptions.BaggageTooHeavyException;

public class BaggageFeeCalculator {

	public int calculate(int weightKg) {
		/*
		* Baggage Fees policy:
		* - 0 PLN if below 24kg;
		* - 100 PLN otherwise provided its below weight limit;
		* - Weight limit is 32kg.
		* */

		if (weightKg <= 0) {
			throw new IllegalArgumentException();
		} else if (weightKg < 24) {
			return 0;
		} else if (weightKg <= 32) {
			return 100;
		} else {
			throw new BaggageTooHeavyException(weightKg);
		}
	}
}
