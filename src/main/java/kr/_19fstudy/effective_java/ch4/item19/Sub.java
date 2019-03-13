package kr._19fstudy.effective_java.ch4.item19;

import java.time.Instant;

public final class Sub extends Super {

    private final Instant instant;

    Sub() {
        this.instant = Instant.now();
    }

    @Override
    public void overrideMe() {
        System.out.println(instant);
    }

    public static void main(String[] args) {
        var sub = new Sub();
        sub.overrideMe();
    }

}
