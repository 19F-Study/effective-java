package kr._19fstudy.effective_java.ch7.item50;

import java.util.Date;

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

		//내부가 변경된 두번째 예
		p.end().setYear(78);
	}
}
