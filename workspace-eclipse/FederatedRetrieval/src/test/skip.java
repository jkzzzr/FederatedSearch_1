package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class skip {
/**
 * 测试：
 * 	skip能跳跃到哪里，是warc/018开头能，还是哪里
 * 结论：
 * 		跳转到：下一个文档的说明头部
 * 					WARC/0.18
						WARC-Type: response
						WARC-Target-URI: http://004967b.netsolhost.com/category.php
 * @param args
 * @throws Exception 
 */
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("/media/clueweb09_1of2/ClueWeb09_English_1/en0000/00.warc"));
		while (!br.readLine().contains("WARC-Identified-Payload-Type:")){
			System.out.print("1");
		}
		String tempLine = br.readLine();
		String temp = tempLine.replaceAll("Content-Length: ", "");
		System.out.println(temp);
		long x = Long.parseLong(temp);
		System.out.println(tempLine);
		br.skip(x);
		for (int a = 0; a< 10; a++)
		System.out.println(br.readLine());
		br.close();
	}

}
