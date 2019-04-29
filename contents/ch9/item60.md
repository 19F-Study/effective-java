# Item 60, 정확한 답이 필요하다면 float와 double은 피하라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

## float & double

- float와 double 타입은 과학과 공학 계산용으로 설계되었다. 이진 부동소수점 연산에 쓰이며, 넓은 범위의 수를 빠르게 정밀한 '근사치'로 계산하도록 세심하게 설계되었다. 따라서 정확한 결과가 필요할 때는 사용하면 안 된다. float와 double 타입은 특히 금융 관련 계산과는 맞지 않는다. 0.1 혹은 10의 음의 거듭 제곱수를 표현할 수 없기 때문이다.

        System.out.println(1.03 - 0.42);
        // prints 0.6100000000000001
        
        System.out.println(1.00 - 9 * 0.10);
        // prints 0.09999999999999998

- 금융 계산에 부동소수 타입을 사용한 '어설픈' 코드
```
public static void main(String[] args) {
    double funds = 1.00;
    int itemsBought = 0;
    for (double price = 0.10; funds >= price; price += 0.10) {
        funds -= price;
        itemsBought++;
        System.out.println("가격: " + price + "에 구입");
    }
    System.out.println(itemsBought + "개 구입");
    System.out.println("잔돈(달라): " + funds);
}

// prints
가격: 0.1에 구입
가격: 0.2에 구입
가격: 0.30000000000000004에 구입
3개 구입
잔돈(달라): 0.3999999999999999
```

## BigDecimal & int, long

- 잘못된 결과다.  이 문제를 올바로 해결하려면 BigDecimal, int 혹은 long을 사용해야 한다.
- 해법
```
// Using BigDecimal
public static void main(String[] args) {
    final BigDecimal TEN_CENTS = new BigDecimal("0.10");

    int itemBought = 0;
    BigDecimal funds = new BigDecimal("1.00");
    for (BigDecimal price = TEN_CENTS; funds.compareTo(price) >= 0; price = price.add(TEN_CENTS)) {
        funds = funds.subtract(price);
        itemBought++;
    }
    System.out.println(itemBought + "개 구입");
    System.out.println("잔돈(달라): " + funds);
}

// Using int or long
// Using cent instead of dollar 
public static void main(String[] args) {
    int itemBought = 0;
    int funds = 100;
    for (int price = 10; funds >= price; price += 10) {
        funds -= price;
        itemBought++;
    }
    System.out.println(itemBought + "개 구입");
    System.out.println("잔돈(달라): " + funds);
}

// prints
4개 구입
잔돈(달라): 0.00
```
- BigDecimal을 이용했을 때의 단점
    - 기본 타입보다 쓰기가 훨씬 불편하고 느리다.
- int, long 타입을 썼을 때의 단점
    - 다룰 수 있는 값의 크기가 제한되고, 소수점을 직접 관리해야 한다.

## 결론

- 정확한 답이 필요한 계산에는 float나 double을 피하라. 소수점 추적은 시스템에 맡기고, 코딩 시의 불편함이나 성능 저하를 신경쓰지 않겠다면 BigDecimal을 사용하라. 반면 성능이 중요하고 소수점을 직접 추적할 수 있고 숫자가 너무 크지 않다면 int나 long을 사용하라. 숫자를 아홉 자리 십진수로 표현할 수 있다면 int를 사용하고, 열여덟 자리 십진수로 표현할 수 있다면 long을 사용하라. 열여덟 자리를 넘어가면 BigDecimal을 사용해야 한다.