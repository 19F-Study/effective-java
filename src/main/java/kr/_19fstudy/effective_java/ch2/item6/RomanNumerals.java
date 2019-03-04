package kr._19fstudy.effective_java.ch2.item6;

public class RomanNumerals {

    public static void main(String[] args) {
        String s = "hello world";
        System.out.println(isRomanNumeral(s));
    }

    private static boolean isRomanNumeral(String s) {
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})" + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    }
}
