package ru.fedinskiy.transfer;

public interface MoneyHolder {
	MoneyHolder add(int amount);
	MoneyHolder withdraw(int amount);
	int getCurrentAmount();
}
