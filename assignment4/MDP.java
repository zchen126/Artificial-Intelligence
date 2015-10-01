package assignment4;

import java.util.ArrayList;
import java.util.Random;

public class MDP {
	
	public static final int NUM_OF_ITERATION = 400;
	public static final int NUM_OF_ACTIONS = 100000;
	public static final int NUM_OF_TRIALS = 5000;
	
	public static final int START_X = 3;
	public static final int START_Y = 4;
	
	public static final double REWARD = -0.04;
	public static final double GAMMA = 0.99;
	
	int xlen,ylen;
	
	Node[][] map;
	
	public MDP(String fileName){
		getMap(fileName);
		valueIteration();
		TDLearning();
		RMSerror();
	}
	
	public void RMSerror(){
		double error;
		int count = 0;
		double sum = 0.0;
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(!map[i][j].content.matches("%")){
					count++;
					sum += Math.pow((map[i][j].util - map[i][j].highestUtil()), 2);
				}
			}
		}
		sum = sum / count;
		error = Math.sqrt(sum);
		System.out.println("RMS error: " + error);
	}
	
	public void TDLearning(){
		for(int i = 1; i <= NUM_OF_TRIALS; i++){
			TDLearningOneTrail();
		}
	}
	
	public void TDLearningOneTrail(){
		
		int currentx = START_X;
		int currenty = START_Y;
		
		ArrayList<String> directions = new ArrayList<String>();
		directions.add("U");
		directions.add("D");
		directions.add("L");
		directions.add("R");
		
		int t = 0;
		
		while(true){
			t++;
			
			double util = Integer.MIN_VALUE;
			String action = "";
			Node currentNode = map[currentx][currenty];
			for(String d : directions){
				double utild = currentNode.actionUtilFunction(d);
				if(util < utild){
					util = utild;
					action = d;
				}
			}
			int[] newlocation = makeOneMove(currentx, currenty, action);
			int newx = newlocation[0];
			int newy = newlocation[1];
			int n = currentNode.getNumOfActions(action);
			double alpha = 60.0 / (59.0 + n);
			
			double originalQ = map[currentx][currenty].getUtil(action);
			double maxNextQ = map[newx][newy].highestUtil();
			double newutil = originalQ + alpha * (rewardFunction(currentx, currenty, map) + GAMMA * maxNextQ - originalQ);
			map[currentx][currenty].setUtil(newutil, action);
			
			currentx = newx;
			currenty = newy;
			
			if(t >= 50)break;
		}
	}
	
	public void TDLearningOneTrailRevised(){
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(!map[i][j].content.matches("%")){
					
				}
			}
		}
	}
	
	public void takeOneActionAndUpdate(int x, int y){
		
		ArrayList<String> directions = new ArrayList<String>();
		directions.add("U");
		directions.add("D");
		directions.add("L");
		directions.add("R");
		
		double util = Integer.MIN_VALUE;
		String action = "";
		Node currentNode = map[x][y];
	}
	
	public int[] makeOneMove(int x, int y, String direction){
		int[] location = new int[2];
		
		int newx,newy;
		
		Random r = new Random();
		double p = r.nextDouble();
		
		switch(direction){
		case "U":
			map[x][y].numOfUp++;
			if(p < 0.8){
				newx = x;
				newy = y - 1;
			}else if(p < 0.9){
				newx = x - 1;
				newy = y;
			}else{
				newx = x + 1;
				newy = y;
			}
			if(map[newx][newy].content.matches("%")){
				newx = x;
				newy = y;
			}
			break;
		case "D":
			map[x][y].numOfDown++;
			if(p < 0.8){
				newx = x;
				newy = y + 1;
			}else if(p < 0.9){
				newx = x - 1;
				newy = y;
			}else{
				newx = x + 1;
				newy = y;
			}
			if(map[newx][newy].content.matches("%")){
				newx = x;
				newy = y;
			}
			break;
		case "L":
			map[x][y].numOfLeft++;
			if(p < 0.8){
				newx = x - 1;
				newy = y;
			}else if(p < 0.9){
				newx = x;
				newy = y - 1;
			}else{
				newx = x;
				newy = y + 1;
			}
			if(map[newx][newy].content.matches("%")){
				newx = x;
				newy = y;
			}
			break;
		case "R":
			map[x][y].numOfRight++;
			if(p < 0.8){
				newx = x + 1;
				newy = y;
			}else if(p < 0.9){
				newx = x;
				newy = y - 1;
			}else{
				newx = x;
				newy = y + 1;
			}
			if(map[newx][newy].content.matches("%")){
				newx = x;
				newy = y;
			}
			break;
		default:
			newx = x;
			newy = y;
			break;
		}
		
		location[0] = newx;
		location[1] = newy;
		
		return location;
	}
	
	public void valueIteration(){
		for(int i = 0; i < NUM_OF_ITERATION; i++){
			map = oneIteration(map);
		}
	}
	
	public Node[][] oneIteration(Node[][] m){
		Node[][] nextMap = new Node[xlen][ylen];
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				nextMap[i][j] = new Node(m[i][j]);
			}
		}
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(!nextMap[i][j].content.matches("%")){
					computeValueForOneNode(i, j, m, nextMap);
				}
			}
		}
		
		return nextMap;
	}
	
	public void computeValueForOneNode(int x, int y, Node[][] current, Node[][] next){
		
		double value = Integer.MIN_VALUE;
		String action = null;
		
		ArrayList<String> directions = new ArrayList<String>();
		directions.add("U");
		directions.add("D");
		directions.add("L");
		directions.add("R");
		
		for(String d : directions){
			double util = computeOneDirection(x, y, current, d);
			
//			if(x == 1 && y == 1)System.out.println(util);
			
			if (value < util){
				value = util;
				action = d;
			}
		}
		
		next[x][y].action = action;
		next[x][y].util = value;
		
	}
	
	public double computeOneDirection(int x, int y, Node[][] current, String direction){
		double result = 0.0;
		
		double reward = rewardFunction(x, y, current);
		
		Node up,down,left,right;
		
		if(current[x][y-1].content.matches("%")){
			up = current[x][y];
		}else{
			up = current[x][y-1];
		}
		
		if(current[x][y+1].content.matches("%")){
			down = current[x][y];
		}else{
			down = current[x][y+1];
		}
		
		if(current[x-1][y].content.matches("%")){
			left = current[x][y];
		}else{
			left = current[x-1][y];
		}
		
		if(current[x+1][y].content.matches("%")){
			right = current[x][y];
		}else{
			right = current[x+1][y];
		}
		
		switch(direction){
		case "U" :
			result = reward + GAMMA * (0.8 * up.util + 0.1 * left.util + 0.1 * right.util);
			break;
		case "D" :
			result = reward + GAMMA * (0.8 * down.util + 0.1 * left.util + 0.1 * right.util);
			break;
		case "L" :
			result = reward + GAMMA * (0.8 * left.util + 0.1 * down.util + 0.1 * up.util);
			break;
		case "R" :
			result = reward + GAMMA * (0.8 * right.util + 0.1 * down.util + 0.1 * up.util);
			break;
		default :
			break;
		}
		
		return result;
	}
	
	public double rewardFunction(int x, int y, Node[][] current){
		double result = 0.0;
		
		if(current[x][y].content.matches(" ")){
			result = REWARD;
		}else if(current[x][y].content.matches("-")){
			result = -1.0;
		}else if(current[x][y].content.matches("\\+")){
			result = 1.0;
		}
		
		return result;
	}
	
	public void getMap(String fileName){
		Reader r = new Reader();
		ArrayList<String> mapString = r.read(fileName);
		xlen = mapString.get(0).length();
		ylen = mapString.size();
		map = new Node[xlen][ylen];
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				map[i][j] = new Node(mapString.get(j).charAt(i));
			}
		}
	}
	
	public String toString(){
		String s = "";
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(map[i][j].content.matches("%"))continue;
				s = s + "(" + (i-1) + "," + (j-1) + "): " + map[i][j].toString() + "\r\n"; 
			}
		}
		
		return s;
	}
	
	public static void main(String[] args){
		MDP m = new MDP("src/assignment4/map1.txt");
		System.out.println(m);
	}

}
