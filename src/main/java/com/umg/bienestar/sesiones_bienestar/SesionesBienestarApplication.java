package com.umg.bienestar.sesiones_bienestar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *
 * @author amada
 */

@SpringBootApplication
@EnableJpaAuditing
public class SesionesBienestarApplication {

	public static void main(String[] args) {
		SpringApplication.run(SesionesBienestarApplication.class, args);
	}

}
