package com.example.Game_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


}





//	@EnableSwagger2
//	@SpringBootApplication
//	@EnableJpaRepositories
//	public class Application {
//		public static void main(String[] args) {
//			SpringApplication.run(com.example.Game_Backend.Application.class, args);
//		}
//
//		@Bean
//		public Docket api() {
//			return new Docket(DocumentationType.SWAGGER_2)
//					.select()
//					.apis(RequestHandlerSelectors.any())
//					.paths(PathSelectors.any())
//					.build();
//		}

