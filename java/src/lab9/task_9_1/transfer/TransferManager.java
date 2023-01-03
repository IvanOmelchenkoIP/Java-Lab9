package lab9.task_9_1.transfer;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import lab9.task_9_1.bank.Account;
import lab9.task_9_1.bank.BankAccountsManager;

public class TransferManager{

	ArrayList<Account> accounts;
	BankAccountsManager accountsManager;
	CountDownLatch barrier;

	public TransferManager(ArrayList<Account> accounts, BankAccountsManager accountsManager, CountDownLatch barrier) {
		this.accounts = accounts;
		this.accountsManager = accountsManager;
		this.barrier = barrier;
	}

	public Runnable newTransfer(Account from, Account to, int amount) {
		return new Runnable() {

			@Override
			public void run() {
				try {
					accountsManager.tryConcurrentTransfer(from, to, amount);
					System.out.println("Transfer in thread " + Thread.currentThread().getName() + " successful");
				} catch (Exception ex) {
					System.out.println("Transfer in thread " + Thread.currentThread().getName() + " unsuccessful: " + ex.getMessage());
				} finally {
					barrier.countDown();
				}
			}
		};
	}
}
