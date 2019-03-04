# 아이템8. finalizer 와 cleaner 사용을 피하라!

### finalizer
> Called by the garbage collector on an object when garbage collection
determines that there are no more references to the object.
A subclass overrides the {@code finalize} method to dispose of
system resources or to perform other cleanup.

### cleaner
> Cleaner manages a set of object references and corresponding cleaning actions.

### finalizer 와 cleaner 를 사용하면 안되는 이유
  1. 즉시 수행된다는 보장이 없다.
    - "파일 닫기" 를 수행하는 finalizer 와 cleaner 보다 "파일 열기" 가 게속 수행된다면..
  2. 수행 시점뿐 아니라 수행 여부조차 보장하지 않는다.
    - 상태를 영구적으로 수정하는 작업에는 절대 사용하면 안된다. ex) 데이터베이스 같은 공유 자원의 영구 락 해제
  3. finalizer 수행 중 예외가 발생하면 처리할 작업이 남았더라고 그 순간 종료된다.
  4. 심각한 성능 문제 동반.
  5. finalizer 를 사용한 클래스는 finalizer 공격에 노출되어 심각한 보안 문제를 일으킬 수도 있다. ????
  
### 어디에 사용?
  - 자바 피어를 회수할 때 네이티브 객체까지 회수하지 못하기 때문에 cleaner 나 finalizer 가 네이티브 객체를 회수.
  - 자원의 소유자가 close 메서드를 호출하지 않는 것에 대비한 안전망 역할. 아예 안하는 것 보다는 늦게라도 하는게 낫다. (fd.close() 호출을 잊은 경우..)
  ```java
    //java 1.8.0 FileInputStream.java
    protected void finalize() throws IOException {
        if ((fd != null) &&  (fd != FileDescriptor.in)) {
            /* if fd is shared, the references in FileDescriptor
             * will ensure that finalizer is only called when
             * safe to do so. All references using the fd have
             * become unreachable. We can call close()
             */
            close();
        }
    }
  ```
  ```java
    //jek-11.0.2 FileInputStream.java
    /**
     * Ensures that the {@link #close} method of this file input stream is
     * called when there are no more references to it.
     * The {@link #finalize} method does not call {@link #close} directly.
     *
     * @apiNote
     * To release resources used by this stream {@link #close} should be called
     * directly or by try-with-resources.
     *
     * @implSpec
     * If this FileInputStream has been subclassed and the {@link #close}
     * method has been overridden, the {@link #close} method will be
     * called when the FileInputStream is unreachable.
     * Otherwise, it is implementation specific how the resource cleanup described in
     * {@link #close} is performed.
     *
     * @deprecated The {@code finalize} method has been deprecated and will be removed.
     *     Subclasses that override {@code finalize} in order to perform cleanup
     *     should be modified to use alternative cleanup mechanisms and
     *     to remove the overriding {@code finalize} method.
     *     When overriding the {@code finalize} method, its implementation must explicitly
     *     ensure that {@code super.finalize()} is invoked as described in {@link Object#finalize}.
     *     See the specification for {@link Object#finalize()} for further
     *     information about migration options.
     *
     * @exception  IOException  if an I/O error occurs.
     * @see        java.io.FileInputStream#close()
     */
    @Deprecated(since="9", forRemoval = true)
    protected void finalize() throws IOException {
    }
  ```
  
### 대안은?
  - AutoCloseable 를 구현해주고 close 메서드를 호출.