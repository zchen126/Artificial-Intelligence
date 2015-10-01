package assignment4;

import java.util.Random;

public class GiftNode {
	
	public static final int NUM_OF_ACTIONS = 200;
	public static final double R_PLUS = 100;
	
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public static final int NO_GIFT = 0;
	public static final int HAS_GIFT = 1;
	
	public static final int NO_MONEY = 0;
	public static final int HAS_MONEY = 1;
	
	String content;
	
	int[][] estimatedAction = new int[2][2];    // estimatedAction[gift][money]
	int[][][] numOfActions = new int[4][2][2];  // numOfActions[direction][gift][money]
	double[][][] util = new double[4][2][2];    // util[direction][gift][money]
	
	public GiftNode(char c){
		content = String.valueOf(c);
	}
	
	public int getNumOfActions(int direction, int gift, int money){
		return numOfActions[direction][gift][money];
	}
	
	public void setUtil(int direction, int gift, int money, double util){
		this.util[direction][gift][money] = util;
	}
	
	public double getUtil(int direction, int gift, int money){
		return util[direction][gift][money];
	}
	
	public double highestUtil(int gift, int money){
		
		if(content == "B")money = 1;
		if(content == "S" && money == 1 && gift == 0){
			money = 0;
			gift = 1;
		}
		if(content == "F" && gift == 1)gift = 0;
		
		double highestUtil = Integer.MIN_VALUE;
		
		for(int i = 0; i < 4; i++){
			if(highestUtil < util[i][gift][money]){
				highestUtil = util[i][gift][money];
				estimatedAction[gift][money] = i;
			}
		}
		
		return highestUtil;
	}
	
	public double actionUtilFunction(int direction, int gift, int money){
		
		Random r = new Random();
		
		if(numOfActions[direction][gift][money] < NUM_OF_ACTIONS){
			return R_PLUS + r.nextDouble();
		}else{
			return util[direction][gift][money];
		}
	}
	
	public String toString(){
		String s = "";
		
		if(content.matches("%")){
			s = "N/A";
		}else{
			String s1 = "0 gift 0 money: " + estimatedAction[0][0] + " " + highestUtil(0,0);
			String s2 = "0 gift 1 money: " + estimatedAction[0][1] + " " + highestUtil(0,1);
			String s3 = "1 gift 0 money: " + estimatedAction[1][0] + " " + highestUtil(1,0);
			String s4 = "1 gift 1 money: " + estimatedAction[1][1] + " " + highestUtil(1,1);
			
			s = s1 + "\r\n" + s2 + "\r\n" + s3 + "\r\n" + s4 + "\r\n";  
		}
		
		return s;
	}

}
