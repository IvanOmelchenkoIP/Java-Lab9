package lab9.task_9_2;

import lab9.task_9_2.buffer.ConcurrentCircularBuffer;

public class Main {

	public static void main(String[] args) {
		final int BUFFER_SIZE = 32;
		
		final int GENERATOR_THREADS = 5;
		final int SWAPPER_THREADS = 2;
		
		final int MESSAGES_PRINTED = 100;
		
		ConcurrentCircularBuffer generatedBuffer = new ConcurrentCircularBuffer(50);
		ConcurrentCircularBuffer swapperBuffer = new ConcurrentCircularBuffer(50);
		
		for (int i = 0; i < MESSAGES_PRINTED; i++) {
		}
	}

}
