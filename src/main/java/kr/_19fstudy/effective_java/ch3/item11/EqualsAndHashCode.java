package kr._19fstudy.effective_java.ch3.item11;

import java.util.HashMap;
import java.util.Map;

public class EqualsAndHashCode {

    public static void main(String[] args) {

        //Equals
        PhoneNumber p1 = new PhoneNumber(707, 867, 5309);
        PhoneNumber p2 = new PhoneNumber(707, 867, 5309);

        System.out.println(p1.equals(p2));

        // HashCode
        Map<PhoneNumber, String> m = new HashMap<>();
        m.put(new PhoneNumber(707, 867, 5309), "Summer");

        System.out.println(m.get(new PhoneNumber(707, 867, 5309)));
    }
}
