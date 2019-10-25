package ru.fedinskiy.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoryStoredAccountTest {
	@Test
	void versionCheck() {
		ImmutableAccount acc = new ImmutableAccount(1);
		final ImmutableAccount changed = acc.add(10);
		assertTrue(acc.canBeChangedTo(changed));
	}
}