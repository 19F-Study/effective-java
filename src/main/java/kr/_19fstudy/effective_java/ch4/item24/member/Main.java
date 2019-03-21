package kr._19fstudy.effective_java.ch4.item24.member;

public class Main {

    public static void main(String[] args) {
        var outer = new Outer();
        outer.sayHi();

        // can make instance like this
        var inner = outer.new MemberInner();
        inner.doSayHi();
    }

}
