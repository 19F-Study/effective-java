package kr._19fstudy.effective_java.ch9.item58;

import java.util.ArrayList;
import java.util.List;

public class DestructiveFiltering {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.removeIf(i -> i.equals(2));
        System.out.println(list);
    }
}
