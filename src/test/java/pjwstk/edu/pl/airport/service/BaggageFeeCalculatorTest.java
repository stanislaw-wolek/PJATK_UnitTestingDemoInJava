package pjwstk.edu.pl.airport.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import pjwstk.edu.pl.airport.domain.exceptions.BaggageTooHeavyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class BaggageFeeCalculatorTest {
	/*
	* You can run test with UI in IDE or:
	* .\mvnw.cmd clean test
	* .\mvnw.cmd -Dtest=BaggageFeeCalculatorTest test
	* .\mvnw.cmd '-Dtest=BaggageFeeCalculatorTest$ValidWeightTests#shouldReturnZeroWhenWeightIsBelow24kg' test
	*
	* Results are available in Airport/target/surefire-reports
	* */

	private final BaggageFeeCalculator calculator = new BaggageFeeCalculator();

	@Nested
	@DisplayName("Valid baggage weight")
	class ValidWeightTests {
		@Test
		void shouldReturnZeroWhenWeightIsBelow24kg() {
			int fee = calculator.calculate(20);
			assertThat(fee).isZero();
		}

		@Test
		void shouldReturn100WhenWeightIs24kg() {
			int fee = calculator.calculate(24);
			assertThat(fee).isEqualTo(100);
		}

		@Test
		@Tag("boundary")
		void shouldReturn100WhenWeightIs32kg() {
			int fee = calculator.calculate(32);
			assertThat(fee).isEqualTo(100);
		}
	}

	@Nested
	@DisplayName("Invalid baggage weight")
	class InvalidWeightTests {
		@Test
		void shouldThrowIllegalArgumentExceptionWhenWeightIs0kg() {
			assertThatThrownBy(() -> calculator.calculate(0))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest(name = "weight {0} kg is invalid")
		@ValueSource(ints = {0, -1, -20})
		void shouldRejectNonPositiveWeight(int weightKg) {
			assertThatThrownBy(() -> calculator.calculate(weightKg))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void shouldThrowBaggageTooHeavyExceptionWhenWeightIsAbove32kg() {
			assertThatThrownBy(() -> calculator.calculate(33))
					.isInstanceOf(BaggageTooHeavyException.class)
					.hasMessageContaining("33");
		}
	}

	@ParameterizedTest(name = "{0} kg -> {1} PLN")
	@CsvSource({
			"1, 0",
			"23, 0",
			"24, 100",
			"30, 100",
			"32, 100"
	})
	void shouldCalculateCorrectFeeForValidWeight(int weightKg, int expectedFee) {
		assertThat(calculator.calculate(weightKg))
				.isEqualTo(expectedFee);
	}

	@Test
	@Disabled("Not implemented yet")
	void shouldCalculateSpecialBaggageFee() {
		//
	}
}