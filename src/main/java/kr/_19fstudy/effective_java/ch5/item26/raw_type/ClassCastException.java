package kr._19fstudy.effective_java.ch5.item26.raw_type;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ClassCastException {
	public static void main(String[] args) {

		final Collection stamps = new ArrayList();

		stamps.add(new Coin());
		stamps.add(new Stamp());

		Stamp stamp =  (Stamp)((ArrayList) stamps).get(0);
		stamp = (Stamp)((ArrayList) stamps).get(1);

	}
}
