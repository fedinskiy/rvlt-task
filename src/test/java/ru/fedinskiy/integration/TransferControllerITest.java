package ru.fedinskiy.integration;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.fedinskiy.database.Account;
import ru.fedinskiy.database.AccountDatabase;
import ru.fedinskiy.database.MemoryStoredAccount;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class TransferControllerITest {
	@Inject
	EmbeddedServer server;

	@Inject
	AccountDatabase database;

	@Inject
	@Client("/")
	HttpClient client;

	private Account first = new MemoryStoredAccount(1);
	private Account second = new MemoryStoredAccount(2);

	@BeforeEach
	void setUp() {
		database.createIfNotExist(first);
		database.createIfNotExist(second);
	}

	@Test
	void transfer() {
		String response = client.toBlocking()
				.retrieve(HttpRequest.POST("/transfer/1/2", "3")
						.contentType(MediaType.TEXT_PLAIN));
		assertEquals("transfered 3 from 1 to 2", response);
	}
}