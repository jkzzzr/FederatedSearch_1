package record;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.sound.midi.SysexMessage;

public class Entry_ReadFile {

	public static void main(String[] args) throws IOException {
		long tt = System.currentTimeMillis();
		new Entry_ReadFile().fun1();
		System.err.println(System.currentTimeMillis() - tt);
	}
	public void fun1() throws IOException{
		String inputPath = "/media/clueweb09_1of2/ClueWeb09_English_";
		String outputPath = "/home/Lee/data/test/ClueWeb09_English_";
		for (int i = 1; i <=10; i++){
			String tempInput = inputPath + i;
			String tempoutput = outputPath + i;
			File outputFile = new File(tempoutput);
			if (!outputFile.exists()){
				outputFile.mkdirs();
			}
			File file = new File(tempInput);
			File[] files = file.listFiles();
			for (File tempf : files){
				String en = tempf.getName();

				String input= "";
				input = inputPath+i+"/"+en;
				
				String output = "";
				output = outputPath+i+"/"+en;
				File outputFile2 = new File(output);
				if (!outputFile2.exists()){
					try{
					outputFile2.mkdir();
					}catch (Exception e) {
						System.err.println(output);
					}
				}
				run(input, output);
		//		new MyThread(input, output).start();;
			}
		}
	}
	public void run(String intputPath, String outputPath){
		File file = new File(intputPath);
	File [] files = file.listFiles();
	System.out.println(intputPath+"\t"+outputPath);
	for (File tempf: files){
		String filename = tempf.getName();
		try {
			new ReadFile_018().filterAndWrite(intputPath+"/"+filename, outputPath+"/"+filename.replaceAll(".warc.gz", ""));
		} catch (Exception e) {
			System.err.println(intputPath+"\t"+outputPath+"\t"+filename.replaceAll(".warc.gz", ""));
			e.printStackTrace();
		}
	}
		
	}
}
class MyThread extends Thread{
	String intputPath = "";
	String outputPath = "";
	public MyThread(String i, String o) {
		intputPath = i;
		outputPath = o;
	}
	@Override
	public void run() {
		super.run();
		File file = new File(intputPath);
		File [] files = file.listFiles();
		for (File tempf: files){
			String filename = tempf.getName();
			try {
				new ReadFile_018().filterAndWrite(intputPath+"/"+filename, outputPath+"/"+filename.replaceAll(".warc.gz", ""));
			} catch (Exception e) {
				System.out.println(intputPath+"\t"+outputPath+"\t"+filename.replaceAll(".warc.gz", ""));
				e.printStackTrace();
			}
		}
	}
}