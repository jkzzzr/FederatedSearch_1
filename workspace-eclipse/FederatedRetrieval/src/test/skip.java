package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	//	BufferedReader br = new BufferedReader(new FileReader("/media/clueweb09_1of2/ClueWeb09_English_1/en0000/00.warc"));
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/Lee/0000tw-00.warc"), "UTF-8"));
	//	BufferedReader br = new BufferedReader(new FileReader("/home/Lee/0000tw-00.warc"));
		Readline readline = new Readline("/home/Lee/0000tw-00.warc");
		//	while (!br.readLine().contains("WARC-Identified-Payload-Type:")){
		String ttt = "";
		int index = 0;
	//	index +=br.readLine().length()+1;
		int step = 2;
		while (!(ttt=readline.readLine()).contains("Content-Type: application/http; msgtype=response")){
			
			index += readline.readLineByteCount;
			System.out.println("原来："+ttt.length()+"\t"+ttt+"");
			System.out.println(index+Skipx(index));
		}
		/*System.out.println("***********************");
		String tempLine = br.readLine();
		index +=tempLine.length()+2;
		
		String temp = tempLine.replaceAll("Content-Length: ", "");
		System.out.println("起点"+tempLine);
		System.out.println(Skipx(index));
		System.out.println("跳跃的长度"+temp);
		long x = Long.parseLong(temp);
		//读完content-length之后，开始跳
		br.skip(x);
		System.out.println(Skipx(index));
		index +=x;
		System.out.println(Skipx(index));
		System.out.println("终点");
		for (int a = 1; a< 2; a++){
			String temp1 = br.readLine();
			System.out.println("new Line:"
					+ a+"\t"+temp1 +"\t"+temp1.isEmpty());
	//		if (temp1.length()!=0)
			if (ttt.length() ==0){
				step = 1;
			}
			index +=temp1.length()+2;
		}
		
		br.close();
		
		
		br = new BufferedReader(new FileReader("/home/Lee/0000tw-00.warc"));	
		br.skip(index);
		temp = br.readLine();
		System.out.println("***"+temp);
		System.out.println(br.readLine());
			*/
	}

	public static String Skipx(long x ) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader("/home/Lee/0000tw-00.warc"));
		br.skip(x);
		return "SKIP\t"+br.readLine()+"\t"+br.readLine();
	}
}
