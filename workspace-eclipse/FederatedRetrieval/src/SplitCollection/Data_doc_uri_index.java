package SplitCollection;

public class Data_doc_uri_index implements Comparable{
	private String docid;
	private String uri = "ERRR";
	private long index;
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	@Override
	public int compareTo(Object o) {
		
		return docid.compareTo(((Data_doc_uri_index)o).docid);
	}
	
	public String toString(){
		return this.docid+"\t"+this.uri+"\t"+this.index;
	}

	/*@Override
	private int compareTo(Object object){
		return 0;
	}*/
}
