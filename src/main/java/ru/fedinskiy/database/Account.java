package ru.fedinskiy.database;

public interface Account {
	int getId();

	void add(int sum);

	void withdraw(int sum);

	int getCurrentAmount();
}
