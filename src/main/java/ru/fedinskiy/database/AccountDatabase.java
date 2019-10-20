package ru.fedinskiy.database;

import java.util.Optional;

public interface AccountDatabase {
	Optional<Account> get(int id);

	boolean put(Account account);
}
