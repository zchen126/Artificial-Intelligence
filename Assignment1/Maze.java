package Assignment1;

import java.io.*;
import java.util.*;

public class Maze {
	
	// some parameters
	// maze: the representation of a maze
	// start: start point
	// goal: goal point
	// xlen,ylen: the size of the maze
	// dots: all the dots in the maze, only used in 1.3
	// numOfDots: number of dots
	
	Point[][] maze;
	Point start;
	Point goal;
	int xlen,ylen;
	ArrayList<Point> dots;
	int numOfDots;
	
	public Maze(String fileName){
		getMaze(fileName);
	}
	
	// read the maze from a txt file
	
	public void getMaze(String fileName){
		
		//read a maze from a txt file
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
	    
	    //initialize the maze array
	    
	    xlen = stringList.get(0).length();
	    ylen = stringList.size();
	    maze = new Point[xlen][ylen];
	    dots = new ArrayList<Point>();
	    
	    for(int i = 0; i < xlen; i++){
	    	for(int j = 0; j < ylen; j++){
	    		maze[i][j] = new Point(i,j);
	    		maze[i][j].content = stringList.get(j).charAt(i);
	    		if(maze[i][j].content == 'P'){
	    			start = maze[i][j];
	    		}
	    		if(maze[i][j].content == '.'){
	    			goal = maze[i][j];
	    			dots.add(goal);
	    		}
	    	}
	    }
	    
	    numOfDots = dots.size();
	    // end of getMaze
	}
	
	// call dfs method, print the result if succeed
	
	public void dfs(){
		if(dfs(start.x,start.y,0,1)){
			System.out.print(this);
			System.out.println("succeed!");
		}
	}
	
	// do dfs search in the maze using recursion, and print the path
	// the order of expanding the node: right, left, up, down
	
	public boolean dfs(int x, int y, int steps,int NodesExpanded){
		
		boolean result = false;
		
		if(!isValid(x,y)){
			return result;
		}
		
		maze[x][y].visited = true;
		
		if((x == goal.x)&&(y == goal.y)){
			result = true;
			System.out.println("steps: " + steps);
			System.out.println("NodesExpanded: " + NodesExpanded);
		}
		
		//check left node
		if(!result){
			NodesExpanded++;
			result = dfs(x+1,y,steps+1,NodesExpanded);
			}
		// check right node
		if(!result){
			NodesExpanded++;
			result = dfs(x-1,y,steps+1,NodesExpanded);
		}
		// check up node
		if(!result){
			NodesExpanded++;
			result = dfs(x,y+1,steps+1,NodesExpanded);
		}
		// check down node
		if(!result){
			NodesExpanded++;
			result = dfs(x,y-1,steps+1,NodesExpanded);
		}
		
		if(result)maze[x][y].content = '.';
		
		return result;
	}
	
	// call bfs method, print the result if succeed
	
	public void bfs(){
		if(bfs(start)){
			System.out.println("Succeed!");
			System.out.println(this);
		}
	}
	
	// do bfs search in the maze, and print the path
	
	public boolean bfs(Point start){
		
		boolean result = false;
		
		LinkedList<Point> frontier = new LinkedList<Point>();
		
		// check if the node is our goal, if it is, return true, if not, add to frontier
		
		start.visited = true;
		if(start == goal){
			result = true;
			return result;
		}
		frontier.add(start);
		
		int NodesExpanded = 0;
		while(frontier.size() > 0){
			Point temp = frontier.poll();
			NodesExpanded++;

			int x = temp.x;
			int y = temp.y;
			if(isValid(x+1,y)){
				maze[x+1][y].visited = true;
				maze[x+1][y].parent = temp;
				if(maze[x+1][y] == goal){
					result = true;
					break;
				}
				frontier.add(maze[x+1][y]);
			}
			if(isValid(x-1,y)){
				maze[x-1][y].visited = true;
				maze[x-1][y].parent = temp;
				if(maze[x-1][y] == goal){
					result = true;
					break;
				}
				frontier.add(maze[x-1][y]);
			}
			if(isValid(x,y+1)){
				maze[x][y+1].visited = true;
				maze[x][y+1].parent = temp;
				if(maze[x][y+1] == goal){
					result = true;
					break;
				}
				frontier.add(maze[x][y+1]);
			}
			if(isValid(x,y-1)){
				maze[x][y-1].visited = true;
				maze[x][y-1].parent = temp;
				if(maze[x][y-1] == goal){
					result = true;
					break;
				}
				frontier.add(maze[x][y-1]);
			}
		}
		
		
		// print the path
		
		Point temp = goal;
		int count = -1;
		while(temp != null){
			temp.content = '.';
			count++;
			temp = temp.parent;
		}
		System.out.println("Nodes Expanded: " + NodesExpanded);
		System.out.println("path cost: " + count);
		
		return result;
		
	}
	
