package com.sashavarlamov.hid.hidinputlogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.lwjgl.input.Controller;

public class WriteData {
	private static String fileName;
	private static boolean wbOpen = false;
	private static ArrayList<WritableSheet> sheets = new ArrayList<WritableSheet>();
	private static WritableFont hFont = new WritableFont(WritableFont.TIMES,
			12, WritableFont.BOLD);
	private static WritableCellFormat floatFormat = new WritableCellFormat(
			NumberFormats.FLOAT);
	private static WritableCellFormat header = new WritableCellFormat(hFont);
	private static WritableFont dFont = new WritableFont(WritableFont.TIMES,
			12, WritableFont.NO_BOLD);
	private static WritableCellFormat data = new WritableCellFormat(dFont);
	private static Number number;
	private static int[] currentRows;
	private static Label label;
	private static WritableWorkbook workbook;
	private static OutputStream out;
	private static File destFile;
	private static Control[] controllers;
	protected static volatile Boolean locked = Boolean.valueOf(false);
	private static boolean hasOpened = false;
	private static int maxRows = 100000;
	private static String dateFormat = "dd-mm-yy hh-mm-ss zzz";

	public static void initWriteData(Control[] controls) {
		try {
			sheets = new ArrayList();
			int cs = 0;
			ArrayList<Control> cTemp = new ArrayList();
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				Calendar current_cal = Calendar.getInstance();
				long ms = current_cal.getTimeInMillis();
				Date d = new Date(ms);

				fileName = "Logs/" + sdf.format(d) + ".xls";
			} catch (Exception e) {
				e.printStackTrace();
			}
			destFile = new File(fileName);
			destFile.mkdirs();
			destFile.delete();
			out = new FileOutputStream(destFile, true);
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(Locale.getDefault());
			workbook = Workbook.createWorkbook(out, ws);
			for (int i = 0; i < controls.length; i++) {
				if ((ReadData.getController(i).getAxisCount() != 0)
						&& (ReadData.getController(i).getButtonCount() != 0)) {
					System.out.println("in if: " + i);

					sheets.add(workbook.createSheet(controls[i].getController()
							.getName() + " " + i, 0));

					label = new Label(0, 0, "Time", header);
					((WritableSheet) sheets.get(sheets.size() - 1))
							.addCell(label);
					for (int j = 0; j < controls[i].getController()
							.getAxisCount(); j++) {
						label = new Label(1 + j, 0, controls[i].getAxis(j)
								.getAxisName(), header);
						((WritableSheet) sheets.get(sheets.size() - 1))
								.addCell(label);
					}
					for (int j = 0; j < controls[i].getController()
							.getButtonCount(); j++) {
						label = new Label(1
								+ controls[i].getController().getAxisCount()
								+ j, 0, controls[i].getButton(j)
								.getButtonName(), header);

						((WritableSheet) sheets.get(sheets.size() - 1))
								.addCell(label);
					}
					cTemp.add(controls[i]);
					cs++;
				} else {
					System.out.println("in else: " + i);
				}
			}
			System.out.println("Controls: " + controls.length);
			currentRows = new int[cs];
			controllers = new Control[cTemp.size()];
			for (int i = 0; i < cs; i++) {
				currentRows[i] = 1;
				controllers[i] = ((Control) cTemp.get(i));
			}
			if (sheets.size() == 0) {
				maxRows = 65000;
			}
			wbOpen = true;
			if (!hasOpened) {
				data.setWrap(true);
				hasOpened = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void updateData(int cIndex, double[] axisVals,
			boolean[] buttonVals, String dTime) throws Exception {
		int currentRow = currentRows[cIndex];
		label = new Label(0, currentRow, dTime, data);
		((WritableSheet) sheets.get(cIndex)).addCell(label);
		for (int i = 0; i < axisVals.length; i++) {
			number = new Number(i + 1, currentRow, axisVals[i], floatFormat);
			((WritableSheet) sheets.get(cIndex)).addCell(number);
		}
		String[] buttonDat = new BoolConverter().contvertToString(buttonVals);
		for (int i = 0; i < buttonVals.length; i++) {
			label = new Label(1 + axisVals.length + i, currentRow,
					buttonDat[i], data);
			((WritableSheet) sheets.get(cIndex)).addCell(label);
		}
		currentRows[cIndex] = (++currentRow);
		int rows = 0;
		for (int i : currentRows) {
			rows += i;
		}
		if (rows >= maxRows) {
			addBook();
		}
		out.flush();
	}

	private static void addBook() {
		locked = Boolean.valueOf(true);
		close();
		initWriteData(controllers);
		System.out.println("Size: " + currentRows.length);
		try {
			Thread.currentThread();
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		locked = Boolean.valueOf(false);
	}

	public static void close() {
		try {
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
