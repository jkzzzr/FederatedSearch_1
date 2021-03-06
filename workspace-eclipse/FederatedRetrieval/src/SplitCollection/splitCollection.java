package SplitCollection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import tools.tools;

public class splitCollection {
	private String inputString = "";
	private String outputString = "";
	public splitCollection(String i,String o){
		inputString = i;
		outputString = o;
	}
	public void run (){
		TreeMap<String, TreeSet<Data_doc_uri_index>> hMap;
		try {
			hMap = split(inputString);
			write(outputString, hMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[]args) throws Exception{
		String inputPath = "/home/Lee/data/test/aaa";
		String outputPath = "/home/Lee/data/test/aa-result";
		/*TreeMap<String, TreeSet<Data_doc_uri_index>> hMap = new splitCollection().split(inputPath);
		for (Entry<String, Data_doc_uri_index> data:hMap.entrySet()){
			System.out.println(data.getKey()+"\t"+((Data_doc_uri_index)data.getValue()).getUri());
		}
		System.out.println("++++++++++++++++++");
		new splitCollection().write(outputPath, hMap);*/
		
	}
	/**
	 * 输入文档路径，然后将信息输出到TreeMap中
	 * @param input 文件路径
	 * @return
	 * @throws Exception
	 */
	public TreeMap<String, TreeSet<Data_doc_uri_index>> split(String input) throws Exception{
		TreeMap<String, TreeSet<Data_doc_uri_index>> result = new TreeMap<>();
		BufferedReader bReader = new BufferedReader(new FileReader(input));
		String line = "";
		while ((line = bReader.readLine())!=null){
			Data_doc_uri_index data = splitWord(line);
			TreeSet<Data_doc_uri_index> tSet = new TreeSet<>();
			try{
				if (result.containsKey(data.getUri())){
					tSet = result.get(data.getUri());
					tSet.add(data);
				}
			}catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println(data.toString());
			}
			try{
				result.put(data.getUri(), tSet);
			}catch (Exception e) {
				System.out.println(line);
				System.exit(1);
			}
		}
		bReader.close();
		return result;
	}
	/**
	 * 从一行数据中读取出doc,uri,index
	 * @param line
	 * @return
	 * @throws Exception
	 */
	public Data_doc_uri_index splitWord(String line) throws Exception{
		StringTokenizer stringTokenizer = new StringTokenizer(line);
		String[] strings = new String[stringTokenizer.countTokens()];
		int index = 0;
		Data_doc_uri_index data = new Data_doc_uri_index();
		try{
		while (stringTokenizer.hasMoreTokens()){
			strings[index++] = stringTokenizer.nextToken();
		}
		data.setDocid(strings[0]);
		data.setUri(extract(strings[1]));
		data.setIndex(Long.parseLong(strings[strings.length-1]));
		}catch (Exception e) {
			tools.run("@splitCollection.splitWord\n\tline:"+ line +"\n\terr:"+ e.getMessage());
		}
		/*if (strings[0].equals("clueweb09-en0002-08-03445")){
			System.out.println("==>> \t"+line);
			System.exit(1);
		}*/
		return data;
	}
	/**
	 * 提取uri
	 * @param uri
	 * @return
	 * @throws URISyntaxException
	 */
	public String extract(String uri) throws URISyntaxException{
	//	System.out.println(uri.length()>65?uri.toCharArray()[65]:"");
	//	uri = uri.contains("{")?uri.substring(0,uri.indexOf('{')):uri;
		URI uri2 = null;
		uri = processURI(uri);
		/*uri = uri.replace("_", "");
		uri = uri.replace("-", "");*/
		try{
			uri2 = new URI(uri);
		}catch (Exception e) {
	//		System.out.println("+_");
			String errmessage = e.getMessage();
		/*	System.out.println(e.getMessage()+"\t"
					+ "Illegal character in path at index ".length()+"\t"
					+uri.indexOf(":"));*/
			String errindex = errmessage.substring(
					errmessage.indexOf("index")+"index ".length(),
					errmessage.indexOf(":"));

			int errindexLong = Integer.parseInt(errindex);
			uri2 = new URI(uri.substring(0, errindexLong));
		}
		String hostname = uri2.getHost();
		if (hostname == null){
			hostname = new URI(uri.replace("_", "")).getHost();
		}
		/*if (uri.contains("_")){
			System.out.println(uri);
		}*/
		return hostname;
	}
	/**
	 * 将转换后的文档写出来
	 * @param outputPath
	 * @param hMap
	 * @throws Exception
	 */
	public void write(String outputPath, TreeMap<String, TreeSet<Data_doc_uri_index>> hMap) throws Exception{
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(outputPath));
		for (Entry<String, TreeSet<Data_doc_uri_index>> data:hMap.entrySet()){
			for (Data_doc_uri_index data_doc_uri_index : data.getValue()){
				bWriter.write(data.getKey()+"\t"+data_doc_uri_index.getDocid()+"\n");
			}
		}
		bWriter.flush();
		bWriter.close();
		
	}
	public String processURI(String uri){
		char [] chars = uri.toCharArray();
		char [] newchars = new char[chars.length];
		int j = 0;
		for (int i = 0; i < chars.length; i++){
			char c = chars[i];
			if (Character.isAlphabetic(c)
					||Character.isDigit(c)
					||c=='/'
					||c==':'
					||c=='.'){
				newchars[j++] = c;
			}
		}
		String newStr = new String(newchars);
		return newStr;
	}
}

