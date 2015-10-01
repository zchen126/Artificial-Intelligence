package Assignment2;

import java.util.ArrayList;

public class Semester {
	
	int cost;
	ArrayList<Integer> courses = new ArrayList<Integer>();
	
	public String convertString(){
		String s = "";
		s += courses.size();
		
		for(int i = 0; i < courses.size(); i++){
			s = s + "  " + courses.get(i);
		}
		
		return s;
	}

}
