# Item 16. public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라

 - 패키지 바깥에서 접근할 수 있는 클래스라면 접근자를 제공함으로써 클래스 내부 표현 방식을 언제든 바꿀 수 있는 유연성을 얻을 수 있다.
 - package-private, private 중첩 클래스라면 데이터 필드를 노출한다 해도 문제가 없다.
 - Dimension 클래스 의 성능문제?
 - Time.java - 과연 좋은가?
    ```java
       public final class Time {
	  	      private static final int HOURS_PER_DAY = 24;
            private static final int MINUTES_PER_HOUR = 60;
         
            public final int hour;
            public final int minute;
         
            public Time(int hour, int minute) {
            	valid(hour, minute);
         	
            	this.hour = hour;   
         	    this.minute = minute;
            }      
       }
    ```