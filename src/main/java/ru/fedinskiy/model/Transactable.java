package ru.fedinskiy.model;

public interface Transactable extends Account {
	Transactable add(int amount);
	Transactable withdraw(int amount);
}
