package lab9.task_9_1.bank;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class Account {

	private int balance;
	
	private ReentrantReadWriteLock accountLock;
	private ReadLock readLock;
	private WriteLock writeLock;
	
	public Account(int initBalance) {
		this.balance = initBalance;
		this.accountLock = new ReentrantReadWriteLock();
		this.readLock = this.accountLock.readLock();
		this.writeLock = this.accountLock.writeLock();
	}
	
	public void withdraw(int amount) {
		balance -= amount;
	}
	
	public void deposit(int amount) {
		balance += amount;
	}
	
	public int getBalance() {
		readLock.lock();
		try{
			return balance;		
		} finally {
			readLock.unlock();
		}
	}
	
	public WriteLock acquireAccountLock() {
		return writeLock;
	}
}
