package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
/**
 * 测试：
 * 结论：br初始化，temp = br.realine();sysout(br.readline);
 * 		结果与：br初始化：br.skip(temp+1);sysout(br.readline);
 * 		相同！，注意的是，一定要+1
 * @author Lee
 *
 */
public class ttt {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader("/media/clueweb09_1of2/ClueWeb09_English_1/en0000/00.warc"));
		String temp = br.readLine();
		int x = temp.length();
		System.out.println(br.read());
		System.out.println(br.readLine());
		br.close();
		br = new BufferedReader(new FileReader("/media/clueweb09_1of2/ClueWeb09_English_1/en0000/00.warc"));
		br.skip((long) x+1);
	//	System.out.println(br.read());
		System.out.println(br.readLine());
	}

}