package com.sashavarlamov.hid.hidinputlogger;

import java.util.ArrayList;
import org.lwjgl.input.Controller;

public class Control {
	private ArrayList<Axis> axis = new ArrayList();
	private ArrayList<Button> buttons = new ArrayList();
	private ArrayList<Rumbler> rumblers = new ArrayList();
	private Controller controller;

	public Control(Controller c) {
		this.controller = c;
		for (int i = 0; i < this.controller.getAxisCount(); i++) {
			this.axis.add(new Axis(i, this.controller.getAxisName(i)));
		}
		for (int i = 0; i < this.controller.getRumblerCount(); i++) {
			this.rumblers
					.add(new Rumbler(i, this.controller.getRumblerName(i)));
		}
		for (int i = 0; i < this.controller.getButtonCount(); i++) {
			this.buttons.add(new Button(i, this.controller.getButtonName(i)));
		}
	}

	public Controller getController() {
		return this.controller;
	}

	public Axis getAxis(int axisNum) {
		Axis a = null;
		for (int i = 0; i < this.axis.size(); i++) {
			Axis a1 = (Axis) this.axis.get(i);
			if (a1.getAxisNumber() == axisNum) {
				a = a1;
				break;
			}
		}
		return a;
	}

	public Button getButton(int buttonNum) {
		Button b = null;
		for (int i = 0; i < this.buttons.size(); i++) {
			Button b1 = (Button) this.buttons.get(i);
			if (b1.getButtonNumber() == buttonNum) {
				b = b1;
				break;
			}
		}
		return b;
	}

	public Rumbler getRumbler(int rumblerNum) {
		Rumbler r = null;
		for (int i = 0; i < this.rumblers.size(); i++) {
			Rumbler r1 = (Rumbler) this.rumblers.get(i);
			if (r1.getRumblerNumber() == rumblerNum) {
				r = r1;
				break;
			}
		}
		return r;
	}

	public Axis getAxis(String axisName) {
		Axis a = null;
		for (int i = 0; i < this.axis.size(); i++) {
			Axis a1 = (Axis) this.axis.get(i);
			if (a1.getAxisName().equals(axisName)) {
				a = a1;
				break;
			}
		}
		return a;
	}

	public Button getButton(String buttonName) {
		Button b = null;
		for (int i = 0; i < this.buttons.size(); i++) {
			Button b1 = (Button) this.buttons.get(i);
			if (b1.getButtonName().equals(buttonName)) {
				b = b1;
				break;
			}
		}
		return b;
	}

	public Rumbler getRumbler(String rumblerName) {
		Rumbler r = null;
		for (int i = 0; i < this.rumblers.size(); i++) {
			Rumbler r1 = (Rumbler) this.rumblers.get(i);
			if (r1.getRumblerName().equals(rumblerName)) {
				r = r1;
				break;
			}
		}
		return r;
	}
}
