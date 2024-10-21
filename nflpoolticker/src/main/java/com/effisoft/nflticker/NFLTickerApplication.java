package com.effisoft.nflticker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NFLTickerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NFLTickerApplication.class, args);
	}

}
