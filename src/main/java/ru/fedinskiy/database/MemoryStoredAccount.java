package ru.fedinskiy.database;

import java.util.Objects;

public final class MemoryStoredAccount implements Account {
	private final int id;
	private final int amount;
	private final int version;

	public MemoryStoredAccount(int id) {
		this.id = id;
		this.amount = 0;
		this.version = 0;
	}

	private MemoryStoredAccount(int id, int amount, int version) {
		this.id = id;
		this.amount = amount;
		this.version = version;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public MemoryStoredAccount add(int sum) {
		int newAmount = amount + sum;
		return new MemoryStoredAccount(id, newAmount, incrementedVersion());
	}

	@Override
	public MemoryStoredAccount withdraw(int sum) {
		final int newAmount = this.amount - sum;
		return new MemoryStoredAccount(id, newAmount, incrementedVersion());
	}

	@Override
	public int getCurrentAmount() {
		return amount;
	}

	public int getVersion() {
		return version;
	}

	private int incrementedVersion() {
		return version + 1;
	}

	boolean canBeChangedTo(MemoryStoredAccount other) {
		return this.id == other.id && this.version+1 == other.version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MemoryStoredAccount that = (MemoryStoredAccount) o;
		return id == that.id &&
				amount == that.amount &&
				version == that.version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, amount, version);
	}
}
