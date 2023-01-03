package lab9.task_9_2.buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ConcurrentCircularBuffer {
	
	final int size;
	
	String[] elements;
	volatile int writeIndex = -1;
	volatile int readIndex = 0;
		
	private ReentrantReadWriteLock bufferLock;
	private ReadLock readLock;
	private WriteLock writeLock;
	private Condition dataAvailable;
	
	public ConcurrentCircularBuffer(int size) {
		this.size = size;
		this.elements = new String[size];
		
		this.bufferLock = new ReentrantReadWriteLock();
		this.readLock = bufferLock.readLock();
		this.writeLock = bufferLock.writeLock();
		this.dataAvailable = writeLock.newCondition();
	}
	
	private int updateIndex(int index) {
		index += 1;
		if (index >= size) {
			index = 0;
		}
		return index;
	}
	
	private void awaitData() throws InterruptedException {
		if (elements[readIndex] == null) {
			readLock.unlock();
			writeLock.lock();
			try {
				while (elements[readIndex] == null) {
					dataAvailable.await();
				}
				readLock.lock();
			} finally {
				writeLock.unlock();
			}
		}
	}
	
	public void put(String message) {
		writeLock.lock();
		try {
			writeIndex = updateIndex(writeIndex);
			elements[writeIndex] = message;
			if (writeIndex == readIndex) {
				dataAvailable.signalAll();
			}
		} finally {
			writeLock.unlock();
		}
	}
	
	public String read() throws InterruptedException {
		readLock.lock();
		try {
			readIndex = updateIndex(readIndex);
			awaitData();
			return elements[readIndex];
		} finally {
			readLock.unlock();
		}
	}
}
