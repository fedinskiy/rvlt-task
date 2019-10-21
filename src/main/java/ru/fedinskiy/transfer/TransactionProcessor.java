package ru.fedinskiy.transfer;

import io.micronaut.context.annotation.Prototype;
import ru.fedinskiy.database.Account;
import ru.fedinskiy.database.AccountDatabase;

import javax.inject.Inject;

@Prototype
public class TransactionProcessor {
	private final AccountDatabase<Account> database;

	@Inject
	public TransactionProcessor(AccountDatabase<Account> database) {
		this.database = database;
	}

	public void transferMoney(int fromId, int toId, int amount) {
		Account source = getAccount(fromId).withdraw(amount);
		Account target = getAccount(toId).add(amount);

		database.updateInSameTransaction(source, target);
	}

	private Account getAccount(int id) {
		return database.get(id)
				.orElseThrow(() -> new IllegalArgumentException("No account with id " + id));
	}
}
