package lab9.task_9_2;

import java.nio.Buffer;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private static Buffer circularBuffer() {
		return BufferUtils.synchronizedBuffer(new CircularFifoBuffer());
	}

}
