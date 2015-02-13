package com.sashavarlamov.hid.hidinputlogger;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import org.lwjgl.input.Controller;

public class ValueUpdateT extends Thread {
	private ArrayList<DisplayTab> dispTabs = null;
	private ArrayList<Controller> controllers = new ArrayList();
	private ArrayList<GraphFrame> graphs = new ArrayList();
	private long delay;
	private int rows = 0;

	public ValueUpdateT(ArrayList<DisplayTab> dt, ArrayList<Controller> c,
			long d) {
		this.dispTabs = dt;
		this.delay = d;
		for (int i = 0; i < c.size(); i++) {
			if ((((Controller) c.get(i)).getAxisCount() != 0)
					&& (((Controller) c.get(i)).getButtonCount() != 0)) {
				this.controllers.add(c.get(i));
				this.graphs.add(new GraphFrame((Controller) c.get(i), ""));
			}
		}
	}

	public void run() {
		long graphInterval = 0L;
		if (this.delay < 200L) {
			graphInterval = this.delay / 200L;
		}
		int graphCount = 0;
		int count = 0;
		for (;;) {
			this.rows += 1;
			for (int i = 0; i < this.controllers.size(); i++) {
				System.out.println(this.controllers.size());
				try {
					String dTime = new Date().toString();

					((Controller) this.controllers.get(i)).poll();

					double[] axisVals = new double[((Controller) this.controllers
							.get(i)).getAxisCount()];
					for (int j = 0; j < ((Controller) this.controllers.get(i))
							.getAxisCount(); j++) {
						axisVals[j] = ((Controller) this.controllers.get(i))
								.getAxisValue(j);
					}
					boolean[] buttonVals = new boolean[((Controller) this.controllers
							.get(i)).getButtonCount()];
					for (int j = 0; j < ((Controller) this.controllers.get(i))
							.getButtonCount(); j++) {
						buttonVals[j] = ((Controller) this.controllers.get(i))
								.isButtonPressed(j);
					}
					if (!WriteData.locked.booleanValue()) {
						WriteData.updateData(i, axisVals, buttonVals, dTime);
					} else {
						while (WriteData.locked.booleanValue()) {
						}
						WriteData.updateData(i, axisVals, buttonVals, dTime);
					}
					if (graphCount == graphInterval) {
						((GraphFrame) this.graphs.get(i)).update(axisVals);
						graphCount = 0;
					}
					((DisplayTab) this.dispTabs.get(i)).updateVals(axisVals,
							buttonVals, dTime);

					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.currentThread();
				Thread.sleep(this.delay);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (this.rows >= 500) {
				for (int i = 0; i < this.dispTabs.size(); i++) {
					((DisplayTab) this.dispTabs.get(i)).clearData();
				}
				this.rows = 0;
			}
		}
	}
}
