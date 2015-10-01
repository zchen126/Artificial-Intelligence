package Assignment12;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

public class Puzzle {
		
	String start = "";
	String goal  = "_12345678";
	
	// randomly shuffle 100-200 times
	
	public Puzzle(){
		start = "_12345678";
		Random r = new Random();
		int num = r.nextInt(100) + 100;
		for(int i = 0; i < num; i++){
			start = shuffle(start);
		}
	}
	
	// do a available shuffle
	
	public String shuffle(String s){
		boolean available = false;
		String result = "";
		
		while(!available){
			Random r = new Random();
			int num = r.nextInt(4) + 1;
			
			switch(num){
			case 1:
				available = up(s);
				if(available)result = moveUp(s);
				break;
			case 2:
				available = down(s);
				if(available)result = moveDown(s);
				break;
			case 3:
				available = left(s);
				if(available)result = moveLeft(s);
				break;
			case 4:
				available = right(s);
				if(available)result = moveRight(s);
				break;
			default:
				break;
			}
		}
		
		return result;
	}
	
	// call astar(), if succeed, print "succeed!"
	
	public void astar(int h){
		if(astar(new PuzzleState(start,0),goal,h)){
			System.out.println("Succeed!");
		}
	}
	
	// given a start state, goal state, heuristic function, do an A* search
	
	public boolean astar(PuzzleState start, String goal, int h){
		
		boolean result = false;
		
		Comparator<PuzzleState> order = new Comparator<PuzzleState>(){
			public int compare(PuzzleState s1, PuzzleState s2){
				int n1 = s1.estimatedCost+s1.currentCost;
				int n2 = s2.estimatedCost+s2.currentCost;
				if(n1 < n2){
					return -1;
				}
				else if(n1 == n2){
					return 0;
				}else{
					return 1;
				}
			}
		};
		
		PriorityQueue<PuzzleState> frontier  = new PriorityQueue<PuzzleState>(11,order);
		HashSet<String> closeSet = new HashSet<String>();
		
		h1(start);
		frontier.add(start);
		
		int NodesExpanded = 0;
		PuzzleState end = null;
		
		while(frontier.size() > 0){
			PuzzleState temp = frontier.poll();
			
			if(temp.puzzle.equals(goal)){
				end = temp;
				result = true;
				break;
			}
			
			if(closeSet.contains(temp.puzzle))continue;
			
			NodesExpanded++;
			
			closeSet.add(temp.puzzle);
			
			if(up(temp)){
				PuzzleState next = moveUp(temp);
				switch(h){
				case 1:
					h1(next);
					break;
				case 2:
					h2(next);
					break;
				case 3:
					h3(next);
					break;
				default:
					break;
				}
				next.parent = temp;
				frontier.add(next);
			}
			
			if(down(temp)){
				PuzzleState next = moveDown(temp);
				switch(h){
				case 1:
					h1(next);
					break;
				case 2:
					h2(next);
					break;
				case 3:
					h3(next);
					break;
				default:
					break;
				}
				next.parent = temp;
				frontier.add(next);
			}
			
			if(left(temp)){
				PuzzleState next = moveLeft(temp);
				switch(h){
				case 1:
					h1(next);
					break;
				case 2:
					h2(next);
					break;
				case 3:
					h3(next);
					break;
				default:
					break;
				}
				next.parent = temp;
				frontier.add(next);
			}
			
			if(right(temp)){
				PuzzleState next = moveRight(temp);
				switch(h){
				case 1:
					h1(next);
					break;
				case 2:
					h2(next);
					break;
				case 3:
					h3(next);
					break;
				default:
					break;
				}
				next.parent = temp;
				frontier.add(next);
			}
			
		}
		
		int count = -1;
		
		while(end != null){
			count++;
			end = end.parent;
		}
		
		System.out.println("Nodes Expanded: " + NodesExpanded);
		System.out.println("path cost: " + count);
		
		return result;
	}
	
	// misplace heuristic function
	
	public void h1(PuzzleState ps){
		
		int count  = 0;
		
		for(int i = 1; i < 9 ;i++){
			if(ps.puzzle.charAt(i) != goal.charAt(i)){
				count ++;
			}
		}
		
		ps.estimatedCost = count;
	}
	
	// sum of manhattan distance
	
	public void h2(PuzzleState ps){
		
		int count = 0;
		int rightx=0,righty=0,wrongx=0,wrongy=0;
		
		for(int i = 1; i <= 8; i++){
			
			rightx = i%3;
			righty = i/3;
			
			for(int j = 0; j < 9; j++){
				if(ps.puzzle.charAt(j) == goal.charAt(i)){
					wrongx = j%3;
					wrongy = j/3;
				}
			}
			
			count = count + Math.abs(wrongx-rightx)+Math.abs(wrongy-righty);
		}
		
		ps.estimatedCost = count;
		
	}
    
	// Gaschnig heuristic function
	// everytime switch the blank tile with a tile which is suppose to be in that place
	// if blank tile is in the right place, randomly switch it with a wrong-placed tile
	
