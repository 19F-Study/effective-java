package kr._19fstudy.effective_java.ch2.item2.hierarchicalbuilder;

import static kr._19fstudy.effective_java.ch2.item2.hierarchicalbuilder.NyPizza.Size.*;
import static kr._19fstudy.effective_java.ch2.item2.hierarchicalbuilder.NyPizza.Size.SMALL;
import static kr._19fstudy.effective_java.ch2.item2.hierarchicalbuilder.Pizza.Topping.*;
import static kr._19fstudy.effective_java.ch2.item2.hierarchicalbuilder.Pizza.Topping.HAM;
import static kr._19fstudy.effective_java.ch2.item2.hierarchicalbuilder.Pizza.Topping.ONION;
import static kr._19fstudy.effective_java.ch2.item2.hierarchicalbuilder.Pizza.Topping.SAUSAGE;

// 계층적 빌더 사용 (21쪽)
public class PizzaTest {
    public static void main(String[] args) {
        NyPizza pizza = new NyPizza.Builder(SMALL)
                .addTopping(SAUSAGE).addTopping(ONION).build();
        Calzone calzone = new Calzone.Builder()
                .addTopping(HAM).sauceInside().build();
        
        System.out.println(pizza);
        System.out.println(calzone);
    }
}
