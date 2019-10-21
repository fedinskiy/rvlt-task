package ru.fedinskiy.transfer;

import org.junit.jupiter.api.Test;
import ru.fedinskiy.database.Account;
import ru.fedinskiy.database.AccountDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionProcessorTest {
	private final AccountDatabase database = new TestDatabase();
	private final TransactionProcessor processor = new TransactionProcessor(database);

	@Test
	void transferMoney() {
		TestAccount first = new TestAccount(1);
		first.add(11);
		TestAccount second = new TestAccount(2);
		second.add(2);
		database.createIfNotExist(first);
		database.createIfNotExist(second);

		processor.transferMoney(1, 2, 10);
		assertEquals(1, first.getCurrentAmount());
		assertEquals(12, second.getCurrentAmount());
	}
}

class TestDatabase implements AccountDatabase<TestAccount> {
	private final TestAccount[] accounts = new TestAccount[10];

	@Override
	public Optional<TestAccount> get(int id) {
		return Optional.ofNullable(accounts[id]);
	}

	@Override
	public boolean createIfNotExist(TestAccount account) {
		final int id = account.getId();
		if (accounts[id] != null) {
			return false;
		}
		accounts[id] = account;
		return true;
	}

	@Override
	public boolean update(TestAccount account) {
		accounts[account.getId()] = account;
		return true;
	}
}

class TestAccount implements Account {
	final int id;
	int amount;

	TestAccount(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	public TestAccount(int id) {
		this.id = id;
		this.amount = 0;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Account add(int sum) {
		this.amount += sum;
		return this;
	}

	@Override
	public Account withdraw(int sum) {
		this.amount -= sum;
		return this;
	}

	@Override
	public int getCurrentAmount() {
		return amount;
	}
}
