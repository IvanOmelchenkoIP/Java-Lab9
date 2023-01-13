package lab9.task_9_1.bank;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class BankAccountsManager {
	
	Bank bank;
	ArrayList<Account> accountList;
	
	public BankAccountsManager(Bank bank) {
		this.bank = bank;
		this.accountList = new ArrayList<Account>();
	}
	
	public Account createAccount(int initBalance) { 
		Account account = new Account(initBalance);
		accountList.add(account);
		return account;
	}
	
	public void tryConcurrentTransfer(Account from, Account to, int amount) throws InterruptedException, Exception { 
		if (!accountList.contains(from) || !accountList.contains(from)) {
			throw new Exception("One of specified accounts does not belong to the bank");
		}
		if (amount > from.getBalance()) {
			throw new Exception("Not enough money on the from account to transfer specified amount");
		}
		bank.transfer(from, to, amount);
	}
	
	public int getTotalAccountBalance() {
		int totalAccountBalance = 0;
		for (Account account : accountList) {
			totalAccountBalance += account.getBalance();
		}
		return totalAccountBalance;
	}
}
