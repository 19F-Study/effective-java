package kr._19fstudy.effective_java.ch6.item37;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class EnumMapExample2 {

    public static void main(String[] args) {
        Plant[] garden = {
                new Plant("hi", Plant.LifeCycle.ANNUAL),
                new Plant("hello", Plant.LifeCycle.BIENNIAL),
                new Plant("aloha", Plant.LifeCycle.PERENNIAL)
        };

        Map<Plant.LifeCycle, List<Plant>> collected = Arrays.stream(garden).collect(
                groupingBy(p -> p.lifeCycle)
        );
        System.out.println(collected);

        EnumMap<Plant.LifeCycle, Set<Plant>> collected2 = Arrays.stream(garden).collect(
                groupingBy(p -> p.lifeCycle, () -> new EnumMap<>(Plant.LifeCycle.class), toSet())
        );
        System.out.println(collected2);
    }

}
