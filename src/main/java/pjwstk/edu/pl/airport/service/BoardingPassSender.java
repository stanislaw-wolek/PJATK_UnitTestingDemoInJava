package pjwstk.edu.pl.airport.service;

import pjwstk.edu.pl.airport.domain.BoardingPass;

public interface BoardingPassSender {
	void send(BoardingPass pass);
}
