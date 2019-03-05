package org.relaxone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HandleOutFile {
	
	public static final int READ = 1;
	public static final int WRITE = 0;

	public static final String SIZE = "size";
	public static final String ADDRESS = "address";
	public static final int PAGESIZE = 64 * 1024;
	
	/**
	 * func: Ԥ����ָ���ļ�
	 * @param filepath_input
	 * @param filepath_output
	 * @throws IOException
	 */
	public void handle(String filepath_input,String filepath_output) throws IOException {
		String str_start = "      0: system.mem_ctrls: ";
		BufferedReader reader = new BufferedReader(new FileReader(filepath_input));
		BufferedWriter writer = new BufferedWriter(new FileWriter(filepath_output));
		String readLine;
		while((readLine = reader.readLine()) !=null) {
			// ȥ���ַ����е� str_start 
			readLine = readLine.substring(str_start.length());
			// �������� address �Ӵ����ַ���
			if(readLine.contains("address") && !readLine.startsWith("000000")) {
				//ȥ���ַ����е� trls, rls,ls,s,:, 
				if(readLine.startsWith("trls: ")) {
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
				// ȥ���ַ����к��� access ���Ӽ�
				if(!readLine.startsWith("access")) {
					String result = "";
					String[] message = new String[2];
					// ���ַ����ָ������,�õ�����д��Ĵ�С��д��ĵ�ַ����
					String[] strings = readLine.split(" ");
					// д���С
					message[0] = strings[5];
					// д���ַ
					message[1] = strings[8];
					if (readLine.startsWith("Read") || readLine.startsWith("IFetch")) {
						result += READ + " " ;
					} else {
						result += WRITE + " ";
					}
					result += message[0] + " " + Integer.parseInt(message[1].substring(2),16) + "\n";
					// ������д�뵽filepath_out �ļ���
					writer.write(result);
				}
			}
			
		}
		System.out.println("����д�� " + filepath_output + " �ɹ�������");
		reader.close();
		writer.close();
	}
	/**
	 * func: ����ÿ�η��ʵ�ҳ�ţ���ͳ��ÿ��ҳ�Ķ�д����
	 * @param sourcePath: out�ļ�����·��
	 * @param destPath: ���ɵ� CSV �ļ���ŵ�λ��
	 * @throws IOException
	 */
	public void countAndWriteToCSV(String sourcePath, String destPath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(sourcePath));
		BufferedWriter writer = new BufferedWriter(new FileWriter(destPath));
		HashMap<String, Integer[]> map = new HashMap<>();
		
		writer.write("pageNumber,writeCount,readCount\n");
		String readLine;
		while ((readLine = reader.readLine()) != null) {
			String[] strings = readLine.split(" ");
			
			// ����ÿ����ַ��ҳ��
			int pageNumber = Integer.parseInt(strings[2]) / PAGESIZE;
			String flag = strings[0];
			
			// �жϵ�ǰ�ļ������Ƿ���ڸ�ҳ��,������ʱ�½����󲢷��뵽������,����ʱ�����������д������
			if(!map.containsKey(String.valueOf(pageNumber))) {
				Integer[] value = new Integer[2];
				// �������в�����ҳ��ʱ,���ҵ�ǰ�����Ƕ�����,����Ӹ�ҳ�浽������,���������������Ϊ 1 д������Ϊ 0
				if (flag.equals("1")) {
					value[0] = 0;
					value[1] = 1;
					map.put(String.valueOf(pageNumber), value);
				}else {
					// �������в�����ҳ��ʱ,���ҵ�ǰ������д����,����Ӹ�ҳ�浽������,���������������Ϊ 0д������Ϊ 1
					value[0] = 1;
					value[1] = 0;
					map.put(String.valueOf(pageNumber), value);
				}
			}else {
				// �������Ƕ�ʱ, ����Ӧҳ���ж������� 1
				if (flag.equals("1")) {
					Integer[] integers = map.get(String.valueOf(pageNumber));
					integers[1] += 1;
					map.put(String.valueOf(pageNumber), integers);
				}else {
					// ��������дʱ, ����Ӧҳ����д������ 1
					Integer[] integers = map.get(String.valueOf(pageNumber));
					integers[0] += 1;
					map.put(String.valueOf(pageNumber), integers);
				}
			}
		}
		// �������е����ݰ�ָ����ʽд�뵽 csv �ļ���
		for(Map.Entry<String, Integer[]> entry: map.entrySet()) {
			String key = entry.getKey();
			Integer[] value = entry.getValue();
			String result = key + "," + value[0] + ","+value[1]+"\n";
			writer.write(result);
		}
		System.out.println("���ݴ�ָ����ʽд��CSV�ļ��ɹ�");
		reader.close();
		writer.close();
	}
	
	public static void main(String[] args) throws IOException {
		String orginPath = "E:\\gem5\\mem.out";
		String destPath = "E:\\gem5\\mem-5.out";
		String resultPath = "E:\\gem5\\mem-2.csv";
		HandleOutFile handleOutFile = new HandleOutFile();
		handleOutFile.handle(orginPath, destPath);
		handleOutFile.countAndWriteToCSV(destPath, resultPath);
	}
	
}
