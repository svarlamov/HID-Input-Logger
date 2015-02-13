package com.sashavarlamov.hid.hidinputlogger;

public class Button {
	private int buttonNumber;
	private String buttonName;

	public Button(int num, String name) {
		this.buttonName = name;
		this.buttonNumber = num;
	}

	public String getButtonName() {
		return this.buttonName;
	}

	public int getButtonNumber() {
		return this.buttonNumber;
	}
}
