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
	public static final int PAGESIZE = 4 * 1024;
	
	/**
	 * func: 预处理指定文件
	 * @param filepath_input
	 * @param filepath_output
	 * @throws IOException
	 */
	public void handle(String filepath_input,String filepath_output) throws IOException {
		
		String str = ": system.mem_ctrls: ";
		int strLen = str.length();
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath_input));
		BufferedWriter writer = new BufferedWriter(new FileWriter(filepath_output));
		String readLine;
		while((readLine = reader.readLine()) !=null) {
			// 去掉字符串中的 str_start 
			readLine = readLine.substring(readLine.indexOf(str)+strLen);
			// 保留包含 address 子串的字符串
			if(readLine.contains("address") && !readLine.startsWith("000000")) {
				// 去掉字符串中含有 access 的子集
				if(!readLine.startsWith("access")) {
					String result = "";
					// 将字符串分割成数组,得到其中写入的大小和写入的地址数据
					String[] strings = readLine.split(" ");
					// 写入地址
					if (readLine.startsWith("Read") || readLine.startsWith("IFetch")) {
						result += READ + " " ;
					} else {
						result += WRITE + " ";
					}
					
					result += strings[5] + " " + Integer.parseInt(strings[8].substring(2),16) + "\n";
					// 将数据写入到filepath_out 文件中
					writer.write(result);
				}
			}
		}
		System.out.println("数据写入 " + filepath_output + " 成功！！！");
		reader.close();
		writer.close();
	}
	/**
	 * func: 计算每次访问的页号，并统计每个页的读写次数
	 * @param sourcePath: out文件所在路径
	 * @param destPath: 生成的 CSV 文件存放的位置
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
			
			// 计算每个地址的页号
			int pageNumber = Integer.parseInt(strings[2]) / PAGESIZE;
			String flag = strings[0];
			
			// 判断当前的集合中是否存在该页面,不存在时新建对象并放入到集合中,存在时更新其读或者写计数器
			if(!map.containsKey(String.valueOf(pageNumber))) {
				Integer[] value = new Integer[2];
				// 当集合中不存在页面时,并且当前操作是读操作,则添加该页面到集合中,并设置其读计数器为 1 写计数器为 0
				if (flag.equals("1")) {
					value[0] = 0;
					value[1] = 1;
					map.put(String.valueOf(pageNumber), value);
				}else {
					// 当集合中不存在页面时,并且当前操作是写操作,则添加该页面到集合中,并设置其读计数器为 0写计数器为 1
					value[0] = 1;
					value[1] = 0;
					map.put(String.valueOf(pageNumber), value);
				}
			}else {
				// 当访问是读时, 将对应页面中读计数加 1
				if (flag.equals("1")) {
					Integer[] integers = map.get(String.valueOf(pageNumber));
					integers[1] += 1;
					map.put(String.valueOf(pageNumber), integers);
				}else {
					// 当访问是写时, 将对应页面中写计数加 1
					Integer[] integers = map.get(String.valueOf(pageNumber));
					integers[0] += 1;
					map.put(String.valueOf(pageNumber), integers);
				}
			}
		}
		// 将数组中的数据按指定格式写入到 csv 文件中
		for(Map.Entry<String, Integer[]> entry: map.entrySet()) {
			String key = entry.getKey();
			Integer[] value = entry.getValue();
			String result = key + "," + value[0] + ","+value[1]+"\n";
			writer.write(result);
		}
		System.out.println("数据从指定格式写入CSV文件成功");
		reader.close();
		writer.close();
	}
	
	public static void main(String[] args) throws IOException {
		String orginPath = "E:\\gem5\\mem-gcc.out";
		String destPath = "E:\\gem5\\mem-gcc-1.out";
		String resultPath = "E:\\gem5\\mem-gcc.csv";
		HandleOutFile handleOutFile = new HandleOutFile();
		handleOutFile.handle(orginPath, destPath);
		handleOutFile.countAndWriteToCSV(destPath, resultPath);
	}
	
}
