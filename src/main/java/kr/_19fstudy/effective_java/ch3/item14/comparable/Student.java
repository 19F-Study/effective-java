package kr._19fstudy.effective_java.ch3.item14.comparable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student implements Comparable<Student> {

    private String name;

    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(Student o) {
        int result = Integer.compare(this.age, o.age);
        if (result == 0) {
            result = this.name.compareTo(o.name);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public static void main(String[] args) {
        Student damian = new Student("damian", 25);
        Student heize = new Student("heize", 15);
        Student woongs = new Student("woongs", 25);
        Student summer = new Student("summer", 19);
        Student teddy = new Student("teddy", 23);

        List<Student> students = new ArrayList<>();
        students.add(damian);
        students.add(heize);
        students.add(woongs);
        students.add(summer);
        students.add(teddy);

        System.out.println(students);
        Collections.sort(students);
        System.out.println(students);
    }

}
