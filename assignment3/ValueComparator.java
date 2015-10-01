package assignment3;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class ValueComparator implements Comparator<Map.Entry<String,Double>>{

	@Override
	public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
		// TODO Auto-generated method stub
		
		double c = o2.getValue() - o1.getValue();
		
		if(c < 0){
			return -1;
		}else if(c > 0){
			return 1;
		}else{
			return 0;
		}
	}

}
