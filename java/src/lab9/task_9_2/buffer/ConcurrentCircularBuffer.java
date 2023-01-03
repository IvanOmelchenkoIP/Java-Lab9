package lab9.task_9_2.buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ConcurrentCircularBuffer {
	
	/*final int size;
	int placesTaken = 0;
	
	String[] elements;
	volatile int writeIndex = -1;
	volatile int readIndex = -1;
		
	private final ReentrantLock bufferLock;
	private final ReentrantLock freeLock;
	private final Condition availableCondition;
	private final Condition fullCondition;
	
	public ConcurrentCircularBuffer(int size) {
		this.size = size;
		this.elements = new String[size];
		
		this.bufferLock = new ReentrantLock();
		this.freeLock = new ReentrantLock();
		this.availableCondition = bufferLock.newCondition();
		this.fullCondition = freeLock.newCondition();
	}
	
	private boolean bufferEmpty() {
		return readIndex == writeIndex || elements[readIndex] == null;
	}
	
	private boolean bufferFull() {
		return placesTaken == size;
	}
	
	private int updateIndex(int index) {
		index += 1;
		if (index >= size) {
			index = 0;
		}
		return index;
	}
	
	
	private void awaitBufferPlacesFreed() throws InterruptedException {
		if (bufferFull()) {
			bufferLock.unlock();
			freeLock.lock();
			try {
				while (bufferEmpty()) {
					fullCondition.await();
				}
				bufferLock.lock();
			} finally {
				freeLock.unlock();
			}
		}
	}

	public void put(String message) throws InterruptedException {
		bufferLock.lock();
		try {
			writeIndex = updateIndex(writeIndex);
			awaitBufferPlacesFreed();
			elements[writeIndex] = message;
			placesTaken += 1;
			if (writeIndex == readIndex) {
				availableCondition.signalAll();
			}
		} finally {
			bufferLock.unlock();
		}
	}
	
	
	private void awaitDataAvailable() throws InterruptedException {
		if (bufferEmpty()) {
			freeLock.unlock();
			bufferLock.lock();
			try {
				while (bufferEmpty()) {
					availableCondition.await();
				}
				freeLock.lock();
			} finally {
				bufferLock.unlock();
			}
		}
	}
	
	public String take() throws InterruptedException {
		freeLock.lock();
		try {
			readIndex = updateIndex(readIndex);
			awaitDataAvailable();
			placesTaken -= 1;
			String element = elements[readIndex];
			elements[readIndex] = null;
			fullCondition.signalAll();
			return element;
		} finally {
			freeLock.unlock();
		}
	}*/
	
	
	
	final int size;
	int placesTaken = 0;
	
	String[] elements;
	volatile int writeIndex = -1;
	volatile int readIndex = 0;
		
	private final ReentrantReadWriteLock bufferLock;
	private final ReadLock readLock;
	private final WriteLock writeLock;
	private final Condition dataAvailable;
	
	public ConcurrentCircularBuffer(int size) {
		this.size = size;
		this.elements = new String[size];
		
		this.bufferLock = new ReentrantReadWriteLock();
		this.readLock = bufferLock.readLock();
		this.writeLock = bufferLock.writeLock();
		this.dataAvailable = writeLock.newCondition();
	}
	
	private boolean bufferEmpty() {
		return readIndex == writeIndex;
	}
	
	private boolean bufferFull() {
		return placesTaken == size;
	}
	
	private int updateIndex(int index) {
		index += 1;
		if (index >= size) {
			index = 0;
		}
		return index;
	}
	
	
	private void awaitDataAvailable() throws InterruptedException {
		if (readIndex == writeIndex) {
			readLock.unlock();
			writeLock.lock();
			try {
				while (readIndex == writeIndex) {
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
			writeIndex = updateIndex(writeIndex);
			elements[writeIndex] = message;
			placesTaken += 1;
			if (writeIndex == readIndex) {
				dataAvailable.signalAll();
			}
		} finally {
			writeLock.unlock();
		}
	}
	
	public String take() throws InterruptedException {
		readLock.lock();
		try {
			readIndex = updateIndex(readIndex);
			awaitDataAvailable();
			placesTaken -= 1;
			return elements[readIndex];
		} finally {
			readLock.unlock();
		}
	}
}
