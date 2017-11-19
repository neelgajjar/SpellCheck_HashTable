package neel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SpellingSuggest{
	private final HashMap<String, Integer> DBWords = new HashMap<String, Integer>();
	
	public SpellingSuggest(String file) throws IOException{
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			Pattern p = Pattern.compile("\\w+");
			for (String temp = "";temp!=null;temp = in.readLine()){
				Matcher m = p.matcher(temp.toLowerCase());
				while(m.find()){
					DBWords.put((temp = m.group()), DBWords.containsKey(temp)?DBWords.get(temp)+1 : 1);
				}
			}
			in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	private final ArrayList<String> edits (String word){
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0;i<word.length();++i){
			result.add(word.substring(0, i) + word.substring(i+1));
		}
		for(int i=0;i<word.length()-1;++i){
			result.add(word.substring(0,i)+word.substring(i+1,i+2)+word.substring(i,i+1)+ word.substring(i+2));
			
		}
		for(int i=0;i<word.length();++i){
			for(char c='a';c<='z';++c){
				result.add(word.substring(0,i)+String.valueOf(c)+word.substring(i+1));
			}
		}
		for(int i=0;i<word.length();++i){
			for(char c ='a';c<='z';++c){
				result.add(word.substring(0, i)+String.valueOf(c)+word.substring(i));
			}
		}
		return result;
	}
	
	public final String correct(String word){
		if(DBWords.containsKey(word)){
			return word;
		}
		ArrayList<String> list_edits = edits(word);
		HashMap<Integer,String> candidates = new HashMap<Integer,String>();
		for(String s: list_edits){
			if(DBWords.containsKey(s)){
				candidates.put(DBWords.get(s), s);
			}
		}
		if(candidates.size()>0){
			return candidates.get(Collections.max(candidates.keySet()));
		}
		for(String s : list_edits){
			for(String w : edits(s)){
				if(DBWords.containsKey(w)){
					candidates.put(DBWords.get(w), w);
				}
			}
		}
		return candidates.size()>0 ? candidates.get(Collections.max(candidates.keySet())) : "Sorry but no possible soluction found";
	}
	
	public static void main(String [] args) throws IOException{
		if(args.length>0){
			System.out.println((new SpellingSuggest("wordprobabilityDatabase.txt")).correct(args[0]));
		}
	}
}