	public void h3(PuzzleState ps){
		
		StringBuffer temp = new StringBuffer(ps.puzzle);
		
		// for convenience, replace '_' by '0'
		
		for(int i =0; i < 9; i++){
			if(temp.charAt(i) == '_'){
				temp.setCharAt(i, '0');
			}
		}
		
		int[] current = new int[9];
		int[] order = new int[9];
		
		// tranform from character to integer

		for(int i =0; i < 9; i++){
			current[i] = temp.charAt(i) - '0';
		}
		
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(current[j] == i){
					order[i] = j;
				}
			}
		}
		
		boolean judge = true;
		for(int i = 0; i < 9; i++){
			if(current[i] != i){
				judge = false;
			}
		}
		if(judge){
			ps.estimatedCost = 0;
			return;
		}
		
		int count = 0;
		
		while(true){
			
			if(current[0] != 0){
				int t = current[order[0]];
				current[order[0]] = current[order[order[0]]];
				current[order[order[0]]] = t;
			}else{
				for(int k = 1; k < 9; k++){
					if(current[k] != k){
						int t = current[k];
						current[k] = 0;
						current[0] = t;
						break;
					}
				}
			}
			
			for(int i = 0; i < 9; i++){
				for(int j = 0; j < 9; j++){
					if(current[j] == i){
						order[i] = j;
					}
				}
			}
			
			count++;
			
			judge = true;
			for(int i = 0; i < 9; i++){
				if(current[i] != i){
					judge = false;
				}
			}
			if(judge){
				break;
			}
			
		}
		
		ps.estimatedCost = count;
		
	}
	
	public boolean up(PuzzleState ps){
		
		return up(ps.puzzle);
	}
	
	// check if it is possible to move the blank tile up
	
	public boolean up(String s){
		boolean result = false;
		int in = s.indexOf("_");
		if(in > 2)result = true;
		return result;
	}
	
	public PuzzleState moveUp(PuzzleState ps){
		
		String content;
		PuzzleState result;
		
		content = moveUp(ps.puzzle);
		result = new PuzzleState(content,ps.currentCost+1);
		return result;
	}
	
	// move the blank tile up
	
	public String moveUp(String s){
		String result;
		int in = s.indexOf("_");
		result = s.substring(0, in-3) + "_" + s.substring(in-2, in) + s.substring(in-3,in-2) + s.substring(in+1);
		return result;
	}
	
	// check if it is possible to move the blank tile down
	
	public boolean down(PuzzleState ps){
		
		return down(ps.puzzle);
	}
	
	public boolean down(String s){
		boolean result = false;
		int in = s.indexOf("_");
		if(in < 6)result = true;
		return result;
	}
	
	// move the blank tile down
	
	public PuzzleState moveDown(PuzzleState ps){
		
		String content;
		PuzzleState result;
		
		content = moveDown(ps.puzzle);
		result = new PuzzleState(content,ps.currentCost+1);
		return result;
	}
	
	public String moveDown(String s){
		String result;
		int in = s.indexOf("_");
		result = s.substring(0, in) + s.substring(in+3, in+4) + s.substring(in+1,in+3) + "_" + s.substring(in+4);
		return result;
	}
	
	// check if it is possible to move the blank tile left
	
	public boolean left(PuzzleState ps){

		return left(ps.puzzle);
	}
	
	public boolean left(String s){
		boolean result = false;
		int in = s.indexOf("_");
		if(in != 0 && in != 3 && in != 6)result = true;
		return result;
	}
	
	// move the blank tile left
	
	public PuzzleState moveLeft(PuzzleState ps) {
		
		String content;
		PuzzleState result;
		
		content = moveLeft(ps.puzzle);
		result = new PuzzleState(content,ps.currentCost+1);
		return result;
	}
	
	public String moveLeft(String s){
		String result;
		int in = s.indexOf("_");
		result = s.substring(0, in-1) + "_" + s.substring(in-1, in) + s.substring(in+1);
		return result;
	}
	
	// check if it is possible to move the blank tile right
	
	public boolean right(PuzzleState ps){

		return right(ps.puzzle);
	}
	
	public boolean right(String s){
		boolean result = false;
		int in = s.indexOf("_");
		if(in != 2 && in != 5 && in != 8)result = true;
		return result;
	}
	
	// move the blank tile right
	
	public PuzzleState moveRight(PuzzleState ps) {
		
		String content;
		PuzzleState result;
		
		content = moveRight(ps.puzzle);
		result = new PuzzleState(content,ps.currentCost+1);
		return result;
	}
	
	public String moveRight(String s){
		String result;
		int in = s.indexOf("_");
		result = s.substring(0, in) + s.substring(in+1, in+2) + "_" + s.substring(in+2);
		return result;
	}
	
	public static void main(String[] args){
		
		Puzzle p = new Puzzle();
		p.astar(1);
		p.astar(2);
		p.astar(3);
		
	}
}
