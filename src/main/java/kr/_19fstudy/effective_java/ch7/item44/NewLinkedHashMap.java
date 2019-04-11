package kr._19fstudy.effective_java.ch7.item44;

import java.util.LinkedHashMap;
import java.util.Map;

public class NewLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final int LIMIT = 10;
    private EldestEntryRemovalFunction<K, V> eldestEntryRemovalFunction;

    @FunctionalInterface
    interface EldestEntryRemovalFunction<K, V> {
        boolean remove(Map<K, V> map, Map.Entry<K, V> eldest);
    }

    public NewLinkedHashMap(EldestEntryRemovalFunction eldestEntryRemovalFunction) {
        this.eldestEntryRemovalFunction = eldestEntryRemovalFunction;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.eldestEntryRemovalFunction.remove(this, eldest);
    }

    public static void main(String[] args) {
        EldestEntryRemovalFunction eldestEntryRemovalFunction = (map, eldest) -> map.size() > LIMIT;
        NewLinkedHashMap<Integer, String> map = new NewLinkedHashMap<>(eldestEntryRemovalFunction);

        for (int i = 0; i < 50; i++) {
            map.put(i, String.valueOf(i));
        }
        System.out.println(map);
    }
}