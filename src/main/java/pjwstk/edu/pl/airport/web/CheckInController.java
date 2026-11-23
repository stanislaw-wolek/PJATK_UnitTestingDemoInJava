package pjwstk.edu.pl.airport.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.edu.pl.airport.domain.BoardingPass;
import pjwstk.edu.pl.airport.service.CheckInService;

@RestController
@RequestMapping("/api/check-ins")
public class CheckInController {

	private final CheckInService checkInService;

	public CheckInController(CheckInService checkInService) {
		this.checkInService = checkInService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BoardingPass checkIn(@RequestBody CheckInRequest request) {
		return checkInService.checkIn(request.bookingReference());
	}
}
