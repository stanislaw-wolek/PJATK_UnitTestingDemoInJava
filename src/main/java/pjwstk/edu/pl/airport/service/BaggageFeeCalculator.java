package pjwstk.edu.pl.airport.service;

import pjwstk.edu.pl.airport.domain.exceptions.BaggageTooHeavyException;

public class BaggageFeeCalculator {

	public int calculate(int weightKg) {
		if (weightKg <= 0) {
			throw new IllegalArgumentException();
		} else if (weightKg <= 24) {
			return 0;
		} else if (weightKg <= 32) {
			return 100;
		} else {
			throw new BaggageTooHeavyException(weightKg);
		}
	}
}
