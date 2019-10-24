package ru.fedinskiy.validation;

public class AccountNotFoundException extends Exception {
	private final int id;

	public AccountNotFoundException(int id) {
		super("Account with id " + id + " was not found!");
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
