package com.ams.ui;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ams.ui.configuration.UIProperties;
import com.ams.ui.controller.SearchQuery;
import com.ams.ui.entity.BookingRecord;
import com.ams.ui.entity.CheckInRecord;
import com.ams.ui.entity.Flight;
import com.ams.ui.entity.Passenger;

@SpringBootApplication
@EnableConfigurationProperties(UIProperties.class)
@EnableDiscoveryClient
public class UiServiceApplication
{
	private static final Logger logger = LoggerFactory.getLogger(UiServiceApplication.class);

	@LoadBalanced
	@Bean
	RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

	@Autowired
	RestTemplate searchClient;

	@Autowired
	RestTemplate bookingClient;

	@Autowired
	RestTemplate checkinClient;

	public static void main(String[] args)
	{
		SpringApplication.run(UiServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UIProperties properties)
	{
		return args ->
		{
			//Search for a flight
			SearchQuery query = new SearchQuery("NYC", "SFO", LocalDate.of(2019, 01, 22));

			RequestEntity<?> requestEntity = new RequestEntity<>(query, HttpMethod.POST, new URI(properties
				.getSearchUrl()));

			ResponseEntity<List<Flight>> responseEntity = searchClient
				.exchange(requestEntity, new ParameterizedTypeReference<List<Flight>>()
															{
															});
			List<Flight>                 flights        = new ArrayList<>();
			if (responseEntity.getStatusCode() == HttpStatus.OK)
			{
				flights = responseEntity.getBody();
			}
			else
			{
				logger.info("Error in searching flights {}", responseEntity.getBody());
			}

			//create a booking only if there are flights.
			if (flights == null || flights.size() == 0)
			{
				return;
			}

			Arrays.asList(flights)
				.forEach(flight -> logger.info(" flight >" + flight));

			Flight flight = flights.get(0);

			BookingRecord bookingRecord = new BookingRecord(flight.getFlightNumber(), flight
				.getOrigin(), flight.getDestination(), flight
					.getFlightDate(), LocalDate.of(2019, 01, 22), flight.getFare()
						.getAmount(), "");

			Set<Passenger> passengers = new HashSet<Passenger>();
			passengers.add(new Passenger("Gavin", "Franc", "Male", bookingRecord));
			bookingRecord.setPassengers(passengers);

			BookingRecord responseRecord = null;
			try
			{
				responseRecord = bookingClient
					.postForObject(properties.getBookingUrl(), bookingRecord, BookingRecord.class);
				if (responseRecord.getId()
					.equals("fallback"))
				{
					logger.info(">> Booking could not proceed");
					return;
				}
				logger.info(">> Booking created " + responseRecord.getId());
			}
			catch (Exception e)
			{
				logger.error(">> Booking Service unavailable...!!!");
				return;
			}

			try
			{
				CheckInRecord checkIn  = new CheckInRecord("Franc", "Gavin", "28C", LocalDateTime
					.of(LocalDate.now(), LocalTime.now()), "BF101", LocalDate
						.of(2019, 01, 22), responseRecord.getId());
				CheckInRecord response = checkinClient
					.postForObject(properties.getCheckinUrl(), checkIn, CheckInRecord.class);

				logger.info(">> Checked IN " + response.getId());
			}
			catch (Exception e)
			{
				logger.error(">> Checkin Service unavailable...!!!");
			}
		};
	}
}
