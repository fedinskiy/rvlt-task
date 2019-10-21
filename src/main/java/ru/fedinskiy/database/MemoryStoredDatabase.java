package ru.fedinskiy.database;

import io.micronaut.context.annotation.Context;

import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Context
public class MemoryStoredDatabase implements AccountDatabase<MemoryStoredAccount> {
	private final ConcurrentMap<Integer, MemoryStoredAccount> accounts;

	public MemoryStoredDatabase() {
		this.accounts = new ConcurrentHashMap<>();
	}

	@Override
	public Optional<MemoryStoredAccount> get(int id) {
		return Optional.ofNullable(accounts.get(id));
	}

	@Override
	public synchronized boolean createIfNotExist(MemoryStoredAccount account) {
		final int id = account.getId();
		final MemoryStoredAccount existing = accounts.get(id);
		if (existing == null) {
			accounts.put(id, account);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean update(MemoryStoredAccount account) {
		final int id = account.getId();
		final MemoryStoredAccount existing = accounts.get(id);
		if (existing == null) {
			throw new IllegalArgumentException("Account " + id + " does not exist");
		}

		if (!existing.canBeChangedTo(account)) {
			throw new ConcurrentModificationException("Account " + id + "was changed in another thread");
		}

		return accounts.replace(id, existing, account);
	}

	@Override
	public synchronized boolean updateInSameTransaction(MemoryStoredAccount one, MemoryStoredAccount another) {
		return update(one) && update(another);
	}
}
