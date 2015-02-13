package com.sashavarlamov.hid.hidinputlogger;

public class BoolConverter {
	public String[] contvertToString(boolean[] bToConv) {
		String[] converted = new String[bToConv.length];
		for (int i = 0; i < bToConv.length; i++) {
			if (bToConv[i]) {
				converted[i] = "Pressed";
			} else {
				converted[i] = "Not Pressed";
			}
		}
		return converted;
	}
}
