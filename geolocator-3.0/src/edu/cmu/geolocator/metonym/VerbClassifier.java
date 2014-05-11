package edu.cmu.geolocator.metonym;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class VerbClassifier {
	private Map<Number, ArrayList<String>> classes = new HashMap<Number, ArrayList<String>>();
	private String levinVerbf = "levin.verbs.txt";
	public VerbClassifier(String metVerbf, String litVerbf){
		try {
			BufferedReader levinVerbReader = new BufferedReader(new FileReader(levinVerbf));
			BufferedReader metVerbReader = new BufferedReader(new FileReader(metVerbf));
			BufferedReader litVerbReader = new BufferedReader(new FileReader(litVerbf));
			String line = "";
			while((line = levinVerbReader.readLine()) != null){
				ArrayList<String> templist = new ArrayList<String>();
				String name = line.substring(0, line.indexOf(','));
				while(line.indexOf(',') >= 0){
					line = line.substring(line.indexOf(',') + 1);
					if(line.indexOf(',') >= 0){
						templist.add(line.substring(0, line.indexOf(',')));
					}else{
						templist.add(line);
					}
				}
				classes.put(new Number(name, 0), templist);
			}
			while((line = metVerbReader.readLine()) != null){
				for(Entry<Number, ArrayList<String>> entry : classes.entrySet()){
					for(String str : entry.getValue()){
						if(str.equalsIgnoreCase(line)){
							entry.getKey().num += 1;
							break;
						}
					}
				}
			}
			while((line = litVerbReader.readLine()) != null){
				for(Entry<Number, ArrayList<String>> entry : classes.entrySet()){
					for(String str : entry.getValue()){
						if(str.equalsIgnoreCase(line)){
							entry.getKey().num -= 1;
							break;
						}
					}
				}
			}
			levinVerbReader.close();
			metVerbReader.close();
			litVerbReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(classes.keySet().toString());
	}
	
	public double classify(String verb){
		for(Entry<Number, ArrayList<String>> entry : classes.entrySet()){
			for(String str : entry.getValue()){
				if(str.equalsIgnoreCase(verb)){
					if(entry.getKey().num >= 1){
						return 1.0/2.0;
					}else if(entry.getKey().num >= 2){
						return 1.0;
					}else if(entry.getKey().num <= -1){
						return -1.0/2.0;
					}else if(entry.getKey().num <= -2){
						return -1.0;
					}else{
						return 0;
					}
				}
			}
		}
		return 0;
	}
	public static void main(String[] args){
		VerbClassifier vc = new VerbClassifier("metverblemmas", "litverblemmas");
		for(Entry<Number, ArrayList<String>> entry : vc.classes.entrySet()){
			if(entry.getKey().num > 0)
				System.out.println(entry.getKey().category + "(" + entry.getKey().num + "): " + entry.getValue().toString());
		}
	}
}
