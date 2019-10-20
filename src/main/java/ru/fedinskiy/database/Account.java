package ru.fedinskiy.database;

public interface Account {
	int getId();

	Account add(int sum);

	Account withdraw(int sum);

	int getCurrentAmount();
}
