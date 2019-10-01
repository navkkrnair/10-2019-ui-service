package com.ams.ui.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import com.ams.ui.configuration.UIProperties;
import com.ams.ui.entity.BookingRecord;
import com.ams.ui.entity.CheckInRecord;
import com.ams.ui.entity.Fare;
import com.ams.ui.entity.Flight;
import com.ams.ui.entity.Passenger;

@Controller
@RequestMapping("/")
@SessionAttributes(names =
{ "selectedFlight", "searchBookingFlight", "passenger" })
public class BrownFieldSiteController
{
	private static final Logger logger = LoggerFactory.getLogger(BrownFieldSiteController.class);

	@Autowired
	RestTemplate searchClient;
	@Autowired
	RestTemplate bookingClient;
	@Autowired
	RestTemplate checkInClient;
	@Autowired
	UIProperties properties;

	@GetMapping
	public String searchForm(Model model)
	{
		logger.info(">> Submitting Search View.");
		SearchQuery query = new SearchQuery("NYC", "SFO", LocalDate.of(2019, 01, 22));
		model.addAttribute("query", query);
		return "search";
	}

	@PostMapping(value = "/search")
	public String searchSubmit(@Valid @ModelAttribute SearchQuery query, Errors errors, Model model)
			throws URISyntaxException
	{
		logger.info(">> Searching for flights");
		RequestEntity<?> requestEntity = new RequestEntity<>(query, HttpMethod.POST, new URI(properties
			.getSearchUrl()));

		ResponseEntity<List<Flight>> responseEntity = searchClient
			.exchange(requestEntity, new ParameterizedTypeReference<List<Flight>>()
			{
			});
		if (responseEntity.getStatusCode() == HttpStatus.OK)
			model.addAttribute("flights", responseEntity.getBody());
		return "result";
	}

	@GetMapping("/booking/{flightNumber}/{origin}/{destination}/{flightDate}/{amount}")
	public String bookQuery(@PathVariable String flightNumber, @PathVariable String origin, @PathVariable String destination, @DateTimeFormat(iso = ISO.DATE) @PathVariable LocalDate flightDate, @PathVariable String amount, Model model)
	{
		Flight flight = new Flight(flightNumber, origin, destination, flightDate, new Fare(amount, "INR"));
		logger.info(">> Selected flight: {}", flight);
		model.addAttribute("selectedFlight", flight);
		model.addAttribute("passenger", new Passenger());
		return "book";
	}

	@PostMapping(value = "/confirm")
	public String confirmBooking(@Valid @ModelAttribute Passenger passenger, Errors errors, @SessionAttribute("selectedFlight") Flight selectedFlight, Model model)
	{
		Flight         flight     = selectedFlight;
		BookingRecord  booking    = new BookingRecord(flight.getFlightNumber(), flight
			.getOrigin(), flight.getDestination(), flight.getFlightDate(), LocalDate.now(), flight
				.getFare()
				.getAmount(), "UNCONFIRMED");
		Set<Passenger> passengers = new HashSet<Passenger>();
		Passenger      pax        = passenger;
		pax.setBookingRecord(booking);
		passengers.add(pax);
		booking.setPassengers(passengers);
		BookingRecord bookingRecord = null;

		bookingRecord = bookingClient
			.postForObject(properties.getBookingUrl(), booking, BookingRecord.class);
		if (bookingRecord.getId()
			.equals("fallback"))
		{
			logger.info(">> Booking could not proceed...");
			model
				.addAttribute("message", "Your Booking could not be processed. Please try again later!!");
		}
		else
		{
			logger.info("Booking created " + bookingRecord.getId());
			model
				.addAttribute("message", "Your Booking is confirmed. Reference Number is "
						+ bookingRecord.getId());
		}
		return "confirm";
	}

	@GetMapping("/search-booking")
	public String searchBookingForm()
	{
		return "bookingsearch";
	}

	@PostMapping("/search-booking-get")
	public String searchBookingSubmit(HttpServletRequest request, Model model)
	{
		String bookingId = request.getParameter("bookingId");
		logger.info(">> Searching BookingRecord with id {}", bookingId);
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", bookingId);

		BookingRecord booking = bookingClient
			.getForObject(properties.getBookingUrl() + "/{id}", BookingRecord.class, params);

		Flight flight = new Flight(booking.getFlightNumber(), booking.getOrigin(), booking
			.getDestination(), booking.getFlightDate(), new Fare(booking.getAmount(), "INR"));
		model.addAttribute("searchBookingFlight", flight);

		Passenger pax   = booking.getPassengers()
			.iterator()
			.next();
		Passenger paxUI = new Passenger(pax.getFirstName(), pax.getLastName(), pax
			.getGender(), booking);
		model.addAttribute("passenger", paxUI);
		model.addAttribute("bookingid", booking.getId());

		return "bookingsearch";
	}

	@GetMapping(value = "/checkin/{flightNumber}/{origin}/{destination}/{flightDate}/{amount}/{firstName}/{lastName}/{gender}/{bookingid}")
	public String bookQuery(@PathVariable String flightNumber, @PathVariable String origin, @PathVariable String destination, @DateTimeFormat(iso = ISO.DATE) @PathVariable LocalDate flightDate, @PathVariable String amount, @PathVariable String firstName, @PathVariable String lastName, @PathVariable String gender, @PathVariable String bookingid, WebRequest request, SessionStatus status, Model model)
	{

		CheckInRecord checkIn  = new CheckInRecord(lastName, firstName, "28C", LocalDateTime
			.now(), flightNumber, flightDate, bookingid);
		CheckInRecord response = checkInClient
			.postForObject(properties.getCheckinUrl(), checkIn, CheckInRecord.class);

		model
			.addAttribute("message", "Checked In, Seat Number is 28c , checkin id is "
					+ response.getId());
		status.setComplete();
		request.removeAttribute("selectedFlight", WebRequest.SCOPE_SESSION);
		request.removeAttribute("searchBookingFlight", WebRequest.SCOPE_SESSION);
		request.removeAttribute("passenger", WebRequest.SCOPE_SESSION);
		return "checkinconfirm";
	}
}