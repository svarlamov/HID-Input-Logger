package com.sashavarlamov.hid.hidinputlogger;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Init {
	JFrame initF = null;

	public Init() {
		try {
			InputStream imgIS = getClass().getClassLoader().getResourceAsStream("splash.png");
			ImageIcon i = new ImageIcon(ImageIO.read(imgIS));
			this.initF = new JFrame();
			this.initF.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.initF.setSize(i.getIconWidth(), i.getIconHeight());
			Dimension frameSize = this.initF.getSize();

			this.initF.setResizable(false);

			this.initF.setUndecorated(true);

			this.initF.setLocation((screenSize.width - frameSize.width) / 2,
					(screenSize.height - frameSize.height) / 2);
			this.initF.setContentPane(new JLabel(i));

			this.initF.pack();
			this.initF.setVisible(true);
			Thread.currentThread();
			Thread.sleep(2000L);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame frame = null;
		JOptionPane.showMessageDialog(this.initF,
				"Please Plug in Your Device Before You Continue.");

		boolean b = ReadData.initAllComs();
		if (b) {
			frame = new MainFrame();
		} else {
			JOptionPane
					.showMessageDialog(this.initF,
							"No Device Recognized. Press \"Ok\" or Close the Dialog to Cease Execution.");

			System.exit(0);
		}
		this.initF.dispose();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Init();
	}
}
