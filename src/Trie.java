import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Trie {

	trieNode root;

	public Trie(){
		root = new trieNode('*');
	}

	// Location of Dictionary---> /Users/AlecGriffin/Documents/dictionary2.txt
	public void buildTrieFromFile(String locationOfDictionary){
		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader(locationOfDictionary));
			String line = input.readLine();
			this.addWord(line);

			while(line != null){
				this.addWord(line);
				line = input.readLine();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	//pre: Assume Root is never null
	//post: Return true if word is present 
	public boolean isWordPresent(String word){
		word = word.toLowerCase();
		trieNode tempNode = root;
		
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			tempNode = tempNode.siblings[c - 97];
			
			if(tempNode == null)
				return false;	
		}
		
		return tempNode.endOfWord;
	}

	//post: Return 1 is word added
	//		Return 0 if word not added
	public int addWord(String word){
		word = word.toLowerCase();

		trieNode tempNode = root;

		for(int i = 0; i < word.length(); i++){
			int characterLocation = ((int) word.charAt(i)) - 97;

			if( tempNode.siblings[characterLocation] == null ){
				tempNode.siblings[characterLocation] = new trieNode(word.charAt(i));

			}
			tempNode = tempNode.siblings[characterLocation];
		}
		tempNode.endOfWord = true;
		return 1;
	}

	// post: Returns the word List Stored within  this data structure
	public static ArrayList<String> getAllWordsInTrie(trieNode node, String wordToBuild, ArrayList<String> wordList){
		for(int i = 0; i < node.siblings.length; i++){

			if(node.siblings[i] != null){
				getAllWordsInTrie(node.siblings[i], wordToBuild + node.siblings[i].data, wordList);
			}
		}

		if(node.endOfWord ){
			wordList.add(wordToBuild);
		}
		return wordList;
	}

	public ArrayList<String> getAllWordsWithPrefix(String prefix){
		prefix = prefix.toLowerCase();
		trieNode tempNode = root;
		for(int i = 0; i < prefix.length(); i++){
			
			if(tempNode == null)
				return new ArrayList<String>();
			
			
			tempNode = tempNode.siblings[prefix.charAt(i) - 97];
		}

		return getAllWordsInTrie(tempNode, prefix, new ArrayList<String>());
	}

	public static void printTrie(trieNode node){
		System.out.println("Data: " + node.data);
		for(int i = 0; i < node.siblings.length; i++){
			if(node.siblings[i] != null)
				System.out.println(node.siblings[i].data);
		}	

		for(int j = 0; j < node.siblings.length; j++){
			if(node.siblings[j] != null)
				printTrie(node.siblings[j]);
		}
	}

	private  class  trieNode{
		private char data;
		private boolean endOfWord;
		private trieNode[] siblings;

		public trieNode(char data){
			this.data = data;
			siblings = new trieNode[26];
			endOfWord = false;
		}

		public char getData(){
			return this.data;
		}

		// post: Returns 1 if node is added
		//		 Returns 0 if node is not added (Add already exists)			 
		public int addNode(trieNode node){
			if(siblingDuplicatePresent(node)){
				return 0;
			}

			this.siblings[node.data - 97] = node;
			return 1;
		}	

		//Post: Returns true if there is already a node containing the specified letter this trieNode
		public boolean siblingDuplicatePresent(trieNode node){
			return node.siblings[node.data - 97] != null;	
		}

	}

}