	// call greedy method, print the result if succeed
	
	public void greedy(){
		if(greedy(start)){
			System.out.println("Succeed!");
			System.out.println(this);
		}
	}
	
	// do greedy bfs in the maze, and print the path
	
	public boolean greedy(Point start){
		
		boolean result = false;
		
		// use a priority queue to store the nodes expanded, using manhattan distance as comparator
		
		Comparator<Point> order = new Comparator<Point>(){
			public int compare(Point p1, Point p2){
				int n1 = p1.distanceToGoal;
				int n2 = p2.distanceToGoal;
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
		
		PriorityQueue<Point> frontier = new PriorityQueue<Point>(11,order);
		
		heuristic(start,goal);
		
		frontier.add(start);
		
		int NodesExpanded = 0;
		while(frontier.size() > 0){
			Point temp = frontier.poll();
			NodesExpanded++;
			
			temp.visited = true;
			if(temp == goal){
				result = true;
				break;
			}

			int x = temp.x;
			int y = temp.y;
			
			// expand the node and keep track of path
			
			if(isValid(x+1,y)){
				heuristic(maze[x+1][y],goal);
				maze[x+1][y].parent = temp;
				if(!frontier.contains(maze[x+1][y])){
				frontier.add(maze[x+1][y]);}
			}
			if(isValid(x-1,y)){
				heuristic(maze[x-1][y],goal);
				maze[x-1][y].parent = temp;
				if(!frontier.contains(maze[x-1][y])){
				frontier.add(maze[x-1][y]);}
			}
			if(isValid(x,y+1)){
				heuristic(maze[x][y+1],goal);
				maze[x][y+1].parent = temp;
				if(!frontier.contains(maze[x][y+1])){
				frontier.add(maze[x][y+1]);}
			}
			if(isValid(x,y-1)){
				heuristic(maze[x][y-1],goal);
				maze[x][y-1].parent = temp;
				if(!frontier.contains(maze[x][y-1])){
				frontier.add(maze[x][y-1]);}
			}
		}
		
		// print the path
		
		Point temp = goal;
		int count = -1;
		while(temp != null){
			temp.content = '.';
			count++;
			temp = temp.parent;
		}
		System.out.println("Nodes Expanded: " + NodesExpanded);
		System.out.println("path cost: " + count);
		
		return result;
		
	}
	
	// call astar method, print the result if succeed
	
	public void astar(){
		if(astar(new State(start), goal)){
			System.out.println("Succeed!");
			System.out.println(this);
		}
	}
	
	// do astar search in the maze, and print the path
	
	public boolean astar(State start, Point goal){
		
		boolean result = false;
		
		// // use a priority queue to store the nodes expanded, using manhattan distance and current cost as comparator
		
		Comparator<State> order = new Comparator<State>(){
			public int compare(State s1, State s2){
				int n1 = s1.p.distanceToGoal+s1.currentCost;
				int n2 = s2.p.distanceToGoal+s2.currentCost;
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
		
		PriorityQueue<State> frontier = new PriorityQueue<State>(11,order);
		
		heuristic(start.p,goal);
		start.currentCost = 0;
		
		frontier.add(start);
		
		int NodesExpanded = 0;
		State end = null;
		while(frontier.size() > 0){
			State temp = frontier.poll();
			if(temp.p.visited){
				continue;
			}
			NodesExpanded++;
			
			temp.p.visited = true;
			if(temp.p == goal){
				result = true;
				end = temp;
				break;
			}

			int x = temp.p.x;
			int y = temp.p.y;
			
			if(isValid(x+1,y)){
				heuristic(maze[x+1][y],goal);
				State s = new State(maze[x+1][y]);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				frontier.add(s);
			}
			if(isValid(x-1,y)){
				heuristic(maze[x-1][y],goal);
				State s = new State(maze[x-1][y]);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				frontier.add(s);
			}
			if(isValid(x,y+1)){
				heuristic(maze[x][y+1],goal);
				State s = new State(maze[x][y+1]);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				frontier.add(s);
			}
			if(isValid(x,y-1)){
				heuristic(maze[x][y-1],goal);
				State s = new State(maze[x][y-1]);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				frontier.add(s);
			}
		}
		
		// print the path
		
		int count = -1;
		while(end != null){
			end.p.content = '.';
			count++;
			end = end.parent;
		}
		System.out.println("Nodes Expanded: " + NodesExpanded);
		System.out.println("path cost: " + count);
		
		return result;
		
	}
	
	// call pacman method, print the path if succeed
	
	public void pacman(){
		if(pacman(new State(start))){
			System.out.println("Succeed!");
			System.out.println(this);
		}
	}
	
	// use a suboptimal algorithm to solve the medium and big input
	
	public void stupidPacman(){
		
		Point current = start;
		int cost = 0;
		Point next = null;
		
		while(dots.size() > 0){
			
			State currentState = new State(current);
			int distance = pacman(currentState,dots.get(0));
			next = dots.get(0);
			
			for(Point dot : dots){
				int d = pacman(currentState,dot);
				if(d < distance){
					distance = d;
					next = dot;
				}
			}
			
			cost = cost + distance;
			current = next;
			dots.remove(current);
			
		}
		
		System.out.println("Path Cost: " + cost);
		System.out.println("Nodes Expanded: " + numOfDots);
	}
	
	// use an admissible heuristic function to solve the maze, return true if succeed
	
	public boolean pacman(State start){
		
		boolean result = false;
		
		// use a priority queue to store the nodes
		
		Comparator<State> order = new Comparator<State>(){
			public int compare(State s1, State s2){
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
		
		PriorityQueue<State> frontier = new PriorityQueue<State>(11,order);
		
		// initialize the start state, and put it into the priority queue
		
		start.currentCost = 0;
		start.remainingDots = (ArrayList<Point>) dots.clone();
		start.checkDots();
		heuristic(start);
		
		frontier.add(start);
		
		int NodesExpanded = 0;
		State end = null;
		ArrayList<State> close = new ArrayList<State>();
		
		while(frontier.size() > 0){
			State temp = frontier.poll();
			if(close.contains(temp))continue;

			NodesExpanded++;
			
			// if reach the goal state, break
			
			if(temp.remainingDots.size() == 0){
				result = true;
				end = temp;
				break;
			}
			
			close.add(temp);

			int x = temp.p.x;
			int y = temp.p.y;
			
			if(isPath(x+1,y)){
				State s = new State(maze[x+1][y]);
				s.remainingDots = (ArrayList<Point>) temp.remainingDots.clone();
				s.checkDots();
				heuristic(s);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				if(!frontier.contains(s))frontier.add(s);
			}
			if(isPath(x-1,y)){
				State s = new State(maze[x-1][y]);
				s.remainingDots = (ArrayList<Point>) temp.remainingDots.clone();
				s.checkDots();
				heuristic(s);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				if(!frontier.contains(s))frontier.add(s);
			}
			if(isPath(x,y+1)){
				State s = new State(maze[x][y+1]);
				s.remainingDots = (ArrayList<Point>) temp.remainingDots.clone();
				s.checkDots();
				heuristic(s);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				if(!frontier.contains(s))frontier.add(s);
			}
			if(isPath(x,y-1)){
				State s = new State(maze[x][y-1]);
				s.remainingDots = (ArrayList<Point>) temp.remainingDots.clone();
				s.checkDots();
				heuristic(s);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				if(!frontier.contains(s))frontier.add(s);
			}
		}
		
		int count = -1;
		
		while(end != null){
			count++;
			System.out.println(end);
			if(end.p.content != ' ' && end.p.content != 'P'){
				int path = numOfDots - end.remainingDots.size();
				end.p.content = path > 9 ? (char)(path - 10 + 'a') : (char)(path + '0');
			}
			end = end.parent;
		}
		System.out.println("Nodes Expanded: " + NodesExpanded);
		System.out.println("path cost: " + count);
		
		return result;
		
	}
	
	// use A* search to compute the distance between current position and goal position
	
	public int pacman(State start, Point goal){
				
		Comparator<State> order = new Comparator<State>(){
			public int compare(State s1, State s2){
				int n1 = s1.p.distanceToGoal+s1.currentCost;
				int n2 = s2.p.distanceToGoal+s2.currentCost;
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
		
		PriorityQueue<State> frontier = new PriorityQueue<State>(11,order);
		
		heuristic(start.p,goal);
		start.currentCost = 0;
		
		frontier.add(start);
		
		State end = null;
		while(frontier.size() > 0){
			State temp = frontier.poll();
			if(temp.p.visited){
				continue;
			}
			
			temp.p.visited = true;
			if(temp.p == goal){
				end = temp;
				break;
			}

			int x = temp.p.x;
			int y = temp.p.y;
			
			if(isValid(x+1,y)){
				heuristic(maze[x+1][y],goal);
				State s = new State(maze[x+1][y]);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				frontier.add(s);
			}
			if(isValid(x-1,y)){
				heuristic(maze[x-1][y],goal);
				State s = new State(maze[x-1][y]);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				frontier.add(s);
			}
			if(isValid(x,y+1)){
				heuristic(maze[x][y+1],goal);
				State s = new State(maze[x][y+1]);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				frontier.add(s);
			}
			if(isValid(x,y-1)){
				heuristic(maze[x][y-1],goal);
				State s = new State(maze[x][y-1]);
				s.currentCost = temp.currentCost+1;
				s.parent = temp;
				frontier.add(s);
			}
		}
		
		int count = -1;
		while(end != null){
			count++;
			end = end.parent;
		}
		
		setUnvisited();
		return count;
				
	}
	
	// check if the point is an available path, not wall or out of bound
	
	public boolean isValid(int x, int y){
		boolean result = false;
		
		if(x>=0 && x<xlen && y>=0 && y<ylen){
			if((maze[x][y].content != '%')&&(!maze[x][y].visited)){
				result = true;
			}
		}
		
		return result;
	}
	
	// set all the point in the maze to be unvisited, so I can do the search again
	
	public void setUnvisited(){
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				maze[i][j].visited = false;
			}
		}
	}
	
	// to check if the point is an available path, I don't care if it is visited or not
	
	public boolean isPath(int x, int y){
		
		boolean result = false;
		
		if(x>=0 && x<xlen && y>=0 && y<ylen){
			if(maze[x][y].content != '%'){
				result = true;
			}
		}
		
		return result;
		
	}
	
	// manhattan distance
	
	public void heuristic(Point p, Point g){
		int distance = Math.abs(p.x-g.x) + Math.abs(p.y-g.y);
		p.distanceToGoal = distance;
	}
	
	// the max distance of all the dots
	
	public void heuristic(State s){
		
		if(s.remainingDots.size() == 0){
			s.estimatedCost = 0;
			return;
		}
		int distance = pacman(s,s.remainingDots.get(0));
		for(Point dot : s.remainingDots){
			distance = Math.max(distance, pacman(s,dot));
		}
		s.estimatedCost = distance ;
		
	}
	
	public String toString(){
		int i,j;
		String s = "";
		StringBuffer buffer = new StringBuffer();
		
		for(j = 0; j < ylen; j++){
			for(i = 0; i < xlen; i++){
				buffer.append(maze[i][j].content);
			}
			buffer.append('\n');
		}
		
		s = buffer.toString();
		return s;
	}
	
	// solution of question 1.1
	
	public static void question11(String mazeName){
		Maze maze = new Maze(mazeName);
		System.out.println("dfs:");
		maze.dfs();
		
		maze = new Maze(mazeName);
		System.out.println("bfs:");
		maze.bfs();
		
		maze = new Maze(mazeName);
		System.out.println("greedy:");
		maze.greedy();
		
		maze = new Maze(mazeName);
		System.out.println("astar:");
		maze.astar();
	}
	
	// solution of question 1.2
	
	public static void question12(String mazeName){
		
		Maze maze = new Maze(mazeName);
		System.out.println("greedy:");
		maze.greedy();
		
		maze = new Maze(mazeName);
		System.out.println("astar:");
		maze.astar();
	}
	
	public static void main(String[] args){
		
		long start = System.currentTimeMillis();
		Maze maze = new Maze("trickysearch.txt");
		maze.pacman();
		long end = System.currentTimeMillis();
		System.out.println(end - start + "ms");
		
	}

}
