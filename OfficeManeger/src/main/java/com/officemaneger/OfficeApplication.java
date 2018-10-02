package com.officemaneger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class OfficeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfficeApplication.class, args);
	}
}


//war

//

//@SpringBootApplication
//@EnableScheduling
//public class OfficeApplication extends SpringBootServletInitializer {
//
//	public static void main(String[] args) {
//		SpringApplication.run(OfficeApplication.class, args);
//	}
//
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(OfficeApplication.class);
//	}
//}
