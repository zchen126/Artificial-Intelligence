package Assignment1;

import java.util.ArrayList;

public class State {
	
	Point p;
	State parent;
	int currentCost;
	int estimatedCost;
	ArrayList<Point> remainingDots;
	
	public void checkDots(){
		if(p.content == '.'){
			for(int i = 0; i < remainingDots.size(); i++){
				if((p.x == remainingDots.get(i).x)&&(p.y == remainingDots.get(i).y)){
					remainingDots.remove(i);
				}
			}
		}
	}
	
	public State(Point p){
		this.p = p;
	}
	
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		State s = (State)obj;
		if(this.p.x != s.p.x || this.p.y != s.p.y)
			return false;
		if(this.remainingDots.size() != s.remainingDots.size())
			return false;
		boolean result = true;
		for(int i = 0; i < this.remainingDots.size(); i++){
			if(this.remainingDots.get(i).x != s.remainingDots.get(i).x || this.remainingDots.get(i).y != s.remainingDots.get(i).y)
				result = false;
		}
		
		return result;
	}

	public String toString(){
		return currentCost + "    " + estimatedCost + p.toString();
	}
}
