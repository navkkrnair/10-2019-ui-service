package com.ams.ui.entity;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class BookingRecord
{
	private String         id;
	
	@NonNull
	private String         flightNumber;
	
	@NonNull
	private String         origin;
	
	@NonNull
	private String         destination;
	
	@NonNull
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate      flightDate;
	
	@NonNull
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate      bookingDate;
	
	@NonNull
	private String         amount;
	
	@NonNull
	private String         status;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<Passenger> passengers;
}
