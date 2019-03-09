package kr._19fstudy.effective_java.ch3.item13.person2_2;

import java.time.LocalDate;

// ref: https://stackabuse.com/javas-object-methods-clone/
public class Main {
    public static void main(String[] args) {
        Person me = new Person("Adam", "McQuistan", LocalDate.parse("1987-09-23"));
        Person me2;

        me2 = me.clone();

        me2.setFirstName("Joe");
        System.out.println("me = " + me);
        System.out.println("me2 = " + me2);

        System.out.println(me == me2);
    }
}
