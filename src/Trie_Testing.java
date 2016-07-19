import java.util.ArrayList;

public class Trie_Testing {

	public static void main(String[] args) {
		Trie trie = new Trie();
		
//		trie.addWord("alect");
		trie.addWord("pre");
		trie.addWord("prefix");
		trie.addWord("prefect");
		trie.addWord("predestination");
		trie.addWord("predestined");
		

//		trie.addWord("");
		System.out.println(trie.getAllWordsWithPrefix("sls"));
		System.out.println(Trie.getAllWordsInTrie(trie.root, "", new ArrayList<String>()));
		
		
	}

}
