package kr._19fstudy.effective_java.ch4.item24.member;

class Outer {

    private final String name = "Jayson Tatum";

    private static final String type = "NBA";

    void sayHi() {
        var inner = new MemberInner();
        inner.doSayHi();
    }


    class MemberInner {
        private String name = "Donovan Mitchell";

        void doSayHi() {
            System.out.println(Outer.type);
            System.out.println("Hi " + Outer.this.name);
            System.out.println("Hi " + this.name);
            System.out.println("Hi " + name);
        }
    }

}
