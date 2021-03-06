package combinAllCollection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import tools.tools;

public class CombineAllCollection {

	public static Stack STACK = new Stack<>();
	//线程安全
	//<uri,vector<docid>>
	public static Hashtable<String, Vector<String>> hashtable = new Hashtable<>();
	private Hashtable<String, Vector<String>> table = new Hashtable<>();
	public static void main(String[] args) {
		new CombineAllCollection().combine();
	}
	public void combine(){
		long currentTime = System.currentTimeMillis();
		System.out.println("第一步开始：录入所有信息到HashTable中");
		String inputPath = "/home/Lee/data/D2/ClueWeb12_";
		String outputPath = "/home/Lee/data/D3/result";
		ArrayList <Hashtable<String, Vector<String>>> aList = new ArrayList<>();
		for (int i = 0; i <=19; i++){
			String istr = String.format("%02d", i);
			String tempInput = inputPath + istr;
			File file = new File(tempInput);
			File[] files = file.listFiles();
			for (File tempf : files){
				
				String en = tempf.getName();

				String input= "";
				input = inputPath+istr+"/"+en;
				
				String output = "";
				File outputFile2 = new File(output);
				if (!outputFile2.exists()){
					try{
					outputFile2.mkdir();
					}catch (Exception e) {
						System.err.println(output);
					}
				}
				Hashtable<String, Vector<String>> temph = new Hashtable<String, Vector<String>>();
				new MyThread(input, temph).start();
				aList.add(temph);
			}
		}
		test signal = new test();
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				synchronized (signal) {
					
					if (STACK.isEmpty()){
						signal.notify();
					}
				}
				
			}
		};
		Timer timer = new Timer();
		timer.schedule(timerTask, 0,10000);
		synchronized (signal) {
			try {
				signal.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("第一步执行完毕！用时：" + (System.currentTimeMillis() - currentTime));
		currentTime = System.currentTimeMillis();
		System.out.println("第二步开始：（合并ArrayList<HashTable>）");
		Hashtable<String, Vector<String>> hashtable = new Hashtable<>();
		hashtable = merge(aList);
		System.out.println("第二部执行完毕！用时："+(System.currentTimeMillis() - currentTime));
		
		currentTime = System.currentTimeMillis();
		System.out.println("第三步开始：（写出Hashtable<String,Vector<String>>）");
		try {
			write(hashtable, outputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(Hashtable<String, Vector<String>> hashtable, String outputPath) throws IOException{
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(outputPath));
		for (String string : hashtable.keySet()){
			Vector<String> tempVector = hashtable.get(string);
			Iterator iterator = tempVector.iterator();
			while (iterator.hasNext()){
				bWriter.write(string+"\t"+iterator.next()+"\n");
			}
			bWriter.flush();
		}
		bWriter.close();
	}
	public Hashtable<String, Vector<String>> merge(ArrayList <Hashtable<String, Vector<String>>> aList){
		System.out.println(aList.size());
		Hashtable<String, Vector<String>> result = aList.get(1);
		for (int i = 1; i < aList.size(); i++){
			Hashtable<String, Vector<String>> tempHashtable = aList.get(i);
			System.out.println(tempHashtable.size()+"++++");
			for (String string: tempHashtable.keySet()){
				if (result.contains(string)){
					Vector<String> tempvector = result.get(string);
					tempvector.addAll(tempHashtable.get(string));
					result.replace(string, tempvector);
				}else {
					result.put(string, tempHashtable.get(string));
				}
			}
		}
		return result;
	}
	public void run(String inputPath, Hashtable<String, Vector<String>> hashtable) throws Exception{
		BufferedReader bReader = new BufferedReader(new FileReader(inputPath));
		String line = "";
		while ((line = bReader.readLine())!=null){
			StringTokenizer st = new StringTokenizer(line);
			String uri = st.nextToken();
			String docid = st.nextToken();
			uri = extract(uri);
			if (uri ==null){
				tools.run("@CombineAllCollection\terr\n\t将uri精简的过程中出多了\n\t"+line, "_CombineAlCollection");
				continue;
			}
			if (hashtable.contains(uri)){
				hashtable.get(uri).add(docid);
			}else {
				hashtable.put(uri, new Vector<String>(){{add(docid);}});
			}
		}
		bReader.close();
		
	}
	public String extract(String uri){
		int index = 0;
		try{
		String subURI = uri.substring(0, uri.lastIndexOf("."));
		index = subURI.lastIndexOf(".");
		//如果是没有点的话，就是-1，那么正好加1
		//如果有的话，那么就应当时点的下表的后面一位开始，所以也是加1
		index ++;
		}catch (Exception e) {
			return null;
		}
		String returnString = uri.substring(index);
		return returnString;
	}
}
class MyThread extends Thread{
	String intputPath = "";
	Hashtable<String, Vector<String>> hashtable = null;
	public MyThread(String i,Hashtable<String, Vector<String>> hashtable) {
		intputPath = i;
		this.hashtable = hashtable;
	}
	@Override
	public void run() {
		super.run();
		CombineAllCollection.STACK.add(false);
		File file = new File(intputPath);
		File [] files = file.listFiles();
		CombineAllCollection cac = new CombineAllCollection();
		for (File tempf: files){
			String filename = tempf.getName();
			try {
				cac.run(intputPath+"/"+filename, hashtable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		CombineAllCollection.STACK.pop();
		Thread.currentThread().interrupt();
	}
}
