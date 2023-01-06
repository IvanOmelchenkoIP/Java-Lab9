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
		
	private final ReentrantLock tailLock;
	private final ReentrantLock headLock;
	/*private final ReentrantReadWriteLock bufferLock;
	private final ReadLock readLock;
	private final WriteLock writeLock;*/
	//private final Condition dataAvailable;
	//private final Condition spaceFreed;
	private final Condition bufferFreed;
	private final Condition bufferReadable;

	
	public ConcurrentCircularBuffer(int bufferSize) {
		this.bufferSize = bufferSize;
		this.elements = new String[bufferSize];
		
		/*this.bufferLock = new ReentrantReadWriteLock();
		this.readLock = bufferLock.readLock();
		this.writeLock = bufferLock.writeLock();
		this.dataAvailable = writeLock.newCondition();
		this.spaceFreed = writeLock.newCondition();*/
		
		this.tailLock = new ReentrantLock();
		this.headLock = new ReentrantLock();
		this.bufferFreed = headLock.newCondition();
		this.bufferReadable = tailLock.newCondition();
	}
	
	private boolean bufferEmpty() {
		return headIndex == tailIndex || elements[headIndex % bufferSize] == null;
	}
	
	private boolean bufferFull() {
		return (tailIndex - headIndex) + 1 == bufferSize;
	}
	
	private void awaitBufferReadable() throws InterruptedException {
		if (bufferEmpty()) {
			headLock.unlock();
			tailLock.lock();
			System.out.println(1);
			try {
				while (bufferEmpty()) {
					bufferReadable.await();
				}
				headLock.lock();
			} finally {
				tailLock.unlock();
			}
		}
		/*if (bufferEmpty()) {
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
		}*/
	}
	
	private void awaitBufferFreed() throws InterruptedException {
		if (bufferFull()) {
			tailLock.unlock();
			headLock.lock();
			try {
				while (bufferFull()) {
					bufferFreed.await();
				}
				tailLock.lock();
			} finally {
				headLock.unlock();
			}
		}
		/*if (bufferFull() ) {
			readLock.lock();
			writeLock.unlock();
			try {
				while(bufferFull()) {
					spaceFreed.await();
				}
				writeLock.lock();
			} finally {
				readLock.unlock();
			}
		}*/
	}
	
	public void put(String message) throws InterruptedException {
		tailLock.lock();
		try {
			awaitBufferFreed();
			//awaitBufferFreed();
			int tmpTail = (tailIndex + 1) % bufferSize;
			elements[tmpTail] = message;
			tailIndex++;
			if (tmpTail == ((headIndex) % bufferSize)) {
				bufferReadable.signalAll();
			}
		} finally {
			tailLock.unlock();
		}
		/*writeLock.lock();
		try {
			awaitBufferFreed();
			int tmpTail = (tailIndex + 1) % bufferSize;
			elements[tmpTail] = message;
			tailIndex++;
			if (tmpTail == (headIndex % bufferSize)) {
				dataAvailable.signalAll();
			}
		} finally {
			writeLock.unlock();
		}*/
	}
	
	public String take() throws InterruptedException {
		headLock.lock();
		try {
			awaitBufferReadable();
			String element = elements[headIndex % bufferSize];
			headIndex++;
			if (!bufferFull()) {
				bufferFreed.signalAll();
			}
			return element;
		} finally {
			headLock.unlock();
		}
		/*readLock.lock();
		try {
			awaitDataAvailable();
			String element = elements[headIndex % bufferSize];
			headIndex++;
			if (!bufferFull()) {
				spaceFreed.signalAll();
			}
			return element;
		} finally {
			readLock.unlock();
		}*/
	}
}
