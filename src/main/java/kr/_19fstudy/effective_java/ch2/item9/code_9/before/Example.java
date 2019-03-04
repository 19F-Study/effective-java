package kr._19fstudy.effective_java.ch2.item9.code_9.before;

import kr._19fstudy.effective_java.ch2.item9.code_9.MyResource;

import java.io.*;

public class Example {

	static String firstLineOfFile(String path) throws Exception {
		//기기에 문제가 생긴다면 readLine 과 close 모두 Exception 던지는 상황 가능.
		//두번째 예외가 첫번째 예외를 삼킨다;
		//문제를 진단하려면 첫번째 예외 정보가 필요하다.

		BufferedReader br = new BufferedReader(new FileReader(path));
		try {
			throw new Exception("try exception occured");
//			return br.readLine();
		} finally {
//			br.close();
			throw new Exception("finally exception occured");
		}
	}

	static void copy(String src, String dst) throws IOException {
		InputStream in = new FileInputStream(src);
		try {
			OutputStream out = new FileOutputStream(dst);
			try {
				byte[] buf= new byte[100000];
				int n;
				while((n = in.read(buf)) >= 0)
					out.write(buf, 0, n);
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}

	static void myMethod() throws Exception {
		MyResource myResource = new MyResource();
		try {
			myResource.exception();
		} finally {
			myResource.close();
		}
	}
}
