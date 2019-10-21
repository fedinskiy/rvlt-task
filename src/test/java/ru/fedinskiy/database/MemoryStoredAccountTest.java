package ru.fedinskiy.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoryStoredAccountTest {
	@Test
	void versionCheck() {
		MemoryStoredAccount acc = new MemoryStoredAccount(1);
		final MemoryStoredAccount changed = acc.add(10);
		assertTrue(acc.canBeChangedTo(changed));
	}
}