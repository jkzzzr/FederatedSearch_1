package URICount;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

/**
 * 用来检验得到的docid uri length文件是否是正确的
 * 就是根据length去查找，然后如果输出出来的都很专区的是warc/0.18就对了
 * @author Lee
 *
 */
public class testReadFile {

	public static void main(String[] args) throws Exception {
		ArrayList<MYCLASS> AL = new ArrayList();
		BufferedReader bReader = new BufferedReader(new FileReader("/home/Lee/data/test/aaa"));
		String recordLine = "";
		while ((recordLine = bReader.readLine())!=null){
			StringTokenizer stringTokenizer = new StringTokenizer(recordLine);
//			System.out.println(recordLine);
			String docid = stringTokenizer.nextToken();
			String uri = stringTokenizer.nextToken();
			String lenString = "";
			while (stringTokenizer.hasMoreTokens()){
				lenString = stringTokenizer.nextToken();
			}
			System.out.println(recordLine);
			long len = 0;
	//		try{
				len = Long.parseLong(lenString);
			/*}catch (Exception e) {//continue;
			}*/
			AL.add(new MYCLASS(docid, uri, len));
		}
		for (int i = 0; i< AL.size();i++){
	//		BufferedReader br = new BufferedReader(new FileReader("/media/clueweb09_1of2/ClueWeb09_English_1/en0000/08.warc.gz"));
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new GZIPInputStream(
									new FileInputStream("/media/clueweb09_1of2/ClueWeb09_English_1/en0002/08.warc.gz"))
							,"UTF-8"));
			br.skip(AL.get(i).len);
			System.out.println(AL.get(i).docid+"**\t"+br.readLine());
			br.close();
			Thread.sleep(1000L);
		}
	}

}
class MYCLASS{
	String docid;
	String uri;
	long len;
	public MYCLASS(String a, String b, long c) {
		docid = a;
		uri = b;
		len = c;
	}
}
