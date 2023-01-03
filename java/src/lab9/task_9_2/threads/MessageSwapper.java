package lab9.task_9_2.threads;

import lab9.task_9_2.buffer.ConcurrentCircularBuffer;

public class MessageSwapper implements Runnable {

	ConcurrentCircularBuffer generatedBuffer;
	ConcurrentCircularBuffer swapperBuffer;
	
	public MessageSwapper(ConcurrentCircularBuffer generatedBuffer, ConcurrentCircularBuffer swapperBuffer) {
		this.generatedBuffer = generatedBuffer;
		this.swapperBuffer = swapperBuffer;
	}

	@Override
	public void run() {
		while(true) {
			try {
				String message = generatedBuffer.take();
				String swappedMessage = "Потік №" + Thread.currentThread().getName() + " переклав повідомлення: '" + message + "'";
				swapperBuffer.put(swappedMessage);
			} catch (InterruptedException ex) {
				System.out.println(ex.getMessage());
				continue;
			}
		}
	}
}
