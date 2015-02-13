package com.sashavarlamov.hid.hidinputlogger;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.lwjgl.input.Controller;

public class GraphFrame extends JFrame {
	private long points = 1L;
	private Controller controller;
	private JFreeChart chart;
	private XYSeriesCollection dataSet = new XYSeriesCollection();
	private ArrayList<XYSeries> series = new ArrayList();

	public GraphFrame(Controller c, String nameSuff) {
		setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(530, 320);
		Dimension frameSize = getSize();
		setResizable(false);

		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		this.controller = c;
		for (int i = 0; i < c.getAxisCount(); i++) {
			this.series.add(new XYSeries(c.getAxisName(i)));
		}
		for (int i = 0; i < this.series.size(); i++) {
			this.dataSet.addSeries((XYSeries) this.series.get(i));
		}
		this.chart = ChartFactory.createXYLineChart(c.getName() + nameSuff,
				"Sampling #", "Value", this.dataSet, PlotOrientation.VERTICAL,
				true, true, false);

		ChartPanel chartPanel = new ChartPanel(this.chart);
		JPanel content = new JPanel();
		content.add(chartPanel);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(content);
		setVisible(true);

		System.out.println(this.chart);
	}

	public void update(double[] axisVals) {
		int j = 0;
		for (int i = 0; i < axisVals.length; i++) {
			double q = this.dataSet.getSeries(j).getItemCount();
			int cnt = this.dataSet.getSeries(j).getItemCount();
			if (cnt == 30) {
				this.dataSet.getSeries(j).remove(0);
				this.dataSet.getSeries(j).add(q + this.points, axisVals[i]);
				this.points += 1L;
			} else {
				this.dataSet.getSeries(j).add(q + 1.0D, axisVals[i]);
			}
			j++;
		}
	}
}
