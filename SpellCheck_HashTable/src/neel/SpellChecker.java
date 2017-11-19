package neel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

class SpellChecker{
	Hashtable<String, String> dictionary;
	boolean suggestWord;
	
	public static void main(String[] args) throws IOException{
		SpellChecker checker= new SpellChecker();
	}
	
	public SpellChecker() throws IOException{
		dictionary = new Hashtable<String, String>();
		System.out.println("******Welcome to the spell checker*****");
        System.out.println("The spell checker would check every line from the input file and then give suggestions if needed after each line. \n");
		
        try {
			BufferedReader dictReader = new BufferedReader(new FileReader("dictionary.txt"));
			while (dictReader.ready()){
				String dictInput = dictReader.readLine();
				String [] dict = dictInput.split("\\s");
				for(int i=0;i<dict.length;i++){
					dictionary.put(dict[i], dict[i]);
				}
			}
			dictReader.close();
			
			String file = "inputtext.txt";
			
			BufferedReader inputFile = new BufferedReader(new FileReader(file));
			System.out.println("Reading from" + file);
			
			SpellingSuggest suggest = new SpellingSuggest("wordprobabilityDatabase.txt");
			
			while(inputFile.ready()){
				String s = inputFile.readLine();
				System.out.println(s);
				String[] result = s.split("\\s");
				for(int x=0;x<result.length;x++){
					suggestWord = true;
					String outputWord = checkWord(result[x]);
					
					if(suggestWord){
						System.out.println("\n"+ "Suggestion for " + result[x] + " are: " +suggest.correct(outputWord) );
					}
				}
			}
			inputFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String checkWord(String wordToCheck){
		String wordCheck, unpunctWord;
		String word = wordToCheck.toLowerCase();
		
		if((wordCheck = (String)dictionary.get(word)) !=null){
			suggestWord = false;
			return wordCheck;
		}
		
		int length = word.length();
		
		if(length>1 && word.substring(0, 1).equals("\"")){
			unpunctWord = word.substring(1,length);
			
			if((wordCheck = (String)dictionary.get(unpunctWord)) != null){
				suggestWord = false;
				return wordCheck;
			}else{
				return unpunctWord;
			}
		}
		if(word.substring(length-1).equals(".") || word.substring(length -1).equals(",") || word.substring(length -1).equals("!")
			||word.substring(length-1).equals(";") || word.substring(length-1).equals(":")){
			unpunctWord = word.substring(0,length-1);
			if((wordCheck = (String)dictionary.get(unpunctWord)) != null){
				suggestWord = false;
				return wordCheck;
			}else
				return unpunctWord;
		}
		if (length > 2 && word.substring(length-2).equals(",\"")  || word.substring(length-2).equals(".\"") 
	            || word.substring(length-2).equals("?\"") || word.substring(length-2).equals("!\"") )
	        {
	            unpunctWord = word.substring(0, length-2);
	            
	            if ((wordCheck = (String)dictionary.get(unpunctWord)) != null)
	            {
	                suggestWord = false;            
	                return wordCheck ;
	            }
	            else // not found
	                return unpunctWord;                  
	        }
		return word;
	}
	

}