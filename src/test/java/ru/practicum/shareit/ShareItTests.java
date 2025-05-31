package ru.practicum.shareit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ShareItTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		// Отправляем GET запрос к эндпоинту actuator health и проверяем, что статус ответа 200 (OK)
		ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Application context should load and actuator be healthy");
	}
}
