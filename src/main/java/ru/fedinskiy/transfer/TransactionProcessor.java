package ru.fedinskiy.transfer;

import io.micronaut.context.annotation.Prototype;
import ru.fedinskiy.database.Account;
import ru.fedinskiy.database.AccountDatabase;
import ru.fedinskiy.validation.AccountNotFoundException;

import javax.inject.Inject;

@Prototype
public class TransactionProcessor {
	private final AccountDatabase<Account> database;

	@Inject
	public TransactionProcessor(AccountDatabase<Account> database) {
		this.database = database;
	}

	public boolean transferMoney(int fromId, int toId, int amount) throws AccountNotFoundException {
		final Account source = getAccount(fromId);
		if (amount > source.getCurrentAmount()) {
			return false;
		}
		final Account changed = source.withdraw(amount);
		final Account target = getAccount(toId).add(amount);
		return database.updateInSameTransaction(changed, target);
	}

	private Account getAccount(int id) throws AccountNotFoundException {
		return database.get(id)
				.orElseThrow(() -> new AccountNotFoundException(id));
	}
}
