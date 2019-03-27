package kr._19fstudy.effective_java.ch5.item33;
import java.util.*;

// 타입 안전 이종 컨테이너 패턴 (199-202쪽)
public class Favorites {
    // 코드 33-3 타입 안전 이종 컨테이너 패턴 - 구현 (200쪽)
    private Map<Class<?>, Object> favorites = new HashMap<>();

    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(Objects.requireNonNull(type), instance);
    }

    // 코드 33-4 동적 형변환으로 런타임 타입 안전성 확보 (202쪽) -> 타입 불변식을 어기지 않도록 보장.
//    public <T> void putFavorite(Class<T> type, T instance) {
//        favorites.put(Objects.requireNonNull(type), type.cast(instance));
//    }

    public <T> T getFavorite(Class<T> type) {
        // Class 객체가 가리키는 타입으로 동적 형변환하여 키와 값의 타입이 동일함을 보장한다.
        // cast는 형변환 연산자의 동적 버전.
        return type.cast(favorites.get(type));
    }

    // 코드 33-2 타입 안전 이종 컨테이너 패턴 - 클라이언트 (199쪽)
    public static void main(String[] args) {
        Favorites f = new Favorites();
        
        f.putFavorite(String.class, "Java");
        f.putFavorite(Integer.class, 0xcafebabe);
        f.putFavorite(Class.class, Favorites.class);

        String favoriteString = f.getFavorite(String.class);
        int favoriteInteger = f.getFavorite(Integer.class);
        Class<?> favoriteClass = f.getFavorite(Class.class);
        
        System.out.printf("%s %x %s%n", favoriteString,
                favoriteInteger, favoriteClass.getName());

        // raw type을 넘기면 Favorites 인스턴스의 타입 안정성이 쉽게 깨진다.
//        f.putFavorite((Class)Integer.class, "Integer의 인스턴스가 아닙니다.");
//        System.out.println(f.getFavorite(Integer.class));

        // 일반 컬렉션 구현체에도 raw type을 넘기면 똑같은 문제가 생긴다.
//        Set<Integer> set = new HashSet<>();
//        ((Set)set).add("문자열 입니다.");

        // 런타임 타입 안정성 확보하기 위한 컬렉션 래퍼들
//        Set<Integer> set = new HashSet<>();
//        set = Collections.checkedSet(set, Integer.class);
//        ((Set)set).add("문자열 입니다.");


        // List<String>.class 는 컴파일되지 않음.
//        f.putFavorite(List<String>.class, List.of("hello", "java"));
//        String[] strArr = {"effective", "java"};
//        f.putFavorite(String[].class, strArr);
//
//        f.putFavorite(List.class, List.of("hello", "java"));
//
//        String[] stringArr = f.getFavorite(String[].class);
//        System.out.println(Arrays.asList(stringArr));
//
//        List<String> stringList = f.getFavorite(List.class);
//        System.out.println(stringList);

    }

}
