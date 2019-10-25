package ru.fedinskiy.transfer;

import io.micronaut.context.annotation.Prototype;
import ru.fedinskiy.model.AccountDatabase;
import ru.fedinskiy.model.Transactable;
import ru.fedinskiy.validation.AccountNotFoundException;

import javax.inject.Inject;

@Prototype
public class TransactionProcessor {
	private final AccountDatabase<Transactable> database;

	@Inject
	public TransactionProcessor(AccountDatabase<Transactable> database) {
		this.database = database;
	}

	public boolean transferMoney(int fromId, int toId, int amount) throws AccountNotFoundException {
		if (amount <= 0) {
			throw new IllegalArgumentException("Only positive amount of money can be transferred!");
		}
		final Transactable source = getAccount(fromId);
		if (amount > source.getCurrentAmount()) {
			return false;
		}
		final Transactable changed = source.withdraw(amount);
		final Transactable target = getAccount(toId).add(amount);
		return database.updateInSameTransaction(changed, target);
	}

	private Transactable getAccount(int id) throws AccountNotFoundException {
		return database.get(id)
				.orElseThrow(() -> new AccountNotFoundException(id));
	}
}
