package kr._19fstudy.effective_java.ch6.item37;

import java.util.HashSet;
import java.util.Set;

public class OrdinalArrayIndex {

    public static void main(String[] args) {
        Set<Plant>[] plantsByLifeCycle =
                (Set<Plant>[]) new Set[Plant.LifeCycle.values().length];
        for (int i = 0; i < plantsByLifeCycle.length; i++) {
            plantsByLifeCycle[i] = new HashSet<>();
        }

        Plant[] garden = {
                new Plant("hi", Plant.LifeCycle.ANNUAL),
                new Plant("hello", Plant.LifeCycle.BIENNIAL),
                new Plant("aloha", Plant.LifeCycle.PERENNIAL)
        };

        for (Plant p : garden) {
            plantsByLifeCycle[p.lifeCycle.ordinal()].add(p);
        }

        for (int i = 0; i < plantsByLifeCycle.length; i++) {
            System.out.printf("%s: %s\n",
                    Plant.LifeCycle.values()[i], plantsByLifeCycle[i]);
        }

    }

}
