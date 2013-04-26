package rosick.jglsdk;

import java.nio.Buffer;


public abstract class BufferableData<T extends Buffer> {		
	
	public abstract T fillBuffer(T buffer);
	
	public T fillAndFlipBuffer(T buffer) {
		buffer.clear();
		fillBuffer(buffer);
		buffer.flip();
		
		return buffer;
	}
}