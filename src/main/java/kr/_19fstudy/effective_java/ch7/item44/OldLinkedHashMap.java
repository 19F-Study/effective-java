package kr._19fstudy.effective_java.ch7.item44;

import java.util.LinkedHashMap;
import java.util.Map;

public class OldLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final int LIMIT = 10;

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > LIMIT;
    }

    public static void main(String[] args) {
        OldLinkedHashMap<Integer, String> map = new OldLinkedHashMap<>();


        for (int i = 0; i < 50; i++) {
            map.put(i, String.valueOf(i));
        }
        System.out.println(map);
    }
}