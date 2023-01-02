package lab9.task_9_1;

import java.util.ArrayList;
import java.util.Random;

import lab9.task_9_1.bank.Account;
import lab9.task_9_1.bank.Bank;

public class Main {

	public static void main(String[] args) {

		Bank bank = new Bank();
		
		//Account a1 = new Account(200);
		//Account a2 = new Account(300);
		
		int accountNumber = 20;
		int maxMoney = 500;
		Random random = new Random();
		int moneyAmount = 0;
		ArrayList<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < accountNumber; i++) {
			int money = random.nextInt(maxMoney);	
			moneyAmount += money;
			accounts.add(new Account(money));
		}
		
		bank.transfer(accounts.get(random.nextInt(accountNumber)), accounts.get(random.nextInt(accountNumber)), random.nextInt(maxMoney));
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < 3000; i++) threads.add(new Thread());
		//bank.transfer(a1, a2, 30);
		
		System.out.println(moneyAmount);
		//System.out.println(a2.getMoney());
	}

}
