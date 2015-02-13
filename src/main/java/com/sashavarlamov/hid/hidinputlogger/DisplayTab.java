package com.sashavarlamov.hid.hidinputlogger;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.lwjgl.input.Controller;

public class DisplayTab extends JTable {
	private Control control;
	private Controller controller;
	private DefaultTableModel model;

	public DisplayTab(Control c) {
		this.control = c;
		this.controller = this.control.getController();
		setup();
	}

	private void setup() {
		this.model = new DefaultTableModel();

		setModel(this.model);
		this.model.addColumn("Time", new Object[] { "Time" });
		if (this.controller.getAxisCount() > 0) {
			for (int i = 0; i < this.controller.getAxisCount(); i++) {
				this.model.addColumn(this.controller.getAxisName(i),
						new Object[] { this.controller.getAxisName(i) });
			}
		}
		if (this.controller.getButtonCount() > 0) {
			for (int i = 0; i < this.controller.getButtonCount(); i++) {
				this.model.addColumn(this.controller.getButtonName(i),
						new Object[] { this.controller.getButtonName(i) });
			}
		}
	}

	public void updateVals(double[] axisVals, boolean[] buttonVals, String dTime) {
		Vector data = new Vector(this.model.getColumnCount());
		data.add(dTime);
		for (int i = 0; i < axisVals.length; i++) {
			data.add(Double.valueOf(axisVals[i]));
		}
		String[] bDat = new BoolConverter().contvertToString(buttonVals);
		for (int i = 0; i < bDat.length; i++) {
			data.add(bDat[i]);
		}
		this.model.addRow(data);
	}

	public void clearData() {
		this.model.setRowCount(0);
		setup();
	}
}
