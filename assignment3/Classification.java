package assignment3;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Classification {
	
	static final int CLASS_AMOUNT = 10;
	static final int XLEN = 28;
	static final int YLEN = 28;
	
	int[] digitCount = new int[CLASS_AMOUNT];
	int[] digitCountTest = new int[CLASS_AMOUNT];
	double[] prior = new double[CLASS_AMOUNT];
	double[][][] probability = new double[CLASS_AMOUNT][XLEN][YLEN];
	
	int xlen,ylen;
	double trainingTime,testTime;
	HashMap<Integer,Double>[][][] advancedProbability;
	
	ArrayList<Digit> trainingDigits;
	ArrayList<Digit> testDigits;
	
	ArrayList<Integer> trainingLabels;
	ArrayList<Integer> testLabels;
	ArrayList<Integer> testResult;
	
	public void proceed(int x, int y, boolean overlap){
		readData();
		double t1 = System.currentTimeMillis();
		differentFeatureLearning(x,y,overlap);
		double t2 = System.currentTimeMillis();
		recognizeAllDigits(x,y,overlap);
		double t3 = System.currentTimeMillis();
		trainingTime = t2 - t1;
		testTime = t3 - t2;
		System.out.println("Training time: " + trainingTime + "ms   test time: " + testTime + "ms");
	}
	
	public void recognizeAllDigits(int x, int y, boolean overlap){
		
		testResult = new ArrayList<Integer>();
		
		for(Digit d : testDigits){
			int n = recognizeOneDigit(x,y,overlap,d);
			testResult.add(n);
		}
		
        int c = 0;
		
		for(int i = 0; i < testResult.size(); i++){
			if((testResult.get(i) - testLabels.get(i)) == 0)c++;
		}
		
		double[][] confusion = new double[CLASS_AMOUNT][CLASS_AMOUNT];
		for(int i = 0; i < testResult.size(); i++){
			confusion[testLabels.get(i)][testResult.get(i)]++;
		}
		
		for(int n : testLabels){
			digitCountTest[n]++;
		}
		
		for(int i = 0; i < CLASS_AMOUNT; i++){
			for(int j = 0; j < CLASS_AMOUNT; j++){
				confusion[i][j] /=  digitCountTest[i];
				DecimalFormat df = new DecimalFormat("0.000");
				System.out.print(df.format(confusion[i][j]) + "\t");
			}
			System.out.println("\n");
		}
		
		System.out.println("Accuracy: " + (c / testResult.size()));
	}
	
	public int recognizeOneDigit(int x, int y, boolean overlap, Digit d){
		
		int result = -1;
		double probability = Integer.MIN_VALUE;
		
		for(int i = 0; i < CLASS_AMOUNT; i++){
			double p = computeProbability(x,y,overlap,d,i);
//			System.out.println(p);
			if(p > probability){
				probability = p;
				result = i;
			}
		}
		
		return result;
	}
	
	public double computeProbability(int x, int y, boolean overlap, Digit d, int n){
		
		double result = 0.0;
		result += Math.log(prior[n]);
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(overlap){
					int featureNumber = computeFeature(i,j,x,y,d);
					double p;
					if(advancedProbability[n][i][j].containsKey(featureNumber)){
						p = (advancedProbability[n][i][j].get(featureNumber) + 1) / (digitCount[n] + 2);
					}else{
						p = 1.0 / (digitCount[n] + 2);
					}
					result += Math.log(p);
				}else{
					int featureNumber = computeFeature(i*x,j*y,x,y,d);
					double p;
					if(advancedProbability[n][i][j].containsKey(featureNumber)){
						p = (advancedProbability[n][i][j].get(featureNumber) + 1) / (digitCount[n] + 2);
					}else{
						p = 1.0 / (digitCount[n] + 2);
					}
					result += Math.log(p);
				}
			}
		 }
		
		return result;
	}
	
	public void differentFeatureLearning(int x, int y, boolean overlap){
		double numOfFeatures = Math.pow(2, x*y);
		if(overlap){
			xlen = 29 - x;
			ylen = 29 - y;
		}else{
			xlen = 28/x;
			ylen = 28/y;
		}
		
		advancedProbability = new HashMap[CLASS_AMOUNT][xlen][ylen];
		
		for(int i = 0; i < CLASS_AMOUNT; i++){
			for(int j = 0; j < xlen; j++){
				for(int k = 0; k < ylen; k++){
					advancedProbability[i][j][k] = new HashMap<Integer,Double>();
				}
			}
		}
		
		for(int i = 0; i < trainingLabels.size(); i++){
			singleDigitLearning(x,y,overlap,trainingDigits.get(i),trainingLabels.get(i));
		}
		
//		for(int i = 0; i < 10; i++){
//			for(int j = 0; j < xlen; j++){
//				for(int k = 0; k < ylen; k++){
//					for(int l = 0; l < numOfFeatures; l++){
//						advancedProbability[i][j][k][l] = (advancedProbability[i][j][k][l] + 1) / (digitCount[i] + 2);
//					}
//				}
//			}
//		}
		
		for(int i = 0; i < CLASS_AMOUNT; i++){
			prior[i] = digitCount[i] / 5000.0;
		}
	}
	
	public void singleDigitLearning(int x, int y, boolean overlap, Digit d, Integer n){
		int featureNumber;
		digitCount[n]++;
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(overlap){
					featureNumber = computeFeature(i,j,x,y,d);
				}else{
					featureNumber = computeFeature(i*x,j*y,x,y,d);
				}
//				advancedProbability[n][i][j][featureNumber]++;
				if(advancedProbability[n][i][j].containsKey(featureNumber)){
					double amount = advancedProbability[n][i][j].get(featureNumber);
//					advancedProbability[n][i][j].remove(featureNumber);
					amount++;
					advancedProbability[n][i][j].put(featureNumber, amount);
				}else{
					advancedProbability[n][i][j].put(featureNumber, 1.0);
				}
			}
		}
	}
	
	public int computeFeature(int xstart, int ystart, int xlen, int ylen, Digit d){
		
		int result = 0;
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				int index = xlen * j + i;
				if(d.digit[xstart+i][ystart+j] != ' '){
					result += Math.pow(2, index);
				}
			}
		}
		
		return result;
	}
	
	public void oddsRatios(int n1, int n2){
		distribution(n1);
		System.out.println("******************************");
		distribution(n2);
		System.out.println("******************************");
		
		double[][] ratios = new double[XLEN][YLEN];
		for(int j = 0; j < YLEN; j++){
			for(int i = 0; i < XLEN; i++){
				ratios[i][j] = probability[n1][i][j] / probability[n2][i][j];
				double p = Math.log(ratios[i][j]);
				String s = "";
				if(p >= 1){
					s = "+";
				}else if(p <= -1){
					s = "-";
				}else{
					s = " ";
				}
				System.out.print(s);
			}
			System.out.println();
		}
	}
	
	public void distribution(int n){
		for(int j = 0; j < YLEN; j++){
			for(int i = 0; i < XLEN; i++){
				double p = Math.log(probability[n][i][j]);
				String s = "";
				if(p > -2){
					s = "+";
				}else if(p > -4){
					s = " ";
				}else{
					s = "-";
				}
				System.out.print(s);
			}
			System.out.println();
		}
	}
	
	public void proceed(double k){
		readData();
		modelLearning(k);
		recognizeAllDigits();
	}
	
	public void readData(){
		readTrainingDigits("src/assignment3/trainingimages");
		readTrainingLabels("src/assignment3/traininglabels");
		readTestDigits("src/assignment3/testimages");
		readTestLabels("src/assignment3/testlabels");
	}
	
	public void recognizeAllDigits(){
		
		Digit[] typicalImages = new Digit[CLASS_AMOUNT];
		Digit[] typicalErrors = new Digit[CLASS_AMOUNT];
		
		double[] highestProbability = new double[CLASS_AMOUNT];
		double[] highestErrors = new double[CLASS_AMOUNT];
		
		for(int i = 0; i < CLASS_AMOUNT; i++){
			highestProbability[i] = Integer.MIN_VALUE;
			highestErrors[i] = Integer.MIN_VALUE;
		}
		
		testResult = new ArrayList<Integer>();
		
		for(Digit d : testDigits){
			
			int labelIndex = testResult.size();
			
			double[] result = recognizeOneDigit(d);
			testResult.add((int)result[0]);
			
			if((testLabels.get(labelIndex) - testResult.get(labelIndex)) == 0){
				if(result[1] > highestProbability[(int)result[0]]){
					typicalImages[(int)result[0]] = d;
					highestProbability[(int)result[0]] = result[1];
				}
			}else{
				if(result[1] > highestErrors[(int)result[0]]){
					typicalErrors[(int)result[0]] = d;
					highestErrors[(int)result[0]] = result[1];
				}
			}
		}
		
		int c = 0;
		
		for(int i = 0; i < testResult.size(); i++){
			if((testResult.get(i) - testLabels.get(i)) == 0)c++;
		}
		
		double[][] confusion = new double[CLASS_AMOUNT][CLASS_AMOUNT];
		for(int i = 0; i < testResult.size(); i++){
			confusion[testLabels.get(i)][testResult.get(i)]++;
		}
		
		for(int n : testLabels){
			digitCountTest[n]++;
		}
		
		for(int i = 0; i < CLASS_AMOUNT; i++){
			for(int j = 0; j < CLASS_AMOUNT; j++){
				confusion[i][j] /=  digitCountTest[i];
				DecimalFormat df = new DecimalFormat("0.000");
				System.out.print(df.format(confusion[i][j]) + "\t");
			}
			System.out.println("\n");
		}
		
		System.out.println((c + 0.0) / testResult.size());
		
		for(Digit d : typicalErrors){
			System.out.println(d);
			System.out.println("----------------");
		}
	}
	
	public double[] recognizeOneDigit(Digit d){
		
		double[] result = new double[2];
		result[0] = 0;
		double probability = Integer.MIN_VALUE;
		
		for(int i = 0; i < CLASS_AMOUNT; i++){
			double p = computeProbability(i,d);
			if(p > probability){
				probability = p;
				result[0] = i;
			}
		}
		result[1] = probability;
		
		return result;
	}
	
	public void modelLearning(double constant){
		for(int i = 0; i < trainingLabels.size(); i++){
			singleDigitLearning(trainingDigits.get(i),trainingLabels.get(i));
		}
		for(int i = 0; i < CLASS_AMOUNT; i++){
			for(int j = 0; j < XLEN; j++){
				for(int k = 0; k < YLEN; k++){
					probability[i][j][k] = (probability[i][j][k] + constant) / (digitCount[i] + 2 * constant);
				}
			}
		}
		for(int i = 0; i < CLASS_AMOUNT; i++){
			prior[i] = digitCount[i] / 5000.0;
		}
	}
	
	public double computeProbability(int n, Digit d){
		
		double result = 0.0;
		result += Math.log(prior[n]);
		
		for(int i = 0; i < XLEN; i++){
			for(int j = 0; j < YLEN; j++){
				if(d.digit[i][j] != ' '){
					result += Math.log(probability[n][i][j]);
				}else{
					result += Math.log(1-probability[n][i][j]);
				}
			}
		}
		
		return result;
	}
	
	public void singleDigitLearning(Digit d, Integer n){
		digitCount[n]++;
		for(int i = 0; i < XLEN; i++){
			for(int j = 0; j < YLEN; j++){
				if(d.digit[i][j] != ' '){
					probability[n][i][j]++;
				}
			}
		}
	}
	
	public void printArray(int[] a){
		for(int aa : a){
			System.out.println(aa);
		}
	}
	
	public void printArray(double[] a){
		
		String s = "[ ";
		
		for(double aa : a){
			s += aa;
			s += " ";
		}
		System.out.println(s + "]\n");
	}
	
	public void printArray2D(double[][] a){
		for(double[] aa : a){
			printArray(aa);
		}
	}
	
	public void printArray3D(double[][][] a){
		for(double[][] aa : a){
			printArray2D(aa);
			System.out.println("-----------------");
		}
	}
	
	public void readTrainingDigits(String fileName){
		trainingDigits = readDigits(fileName);
	}
	
	public void readTestDigits(String fileName){
		testDigits = readDigits(fileName);
	}
	
	public void readTrainingLabels(String fileName){
		trainingLabels = readLabels(fileName);
	}
	
	public void readTestLabels(String fileName){
		testLabels = readLabels(fileName);
	}
	
	public ArrayList<Digit> readDigits(String fileName){
		
		Reader r = new Reader();
		ArrayList<String> data = r.read(fileName);
		
		ArrayList<Digit> digits = new ArrayList<Digit>();
		ArrayList<String> oneDigit = new ArrayList<String>();
		
		for(String s : data){
			oneDigit.add(s);
			
			if(oneDigit.size() == YLEN){
				Digit d = new Digit(oneDigit);
				digits.add(d);
				oneDigit = new ArrayList<String>();
			}
		}
		
		return digits;
	}
	
	public ArrayList<Integer> readLabels(String fileName){
		
		Reader r = new Reader();
		ArrayList<String> data = r.read(fileName);
		
		ArrayList<Integer> labels = new ArrayList<Integer>();
		for(String s : data){
			labels.add(Integer.parseInt(s));
		}
		
		return labels;
	}
	
	public static void main(String[] args){
		for(int k = 1; k <= 1; k++){
			Classification c = new Classification();
			c.proceed(1);
		}
	}
}
