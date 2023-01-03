package lab9.task_9_2;

import lab9.task_9_2.buffer.ConcurrentCircularBuffer;
import lab9.task_9_2.threads.MessageGenerator;
import lab9.task_9_2.threads.MessageSwapper;

public class Main {

	public static void main(String[] args) {
		final int BUFFER_SIZE = 32;
		
		final int GENERATOR_THREADS = 5;
		final int SWAPPER_THREADS = 2;
		
		final int MESSAGES_PRINTED = 100;
		
		ConcurrentCircularBuffer generatedBuffer = new ConcurrentCircularBuffer(BUFFER_SIZE);
		ConcurrentCircularBuffer swapperBuffer = new ConcurrentCircularBuffer(BUFFER_SIZE);
		
		for (int i = 0; i < GENERATOR_THREADS; i++) {
			Thread thread = new Thread(new MessageGenerator(generatedBuffer));
			thread.setDaemon(true);
			thread.setName("GENERATOR_THREAD_" + i);
			thread.start();
		}
		
		for (int i = 0; i < SWAPPER_THREADS; i++) {
			Thread thread = new Thread(new MessageSwapper(generatedBuffer, swapperBuffer));
			thread.setDaemon(true);
			thread.setName("SWAPPER_THREAD_" + i);
			thread.start();
		}
		
		for (int i = 0; i < MESSAGES_PRINTED; i++) {
			try {
				System.out.println("Трансфер " + i + ". " + swapperBuffer.take());
			} catch (InterruptedException ex) {
				System.out.println(ex.getMessage());
			}
		}
		System.out.println("Вибір повідомлень завершено...");
	}
}
