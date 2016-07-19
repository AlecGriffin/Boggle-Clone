
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryParser {
	public static void main(String[] args) throws IOException{
		Trie trie = new Trie();
		int sizeOfDict = 0;
		
		// <----Parser[START]---->
		BufferedReader input = new BufferedReader(new FileReader("/Users/AlecGriffin/Documents/Eclipse/Boggle2/src/enable1.txt"));
		String line = input.readLine();
		trie.addWord(line);

		while(line != null){
			trie.addWord(line);
			line = input.readLine();
			sizeOfDict++;
		}
		// <----Parser[END]---->
		
		
		ArrayList<String> wordList = trie.getAllWordsWithPrefix("");
		
//		System.out.println("WORDLIST:" + wordList);
		System.out.println(wordList.size());
		System.out.println(sizeOfDict);
		
		input.close();
		

		System.out.println(	trie.getAllWordsWithPrefix(""));
	}
	
//	public static  dictionaryParser
}
