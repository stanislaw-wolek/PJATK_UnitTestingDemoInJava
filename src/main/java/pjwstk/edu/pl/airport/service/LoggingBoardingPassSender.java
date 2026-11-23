package pjwstk.edu.pl.airport.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pjwstk.edu.pl.airport.domain.BoardingPass;

@Component
public class LoggingBoardingPassSender implements BoardingPassSender {

	private static final Logger log = LoggerFactory.getLogger(LoggingBoardingPassSender.class);

	@Override
	public void send(BoardingPass pass) {
		log.info("Boarding pass sent: {}", pass);
	}
}
