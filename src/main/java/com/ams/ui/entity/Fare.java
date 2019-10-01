package com.ams.ui.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class Fare
{

	private String id;
	
	@NonNull
	private String amount;
	
	@NonNull
	private String currency;
}
