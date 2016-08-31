package com.maelstrom.notes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Trie {
	public char nodeChar;
	public boolean isLeaf;
	public List<String> safeNames;
	public HashMap<Character, Trie> subtrie;
	
	Trie(char nodeChar, boolean isLeaf) {
		subtrie = new HashMap<>();
		safeNames = new ArrayList<> ();
		this.nodeChar = nodeChar;
		this.isLeaf = isLeaf;
	}
	
	String normalizeWord(String word) {
		return word.toLowerCase().replace(" ", "");
	}
	
	public void insertWord(String word) {
		
		String trieWord = normalizeWord (word);
		
		int len = trieWord.length();
		
		Trie parentTrie = this;
		
		for(int i = 0; i < len; i++) {
			char currentChar = trieWord.charAt(i);
			
			Trie currentTrie = parentTrie.subtrie.get(currentChar);

			if (currentTrie == null)
				currentTrie = new Trie (currentChar, false);

			currentTrie.safeNames.add(word);
			currentTrie.isLeaf = (i + 1 == len);
			
			parentTrie.subtrie.put(currentChar, currentTrie);
			parentTrie = currentTrie;
		}
	}

	public void insertAllWords(List<String> words) {
		for (int i = 0; i < words.size (); i++) {
			insertWord (words.get (i));
		}
	}
	
	public void printTrie() {
		if (this.isLeaf) {
			System.out.println(this.safeNames.toString());
			return;
		}
		
		for (Trie tmp: this.subtrie.values())
			tmp.printTrie();
	}
	
	public void autocomplete(String prefix) {
		Trie tmp = this;
		
		for (int i = 0; i < prefix.length(); i++) {
			tmp = tmp.subtrie.get(prefix.charAt(i));
			if (tmp == null)
				return;
		}

		tmp.printTrie();
	}
	
	void checkDeepPaths(List<Trie> possiblePaths, char toBeFound) {
		if (this.isLeaf)
			return;
		
		for (Trie subtrie: this.subtrie.values())
			if (subtrie.nodeChar != toBeFound )
				subtrie.checkDeepPaths(possiblePaths, toBeFound);
			else
				possiblePaths.add(subtrie);
	}
	
	public List<String> searchSubsequence(String sequence) {

		ArrayList<Trie> possiblePaths = new ArrayList<>(),
				searchablePaths = new ArrayList<>();
		
		searchablePaths.add(this);

		for (int i = 0; i < sequence.length(); i++) {
			for (Trie searchablePath : searchablePaths)
				searchablePath.checkDeepPaths(possiblePaths, sequence.charAt(i));

			searchablePaths = (ArrayList<Trie>) possiblePaths.clone();
			possiblePaths.clear();
		}

		List<String> finalList = new ArrayList<>();

		for(Trie path: searchablePaths) {
			for (String s: path.safeNames) {
				finalList.add (s);
			}
		}

		return finalList;
//		List<String> finalList = searchablePaths.stream()
//				.map(p -> p.safeNames)
//				.flatMap(Collection::stream)
//				.collect(Collectors.toList());
	}
}