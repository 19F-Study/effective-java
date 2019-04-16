 # item50. 적시에 방어적 복사본을 만들라
 
 - 클라이언트가 여러분의 불변식을 깨뜨릴 혈안이 되어 있다고 가정하고 방어적으로 프로그래밍을 해야 한다.
 - 어떤 객체든 그 객체의 허락 없이는 외부에서 내부를 수정하는 일은 불가능하다.
 
 ```java
    public final class Period {
    	private final Date start;
    	private final Date end;
    	
    	public Period(Date start, Date end) {
    		if (start.compareTo(end) > 0) {
    			throw new IllegalArgumentException();
    		}
    		this.start = start;
    		this.end = end;
    	}
    	
    	public Date start() {
    		return start;
    	}
    	
    	public Date end() {
    		return end;
    	}
    
    	public static void main(String[] args) {
    		Date start = new Date();
    		Date end = new Date();
    		Period p = new Period(start, end);
    		//객체 생성에 사용된 end 가 객체 밖에서 수정되었다.
    		end.setYear(78);
    	}
    } 
 ```
 - 생성자에서 받은 객체를 방어적 복사해야한다.
 ```java
    	public Period(Date start, Date end) {
    		this.start = new Date(start);
    		this.end = new Date(end);
    		
    		if (start.compareTo(end) > 0) {
    			throw new IllegalArgumentException();
    		}

    	}
    	
 ```
 - 멀티 스레딩 환경이라면 원본 객체의 유효성 검사한 후 복사본을 만드는 그 찰나의 취약한 순간에 다른 스레드가 원본 객체를 수정할 위험이 있기 때문에 유효성 검사를 하기전에 복사본을 만든다.
 - 방어적 복사에 clone 을 사용하지 않은 이유?? 공격 코드 작성 가능.
 - 매개변수가 제3자에 의해 확장될 수 있는 타입이라면 방어적 복사본을 만들 때 clone 을 사용해서는 안된다.
 ```java

    p.end().setYear(78);


	public Date startAfter() {
		return new Date(start.getTime());
	}
	
	public Date endAfter() {
		return new Date(end.getTime());
	}
 ```
 
 - 길이가 1 이상인 배열은 무조건 가변임을 잊지 말자. 내부에서 사용하는 배열을 클라이언트에게 반환할때는 항상 방어적 복사를 수행
 - 되도록 불변 객체들을 조합해 객체를 구성해야 방어적 복사를 할 일이 줄어든다.
 - 방어적 복사를 생략해도 되는 상황은 해당 클래스와 그 클라이언트가 상호 신뢰할 수 있을 때, 불변식이 깨지더라도 그 영향이 오직 호출한 클라이언트로 국한될때.