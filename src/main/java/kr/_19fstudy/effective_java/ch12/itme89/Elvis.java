package kr._19fstudy.effective_java.ch12.itme89;

import java.io.Serializable;
import java.util.Arrays;

public class Elvis implements Serializable {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() { }
    public static Elvis getInstance() { return INSTANCE; }

    // transient가 아닌 참조 필드를 가지면 문제가 된다.
    private String[] favoriteSongs =
        { "Hound Dog", "Heartbreak Hotel" };
    public void printFavorites() {
        System.out.println(Arrays.toString(favoriteSongs));
    }

    // 역직렬화한 객체는 무시하고 클래스 초기화 때 만들어진 Elvis 인스턴스를 반환한다.
    private Object readResolve() {
        return INSTANCE;
    }
}
