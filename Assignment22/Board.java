package Assignment22;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Board {
	
	/*
	 * parameter:
	 * board: a representation of the game board
	 * xlen: the length in x direction
	 * ylen: the length in y direction
	 * currentPlayer: the player who is going to moving at this time
	 */
	
	Node[][] board;
	int xlen,ylen;
	Color currentPlayer;
	int blueScore,greenScore;
	
	public void getBoard(String fileName){
		
		File file = new File(fileName);
		BufferedReader reader = null;
	    String temp;
	    ArrayList<String> stringList = new ArrayList<String>();
	    
	    try{
	    	reader = new BufferedReader(new FileReader(file));
	    	while((temp = reader.readLine()) != null){
	    		stringList.add(temp);
	    	}
	    	reader.close();
	    }catch(FileNotFoundException e){
	    	e.printStackTrace();
	    }catch(IOException e){
	    	e.printStackTrace();
	    }finally{
	    	if(reader != null){
	    		try {
	                reader.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	    	}
	    }
	    
	    ylen = stringList.size();
	    String[] list = stringList.get(0).split("	");
	    xlen = list.length;
	    board = new Node[xlen][ylen];
	    
	    for (int i = 0; i < stringList.size(); i++){
	    	String[] numberList = stringList.get(i).split("	");
	    	for(int j = 0; j < numberList.length; j++){
	    		int n = Integer.parseInt(numberList[j]);
	    		board[j][i] = new Node(n);
	    	}
	    }
	}
	
	public Board(String fileName){
		getBoard(fileName);
		currentPlayer = Color.blue;
	}
	
	public Board(Board b){
		
		xlen = b.xlen;
		ylen = b.ylen;
		board = new Node[xlen][ylen];
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				board[i][j] = new Node(b.board[i][j].content);
				board[i][j].color = b.board[i][j].color;
			}
		}
		this.currentPlayer = b.currentPlayer;
	}
	
	// check if there are any nodes with the same color around
	
	public boolean isConnected(Color color, int x, int y){
		if(isValid(x+1,y)){
			 if(board[x+1][y].color == color)return true;
		}
		if(isValid(x-1,y)){
			 if(board[x-1][y].color == color)return true;
		}
		if(isValid(x,y+1)){
			 if(board[x][y+1].color == color)return true;
		}
		if(isValid(x,y-1)){
			 if(board[x][y-1].color == color)return true;
		}
		return false;
	}
	
	// check if there are any nodes with  the different color around
	
	public boolean isConnectedToEnemy(Color color, int x, int y){
		
		Color enemy = Color.blue;
		if(color == Color.blue)enemy = Color.green;
		boolean result = isConnected(enemy, x, y);
		
		return result;
	}
	
	// check if this position is in the range of the board
	
	public boolean isValid(int x, int y){
		if(x < 0 || x >= xlen || y < 0 || y >= ylen)return false;
		return true;
	}
	
	// check if the game is done
	
	public boolean isComplete(){
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(board[i][j].color == Color.white)return false;
			}
		}
		return true;
	}
	
	// evaluation function
	
	public int utilityForBlue(){
		
		int utility = 0;
		int blue = 0, green = 0;
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(board[i][j].color == Color.blue){
					blue += board[i][j].content;
				}else if(board[i][j].color == Color.green){
					green += board[i][j].content;
				}
			}
		}
		
		blueScore = blue;
		greenScore = green;
		
		utility = blue - green;
		
		return utility;
	}
	
	public void sabotageSuccess(Color myColor, int x, int y){
		
		deathBlitz(myColor, x, y);
		
	}
	
	public void sabotageFail(Color myColor, int x, int y){
		
		Color enemy = Color.blue;
		if(myColor == Color.blue)enemy = Color.green;
		
		board[x][y].color = enemy;
		
	}
	
	public void deathBlitz(Color myColor, int x, int y){
		
		Color enemy = Color.blue;
		if(myColor == Color.blue)enemy = Color.green;
		
		if(isValid(x+1,y)&&board[x+1][y].color == enemy)board[x+1][y].color = myColor;
		if(isValid(x-1,y)&&board[x-1][y].color == enemy)board[x-1][y].color = myColor;
		if(isValid(x,y+1)&&board[x][y+1].color == enemy)board[x][y+1].color = myColor;
		if(isValid(x,y-1)&&board[x][y-1].color == enemy)board[x][y-1].color = myColor;
	}
	
	public Board doOneMove(int x, int y){
		Board b = new Board(this);
		b.board[x][y].color = currentPlayer;
		if(b.isConnected(currentPlayer,x,y))b.deathBlitz(currentPlayer,x,y);
		if(currentPlayer == Color.blue){
			b.currentPlayer = Color.green;
		}else{
			b.currentPlayer = Color.blue;
		}
		return b;
	}
	
	public ArrayList<Board> expectiMove(int x, int y){
		ArrayList<Board> boards = new ArrayList<Board>();
		
		Board b = new Board(this);
		b.board[x][y].color = currentPlayer;
		if(b.isConnected(currentPlayer,x,y)){
			b.deathBlitz(currentPlayer,x,y);
			if(currentPlayer == Color.blue){
				b.currentPlayer = Color.green;
			}else{
				b.currentPlayer = Color.blue;
			}
			boards.add(b);
		}else if(b.isConnectedToEnemy(currentPlayer, x, y)){
			Board b1 = new Board(b);
			Board b2 = new Board(b);
			b1.sabotageSuccess(currentPlayer, x, y);
			b2.sabotageFail(currentPlayer, x, y);
			if(currentPlayer == Color.blue){
				b1.currentPlayer = Color.green;
				b2.currentPlayer = Color.green;
			}else{
				b1.currentPlayer = Color.blue;
				b2.currentPlayer = Color.blue;
			}
			boards.add(b1);
			boards.add(b2);
		}else{
			if(currentPlayer == Color.blue){
				b.currentPlayer = Color.green;
			}else{
				b.currentPlayer = Color.blue;
			}
			boards.add(b);
		}
		return boards;
	}
	
	public ArrayList<Board> expand(){
		
		ArrayList<Board> children = new ArrayList<Board>();
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(board[i][j].color == Color.white){
					children.add(doOneMove(i,j));
				}
			}
		}
		
		return children;
	}
	
	public ArrayList<ArrayList<Board>> expectiExpand(){
		
		ArrayList<ArrayList<Board>> children = new ArrayList<ArrayList<Board>>();
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(board[i][j].color == Color.white){
					children.add(expectiMove(i,j));
				}
			}
		}
		
		return children;
	}
	
	public ArrayList<Board> smartExpand(){
		
		ArrayList<Board> children = new ArrayList<Board>();
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				if(board[i][j].color == Color.white){
					if(children.size() == 0){
						children.add(doOneMove(i,j));
					}else{
						Board b = doOneMove(i,j);
						if(currentPlayer == Color.blue){
							if(b.utilityForBlue() > children.get(0).utilityForBlue()){
								children.add(0, b);
							}else{
								children.add(b);
							}
						}else{
							if(b.utilityForBlue() > children.get(0).utilityForBlue()){
								children.add(b);
							}else{
								children.add(0, b);
							}
						}
					}
				}
			}
		}
		
		return children;
	}
	
	public String toString(){
		
		String s = "";
		
		for(int i = 0; i < ylen; i++){
			for(int j = 0; j < xlen; j++){
				s = s + board[j][i].content;
				if(board[j][i].color == Color.white){
					s += "(     ) ";
				}else if(board[j][i].color == Color.green){
					s += "(-----) ";
				}else{
					s += "(+++++) ";
				}
			}
			s = s + "\r\n";
		}
		
		return s;
	}
	
	public static void main(String[] args){
		Board b = new Board("src/Assignment22/MyBoard1.txt");
		System.out.println(b);
		System.out.println(b.expand());
	}

}
