package choose_doc_list;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;


public class ChooseDocList {

	public static ArrayList<String> doclist = new ArrayList<>(); 
	public static void main(String[] args) {
		main1();
		System.out.println(doclist.size());
		HashSet<String> hSet = new HashSet<>(doclist);
		System.out.println(hSet.size());
	}

	/**
	 * 测试的，部分数据
	 * /media/ClueWeb12_CatB/ClueWeb12_00/0000tw/0000tw-00.warc.gz
		/media/ClueWeb12_CatB/ClueWeb12_00/0000tw/0000tw-01.warc.gz
		/media/ClueWeb12_CatB/ClueWeb12_00/0000tw/0000tw-02.warc.gz
		/media/ClueWeb12_CatB/ClueWeb12_00/0000tw/0000tw-03.warc.gz
		/media/ClueWeb12_CatB/ClueWeb12_00/0000tw/0000tw-04.warc.gz
		/media/ClueWeb12_CatB/ClueWeb12_00/0000tw/0000tw-05.warc.gz
		/media/ClueWeb12_CatB/ClueWeb12_00/0000tw/0000tw-06.warc.gz
		/media/ClueWeb12_CatB/ClueWeb12_00/0000tw/0000tw-07.warc.gz
		/media/ClueWeb12_CatB/ClueWeb12_00/0000tw/0000tw-08.warc.gz
		/media/ClueWeb12_CatB/ClueWeb12_00/0000tw/0000tw-09.warc.gz
	 */
	private static void main1(){
		System.out.println("START");
		String inputPath = "/home/Lee/data/D1/ClueWeb12_00/0000tw/0000tw-";
		String outputPath = "/home/Lee/data/D1-doclist";
		for (int i = 0; i <10; i++){
			String tempInput = inputPath + String.format("%02d", i);
			try {
				run(tempInput);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		System.out.println("WRITE");
		try {
			write(outputPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("END");
		/*System.out.println("READ!!");
		try {
			ArrayList<String> aList = read(outputPath);
			for (int i = 0; i< aList.size(); i++){
				System.out.println("\t"+aList.get(i));
				Thread.sleep(500);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	/**
	 * 全集
	 */
	private static void main2(){
		String inputPath = "/home/Lee/data/D1/ClueWeb12_";
		String outputPath = "/home/Lee/data/D1-doclist";
		for (int i = 1; i <=10; i++){
			String tempInput = inputPath + String.format("%02d", i);
			File file = new File(tempInput);
			File[] files = file.listFiles();
			for (File tempf : files){
				String en = tempf.getName();

				String input= "";
				input = inputPath+i+"/"+en;
				File file2 = new File(input);
				File [] files2 = file2.listFiles();
				for (File tempf1: files2){
					String filename = tempf1.getName();
					try {
						run(input+"/"+filename);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			write(outputPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void run(String string) throws Exception {
		BufferedReader bReader = new BufferedReader(new FileReader(string));
		String line = "";
		while ((line = bReader.readLine())!=null){
			StringTokenizer st = new StringTokenizer(line);
			
			if (st.hasMoreTokens()){
				String doc = st.nextToken();
				if (doc.startsWith("clueweb12")){
					doclist.add(doc);
				}
			}
		}
		bReader.close();
		
	}
	private static void write(String outputPath) throws Exception{
		FileOutputStream fos = new FileOutputStream(outputPath);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(doclist);
		oos.flush();
		oos.close();
	}
	private static ArrayList<String> read(String inputPath) throws Exception{
		FileInputStream fis = new FileInputStream(inputPath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		@SuppressWarnings("unchecked")
		ArrayList<String> result = (ArrayList<String>) ois.readObject();
		return result;
	}
}
