package com.sashavarlamov.hid.hidinputlogger;

public class ShutDownT extends Thread {
	public void run() {
		WriteData.close();
	}
}
