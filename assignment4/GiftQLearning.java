package assignment4;

import java.util.ArrayList;
import java.util.Random;

public class GiftQLearning {
	
	public static final int NUM_OF_TRIALS = 5000;
	
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public static final int NO_GIFT = 0;
	public static final int HAS_GIFT = 1;
	
	public static final int NO_MONEY = 0;
	public static final int HAS_MONEY = 1;
	
	int xlen,ylen;
	
	GiftNode[][] map;
	
	public int[] makeOneMove(int x, int y, int direction, int gift, int money){
		
		map[x][y].numOfActions[direction][gift][money]++;
		
		int[] newStatus = new int[4]; // 0: x, 1: y, 2: gift, 3: money
		
		int newx,newy,newGift,newMoney;
		
		double pGoStraightforward,pStay,pRightAngle;
		
		Random r = new Random();
		double p = r.nextDouble();
		
		if(gift == HAS_GIFT && map[x][y].content.matches("S")){
			pGoStraightforward = 0.6;
			pStay = 0.9;
			pRightAngle = 0.95;
		}else if(gift == HAS_GIFT){
			pGoStraightforward = 0.8;
			pStay = 0.9;
			pRightAngle = 0.95;
		}else{
			pGoStraightforward = 0.9;
			pStay = 0.9;
			pRightAngle = 0.95;
		}
		
		switch(direction){
		case UP:
			break;
		case DOWN:
			break;
		case LEFT:
			break;
		case RIGHT:
			break;
		default:
			break;
		}
		
		return newStatus;
	}
	
	public double rewardFunction(int x, int y, int gift, int money, GiftNode[][] current){
		double result = 0.0;
		
		if(!current[x][y].content.matches("%") && gift == 0){
			result = -0.1;
		}else if(current[x][y].content.matches("F") && gift == 1){
			result = 5.0;
		}else{
			result = 0.0;
		}
		
		return result;
	}
	
	public void getMap(String fileName){
		Reader r = new Reader();
		ArrayList<String> mapString = r.read(fileName);
		xlen = mapString.get(0).length();
		ylen = mapString.size();
		map = new GiftNode[xlen][ylen];
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				map[i][j] = new GiftNode(mapString.get(j).charAt(i));
			}
		}
	}
	
	public String toString(){
		String s = "";
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(map[i][j].content.matches("%"))continue;
				s = s + "(" + i + "," + j + "): \r\n" + map[i][j].toString(); 
			}
		}
		
		return s;
	}
	
	public static void main(String[] args){
		System.out.println();
	}

}
