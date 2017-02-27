package com.singlepage.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
		"com.singlepage.cms.models"
})
@EnableJpaRepositories(basePackages = {
		"com.singlepage.cms.repository"
})
public class SinglePageCmsApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SinglePageCmsApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SinglePageCmsApplication.class, args);
	}

}
