# Item 59, 라이브러리를 익히고 사용하라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

## n 이하의 무작위 정수 하나를 생성하는 함수를 만들어보자

- 0부터 n사이의 무작위 정수 하나를 생성하고 싶다고 해보자.

        // 간단한 구현
        static Random rnd = new Random();
        
        static int random(int n) {
            return Math.abs(rnd.nextInt()) & n;
        }
        
    - 문제
        - n이 그리 크지 않은 2의 제곱수라면 얼마 지나지 않아 같은 수열이 반복된다.
        - n이 2의 제곱수가 아니라면 몇몇 숫자가 평균적으로 더 자주 반환된다. n 값이 크면 이 현상은 더 두드러진다.

                // 검증
                public static void main(String[] args) {
                    int n = 2 * (Integer.MAX_VALUE / 3);
                    for low = 0;
                    for (int i = 0; i < 1000000; i++)
                        if (random(n) < n / 2)
                            low++;
                    System.out.println(low);
                }

            - random 메서드를 돌려보면 이상적일 경우 약 50만개가 출력돼야 하지만, 실제로 돌려보면 666,666에 가까운 값을 얻는다. 무작위로 생성된 수 중에서 2/3 가량이 중간값보다 낮은 쪽으로 쏠린 것이다.
        - 지정한 범위 '바깥'의 수가 종종 튀어나올 수 잇다. rnd.nextInt()가 반환한 값을 Math.abs를 이용해 음수가 아닌 정수로 매핑하기 때문이다. nextInt()가 Integer.MIN_VALUE를 반환하면 Math.abs도 Integer.MIN_VALUE를 반환하고, 나머지 연산자(%)는 음수를 반환해 버린다. 이렇게 되면 프로그램은 실패할 것이고, 문제를 해결하기도 쉽지 않다.

                // 검증
                public static void main(String[] args) {
                        System.out.println(Math.abs(Integer.MIN_VALUE + 1));
                        System.out.println(Math.abs(Integer.MIN_VALUE));
                        System.out.println(Integer.MIN_VALUE < 0 );
                        System.out.println(Integer.MIN_VALUE * -1);
                }

        - 이런 문제를 해결하기는 매우 어렵지만 다행히 직접 해결할 필요는 없다. Random.nextInt(int) 를 쓰면 된다. 표준 라이브러리를 사용하면 그 코드를 작성한 전문가의 지식과 우리보다 앞서 사용한 다른 프로그래머들의 경험을 활용할 수 있다.
        - 자바 7부터는 Random 보다 ThreadLocalRandom으로 대체하면 대부분 잘 동작한다.
            - Random보다 더 고품질의 무작위 수를 생성할 뿐 아니라 속도도 더 빠르다.
            - 포크-조인 풀이나 병렬 스트림에서는 SplittableRandom을 사용하라.

    ## 표준 라이브러리를 쓰는 장점

    1. 핵심적인 일과 크게 관련 없는 문제를 해결하느라 시간을 허비하지 않아도 된다는 것이다.
    2. 노력하지 않아도 성능이 지속해서 개선된다.
    3. 기능이 점점 많아진다.
    4. 우리가 작성한 코드가 많은 사람에게 낯익은 코드가 된다.

    ## 왜 많은 프로그래머들은 직접 구현해서 사용할까?

    - 모르기 때문이다.

    ## 자바 릴리즈 살펴보기

    - [https://docs.oracle.com/en/java/javase/${JAVA_VERSION}/](https://docs.oracle.com/en/java/javase/12/)
    - 위처럼 메이저 릴리즈마다 주목할 만한 수많은 기능이 라이브러리에 추가된다.
        - ex) curl과 비슷하게 지정한 URL의 내용을 가져오는 명령줄 어플리케이션

                public ststic void main(String[] args) throws IOException {
                    try (InputStream in = new URL(args[0]).openStream()) {
                        in.transferTo(System.out);
                    }
                }

    ## 자바 프로그래머라면

    - 자바 프로그래머라면 적어도 java.lang, java.util, java.io와 그 하위 패키지들에는 익숙해져야 한다.
    - 언급할만한 라이브러리는 몇 개 있다. 컬렉션 프레임워크와 스트림 라이브러리 그리고 java.util.concurrent의 동시성 기능이다.
    - 때때로 라이브러리가 ㅍ리요한 기능을 충분히 제공하지 못할 수 있다. 자바 표준 라이브러리에서 원하는 기능을 찾지 못하면, 그 다음 선택지는 고품질의 서드파티 라이브러리다. 구글의 멋진 구아바 라이브러리가 대표적이다.

    ## 바퀴를 다시 발명하지 말자.