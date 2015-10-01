package Assignment1;

public class Point {
	
	int x;
	int y;
	
	char content;
	boolean visited = false;
	Point parent = null;
	int distanceToGoal;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public String toString(){
		String s = "";
		s = "(" + x + "," + y +")";
		return s;
	}

}
