package ru.fedinskiy.database;

class IdOrderedPair {
	private final VersionedAccount first;
	private final VersionedAccount second;

	IdOrderedPair(VersionedAccount left, VersionedAccount right) {
		final int leftId = left.getId();
		final int rightId = right.getId();
		if (leftId < rightId) {
			this.first = left;
			this.second = right;
		} else if (rightId < leftId) {
			this.first = right;
			this.second = left;
		} else {
			throw new IllegalArgumentException("Account " + leftId + " cannot be updated at the same time withn itself!");
		}
	}

	public VersionedAccount getFirst() {
		return first;
	}

	public VersionedAccount getSecond() {
		return second;
	}
}