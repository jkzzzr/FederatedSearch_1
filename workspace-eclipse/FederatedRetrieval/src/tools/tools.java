package tools;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class tools {
	public static void run(String temp) throws Exception{
		BufferedWriter bWriter = new BufferedWriter(new FileWriter("/home/Lee/data/err",true));
		bWriter.write(temp+"\n");
		bWriter.close();
	}
	public static void run(String text, String outputPath) throws Exception{
		BufferedWriter bWriter = new BufferedWriter(new FileWriter("/home/Lee/data/err"+outputPath,true));
		bWriter.write(text+"\n");
		bWriter.close();
	}
}
