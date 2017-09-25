package io.github.mdillon95.scoutingpc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.UIManager;

public class Main {
	
	public static void main(String[] args) {
		try {
			Log.setOutputFile(new File("logs/latest.log"));
		} catch (IOException e) {
			Log.e(Main.class, e.toString());
		}
		Log.i(Main.class, "Starting Drome Scouting App");
		Log.i(Main.class, "Getting System Properties");
		String osname = System.getProperty("os.name");
		Log.i(Main.class, "OS Name: " + osname);
		if (osname.contains("Windows")) Globals.unix = false;
		else Globals.unix = true;
		Log.i(Main.class, "Setting global UI look and feel");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			Log.e(Main.class, e1.toString());
		}
		// check login file
		Log.i(Main.class, "Attempting to login to Firebase");
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            ArrayList<String> tempStack = new ArrayList<>();
            tempStack.add(e.toString());
            for (int i=0;i < e.getStackTrace().length;i++) {
                StackTraceElement ste = e.getStackTrace()[i];
                tempStack.add("\tin " + ste.getClassName() + "." + ste.getMethodName() + "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")");
            }
            for (String str : tempStack) {
                try {
                    Log.e(Class.forName(e.getStackTrace()[1].getClassName()), str);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
		});
		new LoginDialog();
	}
	
	public static void quit(int status) { // Insert close operation code here
		Log.i(Main.class, "Program Exit");
		Log.end();
		System.exit(status);
	}

}
