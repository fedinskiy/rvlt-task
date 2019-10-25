package ru.fedinskiy.integration;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.fedinskiy.database.AccountDatabase;
import ru.fedinskiy.database.ImmutableAccount;
import ru.fedinskiy.database.VersionedAccount;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest
class AccountControllerITest {
	@Inject
	EmbeddedServer server;

	@Inject
	AccountDatabase<ImmutableAccount> database;

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
		final VersionedAccount created = obtainAccount(11);
		assertEquals(112, created.getCurrentAmount());
	}

	@Test
	void createWithoutBody() {
		assertFalse(database.get(13).isPresent());
		final HttpResponse<String> response = client.toBlocking()
				.exchange(HttpRequest.POST("/accounts/13", "")
						          .contentType(MediaType.TEXT_PLAIN));
		assertEquals(HttpStatus.CREATED, response.getStatus());
		final VersionedAccount created = obtainAccount(13);
		assertEquals(0, created.getCurrentAmount());
	}

	@Test
	void createWithoutAmount() {
		assertFalse(database.get(14).isPresent());
		final HttpResponse<String> response = client.toBlocking()
				.exchange(HttpRequest.POST("/accounts/14", "0")
						          .contentType(MediaType.TEXT_PLAIN));
		assertEquals(HttpStatus.CREATED, response.getStatus());
		final VersionedAccount created = obtainAccount(14);
		assertEquals(0, created.getCurrentAmount());
	}

	@Test
	void createWithNegativeAmount() {
		try {
			final HttpResponse<Object> response = client.toBlocking()
					.exchange(HttpRequest.POST("/accounts/14", "-1")
							          .contentType(MediaType.TEXT_PLAIN));
		} catch (HttpClientResponseException response) {
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
			return;
		}
		Assertions.fail();
	}

	private VersionedAccount obtainAccount(int id) {
		return database.get(id).orElseThrow(() -> new AssertionError("Account was not created!"));
	}

	@Test
	void doubleCreation() {
		client.toBlocking().exchange(HttpRequest.POST("/accounts/13", "").contentType(MediaType.TEXT_PLAIN));
		final HttpResponse<String> secondResponce = client.toBlocking()
				.exchange(HttpRequest.POST("/accounts/13", "")
						          .contentType(MediaType.TEXT_PLAIN));
		assertEquals(HttpStatus.ACCEPTED, secondResponce.getStatus());
	}

	@Test
	void invalidIdCreation() {
		try {
			client.toBlocking().exchange(HttpRequest.POST("/accounts/account", "11")
					                             .contentType(MediaType.TEXT_PLAIN));
		} catch (HttpClientResponseException response) {
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
			return;
		}
		Assertions.fail();
	}

	@Test
	void invalidAmountCreation() {
		try {
			client.toBlocking().exchange(HttpRequest.POST("/accounts/11", "million")
					                             .contentType(MediaType.TEXT_PLAIN));
		} catch (HttpClientResponseException response) {
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
			return;
		}
		Assertions.fail();
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

	@Test
	void getInvalidAccountInfo() {
		final String accountUri = "/accounts/account";
		try {
			client.toBlocking()
					.retrieve(accountUri);
		} catch (HttpClientResponseException response) {
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
			return;
		}
		Assertions.fail();
	}

	@Test
	void getUnexistingAccount() {
		final String accountUri = "/accounts/40";
		try {
			client.toBlocking().retrieve(accountUri);
		} catch (HttpClientResponseException response) {
			assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
			return;
		}
		Assertions.fail();
	}
}