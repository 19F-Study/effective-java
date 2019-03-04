# 아이템9. try-finally 보다는 try-with-resources 를 사용하라. 

### try-finally
  - 전통적으로 자원이 제대로 닫힘을 보장하는 수단.
  - finally 블락에서도 예외 발생 가능. 
  - finally 블락에서 예외 발생할 경우 try 에서 발생한 예외는 확인할 수 없다.
  
### try-with-resources
  - java7 부터 지원
  - try() 안에 AutoCloseable 을 구현한 클래스를 넣을 수 있다.
  - try/catch 문이 끝날때 close() 메서드를 호출해준다.
  - finally 문 안에 지저분한 코드를 쓰지 않아도 된다. null check, resource close ...
  - try 블락에서 예외 발생 후에 close() 에서 예외 발생 시 모든 예외를 회면에 노출.
  