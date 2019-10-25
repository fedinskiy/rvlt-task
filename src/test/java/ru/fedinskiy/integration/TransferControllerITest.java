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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.fedinskiy.model.AccountDatabase;
import ru.fedinskiy.database.ImmutableAccount;

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

	private ImmutableAccount first = new ImmutableAccount(1).add(3);
	private ImmutableAccount second = new ImmutableAccount(2);

	@BeforeEach
	void setUp() {
		database.createIfNotExist(first);
		database.createIfNotExist(second);
	}

	@Test
	void transfer() {
		HttpResponse<String> response = client.toBlocking()
				.exchange(HttpRequest.POST("/transfer/1/2", "3")
						.contentType(MediaType.TEXT_PLAIN));
		assertEquals(HttpStatus.OK, response.status());
	}

	@Test
	void transferFromInvalid() {
		try{
			client.toBlocking()
					.exchange(HttpRequest.POST("/transfer/myaccount/2", "3")
							          .contentType(MediaType.TEXT_PLAIN));
		} catch (HttpClientResponseException response){
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
			return;
		}
		Assertions.fail();
	}

	@Test
	void transferToInvalid() {
		try{
			client.toBlocking()
					.exchange(HttpRequest.POST("/transfer/1/someone", "3")
							          .contentType(MediaType.TEXT_PLAIN));
		} catch (HttpClientResponseException response){
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
			return;
		}
		Assertions.fail();
	}

	@Test
	void transferNegative() {
		try{
			client.toBlocking()
					.exchange(HttpRequest.POST("/transfer/1/2", "-1")
							          .contentType(MediaType.TEXT_PLAIN));
		} catch (HttpClientResponseException response){
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
			return;
		}
		Assertions.fail();
	}

	@Test
	void transferInvalidAmount() {
		try{
			client.toBlocking()
					.exchange(HttpRequest.POST("/transfer/1/2", "million")
							          .contentType(MediaType.TEXT_PLAIN));
		} catch (HttpClientResponseException response){
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
			return;
		}
		Assertions.fail();
	}

	@Test
	void transferToOneself() {
		try{
			client.toBlocking()
					.exchange(HttpRequest.POST("/transfer/1/1", "1")
							          .contentType(MediaType.TEXT_PLAIN));
		} catch (HttpClientResponseException response){
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
			return;
		}
		Assertions.fail();
	}
}