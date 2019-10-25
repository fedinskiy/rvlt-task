package ru.fedinskiy.accounts;

import io.micronaut.context.annotation.Prototype;
import ru.fedinskiy.database.ImmutableAccount;
import ru.fedinskiy.model.Account;
import ru.fedinskiy.model.AccountDatabase;
import ru.fedinskiy.model.Transactable;
import ru.fedinskiy.validation.AccountNotFoundException;

import javax.inject.Inject;
import java.util.Optional;

@Prototype
public class AccountAccessor {
	private final AccountDatabase<Transactable> database;

	@Inject
	public AccountAccessor(AccountDatabase<Transactable> database) {
		this.database = database;
	}

	public int getCurrentSumOnAccount(int id) throws AccountNotFoundException {
		Optional<Transactable> account = database.get(id);
		return account.map(Account::getCurrentAmount)
				.orElseThrow(() -> new AccountNotFoundException(id));
	}

	public AccountCreationResult createAccount(int id, int amount) {
		final ImmutableAccount account = new ImmutableAccount(id).add(amount);
		synchronized (database) {
			if (database.get(id).isPresent()) {
				return AccountCreationResult.ALREADY_EXISTS;
			}
			if (database.createIfNotExist(account)) {
				return AccountCreationResult.CREATED;
			} else {
				return AccountCreationResult.NOT_CREATED;
			}
		}
	}
}
