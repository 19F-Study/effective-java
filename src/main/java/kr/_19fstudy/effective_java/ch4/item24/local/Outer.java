package kr._19fstudy.effective_java.ch4.item24.local;

class Outer {

    private final String name = "Luka Doncic";

    void sayBye() {
        class Local {

            private void doSayBye() {
                System.out.println("Bye " + name);
            }

        }

        var inner = new Local();
        inner.doSayBye();
    }

}
