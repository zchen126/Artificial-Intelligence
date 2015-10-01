package Assignment12;

public class PuzzleState {
	
	String puzzle;
	int currentCost;
	int estimatedCost;
	PuzzleState parent;
	
	public PuzzleState(String puzzle, int cost){
		this.puzzle = puzzle;
		this.currentCost = cost;
	}
	
	public String toString(){
		return currentCost + "    " +estimatedCost;
	}

}
