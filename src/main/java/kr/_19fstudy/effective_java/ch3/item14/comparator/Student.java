package kr._19fstudy.effective_java.ch3.item14.comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Student {

    private static final Comparator<Student> COMPARATOR =
            Comparator.comparingInt((Student student) -> student.age)
                    .thenComparing(student -> student.name);

    private String name;

    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
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
        students.sort(COMPARATOR);
        System.out.println(students);
    }

}
