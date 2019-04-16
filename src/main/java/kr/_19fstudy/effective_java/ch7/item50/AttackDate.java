package kr._19fstudy.effective_java.ch7.item50;

import java.util.Date;

public class AttackDate extends Date {

	private static Date[] dates;

	@Override
	public AttackDate clone() {
		return new AttackDate();
	}
}
