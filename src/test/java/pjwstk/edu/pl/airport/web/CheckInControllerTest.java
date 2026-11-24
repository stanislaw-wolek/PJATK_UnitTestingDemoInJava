package pjwstk.edu.pl.airport.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pjwstk.edu.pl.airport.domain.exceptions.BookingNotFoundException;
import pjwstk.edu.pl.airport.service.CheckInService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckInController.class)
class CheckInControllerTest {

	private static final String VALID_REQUEST = """
			{"bookingReference": "ABC123"}
			""";

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	CheckInService checkInService;

	@Test
	void shouldReturn404_whenBookingDoesNotExist() throws Exception {
		when(checkInService.checkIn("ABC123"))
				.thenThrow(new BookingNotFoundException("ABC123"));

		mockMvc.perform(post("/api/check-ins")
					.contentType(MediaType.APPLICATION_JSON)
					.content(VALID_REQUEST))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.detail").value("Booking ABC123 not found"));
	}
}