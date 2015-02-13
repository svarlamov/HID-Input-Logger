package com.sashavarlamov.hid.hidinputlogger;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PropWriter extends JFrame {
	private JLabel label = new JLabel("Log Interval in Milliseconds");
	private JTextField valField = new JTextField();
	private JLabel note = new JLabel("No Decimals Permitted");
	private String propsFile;
	private FileOutputStream propOStream = null;
	private JButton writeB = new JButton("Write and Restart");
	private File dir = new File(".");

	public PropWriter(String file) {
		this.propsFile = file;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		getContentPane().setLayout(null);
		setSize(new Dimension(282, 91));
		this.label.setBounds(new Rectangle(5, 5, 130, 20));
		this.valField.setBounds(new Rectangle(140, 5, 120, 20));
		this.valField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PropWriter.this.valField_actionPerformed(e);
			}
		});
		this.note.setBounds(new Rectangle(5, 30, 120, 20));
		this.writeB.setBounds(new Rectangle(140, 30, 120, 20));
		this.writeB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PropWriter.this.writeB_actionPerformed(e);
			}
		});
		getContentPane().add(this.writeB, null);
		getContentPane().add(this.note, null);
		getContentPane().add(this.valField, null);
		getContentPane().add(this.label, null);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
		setTitle("HID Logger 2012");
		setVisible(true);
	}

	private void writeIt(String val) {
		if (val == "") {
			dispProb(1);
		}
		int i = new Integer(val).intValue();
		if (i < 1) {
			dispProb(0);
		}
		try {
			this.propOStream = new FileOutputStream(this.propsFile);
			Properties props = new Properties();
			props.setProperty("timeIntervalMs", val);
			props.store(this.propOStream, null);
			String s = this.dir.getCanonicalPath() + "\\start.bat";
			Runtime.getRuntime().exec(s);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			dispProb(0);
		}
	}

	private void dispProb(int i) {
		if (i == 0) {
			JOptionPane.showMessageDialog(this,
					"An error occoured. Your request could not be completed.");
		} else if (i == 1) {
			JOptionPane.showMessageDialog(this,
					"You must enter a value before proceeding.");
		} else {
			JOptionPane.showMessageDialog(this,
					"There was a syntax error in the value given.");
		}
		this.valField.setText("");
	}

	private void writeB_actionPerformed(ActionEvent e) {
		writeIt(this.valField.getText());
	}

	private void valField_actionPerformed(ActionEvent e) {
		writeIt(this.valField.getText());
	}
}
