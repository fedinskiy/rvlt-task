package ru.fedinskiy.accounts;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.net.URI;

@Controller("/accounts")
public class AccountController {
	private final AccountAccessor accounts;

	public AccountController(AccountAccessor accounts) {
		this.accounts = accounts;
	}

	@Post("/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	public HttpResponse create(@PathVariable("id") String id,
	                           String initialAmount) {
		accounts.createAccount(Integer.parseInt(id),
				Integer.parseInt(initialAmount));
		return HttpResponse
				.created(getIdAccessURI(id));
	}

	private static URI getIdAccessURI(Object id) {
		return URI.create(String.format("/accounts/%s", id));
	}

	@Get("/{id}")
	public HttpResponse getAccountInfo(@PathVariable("id") String id) {
		final int sum = accounts.getCurrentSumOnAccount(Integer.parseInt(id));
		return HttpResponse.ok(sum);
	}
}
