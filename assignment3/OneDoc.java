package assignment3;

import java.util.HashMap;
import java.util.Iterator;

public class OneDoc {
	
	HashMap<String,Integer> wordCount = new HashMap<String,Integer>();
	int label;
	int totalWords = 0;;
	
	public OneDoc(String s){
		String[] wordList = s.split(" ");
		label = Integer.parseInt(wordList[0]);
		for(int i = 1; i < wordList.length; i++){
			String[] countingPair = wordList[i].split(":");
			wordCount.put(countingPair[0], Integer.parseInt(countingPair[1]));
			totalWords += Integer.parseInt(countingPair[1]);
		}
	}
	
	public String toString(){
		String s = "";
		
		s = s + label + " " + "\n";
		s = s + totalWords + " " + "\n";
		Iterator iterator = wordCount.keySet().iterator();
		while(iterator.hasNext()) {
			String word = (String)iterator.next();
			int count = wordCount.get(word);
			s = s + word + ":" + count + " " + "\n";
		}
		
		return s;
	}
	
	public static void main(String[] args){
		OneDoc d = new OneDoc("1 represent:1 code:1 office:2 show:1 money:3");
		System.out.print(d);
	}

}
