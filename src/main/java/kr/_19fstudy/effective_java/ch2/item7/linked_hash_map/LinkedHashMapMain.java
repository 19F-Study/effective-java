package kr._19fstudy.effective_java.ch2.item7.linked_hash_map;

import java.util.LinkedHashMap;

public class LinkedHashMapMain {
	public static void main(String[] args) {
		LinkedHashMap linkedHashMap = new LinkedHashMap();

		for (int i = 0; i < 15;  i++) {
			linkedHashMap.put(i, String.format("value of %d", i));
			System.out.println(linkedHashMap.size());
		}

		CachedLinkedHashMap cachedLinkedHashMap = new CachedLinkedHashMap();
		for (int i = 0; i < 15;  i++) {
			cachedLinkedHashMap.put(i, String.format("value of %d", i));
			System.out.println(cachedLinkedHashMap.size());
		}
	}
}
