package org.relaxone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RemoveAccess {
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("E:\\gem5\\mem-2.out"));
			BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\gem5\\mem-3.out"));
			String readLine;
			int i = 0;
			while((readLine = reader.readLine()) != null) {
				if(!readLine.startsWith("access")) {
					writer.write(readLine + "\n");
				}else {
					i++;
				}
			}
			reader.close();
			writer.close();
			System.out.println(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
