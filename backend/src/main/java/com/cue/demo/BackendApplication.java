package com.cue.demo;

import com.cue.demo.entities.User;
import com.cue.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

	public static final Logger logger = LoggerFactory.getLogger(BackendApplication.class);

	static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository userRepository) {
		return (args) -> {
			userRepository.save(new User("rptom", "Robert", "Pana"));
			userRepository.save(new User("cosminbrn", "Cosmin", "Baroana"));
			userRepository.save(new User("costelBomboana", "Costel", "Bomboana"));
			userRepository.save(new User("biancaemo", "bianca", "Bomboana"));


			logger.info("Users found with findAll():");
			logger.info("-------------------------");
			userRepository.findAll().forEach(user->logger.info(user.toString()));
			logger.info("-------------------------");

			// fetch an individual user by ID
			User c1 = userRepository.findById(1L);
			logger.info("Users found with findById(long):");
			logger.info("-------------------------");
			logger.info(c1.toString());
			logger.info("-------------------------");

			// fetch customer by last Name
			logger.info("Users found with findByFirstName(Bomboana):");
			logger.info("-------------------------");

			userRepository.findByLastName("Bomboana").forEach(user ->
					logger.info(user.toString()));
			logger.info("-------------------------");

		};

	}
}
