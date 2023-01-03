package lab9.task_9_2.threads;

import lab9.task_9_2.buffer.ConcurrentCircularBuffer;

public class MessageGenerator implements Runnable {
	
	int msgID = 0;
	ConcurrentCircularBuffer generatedBuffer;
	
	public MessageGenerator(ConcurrentCircularBuffer generatedBuffer) {
		this.generatedBuffer = generatedBuffer;
	}

	@Override
	public void run() {
		while (true) {
			String message = "Потік №" + Thread.currentThread().getName() + " згенерував повідомлення " + msgID++;
			try {
				generatedBuffer.put(message);
			} catch (InterruptedException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
}
