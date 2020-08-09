package org.ghanshyam.weather.controller;

import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class Controller {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public Controller(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.objectMapper = new ObjectMapper();
	}

	@GetMapping("/weather/forecast")
	public @ResponseBody JsonNode getForecast(@RequestParam(name = "q", required = false) String queryString) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		final ObjectNode apiResponse = objectMapper.createObjectNode();
		if (StringUtils.isEmpty(queryString)) {

			apiResponse.put("error", "query string cannot be null");
			return apiResponse;
		}

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("https://samples.openweathermap.org/data/2.5/forecast");
		builder.queryParam("q", queryString);
		builder.queryParam("appid", "439d4b804bc8187953eb36d2a8c26a02");

		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class);
		try {
			JsonNode responseData = objectMapper.readValue(response.getBody(), JsonNode.class);

			Optional.of(responseData.get("list")).ifPresent(listNode -> {
				ArrayNode array = apiResponse.putArray("forecast");
				listNode.iterator().forEachRemaining(node -> {
					ObjectNode value = objectMapper.createObjectNode();
					array.add(value);
					double temp = node.get("main").get("temp").asDouble();
					value.put("temp_min", node.get("main").get("temp_min").asDouble());
					value.put("temp_max", node.get("main").get("temp_max").asDouble());
					value.put("date", node.get("dt_txt").asText());
					if (temp - 273 > 40) {
						value.put("warning", "Put sunscreen lotion");
					}

					if (node.has("rain") && node.get("rain").isEmpty()) {
						value.put("warning", "Carry Umbrella");
					}

				});
			});

			return apiResponse;
		} catch (JsonProcessingException e) {
			apiResponse.put("error", e.getMessage());

		}

		return apiResponse;

	}
}
