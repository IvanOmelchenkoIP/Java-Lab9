package lab9.task_9_1.transfer;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import lab9.task_9_1.bank.Account;
import lab9.task_9_1.bank.BankAccountsManager;

public class TransferManager {
	
	int accountsAmount;
	ArrayList<Account> accounts;
	BankAccountsManager accountManager;
	CountDownLatch barrier;

	public TransferManager(int accountsAmount, ArrayList<Account> accounts, BankAccountsManager accountManager,
			CountDownLatch barrier) {
		this.accountsAmount = accountsAmount;
		this.accounts = accounts;
		this.accountManager = accountManager;
		this.barrier = barrier;
	}

	public Runnable newTransfer() {
		return new Runnable() {
			@Override
			public void run() {
				Random random = new Random();
				Account from = accounts.get(random.nextInt(accountsAmount));
				Account to;
				do {
					to = accounts.get(random.nextInt(accountsAmount));
				} while (from == to);
				accountManager.tryConcurrentTransfer(from, to, random.nextInt(100));
				barrier.countDown();
			}
		};
	}
}
