package assignment4;

import java.util.Random;

public class Node {
	
	public static final int NUM_OF_ACTIONS = 2000;
	public static final double R_PLUS = 100;
	
	String content;
	String action;
	String estimatedAction;
	double util;
	int numOfUp,numOfDown,numOfLeft,numOfRight;
	
	double utilUp,utilDown,utilLeft,utilRight;
	
	public Node(char c){
		content = String.valueOf(c);
		
		util = 0.0;
		
		utilUp = 0;
		utilDown = 0;
		utilLeft = 0;
		utilRight = 0;
		
//		Random r = new Random();
//		
//		utilUp = r.nextDouble() * 10;
//		utilDown = r.nextDouble() * 10;
//		utilLeft = r.nextDouble() * 10;
//		utilRight = r.nextDouble() * 10;
		
		numOfUp = 0;
		numOfDown = 0;
		numOfLeft = 0;
		numOfRight = 0;
	}
	
	public Node(Node n){
		content = n.content;
		action = n.action;
		util = n.util;
	}
	
	public int getNumOfActions(String direction){
		int result = 0;
		
		switch(direction){
		case "U":
			result = numOfUp;
			break;
		case "D":
			result = numOfDown;
			break;
		case "L":
			result = numOfLeft;
			break;
		case "R":
			result = numOfRight;
			break;
		default:
			break;
		}
		
		return result;
	}
	
	public void setUtil(double util, String direction){
		switch(direction){
		case "U":
			utilUp = util;
			break;
		case "D":
			utilDown = util;
			break;
		case "L":
			utilLeft = util;
			break;
		case "R":
			utilRight = util;
			break;
		default:
			break;
		}
	}
	
	public double getUtil(String direction){
		double result = 0.0;
		
		switch(direction){
		case "U":
			result = utilUp;
			break;
		case "D":
			result = utilDown;
			break;
		case "L":
			result = utilLeft;
			break;
		case "R":
			result = utilRight;
			break;
		default:
			break;
		}
		
		return result;
	}
	
	public double highestUtil(){
		double result = Integer.MIN_VALUE;
		
		if(utilDown < utilUp){
			result = utilUp;
			estimatedAction = "U";
		}else{
			result = utilDown;
			estimatedAction = "D";
		}
		
		if(result < utilLeft){
			result = utilLeft;
			estimatedAction = "L";
		}
		
		if(result < utilRight){
			result = utilRight;
			estimatedAction = "R";
		}
		
		return result;
	}
	
	public double actionUtilFunction(String direction){
		double result = 0.0;
		
		Random r = new Random();
		
		switch(direction){
		case "U":
			if(numOfUp < NUM_OF_ACTIONS){
				result = R_PLUS + r.nextDouble();
			}else{
				result = utilUp;
			}
			break;
		case "D":
			if(numOfDown < NUM_OF_ACTIONS){
				result = R_PLUS + r.nextDouble();
			}else{
				result = utilDown;
			}
			break;
		case "L":
			if(numOfLeft < NUM_OF_ACTIONS){
				result = R_PLUS + r.nextDouble();
			}else{
				result = utilLeft;
			}
			break;
		case "R":
			if(numOfRight < NUM_OF_ACTIONS){
				result = R_PLUS + r.nextDouble();
			}else{
				result = utilRight;
			}
			break;
		default:
			break;
		}
		
		return result;
	}
	
	public String toString(){
		String s = "";
		
		if(content.matches(" ") || content.matches("S")){
			int totalActions = numOfUp+numOfDown+numOfLeft+numOfRight;
			double hu = highestUtil();
			s = action + "  " + util + "  estimate: " + estimatedAction + " " + hu + " actions: " + totalActions;
		}else if(content.matches("-") || content.matches("\\+")){
			int totalActions = numOfUp+numOfDown+numOfLeft+numOfRight;
			double hu = highestUtil();
			s = action + "  " + util + "  estimate: " + estimatedAction + " " + hu + " actions: " + totalActions;
		}else{
			s = "N/A";
		}
		
		return s;
	}

}
