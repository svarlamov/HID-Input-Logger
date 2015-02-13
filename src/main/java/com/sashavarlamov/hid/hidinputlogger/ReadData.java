package com.sashavarlamov.hid.hidinputlogger;

import java.util.ArrayList;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import org.lwjgl.input.Controllers;

public class ReadData {
	private static ArrayList<Control> controllers = new ArrayList();
	private static ArrayList<String> previ = new ArrayList();
	private static ArrayList<Integer> previNum = new ArrayList();

	public static boolean initAllComs() {
		boolean success = true;
		try { Controller[] c = ControllerEnvironment
					.getDefaultEnvironment().getControllers();
			System.out.println(c.length);

			Controllers.create();
			for (int i = 0; i < c.length; i++) {
				if ((c[i].getType() == Controller.Type.STICK)
						|| (c[i].getType() == Controller.Type.FINGERSTICK)
						|| (c[i].getType() == Controller.Type.GAMEPAD)
						|| (c[i].getType() == Controller.Type.HEADTRACKER)
						|| (c[i].getType() == Controller.Type.RUDDER)
						|| (c[i].getType() == Controller.Type.TRACKBALL)
						|| (c[i].getType() == Controller.Type.TRACKPAD)
						|| (c[i].getType() == Controller.Type.WHEEL)) {
					System.out.println(i);
					for (int j = 0; j < Controllers.getControllerCount(); j++) {
						if (previ.size() > 0) {
							for (int y = 0; y < previ.size(); y++) {
								if (previ.get(y) == Controllers
										.getController(j).getName()) {
									int num = ((Integer) previNum.get(y))
											.intValue();
									num++;
									previNum.set(y, Integer.valueOf(num));
									controllers
											.add(new Control(
													Controllers.getController(j
															+ ((Integer) previNum
																	.get(y))
																	.intValue())));
								}
							}
						} else {
							previ.add(Controllers.getController(j).getName());
							previNum.add(Integer.valueOf(0));
							controllers.add(new Control(Controllers
									.getController(j
											+ ((Integer) previNum.get(previNum
													.size() - 1)).intValue())));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	public static double readAxis(int controllerNum, int axisToRead) {
		double val = ((Control) controllers.get(controllerNum)).getController()
				.getAxisValue(axisToRead);
		return val;
	}

	public static double readAxis(int controllerNum, String axisToRead) {
		int num = ((Control) controllers.get(controllerNum))
				.getAxis(axisToRead).getAxisNumber();
		double val = ((Control) controllers.get(controllerNum)).getController()
				.getAxisValue(num);
		return val;
	}

	public static double[] readAllAxis(int controllerNum) {
		double[] val = new double[controllers.size()];
		for (int i = 0; i < ((Control) controllers.get(controllerNum))
				.getController().getAxisCount(); i++) {
			val[i] = ((Control) controllers.get(controllerNum)).getController()
					.getAxisValue(i);
		}
		return val;
	}

	public static boolean readButton(int controllerNum, int buttonToRead) {
		boolean val = ((Control) controllers.get(controllerNum))
				.getController().isButtonPressed(buttonToRead);
		return val;
	}

	public static boolean readButton(int controllerNum, String buttonToRead) {
		int b = ((Control) controllers.get(controllerNum)).getButton(
				buttonToRead).getButtonNumber();
		boolean val = ((Control) controllers.get(controllerNum))
				.getController().isButtonPressed(b);
		return val;
	}

	public static boolean[] readAllButtons(int controllerNum) {
		boolean[] val = new boolean[controllers.size()];
		for (int i = 0; i < ((Control) controllers.get(controllerNum))
				.getController().getButtonCount(); i++) {
			val[i] = ((Control) controllers.get(controllerNum)).getController()
					.isButtonPressed(i);
		}
		return val;
	}

	public static int getControllerCount() {
		return controllers.size();
	}

	public static String[] getControllerNames() {
		String[] s = new String[controllers.size()];
		for (int i = 0; i < controllers.size(); i++) {
			s[i] = ((Control) controllers.get(i)).getController().getName();
		}
		return s;
	}

	public static org.lwjgl.input.Controller getController(int controllerNumber) {
		org.lwjgl.input.Controller c = ((Control) controllers
				.get(controllerNumber)).getController();
		return c;
	}

	public static ArrayList<org.lwjgl.input.Controller> getControllers() {
		ArrayList<org.lwjgl.input.Controller> c = new ArrayList();
		for (int i = 0; i < controllers.size(); i++) {
			c.add(((Control) controllers.get(i)).getController());
		}
		return c;
	}

	public static Control getControl(int index) {
		return (Control) controllers.get(index);
	}

	public static Control[] getAllControls() {
		Control[] control = new Control[controllers.size()];
		for (int i = 0; i < controllers.size(); i++) {
			control[i] = ((Control) controllers.get(i));
		}
		return control;
	}
}
