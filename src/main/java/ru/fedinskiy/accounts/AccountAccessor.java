package ru.fedinskiy.accounts;

import io.micronaut.context.annotation.Prototype;
import ru.fedinskiy.database.Account;
import ru.fedinskiy.database.AccountDatabase;
import ru.fedinskiy.database.MemoryStoredAccount;
import ru.fedinskiy.validation.AccountNotFoundException;

import javax.inject.Inject;
import java.util.Optional;

@Prototype
public class AccountAccessor {
	private final AccountDatabase<Account> database;

	@Inject
	public AccountAccessor(AccountDatabase<Account> database) {
		this.database = database;
	}

	public int getCurrentSumOnAccount(int id) throws AccountNotFoundException {
		Optional<Account> account = database.get(id);
		return account.map(Account::getCurrentAmount)
				.orElseThrow(() -> new AccountNotFoundException(id));
	}

	public AccountCreationResult createAccount(int id, int amount) {
		final MemoryStoredAccount account = new MemoryStoredAccount(id).add(amount);
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
