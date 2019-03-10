package kr._19fstudy.effective_java.ch3.item13.person1;

import java.time.LocalDate;

// ref: https://stackabuse.com/javas-object-methods-clone/
public class Main {
    public static void main(String[] args) {
        // for primitive values
        int x = 10;
        int y = x;
        y = 20;

        System.out.println("x = " + x);
        System.out.println("y = " + y);

        Person me = new Person("Adam", "McQuistan", LocalDate.parse("1987-09-23"));
        Person me2 = me;

        System.out.println(me);
        System.out.println(me2);

        System.out.println(me == me2);

    }
}
