package kr._19fstudy.effective_java.ch2.item9.code_9.after;

import kr._19fstudy.effective_java.ch2.item9.code_9.MyResource;

import java.io.*;

public class Example {

	static String firstLineOfFile(String path) throws IOException {


		//try () 안에는 AutoCloseable 구현체만 들어갈 수 있다.
		//try catch 문이 종료되면서 AutoCloseable.close 를 저절로 호출해준다.
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			return br.readLine();
		} catch(IOException e) {
			return "DEFAULT_VALUE";
		}
	}

	static void copy(String src, String dst) throws IOException {
		try (InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dst)) {
			byte[] buf = new byte[100000];
			int n;
			while (( n = in.read(buf)) >= 0)
					out.write(buf, 0, n);
		}
	}

	static void myMethod() throws Exception {
		try(MyResource myResource = new MyResource()) {
			myResource.exception();
		}
	}
}
