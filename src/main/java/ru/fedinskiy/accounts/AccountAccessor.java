package ru.fedinskiy.accounts;

import io.micronaut.context.annotation.Prototype;
import ru.fedinskiy.database.Account;
import ru.fedinskiy.database.AccountDatabase;
import ru.fedinskiy.database.MemoryStoredAccount;

import javax.inject.Inject;
import java.util.Optional;

@Prototype
public class AccountAccessor {
	private final AccountDatabase database;

	@Inject
	public AccountAccessor(AccountDatabase database) {
		this.database = database;
	}

	public int getCurrentSumOnAccount(int id) {
		Optional<Account> account = database.get(id);
		return account.map(Account::getCurrentAmount)
				.orElseThrow(() -> new IllegalStateException("Account not found!"));
	}

	public void createAccount(int id, int amount) {
		final MemoryStoredAccount account = new MemoryStoredAccount(id).add(amount);
		synchronized (database){
			if(database.get(id).isPresent()){
				throw new IllegalStateException("Account with this if exists!");
			}
			database.createIfNotExist(account);
		}
	}
}
