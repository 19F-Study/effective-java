package kr._19fstudy.effective_java.ch8.item52;

import java.math.BigInteger;
import java.util.*;

public class CollectionClassifier {

//    public static String classify(Set<?> set) {
//        return "Set";
//    }
//
//    public static String classify(List<?> list) {
//        return "List";
//    }
//
//    public static String classify(Collection<?> collection) {
//        return "Unknown Collection";
//    }

    public static String classify(Collection<?> collection) {
        return collection instanceof Set ? "Set" :
                collection instanceof List ? "List" :
                        "Unknown Collection";
    }

    public static void main(String[] args) {
        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<BigInteger>(),
                new HashMap<String, String>().values()
        };

        for (Collection<?> c : collections) {
            // the choice of what overloading to invoke is made at compile time
            // compile-time type of c is Collection<?>
            // runtime type does not affect the choice
            System.out.println(classify(c));
        }

        // caution!
        System.out.println(classify(new HashSet<String>()));
    }

}
