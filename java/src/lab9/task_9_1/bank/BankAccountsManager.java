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
	
	public void tryConcurrentTransfer(Account from, Account to, int amount) { 
		if (!accountList.contains(from) || !accountList.contains(from)) return;
		if (amount > from.getBalance()) return;
		
		Lock fromLock = from.acquireAccountLock();
		Lock toLock = to.acquireAccountLock();
		fromLock.lock();
		toLock.lock();
		try {
			bank.transfer(from, to, amount);
		} finally {
			fromLock.unlock();
			toLock.unlock();
		}
	}
	
	public int getTotalAccountBalance() {
		int totalAccountBalance = 0;
		for (Account account : accountList) {
			totalAccountBalance += account.getBalance();
		}
		return totalAccountBalance;
	}
}
