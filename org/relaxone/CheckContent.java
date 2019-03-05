package org.relaxone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CheckContent {
	public static void main(String[] args) throws IOException {
		
		int index = 0;
		BufferedReader reader = new BufferedReader(new FileReader("E:\\gem5\\mem-2.out"));
		
		String readLine;
		while((readLine = reader.readLine())!=null) {
			if(readLine.startsWith("access wrote 64 bytes to address")){
				index++;
			}
		}
		reader.close();
		System.out.println(index);
		
	}
}
