package kr._19fstudy.effective_java.ch4.item24.static_;

class Outer {

    private static final String name = "Damian Lillard";

    private final String name2 = "Damian Lillard";

    static class Static {

        void say() {
            System.out.println(Outer.name);
            System.out.println(name);

//            System.out.println(name2);
//            System.out.println(Outer.this.name2);
//            System.out.println(Outer.name2);
        }
    }

}
