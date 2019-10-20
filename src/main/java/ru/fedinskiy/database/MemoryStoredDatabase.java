package ru.fedinskiy.database;

import io.micronaut.context.annotation.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Context
public class MemoryStoredDatabase implements AccountDatabase {
	private final Map<Integer, Account> accounts;

	public MemoryStoredDatabase() {
		this.accounts = new HashMap<>();
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
