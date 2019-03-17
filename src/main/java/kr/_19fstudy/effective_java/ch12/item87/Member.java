package kr._19fstudy.effective_java.ch12.item87;

import java.io.Serializable;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String team;
    private int age;
    //private String email;

    public Member(String name, String team, int age) {
        this.name = name;
        this.team = team;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", age=" + age +
                '}';
    }
}
