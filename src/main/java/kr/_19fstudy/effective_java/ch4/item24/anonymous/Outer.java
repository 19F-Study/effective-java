package kr._19fstudy.effective_java.ch4.item24.anonymous;

class Outer {

    private final String name = "Buddy Hield";

    void sayHello() {
        var inner = new AnonymousInner() {
            @Override
            public void doSayHello() {
                System.out.println("Hello " + name);
            }
        };
        inner.doSayHello();
    }

    interface AnonymousInner {

        void doSayHello();

    }

}

