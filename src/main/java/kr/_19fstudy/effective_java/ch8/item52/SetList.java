package kr._19fstudy.effective_java.ch8.item52;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SetList {

    public static void main(String[] args) {
        Set<Integer> set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i);
        }
        System.out.println(set + " " + list);
        for (int i = 0; i < 3; i++) {
            // selects the overloading remove(E)
            set.remove(i);
            // selects remove(int i), which removes the element at the specified position
//            list.remove(i);
            list.remove(Integer.valueOf(i));
        }
        System.out.println(set + " " + list);
    }

}
