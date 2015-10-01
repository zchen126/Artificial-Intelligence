package assignment4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
	
	public ArrayList<String> read(String fileName){
		
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
	    
//	    for(String s : stringList){
//	    	System.out.println(s);
//	    }
	    
	    return stringList;
	}
	
	public static void main(String[] args) throws IOException{
		Reader r = new Reader();
		r.read("src/assignment4/map1.txt");
	}

}
