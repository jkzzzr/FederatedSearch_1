package combinAllCollection;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import tools.tools;

public class test {

	public static int x = 2;
	public static void main(String[] args) {
		
		new test().notifytest();
		
	}


	/**
	 * String abc = new test().extract("networkedblogs.com");
		System.out.println(abc);
	 * @param uri
	 * @return
	 */
	public String extract(String uri){
		String subURI = uri.substring(0, uri.lastIndexOf("."));
		int index = subURI.lastIndexOf(".");
		index ++;
		String returnString = uri.substring(index);
		return returnString;
	}
	public void testHashTable(){
		Hashtable<String, Vector<String>> hashtable = new Hashtable<>();
		hashtable.put("aaa", new Vector<String>(){{add("a11");add("a22");}});
		hashtable.get("aaa").add("bbb");
		Iterator<String> iterator = hashtable.get("aaa").iterator();
		while (iterator.hasNext()){
			System.out.println(iterator.next());
		}
	}
	public void notifytest(){
		//先设置终止条件，不然待会儿没东西唤醒程序了。。。
		System.out.println("设置x = -4");
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				x= -4;
				
			}
		};
		Timer timer = new Timer();
		timer.schedule(timerTask, 3000);
		
		System.out.println("启动notify");
		
		//设置信号量
		tools TTT = new tools();
		
		
		//设置notify唤醒
		TimerTask timerTask2 = new TimerTask() {
			
			@Override
			public void run() {
				synchronized (TTT) {
					
				if (x<0){
					TTT.notify();
				}

				}
			}
		};
		Timer timer2= new Timer();
		timer.schedule(timerTask2, 0,1000);
		//s设置等待
		synchronized (TTT) {
			try {
				TTT.wait();
				System.out.println("+++++++");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
