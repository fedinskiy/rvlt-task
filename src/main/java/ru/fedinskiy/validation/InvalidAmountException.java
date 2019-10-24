package ru.fedinskiy.validation;

public class InvalidAmountException extends Exception {
	public InvalidAmountException(String id) {
		super(id + " is not a valid amount of money!");
	}
}
