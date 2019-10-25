package ru.fedinskiy.database;

public interface VersionedAccount {
	int getId();

	int getCurrentAmount();

	int getVersion();

	default boolean canBeChangedTo(VersionedAccount other) {
		return this.getId() == other.getId() && this.getVersion() + 1 == other.getVersion();
	}
}
