package kr._19fstudy.effective_java.ch6.item40.interface_example;

import java.math.BigDecimal;

public class CreditCard implements Card {
	@Override
	public boolean pay(Number b) {
		return true;
	}
}
