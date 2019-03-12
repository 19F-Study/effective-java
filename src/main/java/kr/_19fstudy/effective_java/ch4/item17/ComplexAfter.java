package kr._19fstudy.effective_java.ch4.item17;

public class ComplexAfter {
    private final double re;
    private final double im;

    private ComplexAfter(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public static ComplexAfter valueOf(double re, double im) {
        return new ComplexAfter(re, im);
    }

    /**
     * ...
     */
}
