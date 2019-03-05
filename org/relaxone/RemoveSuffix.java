package org.relaxone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RemoveSuffix {
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("E:\\gem5\\mem-1.out"));
			BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\gem5\\mem-2.out"));
			String readLine;
			while((readLine = reader.readLine()) != null) {
				if(readLine.startsWith("000000")) {
					continue;
				}else if(readLine.startsWith("trls: ")) {
					readLine = readLine.substring("trls: ".length());
				}else if(readLine.startsWith("rls: ")) {
					readLine = readLine.substring("rls: ".length());
				}else if(readLine.startsWith("ls: ")) {
					readLine = readLine.substring("ls: ".length());
				}else if(readLine.startsWith("s: ")) {
					readLine = readLine.substring("s: ".length());
				}else if(readLine.startsWith(": ")) {
					readLine = readLine.substring(": ".length());
				}else if(readLine.startsWith(" ")) {
					readLine = readLine.substring(" ".length());
				}
				System.out.println(readLine);
				writer.write(readLine + "\n");
			}
			reader.close();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
