package lab9.task_9_1.bank;

public class Bank {
	
	public void transfer(Account from, Account to, int amount) {
		from.withdraw(amount);
		to.deposit(amount);
	}
}
