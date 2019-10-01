package com.ams.ui.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class Passenger
{
	private String id;

	@NonNull
	private String firstName;

	@NonNull
	private String lastName;

	@NonNull
	private String gender;

	@NonNull
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	private BookingRecord bookingRecord;

}
