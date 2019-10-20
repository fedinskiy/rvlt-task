package ru.fedinskiy.database;

import io.micronaut.context.annotation.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@Context
public class MemoryStoredDatabase implements AccountDatabase {
	private final Map<Integer, Account> accounts;

	public MemoryStoredDatabase() {
		this.accounts = new HashMap<>();
		load(accounts);
	}

	private static void load(Map<Integer, Account> accounts) {
		IntStream.rangeClosed(0, 10)
				.mapToObj(MemoryStoredAccount::new)
				.peek(account -> account.add(100))
				.forEach(acc -> accounts.put(acc.getId(), acc));
	}

	@Override
	public Optional<Account> get(int id) {
		return Optional.ofNullable(accounts.get(id));
	}

	@Override
	public boolean put(Account account) {
		accounts.put(account.getId(), account);
		return true;
	}
}
