package com.sashavarlamov.hid.hidinputlogger;

public class Rumbler {
	private int rumblerNumber;
	private String rumblerName;

	public Rumbler(int num, String name) {
		this.rumblerName = name;
		this.rumblerNumber = num;
	}

	public String getRumblerName() {
		return this.rumblerName;
	}

	public int getRumblerNumber() {
		return this.rumblerNumber;
	}
}
