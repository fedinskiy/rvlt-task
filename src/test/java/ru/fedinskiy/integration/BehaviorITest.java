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
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class BehaviorITest {
	@Inject
	EmbeddedServer server;

	@Inject
	@Client("/")
	HttpClient client;

	@Test
	void defaultCase() {
		final boolean firstCreated = createAccount(1, 1);
		final boolean secondCreated = createAccount(2, 200);
		assertTrue(firstCreated);
		assertTrue(secondCreated);
		assertEquals(1, getSum(1));
		assertEquals(200, getSum(2));
		client.toBlocking()
				.exchange(HttpRequest.POST("/transfer/2/1", "100")
						.contentType(MediaType.TEXT_PLAIN));
		assertEquals(101, getSum(1));
		assertEquals(100, getSum(2));
	}

	private boolean createAccount(int id, int initialAmount) {
		final HttpResponse<Object> result = client.toBlocking().exchange(HttpRequest.POST("/accounts/" + id,
				String.valueOf(initialAmount))
				.contentType(MediaType.TEXT_PLAIN));
		assertEquals(HttpStatus.CREATED, result.getStatus());
		return true;
	}

	private int getSum(int id) {
		final String result = client.toBlocking().retrieve(HttpRequest.GET("/accounts/" + id)
				.contentType(MediaType.TEXT_PLAIN));
		return Integer.parseInt(result);
	}
}