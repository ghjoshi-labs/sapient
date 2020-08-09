package org.ghanshyam.weather;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.ghanshyam.weather.controller.Controller;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.JsonNode;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class WeatherApplicationTests {

	@Inject
	private Controller controller;

	@Test
	void testApi() {
		JsonNode node = controller.getForecast("London,uk");
		assertTrue(node.has("forecast"));
		assertFalse(node.get("forecast").isEmpty());

		node = controller.getForecast("");
		assertTrue(node.has("error"));
	}

}
