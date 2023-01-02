package lab9.task_9_1.bank;

public class Account {

	private int money;
	
	public Account(int money) {
		this.money = money;
	}
	
	public void withdraw(int amount) {
		int changed = money - amount;
		if (changed >= 0) {
			money -= amount;
		}
	}
	
	public void deposit(int amount) {
		money += amount;
	}
	
	public int getMoney() {
		return money;
	}
}
