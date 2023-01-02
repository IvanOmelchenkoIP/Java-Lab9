package lab9.task_9_1.bank;

import java.util.ArrayList;

public class BankAccountsManager {

	private final static int bankID = 0;
	private int bankTotalMoney = 0;
	ArrayList<Account> accountList;
	
	public BankAccountsManager() {
		accountList = new ArrayList<Account>();
	}
	
	public Account createAccount(int initBalance) { throw new RuntimeException("NotImplemented"); }
	
	public void tryTransfer(Account from, Account to, int amount) { }
}
