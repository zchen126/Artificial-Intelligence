package Assignment22;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Strategy {
	
	static final int MINIMAX_DEPTH = 4;
	static final int ALPHABETA_DEPTH = 4;
	static final double GAMMA = 0.2;
	
	public static Board minimax(Board b, Counter c){
		return minimax(b, MINIMAX_DEPTH, c);
	}
	
	public static Board minimax(Board b, int depth, Counter c){
		
		//base case
		
		if(depth == 0 || b.isComplete()){
			return b;
		}
		
		ArrayList<Board> children = b.expand();
		Board next = null;
		
		if(b.currentPlayer == Color.blue){
			
			int max = Integer.MIN_VALUE;
			
			for(Board child : children){
				c.count++;
				int util = minimax(child, depth - 1, c).utilityForBlue();
				if(util > max){
					max = util;
					next = child;
				}
			}
		}else{
			
			int min = Integer.MAX_VALUE;
			
			for(Board child : children){
				c.count++;
				int util = minimax(child, depth - 1, c).utilityForBlue();
				if(util < min){
					min = util;
					next = child;
				}
			}
		}
		
		return next;
	}
	
	public static Board alphaBeta(Board b, Counter c){
		return alphaBeta(b, ALPHABETA_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, c);
	}
	
	public static Board alphaBeta(Board b, int depth, int alpha, int beta, Counter c){
		
		// base case
		
		if(depth == 0 || b.isComplete()){
			return b;
		}
		
		ArrayList<Board> children = b.smartExpand();
		Board next = null;
		
		if(b.currentPlayer == Color.blue){
			int util = Integer.MIN_VALUE;
			for(Board child : children){
				c.count++;
				int u = alphaBeta(child, depth-1, alpha, beta, c).utilityForBlue();
				if(util < u){
					util = u;
					next = child;
				}
				if(util > alpha){
					alpha = util;
				}
				if(alpha >= beta){
					break;
				}
			}
		}else{
			int util = Integer.MAX_VALUE;
			for(Board child : children){
				c.count++;
				int u = alphaBeta(child, depth-1, alpha, beta, c).utilityForBlue();
				if(u < util){
					util = u;
					next = child;
				}
				if(util < beta){
					beta = util;
				}
				if(beta <= alpha){
					break;
				}
			}
		}
		return next;
	}
	
	public static Board expectiMinimax(Board b, Counter c){
		return expectiMinimax(b, MINIMAX_DEPTH, c);
	}
	
	public static Board expectiMinimax(Board b, int depth, Counter c){
		
		if(depth == 0 || b.isComplete()){
			return b;
		}
		
		Board next = null;
		ArrayList<ArrayList<Board>> children = b.expectiExpand();
		
		if(b.currentPlayer == Color.blue){
			
			double max = Integer.MIN_VALUE;
			
			for(ArrayList<Board> child : children){
				c.count++;
				if(child.size() == 1){
					double util = expectiMinimax(child.get(0), depth-1, c).utilityForBlue();
					if(util > max){
						max = util;
						next = child.get(0);
						c.flag = false;
					}
				}else{
					double util = 
							GAMMA * expectiMinimax(child.get(0), depth-1, c).utilityForBlue()
					+ (1 - GAMMA) * expectiMinimax(child.get(1), depth-1, c).utilityForBlue();
					if(util > max){
						max = util;
						Random r = new Random();
						double randomNumber = r.nextInt(1001)/1000.0;
						if(randomNumber <= GAMMA){
							next = child.get(0);
						}else{
							next = child.get(1);
						}
						c.flag = true;
					}
				}
			}
		}else{
			
			double min = Integer.MAX_VALUE;
			
			for(ArrayList<Board> child : children){
				c.count++;
				if(child.size() == 1){
					double util = expectiMinimax(child.get(0), depth-1, c).utilityForBlue();
					if(util < min){
						min = util;
						next = child.get(0);
						c.flag = false;
					}
				}else{
					double util = 
							GAMMA * expectiMinimax(child.get(0), depth-1, c).utilityForBlue()
					+ (1 - GAMMA) * expectiMinimax(child.get(1), depth-1, c).utilityForBlue();
					if(util < min){
						min = util;
						Random r = new Random();
						double randomNumber = r.nextInt(1001)/1000.0;
						if(randomNumber <= GAMMA){
							next = child.get(0);
						}else{
							next = child.get(1);
						}
						c.flag = true;
					}
				}
			}
		}
		
		return next;
	}

}
