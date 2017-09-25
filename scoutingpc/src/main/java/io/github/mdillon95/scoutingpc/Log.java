package io.github.mdillon95.scoutingpc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log {
	
	private static File output = null;
	private static BufferedWriter bw = null;
	
	public static void setOutputFile(File f) throws IOException {
		output = f;
		if (!output.exists()) {
			System.out.println(output.getAbsolutePath());
			String path;
			if (!Globals.unix) path = output.getAbsolutePath().substring(0,output.getAbsolutePath().lastIndexOf("\\"));
			else path = output.getAbsolutePath().substring(0,output.getAbsolutePath().lastIndexOf("/"));
			new File(path).mkdir();
			output.createNewFile();
		} else {
			f.renameTo(new File("logs/" + System.currentTimeMillis() + "-log.log"));
			String path;
			if (!Globals.unix) path = output.getAbsolutePath().substring(0,output.getAbsolutePath().lastIndexOf("\\"));
			else path = output.getAbsolutePath().substring(0,output.getAbsolutePath().lastIndexOf("/"));
			new File(path).mkdir();
			output.createNewFile();
		}
		bw = new BufferedWriter(new FileWriter(output));
	}
	
	public static File getOutputFile() {
		return output;
	}
	
	public static void d(Class<?> cls, CharSequence csq) {
		System.out.println("[" + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds() + "]" + "[DEBUG] " + cls.getName() + ": " + csq);
		if (bw != null) {
			try {
				bw.write("[" + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds() + "]" + "[DEBUG] " + cls.getName() + ": " + csq);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void i(Class<?> cls, CharSequence csq) {
		System.out.println("[" + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds() + "]" + "[INFO] " + cls.getName() + ": " + csq);
		if (bw != null) {
			try {
				bw.write("[" + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds()
						+ "]" + "[INFO] " + cls.getName() + ": " + csq);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void e(Class<?> cls, CharSequence csq) {
		System.err.println("[" + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds() + "]" + "[ERROR] " + cls.getName() + ": " + csq);
		if (bw != null) {
			try {
				bw.write("[" + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds()
						+ "]" + "[ERROR] " + cls.getName() + ": " + csq);
				bw.newLine();
			} catch (IOException i) {
				i.printStackTrace();
			}
		}
	}
	
	public static boolean end() {
		if (bw != null) {
			try {
				bw.flush();
				bw.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

}
