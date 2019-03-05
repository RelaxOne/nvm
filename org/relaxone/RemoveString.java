package org.relaxone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RemoveString {
	public static final int READ = 1;
	public static final int WRITE = 0;

	public static final String SIZE = "size";
	public static final String ADDRESS = "address";

	public static void handle() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("E:\\gem5\\mem-3.out"));
			BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\gem5\\mem-4.out"));
			String readLine;
			while ((readLine = reader.readLine()) != null) {
				String result = "";
				String[] messages = handleString(readLine);
				if (readLine.startsWith("Read") || readLine.startsWith("IFetch")) {
					result += READ + " " ;
				} else {
					result += WRITE + " ";
				}
				result += messages[0] + " " + Integer.parseInt(messages[1].substring(2),16) + "\n";
				writer.write(result);
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String[] handleString(String str) {
		String[] result = new String[2];
		String[] strings = str.split(" ");
		result[0] = strings[5];
		result[1] = strings[8];
		return result;
	}
	
	public static void main(String[] args) {
		handle();
	}
}
