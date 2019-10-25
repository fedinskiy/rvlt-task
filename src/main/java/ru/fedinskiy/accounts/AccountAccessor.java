package ru.fedinskiy.accounts;

import io.micronaut.context.annotation.Prototype;
import ru.fedinskiy.database.AccountDatabase;
import ru.fedinskiy.database.ImmutableAccount;
import ru.fedinskiy.database.VersionedAccount;
import ru.fedinskiy.validation.AccountNotFoundException;

import javax.inject.Inject;
import java.util.Optional;

@Prototype
public class AccountAccessor {
	private final AccountDatabase<ImmutableAccount> database;

	@Inject
	public AccountAccessor(AccountDatabase<ImmutableAccount> database) {
		this.database = database;
	}

	public int getCurrentSumOnAccount(int id) throws AccountNotFoundException {
		Optional<ImmutableAccount> account = database.get(id);
		return account.map(VersionedAccount::getCurrentAmount)
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
