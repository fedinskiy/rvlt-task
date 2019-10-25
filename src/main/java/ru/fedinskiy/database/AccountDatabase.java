package ru.fedinskiy.database;

import ru.fedinskiy.transfer.MoneyHolder;

import java.util.Optional;

public interface AccountDatabase<T extends MoneyHolder> {
	Optional<T> get(int id);

	boolean createIfNotExist(T account);

	boolean update(T account);

	boolean updateInSameTransaction(T one, T another);
}
