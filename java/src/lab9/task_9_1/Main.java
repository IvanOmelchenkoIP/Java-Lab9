package lab9.task_9_1;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import lab9.task_9_1.bank.Account;
import lab9.task_9_1.bank.Bank;
import lab9.task_9_1.bank.BankAccountsManager;
import lab9.task_9_1.transfer.TransferManager;

public class Main {

	public static void main(String[] args) {		
		final int ACCOUNTS_AMOUNT = 250;
		final int ACCOUNT_MAX_INIT_BALANCE = 10000000;
		final int MAX_TRANSACTION = 500000;
		final int THREAD_AMOUNT = 5000;
		
		Random random = new Random();
		
		BankAccountsManager accountsManager = new BankAccountsManager(new Bank());
		ArrayList<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < ACCOUNTS_AMOUNT; i++) {
			accounts.add(accountsManager.createAccount(random.nextInt(ACCOUNT_MAX_INIT_BALANCE)));
		}
		int initTotalBalance = accountsManager.getTotalAccountBalance();	
		
		CountDownLatch barrier = new CountDownLatch(THREAD_AMOUNT);
		TransferManager transferManager = new TransferManager(accounts, accountsManager, barrier);
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < THREAD_AMOUNT; i++) {
			Account from = accounts.get(random.nextInt(ACCOUNTS_AMOUNT));
			Account to = accounts.get(random.nextInt(ACCOUNTS_AMOUNT));
			int amount = random.nextInt(MAX_TRANSACTION);
			//threads.add(new Thread(transferManager.newTransfer(from, to, amount)));
			new Thread(transferManager.newTransfer(from, to, amount)).start();
		}
		/*for (Thread thread : threads) {
			thread.start();
		}*/
		try {
			barrier.await();
			int transferTotalBalance = accountsManager.getTotalAccountBalance();
		
			System.out.println("Initial balance: " + initTotalBalance);
			System.out.println("Balance after transactions: " + transferTotalBalance);
			System.out.println("Balances are equal: " + (initTotalBalance == transferTotalBalance));
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
