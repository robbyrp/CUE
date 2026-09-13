package com.cue.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Boots the full application context against a disposable PostgreSQL Testcontainer.
 * That makes the smoke test hermetic: it passes the same way on a clean CI runner
 * as it does locally, with no manually started database required.
 */
@SpringBootTest
@Testcontainers
class BackendApplicationIT {

	@Container
	@ServiceConnection
	@SuppressWarnings("resource")
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
			.withInitScript("init-unaccent.sql");

	@Test
	void contextLoads() {
	}

}
