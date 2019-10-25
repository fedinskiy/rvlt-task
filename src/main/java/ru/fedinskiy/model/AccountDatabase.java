package ru.fedinskiy.model;

import java.util.Optional;

public interface AccountDatabase<T extends Transactable> {
	Optional<T> get(int id);

	boolean createIfNotExist(T account);

	boolean update(T account);

	boolean updateInSameTransaction(T one, T another);
}
