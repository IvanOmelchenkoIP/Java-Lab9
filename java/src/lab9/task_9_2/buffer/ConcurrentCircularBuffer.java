package lab9.task_9_2.buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentCircularBuffer {
	
	private final int bufferSize;
	
	private String[] buffer;
	private volatile int tailIndex = 0;
	private volatile int headIndex = 0;
		
	private final ReentrantLock tailLock;
	private final ReentrantLock headLock;
	private final Condition bufferFreed;
	private final Condition bufferReadable;

	
	public ConcurrentCircularBuffer(int bufferSize) {
		this.bufferSize = bufferSize;
		this.buffer = new String[bufferSize];
		
		this.tailLock = new ReentrantLock();
		this.headLock = new ReentrantLock();
		this.bufferFreed = headLock.newCondition();
		this.bufferReadable = tailLock.newCondition();
	}
	
	private boolean bufferEmpty() {
		return headIndex >= tailIndex;
	}
	
	private boolean bufferFull() {
		return (tailIndex - headIndex) == bufferSize;
	}
	
	private void awaitBufferReadable() throws InterruptedException {
		if (bufferEmpty()) {
			headLock.unlock();
			tailLock.lock();
			try {
				while (bufferEmpty()) {
					bufferReadable.await();
				}
				headLock.lock();
			} finally {
				tailLock.unlock();
			}
		}
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
	}
	
	public void put(String message) throws InterruptedException {
		tailLock.lock();
		try {
			awaitBufferFreed();
			buffer[tailIndex % bufferSize] = message;
			if (tailIndex == headIndex) {
				bufferReadable.signal();
			}
			tailIndex++;
		} finally {
			tailLock.unlock();
		}
	}
	
	public String take() throws InterruptedException {
		headLock.lock();
		try {
			awaitBufferReadable();
			String element = buffer[headIndex % bufferSize];
			bufferFreed.signal();
			headIndex++;
			return element;
		} finally {
			headLock.unlock();
		}
	}
}