package getSingleHtml;

public class test_getSingleHtml {

	public static void main(String[] args) {
		GetSingleHtml getSingleHtml = new GetSingleHtml();
		getSingleHtml.docid = "clueweb09-en0002-08-00000";
		getSingleHtml.index = 419L;
		getSingleHtml.input = "/media/clueweb09_1of2/ClueWeb09_English_1/en0002/08.warc.gz";
		getSingleHtml.output = "/home/Lee/data/test/111";
		getSingleHtml.run();
		System.out.println("end");
	}

}