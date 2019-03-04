package kr._19fstudy.effective_java.ch2.item7.weak_hash_map;

import java.util.WeakHashMap;

public class WeakHashMapMain {
	public static void main(String[] args) {
		WeakHashMap<Integer, String> map = new WeakHashMap<>();

		Integer key1 = 1000;
		Integer key2 = 2000;

		map.put(key1, "test a");
		map.put(key2, "test b");

		key1 = null;

		System.gc();  //강제 Garbage Collection

		map.entrySet().forEach(System.out::println);
	}
}
