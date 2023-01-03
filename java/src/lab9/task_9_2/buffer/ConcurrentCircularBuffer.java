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
	
	private boolean bufferFilled = false;
	
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
	
	public void add(String message) {
		writeLock.lock();
		try {
			writeIndex += 1;
			if (writeIndex >= size) {
				writeIndex = 0;
			}
			elements[writeIndex] = message;
			if (!bufferFilled && writeIndex == readIndex) {
				dataAvailable.signalAll();
			}
		} finally {
			writeLock.unlock();
		}
	}
	
	public String take() throws InterruptedException {
		readLock.lock();
		try {
			readIndex += 1;
			if (readIndex >= size) {
				readIndex = 0;
			}
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
			return elements[readIndex];
		} finally {
			readLock.unlock();
		}
	}
}
