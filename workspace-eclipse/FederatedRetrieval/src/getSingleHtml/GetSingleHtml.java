package getSingleHtml;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GetSingleHtml {
	public void get(String inputPath, String outputPath, String docid, long index) throws Exception{
		GZIPInputStream gzipInputStream = new GZIPInputStream(
				new FileInputStream(inputPath));
		BufferedReader bReader = new BufferedReader(
				new InputStreamReader(gzipInputStream, "UTF-8"));
		
		String line = "";
		
		bReader.skip(index);
		while (true){
			line = bReader.readLine();
			if (line == null){
				break;
			}
			if (line .startsWith("WARC/0.18")){
				bReader.readLine();
				bReader.readLine();
				bReader.readLine();
				bReader.readLine();
				bReader.readLine();
				line = bReader.readLine();break;
			}else if (line .startsWith("WARC-Type:")){
				bReader.readLine();
				bReader.readLine();
				bReader.readLine();
				bReader.readLine();
				line = bReader.readLine();break;
			}else if (line .startsWith("WARC-Target-URI:")){
				bReader.readLine();
				bReader.readLine();
				bReader.readLine();
				line = bReader.readLine();break;
			}else if (line .startsWith("WARC-Warcinfo-ID:")){
				bReader.readLine();
				bReader.readLine();
				line = bReader.readLine();break;
			}else if (line .startsWith("WARC-Date:")){
				bReader.readLine();
				line = bReader.readLine();break;
			}else if (line .startsWith("WARC-Record-ID")){
				line = bReader.readLine();break;
			}
			if (line.startsWith("WARC-TREC-ID:")){
				String tempdoc = line.substring(line.indexOf("clueweb"));
				if (tempdoc.equals(docid)){
					break;
				}else {
					continue;
				}
			}
		}
		
		if (line==null){
			return;
		}
		//不为空则找到了对应文档编号的文档
		//且line停留在WARC-TREC-ID: clueweb09-en0000-00-00000这一行
		while (true){
			
		}
		for (int i = 0; i < 20){
			
		}
		
		
	}
}