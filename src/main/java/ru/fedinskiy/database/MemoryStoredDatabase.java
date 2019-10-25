package ru.fedinskiy.database;

import io.micronaut.context.annotation.Context;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Context
public class MemoryStoredDatabase implements AccountDatabase<ImmutableAccount> {
	private final ConcurrentMap<Integer, StoredAccount> accounts;

	public MemoryStoredDatabase() {
		this.accounts = new ConcurrentHashMap<>();
	}

	@Override
	public Optional<ImmutableAccount> get(int id) {
		final StoredAccount stored = accounts.get(id);
		if (stored == null) {
			return Optional.empty();
		} else {
			synchronized (stored) {
				return Optional.of(new ImmutableAccount(stored));
			}
		}
	}

	@Override
	public synchronized boolean createIfNotExist(ImmutableAccount account) {
		final int id = account.getId();
		final StoredAccount existing = accounts.get(id);
		if (existing == null) {
			accounts.put(id, new StoredAccount(account));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean update(ImmutableAccount account) {
		final StoredAccount existing = getStoredAccount(account.getId());
		synchronized (existing) {
			if (existing.canBeChangedTo(account)) {
				existing.updateAmount(account.getCurrentAmount());
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean updateInSameTransaction(ImmutableAccount one, ImmutableAccount another) {
		final IdOrderedPair pair = new IdOrderedPair(one, another);
		return updatePair(pair);
	}

	private boolean updatePair(IdOrderedPair pair) {
		final VersionedAccount first = pair.getFirst();
		final VersionedAccount second = pair.getSecond();
		final StoredAccount existingFirst = getStoredAccount(first.getId());
		final StoredAccount existingSecond = getStoredAccount(second.getId());
		synchronized (existingFirst) {
			synchronized (existingSecond) {
				if (existingFirst.canBeChangedTo(first) && existingSecond.canBeChangedTo(second)) {
					existingFirst.updateAmount(first.getCurrentAmount());
					existingSecond.updateAmount(second.getCurrentAmount());
					return true;
				}
			}
		}
		return false;
	}

	private StoredAccount getStoredAccount(int id) {
		final StoredAccount existing = accounts.get(id);
		if (existing == null) {
			throw new IllegalArgumentException("Account " + id + " does not exist");
		}
		return existing;
	}

}
