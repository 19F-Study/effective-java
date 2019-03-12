package kr._19fstudy.effective_java.ch4.item16;

import java.awt.*;

public class ClientItem16 {
	public static void main(String[] args) {
//		Dimension dimension = new Dimension();
//		dimension.width = 100;
//		dimension.height = 1000;
//
//		System.out.println(dimension.getSize());

		String s = "SETH";
		String s1 = "SET" + "H";
		String s2 = "SET";
		String s3 = "H";

		String s4 = s2 + s3;

		System.out.println(s4);
	}
}
