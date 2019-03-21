package kr._19fstudy.effective_java.ch4.item15.PublicMemberExample;

import java.io.Serializable;

public class Employee implements Serializable {
	private String name;

	private String workCode;

	private boolean isWorking;

	public Employee(String name, String workCode, boolean isWorking) {
		this.name = name;
		this.workCode = workCode;
		this.isWorking = isWorking;
	}

	public String getName() {
		return this.name;
	}

	public String getWorkCode() {
		return this.workCode;
	}

	public boolean isWorking() {
		return this.isWorking;
	}

	private String please() {
		System.out.println("PLEASE");
		return "PLEASE";
	}

	@Override
	public String toString() {
		return "Employee name : " + getName() + " workCode : " + getWorkCode();
	}
}
