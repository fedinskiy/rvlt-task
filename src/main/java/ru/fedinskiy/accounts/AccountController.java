package ru.fedinskiy.accounts;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import ru.fedinskiy.validation.AccountNotFoundException;
import ru.fedinskiy.validation.InputValidation;
import ru.fedinskiy.validation.InvalidAccountIdException;
import ru.fedinskiy.validation.InvalidAmountException;

import javax.annotation.Nullable;
import java.net.URI;

@Controller("/accounts")
public class AccountController implements InputValidation {
	private final AccountAccessor accounts;

	public AccountController(AccountAccessor accounts) {
		this.accounts = accounts;
	}

	@Post("/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	public HttpResponse create(@PathVariable("id") String id,
	                           @Nullable String initialAmount) {
		final AccountCreationResult result;
		try {
			result = accounts.createAccount(parseId(id),
			                                nullToZero(initialAmount));
			switch (result) {
				case CREATED:
					return HttpResponse.created(getIdAccessURI(id));
				case ALREADY_EXISTS:
					return HttpResponse.accepted(getIdAccessURI(id));
				case NOT_CREATED:
					return HttpResponse.status(HttpStatus.SERVICE_UNAVAILABLE);
				default:
					return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected result: " + result.name());
			}
		} catch (InvalidAmountException | InvalidAccountIdException e) {
			return HttpResponse.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}


	private int nullToZero(String nullable) throws InvalidAmountException {
		return nullable == null ? 0 : parseAmount(nullable);
	}

	@Override
	public int parseAmount(String source) throws InvalidAmountException {
		try {
			final int result = Integer.parseInt(source);
			if (result < 0) {
				throw new IllegalArgumentException("Negative value!");
			}
			return result;
		} catch (Exception ex) {
			throw new InvalidAmountException(source);
		}
	}

	private static URI getIdAccessURI(Object id) {
		return URI.create(String.format("/accounts/%s", id));
	}

	@Get("/{id}")
	public HttpResponse getAccountInfo(@PathVariable("id") String id) {
		try {
			final int sum = accounts.getCurrentSumOnAccount(parseId(id));
			return HttpResponse.ok(sum);
		} catch (AccountNotFoundException e) {
			return HttpResponse.notFound(id);
		} catch (InvalidAccountIdException e) {
			return HttpResponse.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
