package ru.fedinskiy.database;

import java.util.Optional;

public interface AccountDatabase<T extends Account> {
	Optional<T> get(int id);

	boolean createIfNotExist(T account);

	boolean update(T account);

	boolean updateInSameTransaction(T one, T another);
}
