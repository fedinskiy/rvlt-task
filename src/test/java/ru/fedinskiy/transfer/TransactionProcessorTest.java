package ru.fedinskiy.transfer;

import org.junit.jupiter.api.Test;
import ru.fedinskiy.model.AccountDatabase;
import ru.fedinskiy.model.Transactable;
import ru.fedinskiy.validation.AccountNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionProcessorTest {
	private final AccountDatabase database = new TestDatabase();
	private final TransactionProcessor processor = new TransactionProcessor(database);

	@Test
	void transferMoney() throws AccountNotFoundException {
		TestAccount first = new TestAccount(1);
		first.add(11);
		TestAccount second = new TestAccount(2);
		second.add(2);
		database.createIfNotExist(first);
		database.createIfNotExist(second);

		final boolean success = processor.transferMoney(1, 2, 10);
		assertTrue(success);
		assertEquals(1, first.amount);
		assertEquals(12, second.amount);
	}

	@Test
	void transferTooMuchMoney() throws AccountNotFoundException {
		TestAccount first = new TestAccount(3);
		first.add(11);
		TestAccount second = new TestAccount(4);
		second.add(2);
		database.createIfNotExist(first);
		database.createIfNotExist(second);

		final boolean success = processor.transferMoney(3, 4, 100);
		assertFalse(success);
		assertEquals(11, first.amount);
		assertEquals(2, second.amount);
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

	@Override
	public boolean updateInSameTransaction(TestAccount one, TestAccount another) {
		return update(one) && update(another);
	}
}

class TestAccount implements Transactable {
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

	public int getId() {
		return id;
	}

	@Override
	public TestAccount add(int sum) {
		this.amount += sum;
		return this;
	}

	@Override
	public TestAccount withdraw(int sum) {
		this.amount -= sum;
		return this;
	}

	@Override
	public int getCurrentAmount() {
		return amount;
	}
}
