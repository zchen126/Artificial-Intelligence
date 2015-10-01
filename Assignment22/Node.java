package Assignment22;

import java.awt.Color;

public class Node {
	
	/*
	 * parameter:
	 * content: the number of resources in that point
	 * color: white means not occupied, blue means occupied by player blue, green means 
	 * occupied by player green
	 */
	
	int content;
	Color color;
	
	public Node(int content){
		this.content = content;
		color = Color.white;
	}

}
