package ru.fedinskiy.database;

import ru.fedinskiy.transfer.MoneyHolder;

import java.util.Objects;

public final class ImmutableAccount implements VersionedAccount, MoneyHolder {
	private final int id;
	private final int amount;
	private final int version;

	public ImmutableAccount(int id) {
		this.id = id;
		this.amount = 0;
		this.version = 0;
	}

	public ImmutableAccount(VersionedAccount account) {
		this.id = account.getId();
		this.amount = account.getCurrentAmount();
		this.version = account.getVersion();
	}

	private ImmutableAccount(int id, int amount, int version) {
		this.id = id;
		this.amount = amount;
		this.version = version;
	}

	@Override
	public int getId() {
		return id;
	}

	public ImmutableAccount add(int sum) {
		int newAmount = amount + sum;
		return new ImmutableAccount(id, newAmount, incrementedVersion());
	}

	public ImmutableAccount withdraw(int sum) {
		final int newAmount = this.amount - sum;
		return new ImmutableAccount(id, newAmount, incrementedVersion());
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableAccount that = (ImmutableAccount) o;
		return id == that.id &&
				amount == that.amount &&
				version == that.version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, amount, version);
	}
}
