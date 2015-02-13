package com.sashavarlamov.hid.hidinputlogger;

public class Axis {
	private int axisNumber;
	private String axisName;

	public Axis(int num, String name) {
		this.axisName = name;
		this.axisNumber = num;
	}

	public String getAxisName() {
		return this.axisName;
	}

	public int getAxisNumber() {
		return this.axisNumber;
	}
}