package ru.fedinskiy.validation;

public class InvalidAccountIdException extends Exception {
	public InvalidAccountIdException(String id) {
		super(id + " is not a valid account id!");
	}
}
