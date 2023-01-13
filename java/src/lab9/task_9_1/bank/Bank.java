package lab9.task_9_1.bank;

import java.util.concurrent.locks.Lock;

public class Bank {
	
	public void transfer(Account from, Account to, int amount) {
		Lock fromLock = from.acquireAccountLock();
		Lock toLock = to.acquireAccountLock();
		fromLock.lock();
		toLock.lock();
		try {
			from.withdraw(amount);
			to.deposit(amount);
		} finally {
			toLock.unlock();
			fromLock.unlock();
		}
		
	}
}
