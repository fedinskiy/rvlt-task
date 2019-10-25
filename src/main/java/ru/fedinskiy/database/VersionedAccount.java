package ru.fedinskiy.database;

import ru.fedinskiy.model.Account;

interface VersionedAccount extends Account {
	int getId();

	int getVersion();

	default boolean canBeChangedTo(VersionedAccount other) {
		return this.getId() == other.getId() && this.getVersion() + 1 == other.getVersion();
	}
}
