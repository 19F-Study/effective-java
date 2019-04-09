package kr._19fstudy.effective_java.ch7.item44;

import java.util.LinkedHashMap;
import java.util.Map;

public class NewLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final int LIMIT = 10;

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > LIMIT;
    }

    @FunctionalInterface
    interface EldestEntryRemovalFunction<K, V> {
        boolean remove(Map<K, V> map, Map.Entry<K, V> eldest);
    }



    public static void main(String[] args) {
        NewLinkedHashMap<Integer, String> map = new NewLinkedHashMap<>();


        for (int i = 0; i < 50; i++) {
            map.put(i, String.valueOf(i));
        }
        System.out.println(map);
    }
}