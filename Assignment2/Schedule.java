package Assignment2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Schedule {
	
	/* 
	 * parameter:
	 * numOfCourses: number of the courses that are provided
	 * minCredit: minimum credit hours each semester
	 * maxCredit: maximum credit hours each semester
	 * numOfInterest: number of courses that MC is interested in
	 * budget: MC's budget of the program
	 * cost: actual cost
	 * maxSemesters: the max number of semesters that MC needs to take
	 * courseList: the courses that are provided, in the second index, 0 represents fall price,
	 * 1 represents spring price, 2 represents credit hours, 3 is priority of this course, for example,
	 * if the value is 3, this course can only be assigned to semester 3 or later; 4 is the number of
	 * courses that have this course as prerequisite
	 * isPrerequisite: a table that stores the prerequisite relationship, if course A is a prerequisite
	 * course of B, then isPrerequisite[A][B] = true
	 * interestedList: the courses that MC is interested in
	 * basicList: interested courses and prerequisite courses of interested courses
	 * assignment: the index represents the course, the content represents the semester
	 * the output that is required
	 * bestAssignment: the best assignment
	 */
	
	int numOfCourses;
	int minCredit;
	int maxCredit;
	int numOfInterest;
	int budget;
	int cost;
	int maxSemesters;
	int[][] courseList;
	boolean[][] isPrerequisite;
	boolean[] hasPrerequisite;
	int[] interestedList;
	int[] basicList;
	int[] assignment;
	int[] bestAssignment;
	
	// get all the information from the input file
	
	private void getInformation(String fileName){
		
		// read the information from input file, and transform into an arraylist
		
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
	    
	    // read the first line
	    
	    String s = stringList.get(0);
	    String[] numberList = s.split(" ");
	    numOfCourses = Integer.parseInt(numberList[0]);
	    minCredit = Integer.parseInt(numberList[1]);
	    maxCredit = Integer.parseInt(numberList[2]);
	    
	    stringList.remove(0);
	    
	    courseList = new int[numOfCourses+1][5];
	    isPrerequisite = new boolean[numOfCourses+1][numOfCourses+1];
	    hasPrerequisite = new boolean[numOfCourses+1];
	    assignment = new int[numOfCourses+1];
	    
	    // read the information of all the courses
	    
	    for(int i = 0; i < numOfCourses; i++){
	    	s = stringList.get(0);
	    	numberList = s.split(" ");
	    	courseList[i+1][0] = Integer.parseInt(numberList[0]);
	    	courseList[i+1][1] = Integer.parseInt(numberList[1]);
	    	courseList[i+1][2] = Integer.parseInt(numberList[2]);
	    	stringList.remove(0);
	    }
	    
	    // read the prerequisites of all the courses
	    
	    for(int i = 1; i <= numOfCourses; i++){
	    	s = stringList.get(0);
	    	numberList = s.split(" ");
	    	hasPrerequisite[i] = false;
	    	
	    	if((courseList[i][4] = Integer.parseInt(numberList[0])) != 0){
	    		hasPrerequisite[i] = true;
	    		for(int j = 1; j < numberList.length; j++){
	    			isPrerequisite[Integer.parseInt(numberList[j])][i] = true;
	    		}
	    	}

	    	stringList.remove(0);
	    }
	    
	    // get the course that MC is interested in 
	    
	    s = stringList.get(0);
	    numberList = s.split(" ");
	    numOfInterest = Integer.parseInt(numberList[0]);
	    interestedList = new int[numOfInterest];
	    for(int i = 0; i < numOfInterest; i++){
	    	interestedList[i] = Integer.parseInt(numberList[i+1]);
	    }
	    stringList.remove(0);
	    
	    // get the budget
	    
	    budget = Integer.parseInt(stringList.get(0));
		
	}
	
	// set the basic course list according to the interested list
	
	private void setBasicList(){
		
		ArrayList<Integer> temp = new ArrayList<Integer>();
		ArrayList<Integer> tlist = new ArrayList<Integer>();
		
		for(int i = 0; i < interestedList.length; i++){
			temp.add(interestedList[i]);
		}
		
		while(temp.size() > 0){
			int c = temp.remove(0);
			if(tlist.contains(c))continue;
			tlist.add(c);
			for(int i = 1; i <= numOfCourses; i++){
				if(isPrerequisite[i][c]){
					temp.add(i);
				}
			}
		}
		
		basicList = new int[tlist.size()];
		for(int i = 0; i < tlist.size(); i++){
			basicList[i] = tlist.get(i);
		}
		
	}

	private void computeSemesters(){
		
		int totalCredits = 0;
		for(int i = 0; i < basicList.length; i++){
			int credit = courseList[basicList[i]][2];
			totalCredits += credit;
		}
		
//		for(int i = 1; i <= numOfCourses; i++){
//			int credit = courseList[i][2];
//			totalCredits += credit;
//		}
	
		maxSemesters = totalCredits/minCredit + 1;
		
		int maxPriority = courseList[1][3] + 1;
		
		for(int i =1; i < numOfCourses; i++){
			maxPriority = Math.max(maxPriority, courseList[i][3] + 1);
		}
		
		maxSemesters = Math.max(maxPriority, maxSemesters);
		
	}
	
	public Schedule(String fileName){
		getInformation(fileName);
		setBasicList();
		computePriority();
		computeSemesters();
		computeTimesAsPre();
//		System.out.println(numOfCourses);
//		System.out.println(minCredit);
//		System.out.println(maxCredit);
//		System.out.println(budget);
		System.out.println(maxSemesters);
		print2dArray(courseList);
//		printArray(interestedList);
		printArray(basicList);
	}
	
	// call setSchedule, print the result
	
	public void setSchedule(){
//		if(setSchedule(assignment)){
//			printArray(assignment);
//			convertOutput(assignment);
//		}
		Counter c = new Counter();
		setSchedule(assignment,c);
		printArray(bestAssignment);
		convertOutput(bestAssignment);
		System.out.println("number of attempts: " + c.count);
	}
	
	// do backtracking research
	
	public boolean setSchedule(int[] assignment, Counter c){
		
//		if(checkComplete(assignment)){
//			return true;
//		}
		
		if(checkComplete(assignment)){
			if(cost == 0){
				cost = computeCost(assignment);
				bestAssignment = assignment.clone();
			}else{
				if(computeCost(assignment) < cost){
					cost = computeCost(assignment);
					bestAssignment = assignment.clone();
				}
			}
			return false;
		}
		
		if(checkAllAssigned(assignment)){
			return false;
		}
		
		if(creditsNotEnough(assignment)){
			return false;
		}
		
		if(computeCost(assignment) > budget)return false;
	
		if(computeCost(assignment) > cost && cost != 0)return false;
		
		int nextCourseToAssign = selectUnassignedCourse(assignment);
		
		ArrayList<Integer> domain = computeDomain(assignment, nextCourseToAssign);
		
		for(int i : domain){
			if(satisfyConstraints(assignment, nextCourseToAssign, i)){
				c.count++;
				assignment[nextCourseToAssign] = i;
				boolean result = setSchedule(assignment, c);
//				if(result)return true;
				assignment[nextCourseToAssign] = 0;
			}
		}
		
		return false;
	}
	
	// check if the assignment is complete
	
	public boolean checkComplete(int[] assignment){
		
		int semesters = assignment[1];
		
		// check if every required course is taken
		
		for(int n : basicList){
			if(assignment[n] == 0 || assignment[n] == -1){
				return false;
			}
		}
		
		for(int i = 1; i <= numOfCourses; i++){
			semesters = Math.max(semesters, assignment[i]);
		}
		
		// compute the credits in each semester
		
		int[] credits = new int[semesters+1];
		
		for(int i = 1; i <= numOfCourses; i++){
			for(int j = 1; j <= semesters; j++){
				if(assignment[i] == j){
					credits[j] += courseList[i][2];
					break;
				}
			}
		}
		
		// check the credits in each semester
		
		for(int j = 1; j <= semesters; j++){
			if(credits[j] < minCredit || credits[j] > maxCredit){
				return false;
			}
		}
		
		// check if every course is taken after prerequisite
		
		for(int i = 1; i <= numOfCourses; i++){
			if(assignment[i] > 0){
				for(int j = 1; j <= numOfCourses; j++){
					if(isPrerequisite[j][i] && (assignment[j] == 0 || assignment[j] >= assignment[i] || assignment[j] == -1)){
						return false;
					}
				}
			}
		}
		
//		if(computeCost(assignment) > budget)return false;
		
		return true;
	}
	
	/* choose course according to their priority level:
	 * first choose a course from basic list which has a maximum number of prerequisites
	 * if basic list is empty, choose a course from other list which has a maximum number of prerequisites
	 */
	
	public int selectUnassignedCourse(int[] assignment){
		
		int numOfPre = -1;
		int courseIndex = 0;
		
		for(int n : basicList){ 
			if(assignment[n] == 0){
				if(courseList[n][4] > numOfPre){
					numOfPre = courseList[n][4];
					courseIndex = n;
				}
			}
		}
		if(courseIndex != 0)return courseIndex;
		
		for(int i = 1; i <= numOfCourses; i++){
			if(assignment[i] == 0){
				if(courseList[i][4] > numOfPre){
					numOfPre = courseList[i][4];
					courseIndex = i;
				}
			}
		}
		if(courseIndex != 0)return courseIndex;
		
		if(courseIndex == 0){
			System.out.println("run out of courses!");
		}
		
		return 0;
	}
	
	
	// check if one assignment violate constraints 
	
	public boolean satisfyConstraints(int[] assignment, int course, int semester){
		
		if(semester == -1){
			for(int i = 1; i <= numOfCourses; i++){
				if(isPrerequisite[course][i] && assignment[i] > 0){
					return false;
				}
			}
			return true;
		}
		
		// check if the total credits in this semester exceed the max value
		
		int totalCredits = courseList[course][2];
		for(int i = 1; i <= numOfCourses; i++){
			if(assignment[i] == semester){
				totalCredits += courseList[i][2];
			}
		}
		if(totalCredits > maxCredit)return false;
		
		// if this course is already taken, return false
		
		if(assignment[course] > 0 || assignment[course] == -1)return false;
		
		// if this course doesn't have any prerequisites, return true
		
		if(!hasPrerequisite[course])return true;
		
		// check if all the prerequisite course is taken before this course or not assigned
		
		for(int i = 1; i <= numOfCourses; i++){
			if(isPrerequisite[i][course]){
				if(assignment[i] >= semester || assignment[i] == -1){
					return false;
				}
			}
		}
		
//		for(int i = 1; i <= numOfCourses; i++){
//			if(isPrerequisite[course][i]){
//				if(assignment[i] <= semester && assignment[i] != -1 && assignment[i] != 0){
//					return false;
//				}
//			}
//		}
		
		return true;
	}
	
	// compute the priority of one course
	
	public void computeTimesAsPre(){
		for(int i = 1; i <= numOfCourses; i++){
			int n = 0;
			for(int j = 1; j <= numOfCourses; j++){
				if(isPrerequisite[i][j])n++;
			}
			courseList[i][4] = n;
		}
	}
	
	// compute the domain an assigning variable
	
	public ArrayList<Integer> computeDomain(int[] assignment, int course){
		int upper = 100;
		int lower = 0;
		
		for(int i = 1; i <= numOfCourses; i++){
			if(assignment[i] != 0){
				if(isPrerequisite[i][course]){
					lower = Math.max(lower, assignment[i]+1);
				}
				if(isPrerequisite[course][i]){
					upper = Math.min(upper, assignment[i]-1);
				}
			}
		}
		
		upper = Math.min(upper, maxSemesters);
		lower = Math.max(lower, courseList[course][3]);
		
		ArrayList<Integer> odd = new ArrayList<Integer>();
		ArrayList<Integer> even = new ArrayList<Integer>();
		ArrayList<Integer> domain = new ArrayList<Integer>();
		
		for(int i = lower; i <= upper; i++){
			if(i%2 == 0){
				even.add(i);
			}else{
				odd.add(i);
			}
		}
		
		if(courseList[course][0] < courseList[course][1]){
			odd.addAll(even);
			domain = odd;
		}else{
			even.addAll(odd);
			domain = even;
		}
		
		boolean flag = false;
		for(int n : basicList){
			if(course == n)flag = true;
		}
		if(!flag)domain.add(-1);
		
		return domain;
	}
	
	public boolean creditsNotEnough(int[] assignment){
		
		int semesters = assignment[1];
		
		for(int i = 1; i <= numOfCourses; i++){
			semesters = Math.max(semesters, assignment[i]);
		}
		
		int totalCredits = 0;
		
		for(int i = 1; i <= numOfCourses; i++){
			if(assignment[i] != -1){
				totalCredits += courseList[i][2];
			}
		}
		
		if(totalCredits >= semesters * minCredit){
			return false;
		}else{
			return true;
		}
	}
	
	public int computePriority(int course){
		
		if(!hasPrerequisite[course])return 1;
		
		int priority = 0;
		for(int i = 1; i <= numOfCourses; i++){
			if(isPrerequisite[i][course]){
				priority = Math.max(priority, computePriority(i));
			}
		}
		
		return priority +1;
	}
	
	// compute the priority of all the courses
	
	public boolean checkAllAssigned(int[] assignment){

		for(int i = 1; i <= numOfCourses; i++){
			if(assignment[i] == 0){
				return false;
			}
		}
		
		return true;
	}
	
	public void computePriority(){
		
		for(int i = 1; i <= numOfCourses; i++){
			courseList[i][3] = computePriority(i);
		}
		
	}
	
	// compute the total cost of one assignment
	
	public int computeCost(int[] assignment){
		
		int cost = 0;
		
		for(int i = 1; i <= numOfCourses; i++){
			int price;
			if(assignment[i] != 0 && assignment[i] != -1){
				if(assignment[i] % 2 == 0){
					price = courseList[i][1];
				}else{
					price = courseList[i][0];
				}
				cost += price;
			}
		}
		
		return cost;
	}
	
	// convert output to a required format
	
	public void convertOutput(int[] assignment){
		
		ArrayList<String> result = new ArrayList<String>();
		
		int totalCost = computeCost(assignment);
		int semester = 0;
		for(int i = 1; i <= numOfCourses; i++){
			semester = Math.max(semester, assignment[i]);
		}
		
		Semester[] temp = new Semester[semester + 1];
		for(int i = 1; i <= semester; i++){
			temp[i] = new Semester();
		}
		
		for(int i = 1; i <= numOfCourses; i++){
			int price;
			if(assignment[i] != 0 && assignment[i] != -1){
				temp[assignment[i]].courses.add(i);
				if(assignment[i] % 2 == 0){
					price = courseList[i][1];
				}else{
					price = courseList[i][0];
				}
				temp[assignment[i]].cost += price;
			}
		}
		
		result.add(new String(totalCost + "  " + semester));
		
		String s = "";
		for(int i = 1; i <= semester; i++){
			result.add(temp[i].convertString());
			s = s + temp[i].cost + "  ";
		}
		
		result.add(s);
		
		for(int i = 0; i < result.size(); i++){
			System.out.println(result.get(i));
		}
	}
	
	public void printArray(int[] array){
		System.out.print("[ ");
		for(int i = 0; i < array.length; i++){
			System.out.print(array[i] + " ");
		}
		System.out.print("]\r\n");
	}
	
	public void print2dArray(int[][] array){
		for(int i = 0; i < array.length; i++){
			printArray(array[i]);
		}
	}
	
	public void printArray(boolean[] array){
		System.out.print("[ ");
		for(int i = 0; i < array.length; i++){
			System.out.print(array[i] + " ");
		}
		System.out.print("]\r\n");
	}
	
	public void print2dArray(boolean[][] array){
		for(int i = 0; i < array.length; i++){
			printArray(array[i]);
		}
	}
	
	public static void main(String[] args){
		
		Schedule s = new Schedule("src/Assignment2/fourthScenario.txt");
		double ss = System.currentTimeMillis();
		s.setSchedule();
		double ee = System.currentTimeMillis();
		System.out.println(ee-ss + "ms");
	}

}
