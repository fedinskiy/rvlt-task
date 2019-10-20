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
import ru.fedinskiy.database.AccountDatabase;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class AccountControllerITest {
	@Inject
	EmbeddedServer server;

	@Inject
	AccountDatabase database;

	@Inject
	@Client("/")
	HttpClient client;

	@Test
	void create() {
		final HttpResponse<String> response = client.toBlocking()
				.exchange(HttpRequest.POST("/accounts/11", "112")
						.contentType(MediaType.TEXT_PLAIN));
		assertEquals(HttpStatus.CREATED, response.getStatus());
	}

	@Test
	void getAccountInfo() {
		final String accountUri = "/accounts/12";
		final String initialSum = "113";
		client.toBlocking()
				.exchange(HttpRequest.POST(accountUri, initialSum)
						.contentType(MediaType.TEXT_PLAIN));
		final String amount = client.toBlocking()
				.retrieve(accountUri);
		assertEquals(initialSum, amount);
	}
}