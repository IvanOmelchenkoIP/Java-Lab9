package lab9.task_9_1.bank;

import java.util.ArrayList;

public class BankAccountsManager {

	private int bankTotalMoney = 0;
	ArrayList<Account> accountList;
	
	public BankAccountsManager() {
		accountList = new ArrayList<Account>();
	}
	
	public Account createAccount(int initBalance) { 
		bankTotalMoney += initBalance;
		return new Account(initBalance);
	}
	
	public void tryTransfer(Account from, Account to, int amount) { 
		
	}
}
