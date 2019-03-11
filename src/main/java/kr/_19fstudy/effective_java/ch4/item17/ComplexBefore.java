package kr._19fstudy.effective_java.ch4.item17;

import java.util.Objects;

public class ComplexBefore {
    private final double re;
    private final double im;

    public ComplexBefore(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart() {
        return re;
    }

    public double imaginaryPart() {
        return im;
    }

    public ComplexBefore plus(ComplexBefore c) {
        return new ComplexBefore(re + c.re, im + c.im);
    }

    public ComplexBefore minus(ComplexBefore c) {
        return new ComplexBefore(re - c.re, im - c.im);
    }

    public ComplexBefore times(ComplexBefore c) {
        return new ComplexBefore(re * c.re - im * c.im, re * c.im + im * c.re);
    }

    public ComplexBefore dividedBy(ComplexBefore c) {
        double temp = c.re * c.re + c.im * c.im;
        return new ComplexBefore((re * c.re + im * c.im) / temp, (im * c.re - re * c.im) / temp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComplexBefore)) return false;
        ComplexBefore complexBefore = (ComplexBefore) o;
        return Double.compare(complexBefore.re, re) == 0 &&
                Double.compare(complexBefore.im, im) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(re, im);
    }

    @Override
    public String toString() {
        return "Complex{" +
                "re=" + re +
                ", im=" + im +
                '}';
    }
}
