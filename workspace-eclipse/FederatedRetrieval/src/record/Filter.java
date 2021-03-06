package record;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

import javax.sound.midi.SysexMessage;
/**
 * 给定docid列表，只解压这些文件
 * @author Lee
 *
 */
public class Filter {

	private  String inputPath = "";
	private  String outputPath = "";
	private int x = 0;
	private  HashMap<String, TreeSet<String>> TreeSet = new HashMap<>();
	
	public Filter(){
		
	}
	public Filter(int number) {
		super();
		this.inputPath = "/media/clueweb09_1of2/ClueWeb09_English_" + number;
		this.outputPath = "/home/Lee/音乐/result/ClueWeb09_English_"+number;
		this.x  = number;
		try {
			TreeSet = readFile("/home/Lee/音乐/123/ClueWeb09_English_"+number);
		} catch (Exception e) {
		//	new toolsWrite().print(x, e.getMessage());
		}
	}

	
	
	
	/**
	 * 输入为/media/clueweb09_1of2/ClueWeb09_English_1的地址
	 * @param alaPath
	 * @throws Exception
	 */
	public  void run1() throws Exception {
		
		File files = new File(inputPath);
		File [] files2 = files.listFiles();
		for (File file : files2){
			String floder = file.getName();
			decorate(floder);
			
		}
		
		
	}
	
	
	/**
	 * 输入文件夹名字en0000,解压这个文件夹下面文件
	 * @param flodername
	 * @throws Exception
	 */
	private void decorate(String flodername) throws Exception{
		HashMap<String, TreeSet<String>> treeSet = TreeSet;
		//进入这个目录,同时创建输出目录
		String inputFilePath = inputPath + "/" + flodername + "/";
		String outputFilePath = outputPath + "/" + flodername +"/";
		File file2 = new File(outputFilePath);
		if (!file2.exists()){
			file2.mkdir();
		}
		
		
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
		for (String temp_filename :sub_filename){
		//	runn("/media/clueweb09_1of2/ClueWeb09_English_1/en0000/00.warc.gz", "/home/Lee/音乐/result/en0000/00.warc", treeSet.get("00"));
			runn(inputFilePath + temp_filename +".warc.gz",outputFilePath + temp_filename + ".warc", treeSet.get(flodername + "+" + temp_filename));
		}
	}
	/**读取inputPath下的文件，解压的哦啊outputPath中，只解压hSet中包含的doc
	*/
	private void runn(String inputPath, String outputPath, TreeSet<String> hSet) throws FileNotFoundException, IOException{

		
		GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(inputPath));
		OutputStream outputStream = new FileOutputStream(outputPath);

		BufferedReader bReader = new BufferedReader(new InputStreamReader(gzipInputStream, "UTF-8"));
		BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
		
		String temp_doc = hSet.first();
		
		String temp = "";
		temp = bReader.readLine();
		bWriter.write(temp+"\n");
		//去掉每个文件，最开头的一个说明小段落
		while ((temp = bReader.readLine())!=null){
			if (temp.startsWith("WARC/0.18")){
				break;
			}
			bWriter.write(temp+"\n");
		}
			StringBuffer sBuffer = new StringBuffer();
			boolean find = false;
			
			String temp_out_loop ="";
			while ((temp_out_loop = bReader.readLine())!=null){
				if (temp_out_loop.startsWith("WARC/0.18")){
					sBuffer = new StringBuffer();
					find = false;
				}
				StringBuffer subhead = new StringBuffer();
		//		bWriter.newLine();
				String temp_in_loop="";
				
				if (!find){
					//查找对应文档标号，也就是说，这个循环此时需要做的是在subhead中存储对应文档编号的头文件
					boolean infindProc = true;
					while ((temp_in_loop = bReader.readLine())!=null){
						if (infindProc){
							subhead.append("\n"+temp_in_loop);
			//				System.out.println(subhead.length());
						}else {
							if (temp_in_loop.startsWith("WARC/0.18")){
								infindProc = true;
								subhead.delete(0, subhead.length());
								subhead = new StringBuffer();
								subhead.append("\n"+temp_in_loop);
							}
							continue;
						}
						if (temp_in_loop.startsWith("WARC-TREC-ID:")){
							if (temp_doc == null || temp_doc.isEmpty()){
								bReader.close();
								bWriter.close();
								return;
							}
							String temp_doc_in_warc = "";
							temp_doc_in_warc = temp_in_loop.substring(temp_in_loop.indexOf("clueweb09"));
							if (temp_doc_in_warc.equals(temp_doc)){
						//		System.out.println("found" + temp_doc);
								hSet.pollFirst();
								if (hSet.isEmpty()){
									temp_doc = null;
								}else {
									temp_doc = hSet.first();
								}
								infindProc = false;
								find = true;
							}
							else {
								infindProc = false;
								if (temp_doc_in_warc.compareTo(temp_doc) <0){
									find = false;					
									
								}else {
									System.err.println("error "+ temp_doc);
									hSet.pollFirst();

				//					new toolsWrite().print(x, "****************\n\t"+temp_doc);
								}
							}
						}
					
						if (find){
							sBuffer.append("\n"+subhead+"\n");
							bWriter.write(subhead.toString()+"\n");
							temp_out_loop = bReader.readLine();
							break;
						}else {
							continue;
						}
					}
				}
				bWriter.write(temp_out_loop+"\n");
				
			}
		bReader.close();
		bWriter.close();
	}
	/**输入为00，表示的是clueweb09-en0000-00-12334.中的倒数第二组目录，<br>
	* 就是读取ClueWeb09_English_1文件下面，所有的00文档，进入HashMap中，<br>
	* 因为这些文档，在硬盘中存储的时候，是按照00这个文件夹存储的
	* <key, value> <en0000+00, clueweb09-en0000-00-12334>
	*/
	private  HashMap<String, TreeSet<String>> readFile(String docidPath) throws Exception{
		HashMap<String, TreeSet<String>> hMap = new HashMap<>();
		
		BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(docidPath)));
		String string = "";
		while ((string = bReader.readLine())!=null){
			String key1 = string.substring(17,19);
			String key2 = string.substring(10,16);
			TreeSet<String> tSet = new TreeSet<>();
			if (hMap.containsKey(key2+"+"+key1)){
				tSet = hMap.get(key2+"+"+key1);
				tSet.add(string);
			}
			tSet.add(string);
			hMap.put(key2+"+"+key1, tSet);
		}
		bReader.close();
		return hMap;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
