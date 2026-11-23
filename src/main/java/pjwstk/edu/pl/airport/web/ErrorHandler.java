package pjwstk.edu.pl.airport.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pjwstk.edu.pl.airport.domain.exceptions.AlreadyCheckedInException;
import pjwstk.edu.pl.airport.domain.exceptions.BookingNotFoundException;
import pjwstk.edu.pl.airport.domain.exceptions.CheckInClosedException;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(BookingNotFoundException.class)
	public ProblemDetail handleNotFound(BookingNotFoundException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler({AlreadyCheckedInException.class, CheckInClosedException.class})
	public ProblemDetail handleConflict(RuntimeException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
	}
}
