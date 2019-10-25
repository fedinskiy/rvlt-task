package ru.fedinskiy.database;

import java.util.Objects;

public final class StoredAccount implements VersionedAccount {
	private final int id;
	private volatile int amount;
	private volatile int version;

	public StoredAccount(VersionedAccount account) {
		this.id = account.getId();
		this.amount = account.getCurrentAmount();
		this.version = 0;
	}

	public int getId() {
		return id;
	}

	public int getCurrentAmount() {
		return amount;
	}

	public int getVersion() {
		return version;
	}

	public synchronized void updateAmount(int amount) {
		this.amount = amount;
		this.version++;
	}

	public synchronized boolean canBeChangedTo(VersionedAccount other) {
		return this.id == other.getId() && this.version + 1 == other.getVersion();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StoredAccount that = (StoredAccount) o;
		return id == that.id &&
				amount == that.amount &&
				version == that.version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, amount, version);
	}
}
