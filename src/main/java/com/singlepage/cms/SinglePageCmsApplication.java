package com.singlepage.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class SinglePageCmsApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SinglePageCmsApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SinglePageCmsApplication.class, args);
	}

//
//	public static void main(String[] args) {
//		SpringApplication.run(SinglePageCmsApplication.class, args);
//	}
}
