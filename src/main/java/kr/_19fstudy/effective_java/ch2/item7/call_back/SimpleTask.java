package kr._19fstudy.effective_java.ch2.item7.call_back;

public class SimpleTask extends Task {
	@Override
	public void execute() {
		System.out.println("필요한 코드 처리 하고 후에 콜백 메소드 호출 됨");
	}
}
