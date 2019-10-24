package ru.fedinskiy.integration;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class HeartBeatControllerITest {
	@Inject
	EmbeddedServer server;

	@Inject
	@Client("/")
	HttpClient client;

	@Test
	void livenessCheck() {
		HttpResponse<String> response = client.toBlocking()
				.exchange(HttpRequest.GET("/is_alive").contentType(MediaType.TEXT_PLAIN), String.class);
		assertEquals(HttpStatus.OK, response.status());
		assertEquals("yes", response.body());
	}
}