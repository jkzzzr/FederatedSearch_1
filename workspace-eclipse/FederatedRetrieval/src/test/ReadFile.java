package test;

public class ReadFile {

	public static void main(String[] args) throws Exception {
		new ReadFile().filterAndWrite();
	}
	public void filterAndWrite() throws Exception{
		long tt = System.currentTimeMillis();
		record.ReadFile.filterAndWrite("/media/clueweb09_1of2/ClueWeb09_English_1/en0000/00.warc.gz"
				, "/home/Lee/data/test/aaa");
		System.out.println(System.currentTimeMillis() - tt);
	}

}