package org.relaxone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Check64 {
	public static void main(String[] args) throws IOException {
		int index = 0;
		BufferedReader reader = new BufferedReader(new FileReader("E:\\gem5\\mem-2.out"));

		String readLine;
		int t = 0;
		while ((readLine = reader.readLine()) != null) {
			t++;
			if (readLine.contains("64")) {
				index++;
			} else {
				System.out.println(t + "  " + readLine);
			}
		}
		reader.close();
		System.out.println(index);
	}
}
