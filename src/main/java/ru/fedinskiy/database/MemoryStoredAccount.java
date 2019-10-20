package ru.fedinskiy.database;

public class MemoryStoredAccount implements Account {
	private final int id;
	private int amount = 0;

	public MemoryStoredAccount(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void add(int sum) {
		amount += sum;
	}

	@Override
	public void withdraw(int sum) {
		amount -= sum;
	}

	@Override
	public int getCurrentAmount() {
		return amount;
	}
}
