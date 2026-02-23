package com.example.dyeTrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DyeTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(DyeTrackApplication.class, args);
	}

}
