package lab9.task_9_2.buffer;

public class ConcurrentCircularBuffer {
	
	final int size;
	
	String[] elements;
	int writeIndex = 0;
	int readIndex = 0;
	
	public ConcurrentCircularBuffer(int size) {
		this.size = size;
		this.elements = new String[size];
	}
	
	public void add(String message) {
		elements[writeIndex++] = message;
		if (writeIndex >= size) writeIndex = 0;
	}
	
	public String take() {
		String element = elements[readIndex++];
		if (readIndex >= size) readIndex = 0;
		return element;
	}
}
