package assignment3;

import java.util.ArrayList;

public class Digit {
	
	int xlen,ylen;
	char[][] digit;
	
	public Digit(ArrayList<String> input){
		
		xlen = input.get(0).length();
		ylen = input.size();
		
		digit = new char[xlen][ylen];
		
		for(int i = 0; i < xlen; i++){
			for(int j = 0; j < ylen; j++){
				digit[i][j] = input.get(j).charAt(i);
			}
		}
	}
	
	public String toString(){
		
		String s = "";
		
		for(int j = 0; j < ylen; j++){
			for(int i = 0; i < xlen; i++){
				s += digit[i][j];
			}
			s += "\n";
		}
		
		return s;
	}

}
