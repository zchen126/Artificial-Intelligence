package assignment3;

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
	    
//	    System.out.println(stringList.size());
	    
	    return stringList;
	}
	
	public static void main(String[] args) throws IOException{
		Reader r = new Reader();
		ArrayList<String> labels = r.read("test.label");
		ArrayList<String> data = r.read("test.data");
		
		FileWriter fw = new FileWriter("test20.txt");
		for(int i = 1; i <= labels.size(); i++){
			int label = Integer.parseInt(labels.get(i-1));
			String doc = "" + (label-1);
			while(!data.isEmpty()){
				String s = data.get(0);
				String[] temp = s.split(" ");
				if(Integer.parseInt(temp[0]) != i){
					break;
				}
				data.remove(0);
				String oneWord = " " + temp[1] + ":" + temp[2];
				doc = doc + oneWord;
			}
			doc = doc + "\r\n";
			fw.write(doc);
			System.out.println("doc " + i + "  finished");
		}
		fw.close();
	}

}
