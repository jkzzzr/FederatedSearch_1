package test;

public class Process_uri {

	public static void main(String[] args) {
		String uri = "http://-blameitonthesummerrain.tumblr.com/post/17564341814/france-ten-france-twenty-france";
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
		System.out.println(newStr);
	}

}
