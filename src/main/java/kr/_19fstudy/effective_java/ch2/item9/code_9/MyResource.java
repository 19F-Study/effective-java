package kr._19fstudy.effective_java.ch2.item9.code_9;

public class MyResource implements AutoCloseable {
	@Override
	public void close() throws Exception {
		throw new Exception("close() Exception");
	}

	public void exception() throws Exception {
		throw new Exception("exception() Exception");
	}
}
