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
import ru.fedinskiy.database.Account;
import ru.fedinskiy.database.AccountDatabase;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest
class AccountControllerITest {
	@Inject
	EmbeddedServer server;

	@Inject
	AccountDatabase<Account> database;

	@Inject
	@Client("/")
	HttpClient client;

	@Test
	void create() {
		assertFalse(database.get(11).isPresent());
		final HttpResponse<String> response = client.toBlocking()
				.exchange(HttpRequest.POST("/accounts/11", "112")
						.contentType(MediaType.TEXT_PLAIN));
		assertEquals(HttpStatus.CREATED, response.getStatus());
		final Account created = obtainAccount(11);
		assertEquals(112, created.getCurrentAmount());
	}

	@Test
	void createWithoutBody() {
		assertFalse(database.get(13).isPresent());
		final HttpResponse<String> response = client.toBlocking()
				.exchange(HttpRequest.POST("/accounts/13", "")
						.contentType(MediaType.TEXT_PLAIN));
		assertEquals(HttpStatus.CREATED, response.getStatus());
		final Account created = obtainAccount(13);
		assertEquals(0, created.getCurrentAmount());
	}

	private Account obtainAccount(int id) {
		return database.get(id).orElseThrow(() -> new AssertionError("Account was not created!"));
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