package com.sashavarlamov.hid.hidinputlogger;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

public class MainFrame extends JFrame {
	private JTabbedPane controllerDisp = new JTabbedPane();
	private ArrayList<DisplayTab> dispTabs = new ArrayList();
	//private String propsFile = "props.properties";
	private URL propsFile = getClass().getClassLoader().getResource("props.properties");
	private FileInputStream propIStream = null;
	private Control[] controls;
	private ValueUpdateT vuT;
	private long interval = 1000L;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItem;

	public MainFrame() {
		try {
			Properties rwProps = new Properties();
			System.out.println(this.propsFile.toString().replaceFirst("//", ""));
			//this.propIStream = new FileInputStream(this.propsFile.toString().replaceFirst("//", ""));
			this.propIStream = new FileInputStream("C:\\Users\\ckpcAdmin\\workspace\\hidinputlogger\\target\\classes\\props.properties");

			rwProps.load(this.propIStream);
			this.interval = new Integer(rwProps.getProperty("timeIntervalMs"))
					.intValue();

			this.controls = ReadData.getAllControls();
			WriteData.initWriteData(this.controls);

			this.controls = ReadData.getAllControls();
			Runtime.getRuntime().addShutdownHook(new ShutDownT());
			this.propIStream.close();
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
		setTitle("HID Input Logger");
		setSize(new Dimension(400, 300));
		for (int i = 0; i < ReadData.getControllerCount(); i++) {
			if ((ReadData.getController(i).getAxisCount() != 0)
					&& (ReadData.getController(i).getButtonCount() != 0)) {
				this.dispTabs.add(new DisplayTab(ReadData.getControl(i)));
				this.controllerDisp
						.addTab(ReadData.getControllerNames()[i],
								(Component) this.dispTabs.get(this.dispTabs
										.size() - 1));
			}
		}
		this.menuBar = new JMenuBar();

		this.menu = new JMenu("File");
		this.menu.setMnemonic(70);
		this.menu.getAccessibleContext().setAccessibleDescription("File");

		this.menuBar.add(this.menu);

		this.menuItem = new JMenuItem("Modify Log Interval", 84);

		this.menuItem.setAccelerator(KeyStroke.getKeyStroke(49, 8));

		this.menuItem.getAccessibleContext().setAccessibleDescription(
				"Modify Log Interval");

		this.menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PropWriter(MainFrame.this.propsFile.toString());
			}
		});
		this.menu.add(this.menuItem);

		this.menuBar.add(this.menu);
		setJMenuBar(this.menuBar);

		this.vuT = new ValueUpdateT(this.dispTabs, ReadData.getControllers(),
				this.interval);
		this.vuT.start();

		JScrollPane jsp = new JScrollPane(this.controllerDisp);
		jsp.setAutoscrolls(true);
		add(jsp, null);
	}
}
