package kr._19fstudy.effective_java.ch6.item34;

import static kr._19fstudy.effective_java.ch6.item34.Inverse.inverse;

public class Main {

    public static void main(String[] args) {
        //Operation
        double x = Double.parseDouble("2");
        double y = Double.parseDouble("4");

        for (Operation op : Operation.values()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }

        //PayrollDay
        for (PayrollDay day : PayrollDay.values())
            System.out.printf("%-10s%d%n", day, day.pay(8 * 60, 1));


        //Inverse
        for (Operation op : Operation.values()) {
            Operation invOp = inverse(op);
            System.out.printf("%f %s %f %s %f = %f%n",
                    x, op, y, invOp, y, invOp.apply(op.apply(x, y), y));
        }
    }
}
