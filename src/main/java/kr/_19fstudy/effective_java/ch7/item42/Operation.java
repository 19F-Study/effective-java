package kr._19fstudy.effective_java.ch7.item42;

import java.util.function.DoubleBinaryOperator;

// 코드 42-4 함수 객체(람다)를 인스턴스 필드에 저장해 상수별 동작을 구현한 열거 타입 (256-257쪽)
/*
    # 상수 별 클래스 몸체 제거.
    - 열거타입 상수의 동작을 람다로 구현해 생성자에 넘기고, 생성자는 이 람다를 인스턴스 필드로 저장.
    - apply 메서드에서 필드에 저장된 람다를 호출

    # 상수별 클래스 몸체를 사용해야 하는 경우.
    1. 상수 별 동작을 단 몇 줄로 구현하기 어려운 경우
    2. 인스턴스 필드나 메서드를 사용해야 하는 경우
       -> 열거 타입 생성자 안의 람다는 열거타입의 인스턴스 멤버에 접근 불가 (인스턴스는 런타임에 만들어 지기 때문)
 */
public enum Operation {
    PLUS  ("+", (x, y) -> x + y),
    MINUS ("-", (x, y) -> x - y),
    TIMES ("*", (x, y) -> x * y),
    DIVIDE("/", (x, y) -> x / y);

    private final String symbol;
    private final DoubleBinaryOperator op;

    Operation(String symbol, DoubleBinaryOperator op) {
        this.symbol = symbol;
        this.op = op;
    }

    @Override public String toString() { return symbol; }

    public double apply(double x, double y) {
        return op.applyAsDouble(x, y);
    }

    // 아이템 34의 메인 메서드 (215쪽)
    public static void main(String[] args) {
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        for (Operation op : Operation.values())
            System.out.printf("%f %s %f = %f%n",
                    x, op, y, op.apply(x, y));
    }
}
