package lab9.task_9_2.buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ConcurrentCircularBuffer {
	
	private final int bufferSize;
	
	private String[] elements;
	private volatile int tailIndex = -1;
	private volatile int headIndex = 0;
		
	private final ReentrantReadWriteLock bufferLock;
	private final ReadLock readLock;
	private final WriteLock writeLock;
	private final Condition dataAvailable;
	
	public ConcurrentCircularBuffer(int bufferSize) {
		this.bufferSize = bufferSize;
		this.elements = new String[bufferSize];
		
		this.bufferLock = new ReentrantReadWriteLock();
		this.readLock = bufferLock.readLock();
		this.writeLock = bufferLock.writeLock();
		this.dataAvailable = writeLock.newCondition();
	}
	
	private boolean bufferEmpty() {
		return headIndex == tailIndex || elements[headIndex % bufferSize]== null;
	}
	
	private boolean bufferFull() {
		return headIndex >= tailIndex;
	}
	
	private void awaitDataAvailable() throws InterruptedException {
		if (bufferEmpty()) {
			readLock.unlock();
			writeLock.lock();
			try {
				while (bufferEmpty()) {
					dataAvailable.await();
				}
				readLock.lock();
			} finally {
				writeLock.unlock();
			}
		}
	}
	
	public void put(String message) throws InterruptedException {
		writeLock.lock();
		try {
			int tmpTail = (tailIndex + 1) % bufferSize;
			elements[tmpTail] = message;
			tailIndex++;
			if (tmpTail == (headIndex % bufferSize)) {
				dataAvailable.signalAll();
			}
		} finally {
			writeLock.unlock();
		}
	}
	
	public String take() throws InterruptedException {
		readLock.lock();
		try {
			awaitDataAvailable();
			String element = elements[headIndex % bufferSize];
			headIndex++;
			return element;
		} finally {
			readLock.unlock();
		}
	}
}
