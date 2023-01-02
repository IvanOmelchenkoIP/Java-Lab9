package lab9.task_9_1;

import java.util.ArrayList;
import java.util.Random;

import lab9.task_9_1.bank.Account;
import lab9.task_9_1.bank.Bank;
import lab9.task_9_1.bank.BankAccountsManager;

public class Main {

	public static void main(String[] args) {		
		//Account a1 = new Account(200);
		//Account a2 = new Account(300);
		
		final int ACCOUNTS_AMOUNT = 50;
		final int ACCOUNT_MAX_INIT_BALANCE = 500;
		final int THREAD_AMOUNT = 200;
		
		Random random = new Random();
		
		BankAccountsManager manager = new BankAccountsManager(new Bank());
		ArrayList<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < ACCOUNTS_AMOUNT; i++) {
			accounts.add(manager.createAccount(random.nextInt(ACCOUNT_MAX_INIT_BALANCE)));
		}
		
		int initTotalBalance = manager.getTotalAccountBalance();		
		System.out.println(initTotalBalance);
		
		/*int accountNumber = 20;
		int maxMoney = 500;
		Random random = new Random();
		int moneyAmount = 0;
		ArrayList<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < accountNumber; i++) {
			int money = random.nextInt(maxMoney);	
			moneyAmount += money;
			accounts.add(new Account(money));
		}		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < 3000; i++) threads.add(new Thread());
		//bank.transfer(a1, a2, 30);
		
		System.out.println(moneyAmount);*/
		//System.out.println(a2.getMoney());
	}

}
