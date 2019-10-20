package ru.fedinskiy.transfer;

import org.junit.jupiter.api.Test;
import ru.fedinskiy.database.Account;
import ru.fedinskiy.database.AccountDatabase;
import ru.fedinskiy.database.MemoryStoredAccount;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionProcessorTest {
	private final TestDatabase database = new TestDatabase();
	private final TransactionProcessor processor = new TransactionProcessor(database);

	@Test
	void transferMoney() {
		Account first = new MemoryStoredAccount(1);
		first.add(11);
		Account second = new MemoryStoredAccount(2);
		second.add(2);
		database.put(first);
		database.put(second);

		processor.transferMoney(1, 2, 10);
		assertEquals(1, first.getCurrentAmount());
		assertEquals(12, second.getCurrentAmount());
	}
}

class TestDatabase implements AccountDatabase {
	private final Account[] accounts = new Account[10];

	@Override
	public Optional<Account> get(int id) {
		return Optional.ofNullable(accounts[id]);
	}

	@Override
	public boolean put(Account account) {
		accounts[account.getId()] = account;
		return true;
	}
}