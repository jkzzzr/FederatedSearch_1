package record;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

import javax.security.auth.x500.X500Principal;
import javax.sound.midi.SysexMessage;

import tools.tools;

public class ReadFile {

	/**
	 * 记录硬盘中的ClueWeb09_English_2文件夹下面包含哪些en0000的文件夹
	 */
	private HashMap<String,String> HMap_floder_en = new HashMap<String,String>();
	public static void main(String[] args) {
		

	}
	public void run(){
		
		
	}
	/**
	 * 读取ClueWeb09_English_1一级的目录，将这个目录中包含的en0000存储在HMap_floder_en中
	 * 初始化HMap_floder_en对象
	 */
	public void init_floder_en(){
		String input_Prefix = "/media/clueweb09_1of2/ClueWeb09_English_";
		String outputPath_Prefix = "";
		
		//将en0000与外层的文件夹对应起来ClueWeb09_English_1
		//记录每个ClueWeb09_English_1下面存储的en0000文件夹有哪些
		HashMap<String, String> hMap = new HashMap<>();
		for (int i1 = 1; i1 <= 10; i1++){
			File file = new File(input_Prefix + i1 +"/");
		//	System.out.println(file.isDirectory() +"\t"+file.getAbsolutePath());
			File [] flordNames = file.listFiles();
			for (File string: flordNames){
				hMap.put(string.getName(), "ClueWeb09_English_" + i1);
			}
		}
		HMap_floder_en = hMap;
	}
	/**
	 * 读取en0000一级的目录，将这个目录包含的warc.gz文件存储在数组中返回
	 * @param output_prefix 示例：/home/Lee/音乐/result/en0000
	 */
	public String[] read_en(String flodername,String inputPath_prefix){
		//进入这个目录,同时创建输出目录
		String inputFilePath = inputPath_prefix + "/" + flodername + "/";
		
		
		File file = new File(inputFilePath);
		
		
		File [] files = file.listFiles();
		String [] sub_filename = new String[files.length];
		int index = 0;
		for (File temp_file: files){
			if (!temp_file.getName().contains("warc.gz")){
				continue;
			}
			String filename = temp_file.getName();
			filename = filename.substring(0, 2);
			sub_filename[index++] = filename;
		}
		return sub_filename;
		
	}
	/**
	 * 
	 * @param flodername	示例：en0000
	 * @param inputPath_prefix	示例：media/clueweb09_1of2/ClueWeb09_English_1/en0000
	 * runn("/media/clueweb09_1of2/ClueWeb09_English_1/en0000/00.warc.gz", "/home/Lee/音乐/result/en0000/00.warc", treeSet.get("00"));
			
	 * @throws Exception 
	 */
	public static void filterAndWrite(String inputPath, String outputPath) throws Exception{
		File file2 = new File(outputPath);
		if (!file2.exists()){
			file2.createNewFile();
		}

		GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(inputPath));
		OutputStream outputStream = new FileOutputStream(outputPath);
		
		BufferedReader bReader = new BufferedReader(new InputStreamReader(gzipInputStream, "UTF-8"));
//		BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(outputPath));
		
		String temp_doc = "";
		
		String templine = "";
		//去掉每个文件，最开头的一个说明小段落
		Index index = new Index();
		index.x = 0;
		while (true){
			templine = bReader.readLine();
			if (templine == null){
				break;
			}
			index.incream(templine);
			if (templine.startsWith("WARC/0.18")){
				LINEInfo lineInfo = new LINEInfo();
				lineInfo.skip = index.x - 1 - templine.length();
				//中间一行是WARC-Type: response，没什么用到
				if ((templine = bReader.readLine()) ==null){
					index.incream(templine);
					break;
				}
				templine = bReader.readLine();
				index.incream(templine);
				if (templine == null){
					break;
				}
				if (templine.contains("WARC-Target-URI:")){
					String extractURI = "";
					try{
						extractURI = templine.replaceAll("WARC-Target-URI: ", "");
					}catch(Exception e){
						extractURI = templine.substring(templine.indexOf("http"));
					}
					lineInfo.URI = extractURI;
				}
				//**************
				for (int tempx = 1;tempx <=4;tempx++){
					templine = bReader.readLine();
					index.incream(templine);
				}
				if (templine.startsWith("WARC-TREC-ID: ")){
					String extractDocid = "";
					try{
						extractDocid = templine.replaceAll("WARC-TREC-ID: ", "");
					}catch(Exception e){
						extractDocid = templine.substring(templine.indexOf("clueweb"));
					}
					lineInfo.docid = extractDocid;
				}
				//**************
				for (int tempx = 1;tempx <=3;tempx++){
					index.incream(bReader.readLine());
				}
				if (templine.contains("Content-Length:")){
					String extractLength = "";
					extractLength = templine.replaceAll("Content-Length: ", "");
					long needskip = Long.parseLong(extractLength);
					bReader.skip(needskip);
					index.x += needskip;
				}
				if (lineInfo.docid.length()<2){
			//		tools.run(templine+"\t"+lineInfo.toString());
				}else {
					bWriter.write(lineInfo.toString());
					bWriter.newLine();
				}
			}
			
	}
		bWriter.flush();
		bWriter.close();
	}
	public static long incream(long x,String line){
		return x+line.length()+1;
	}
}
 class Index{
	long x = 0;
	void incream(String line ){
		x+=line.length()+1;
	}
}
 class LINEInfo{
	 String docid = "";
	 String URI = "";
	 long skip = 0;
	 @Override
	 public String toString(){
		 return ""+docid+"\t"+URI+"\t"+skip+"";
	 }
 }
