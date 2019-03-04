package kr._19fstudy.effective_java.ch2.item7.linked_hash_map;

import java.util.LinkedHashMap;
import java.util.Map;

public class CachedLinkedHashMap extends LinkedHashMap {
	private static final int MAX_ENTRIES = 10;

	protected boolean removeEldestEntry(Map.Entry eldest) {
		return size() > MAX_ENTRIES;
	}
}
