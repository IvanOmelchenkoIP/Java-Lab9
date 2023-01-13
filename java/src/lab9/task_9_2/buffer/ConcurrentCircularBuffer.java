package lab9.task_9_2.buffer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentCircularBuffer {
	
	private final int bufferSize;
	
	private final String[] buffer;
	private int tailIndex = 0;
	private int headIndex = 0;
	
	private final ReentrantLock lock;
	private final Condition bufferFreed;
	private final Condition bufferReadable;
	
	public ConcurrentCircularBuffer(int bufferSize) {
		this.bufferSize = bufferSize;
		this.buffer = new String[bufferSize];

		this.lock = new ReentrantLock();
		this.bufferFreed = lock.newCondition();
		this.bufferReadable = lock.newCondition();
	}
	
	private boolean bufferEmpty() {
		return headIndex >= tailIndex;
	}
	
	private boolean bufferFull() {
		return (tailIndex - headIndex) == bufferSize;
	}
	
	public void put(String message) throws InterruptedException {
		lock.lock();
		try {
			while (bufferFull() ) {
				bufferFreed.await();
			}
			buffer[tailIndex % bufferSize] = message;
			if (tailIndex == headIndex) {
				bufferReadable.signal();
			}
			tailIndex++;
		} finally {
			lock.unlock();
		}
	}
	
	public String take() throws InterruptedException {
		lock.lock();
		try {
			while (bufferEmpty()) {
				bufferReadable.await();
			}
			String element = buffer[headIndex % bufferSize];
			bufferFreed.signal();
			headIndex++;
			return element;
		} finally {
			lock.unlock();
		}
	}
}