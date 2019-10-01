package com.ams.ui.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "ui.config")
public class UIProperties
{
	private String searchUrl = "http://localhost:8090/search";
	private String bookingUrl = "http://localhost:8060/booking";
	private String checkinUrl = "http://localhost:8070/checkin";

}
