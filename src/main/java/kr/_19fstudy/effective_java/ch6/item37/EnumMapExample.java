package kr._19fstudy.effective_java.ch6.item37;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnumMapExample {

    public static void main(String[] args) {
        Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle =
                new EnumMap<Plant.LifeCycle, Set<Plant>>(Plant.LifeCycle.class);

        for (Plant.LifeCycle lc : Plant.LifeCycle.values()) {
            plantsByLifeCycle.put(lc, new HashSet<>());
        }

        Plant[] garden = {
                new Plant("hi", Plant.LifeCycle.ANNUAL),
                new Plant("hello", Plant.LifeCycle.BIENNIAL),
                new Plant("aloha", Plant.LifeCycle.PERENNIAL)
        };

        for (Plant p : garden) {
            plantsByLifeCycle.get(p.lifeCycle).add(p);
        }
        System.out.println(plantsByLifeCycle);
    }

}
