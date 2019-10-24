package ru.fedinskiy.validation;

public interface InputValidation {
	default int parseId(String source) throws InvalidAccountIdException {
		try {
			return toPositiveInteger(source);
		} catch (Exception ex) {
			throw new InvalidAccountIdException(source);
		}
	}

	default int parseAmount(String source) throws InvalidAmountException {
		try {
			return toPositiveInteger(source);
		} catch (Exception ex) {
			throw new InvalidAmountException(source);
		}
	}

	static int toPositiveInteger(String source) throws NumberFormatException, IllegalArgumentException {
		final int result = Integer.parseInt(source);
		if (result <= 0) {
			throw new IllegalArgumentException("Negative value!");
		}
		return result;
	}
}
