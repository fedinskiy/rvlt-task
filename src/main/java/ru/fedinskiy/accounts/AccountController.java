package ru.fedinskiy.accounts;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import javax.annotation.Nullable;
import java.net.URI;

@Controller("/accounts")
public class AccountController {
	private final AccountAccessor accounts;

	public AccountController(AccountAccessor accounts) {
		this.accounts = accounts;
	}

	@Post("/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	public HttpResponse create(@PathVariable("id") Integer id,
	                           @Nullable Integer initialAmount) {
		accounts.createAccount(id,
				nullToZero(initialAmount));
		return HttpResponse
				.created(getIdAccessURI(id));
	}

	private int nullToZero(Integer nullable) {
		return nullable == null ? 0 : nullable;
	}

	private static URI getIdAccessURI(Object id) {
		return URI.create(String.format("/accounts/%s", id));
	}

	@Get("/{id}")
	public HttpResponse getAccountInfo(@PathVariable("id") Integer id) {
		final int sum = accounts.getCurrentSumOnAccount(id);
		return HttpResponse.ok(sum);
	}
}
