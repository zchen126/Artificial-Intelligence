package Assignment22;

import java.awt.Color;

public class Game {
	
	Board currentBoard;
	int util;
	int nodesExpandedByBlue;
	int nodesExpandedByGreen;
	int timesOfSabotage;
	double totalTimeBlue;
	double totalTimeGreen;
	double start,end;
	Counter c;
	Counter s;
	
	public Game(String fileName){
		currentBoard = new Board(fileName);
		nodesExpandedByBlue = 0;
		nodesExpandedByGreen = 0;
		totalTimeBlue = 0;
		totalTimeGreen = 0;
		timesOfSabotage = 0;
	}
	
	public void minVSmin(){
		
		System.out.println("Game start!");
		System.out.println(currentBoard);
		
		while(!currentBoard.isComplete()){
			c = new Counter();
			start = System.currentTimeMillis();
			currentBoard = Strategy.minimax(currentBoard, c);
//			if(currentBoard.currentPlayer == Color.blue){
//				currentBoard = Strategy.minimax(currentBoard, 2, c);
//			}else{
//				currentBoard = Strategy.minimax(currentBoard, 4, c);
//			}
			end = System.currentTimeMillis();
			if(currentBoard.currentPlayer == Color.blue){
				nodesExpandedByGreen += c.count;
				totalTimeGreen += (end - start);
			}else{
				nodesExpandedByBlue += c.count;
				totalTimeBlue += (end - start);
			}
			System.out.println(currentBoard);
			System.out.println(end - start);
			try {
				Thread.currentThread().sleep(0000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		printResult();
	}
	
	public void abVSab(){
		
		System.out.println("Game start!");
		System.out.println(currentBoard);
		
		while(!currentBoard.isComplete()){
			c = new Counter();
			start = System.currentTimeMillis();
			currentBoard = Strategy.alphaBeta(currentBoard, c);
			end = System.currentTimeMillis();
			if(currentBoard.currentPlayer == Color.blue){
				nodesExpandedByGreen += c.count;
				totalTimeGreen += (end - start);
			}else{
				nodesExpandedByBlue += c.count;
				totalTimeBlue += (end - start);
			}
			System.out.println(currentBoard);
			try {
				Thread.currentThread().sleep(0000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		printResult();
	}
	
	public void abVSmin(){
		
		System.out.println("Game start!");
		System.out.println(currentBoard);
		
		while(!currentBoard.isComplete()){
			c = new Counter();
			start = System.currentTimeMillis();
			if(currentBoard.currentPlayer == Color.blue){
				currentBoard = Strategy.alphaBeta(currentBoard, c);
			}else{
				currentBoard = Strategy.minimax(currentBoard, c);
			}
			end = System.currentTimeMillis();
			if(currentBoard.currentPlayer == Color.blue){
				nodesExpandedByGreen += c.count;
				totalTimeGreen += (end - start);
			}else{
				nodesExpandedByBlue += c.count;
				totalTimeBlue += (end - start);
			}
			System.out.println(currentBoard);
			try {
				Thread.currentThread().sleep(0000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		printResult();
	}
	
	public void minVSab(){
		
		System.out.println("Game start!");
		System.out.println(currentBoard);
		
		while(!currentBoard.isComplete()){
			c = new Counter();
			start = System.currentTimeMillis();
			if(currentBoard.currentPlayer == Color.blue){
				currentBoard = Strategy.minimax(currentBoard, c);
			}else{
				currentBoard = Strategy.alphaBeta(currentBoard, c);
			}
			end = System.currentTimeMillis();
			if(currentBoard.currentPlayer == Color.blue){
				nodesExpandedByGreen += c.count;
				totalTimeGreen += (end - start);
			}else{
				nodesExpandedByBlue += c.count;
				totalTimeBlue += (end - start);
			}
			System.out.println(currentBoard);
			try {
				Thread.currentThread().sleep(0000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		printResult();
	}
	
	public void expectiPlay(){
		
		System.out.println("Game start!");
		System.out.println(currentBoard);
		
		while(!currentBoard.isComplete()){
			c = new Counter();
			start = System.currentTimeMillis();
			currentBoard = Strategy.expectiMinimax(currentBoard, c);
			end = System.currentTimeMillis();
			if(c.flag)timesOfSabotage++;
			if(currentBoard.currentPlayer == Color.blue){
				nodesExpandedByGreen += c.count;
				totalTimeGreen += (end - start);
			}else{
				nodesExpandedByBlue += c.count;
				totalTimeBlue += (end - start);
			}
			System.out.println(currentBoard);
			try {
				Thread.currentThread().sleep(000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		printResult();
		System.out.println("\ntimes of sabotage: " + timesOfSabotage);
	}
	
	public void printResult(){
		if((util = currentBoard.utilityForBlue()) > 0){
			System.out.println("blue win!\n");
		}else if((util = currentBoard.utilityForBlue()) < 0){
			System.out.println("green win!\n");
		}else{
			System.out.println("no one win!\n");
		}
		
		System.out.println("blue score: " + currentBoard.blueScore);
		System.out.println("nodes expanded: " + nodesExpandedByBlue);
		System.out.println("average nodes per move: " 
		+ nodesExpandedByBlue/(currentBoard.xlen * currentBoard.ylen/2));
		System.out.println("average time per move: " 
		+ totalTimeBlue/(currentBoard.xlen * currentBoard.ylen/2) + "ms\n");
		System.out.println("green score: " + currentBoard.greenScore);
		System.out.println("nodes expanded: " + nodesExpandedByGreen);
		System.out.println("average nodes per move: " 
		+ nodesExpandedByGreen/(currentBoard.xlen * currentBoard.ylen/2));
		System.out.println("average time per move: " 
		+ totalTimeGreen/(currentBoard.xlen * currentBoard.ylen/2) + "ms\n");
		System.out.println("total nodes: " + (nodesExpandedByBlue+nodesExpandedByGreen));
		System.out.println("total time: " + (totalTimeBlue+totalTimeGreen) + "ms");
	}
	
	public static void main(String[] args){
		Game g = new Game("src/Assignment22/MyBoard1.txt");
		g.minVSmin();
	}

}
