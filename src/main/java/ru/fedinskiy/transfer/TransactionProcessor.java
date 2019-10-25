package ru.fedinskiy.transfer;

import io.micronaut.context.annotation.Prototype;
import ru.fedinskiy.database.AccountDatabase;
import ru.fedinskiy.validation.AccountNotFoundException;

import javax.inject.Inject;

@Prototype
public class TransactionProcessor {
	private final AccountDatabase<MoneyHolder> database;

	@Inject
	public TransactionProcessor(AccountDatabase<MoneyHolder> database) {
		this.database = database;
	}

	public boolean transferMoney(int fromId, int toId, int amount) throws AccountNotFoundException {
		final MoneyHolder source = getAccount(fromId);
		if (amount > source.getCurrentAmount()) {
			return false;
		}
		final MoneyHolder changed = source.withdraw(amount);
		final MoneyHolder target = getAccount(toId).add(amount);
		return database.updateInSameTransaction(changed, target);
	}

	private MoneyHolder getAccount(int id) throws AccountNotFoundException {
		return database.get(id)
				.orElseThrow(() -> new AccountNotFoundException(id));
	}
}
