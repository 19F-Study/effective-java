package kr._19fstudy.effective_java.ch8.item52;


import java.util.List;

class Wine {

    String name() {
        return "wine";
    }

}

class SparklingWine extends Wine {

    @Override
    String name() {
        return "sparkling wine";
    }

}

class Champagne extends SparklingWine {

    @Override
    String name() {
        return "champagne";
    }

}


public class OverridingExample {

    public static void main(String[] args) {
        List<Wine> wines = List.of(new Wine(), new SparklingWine(), new Champagne());

        // even though w's compile-time type is Wine
        // compile time type does not affect when an overriden method is invoked
        // the "most specific" overriding method always gets executed.
        wines.forEach(w -> System.out.println(w.name()));
    }

}
