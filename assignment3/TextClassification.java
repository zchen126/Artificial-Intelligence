package assignment3;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TextClassification {
	
	static final int NUM_OF_CLASS = 20;
	
	double total = 0, right = 0;
	double totalClassCount = 0.0;
	
	int[] classCount = new int[NUM_OF_CLASS];
	int[] classCountTest = new int[NUM_OF_CLASS];
	double[] prior = new double[NUM_OF_CLASS];
	double[] totalWordsCount = new double[NUM_OF_CLASS];
	
	HashSet<String> uniqueWords = new HashSet<String>();
	HashMap<String,Double>[] wordDistribution = new HashMap[NUM_OF_CLASS];
	
	ArrayList<OneDoc> TrainingText;
	ArrayList<OneDoc> TestText;
	ArrayList<Integer> TestResults;
	
	public void proceed(){
		TestText = readText("src/assignment3/test20.txt");
		TrainingText = readText("src/assignment3/train20.txt");
		modelLearning();
		recognizeAllDocs();
//		for(int i = 0; i < NUM_OF_CLASS; i++){
//			System.out.println("class " + i + ": ");
//			printTop20(wordDistribution[i]);
//			System.out.println();
//		}
		oddsRatios(wordDistribution[18],wordDistribution[19]);
	}
	
	public void oddsRatios(HashMap<String,Double> mp1, HashMap<String,Double> mp2){
		HashMap<String,Double> ratios = new HashMap<String,Double>();
		Iterator it = uniqueWords.iterator();
		while(it.hasNext()){
			String word = (String)it.next();
			double p1 = mp1.get(word);
			double p2 = mp2.get(word);
			double p = Math.log(p1/p2);
			ratios.put(word, p);
		}
		System.out.println("odds ratio:");
		printTop20(ratios);
	}
	
	public void printTop20(HashMap<String,Double> mp){
		List<Map.Entry<String,Double>> list = new ArrayList<>();
		list.addAll(mp.entrySet());
		ValueComparator c = new ValueComparator();
		Collections.sort(list,c);
		Iterator<Map.Entry<String, Double>> it = list.iterator();
		int count = 0;
		while(it.hasNext()){
			if(count >= 20)break;
			count++;
			System.out.println(it.next());
		}
	}
	
	public void recognizeAllDocs(){
		
		TestResults = new ArrayList<Integer>();
		
		for(OneDoc d : TestText){
			total++;
			int result = recognizeOneDoc(d);
			TestResults.add(result);
			if(d.label == result)right++;
		}
		
		System.out.println(right / total);
		System.out.println();
		double[][] confusion = new double[NUM_OF_CLASS][NUM_OF_CLASS];
		
		for(int i = 0; i < TestResults.size(); i++){
			confusion[TestText.get(i).label][TestResults.get(i)]++;
		}
		
		for(OneDoc d: TestText){
			classCountTest[d.label]++;
		}
		
		for(int i = 0; i < NUM_OF_CLASS; i++){
			for(int j = 0; j < NUM_OF_CLASS; j++){
				confusion[i][j] /=  classCountTest[i];
				DecimalFormat df = new DecimalFormat("0.000");
				System.out.print(df.format(confusion[i][j]) + "\t");
			}
			System.out.println();
		}
		System.out.println();
//		System.out.println(uniqueWords.size());
//		for(int i : TestResults){
//			System.out.println(i);
//		}
	}
	
	public int recognizeOneDoc(OneDoc d){
		
		int result = -1;
		double probability = Integer.MIN_VALUE;
		
		for(int i = 0; i < NUM_OF_CLASS; i++){
			double p = computeProbability(d,i);
			if(p > probability){
				probability = p;
				result = i;
			}
		}
		
		return result;
	}
	
	public double computeProbability(OneDoc d, int docClass){
		
		double result = 0.0;
		result += Math.log(prior[docClass]);
		
		Iterator iterator = d.wordCount.keySet().iterator();
		while(iterator.hasNext()){
			String word = (String)iterator.next();
			if(uniqueWords.contains(word)){
				double count = d.wordCount.get(word);
				result = result + count * Math.log(wordDistribution[docClass].get(word));
			}
		}
		
		return result;
	}
	
	public void modelLearning(){
		
		for(int i = 0; i < NUM_OF_CLASS; i++){
			wordDistribution[i] = new HashMap<String,Double>();
		}
		
		for(OneDoc d : TrainingText){
			sinleTextLearning(d);
		}
		
		for(int i = 0; i < NUM_OF_CLASS; i++){
			prior[i] = classCount[i]/totalClassCount;
		}
		
		int numOfUnique = uniqueWords.size();
		
		for(int i = 0; i < NUM_OF_CLASS; i++){
			HashMap<String,Double> hm = wordDistribution[i];
			Iterator iterator = uniqueWords.iterator();
			while(iterator.hasNext()){
				String word = (String)iterator.next();
				double count;
				if(hm.containsKey(word)){
					count = hm.get(word);
				}else{
					count = 0;
				}
				double p = (count + 0.01) / (totalWordsCount[i] + numOfUnique * 0.01);
				hm.put(word, p);
			}
		}
	}
	
	public void sinleTextLearning(OneDoc d){
		
		int label = d.label;
		
		classCount[label]++;
		totalClassCount++;
		totalWordsCount[label] += d.totalWords;
		
		Iterator iterator = d.wordCount.keySet().iterator();
		while(iterator.hasNext()) {
			String word = (String)iterator.next();
			if(!uniqueWords.contains(word)){
				uniqueWords.add(word);
			}
			double count = d.wordCount.get(word);
			if(wordDistribution[label].containsKey(word)){
				double c = wordDistribution[label].remove(word);
				c += count;
				wordDistribution[label].put(word, c);
			}else{
				wordDistribution[label].put(word, count);
			}
		}
	}
	
	public ArrayList<OneDoc> readText(String fileName){
		
		ArrayList<OneDoc> result = new ArrayList<OneDoc>();
		
		Reader r = new Reader();
		ArrayList<String> stringList = r.read(fileName);
		for(String s : stringList){
			result.add(new OneDoc(s));
		}
		
		return result;
	}
	
	public static void main(String[] args){
		TextClassification t = new TextClassification();
		t.proceed();
	}

}
