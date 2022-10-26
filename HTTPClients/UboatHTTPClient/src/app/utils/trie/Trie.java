package app.utils.trie;

import app.utils.trie.TrieNode.TrieNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Trie {
    private final TrieNode root;

    public Trie(){
        root = new TrieNode("");
    }

    public void insert(String word) {
        TrieNode current = root;

        for (char l: word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(l, c -> new TrieNode(String.valueOf(l)));
        }

        current.setIsWord(true);
    }

    public boolean find(String word) {
        TrieNode node = findTrieNode(word);
        return node != null && node.isWord();
    }

    private TrieNode findTrieNode(String word){
        TrieNode current = root;

        for (Character character : word.toCharArray()) {
            TrieNode node = current.getChildren().get(character);
            if (node == null)
                return null;
            current = node;
        }
        return current;
    }

    public List<String> allWords(){
        return findAll("");
    }

    public List<String> findAll(String prefix){
        List<String> words = new LinkedList<>();
        Optional<TrieNode> root = Optional.ofNullable(findTrieNode(prefix));
        String new_prefix;

        if (!prefix.equals(""))
            new_prefix = prefix.substring(0, prefix.length() - 1);
        else
            new_prefix = "";

        root.ifPresent(trieNode ->  inOrderTraverse(trieNode, words, new_prefix));
        return words;
    }

    private void inOrderTraverse(TrieNode root, List<String> wordList, String prefix){
        if (root == null)
            return;
        String newPrefix = prefix + root.getContent();
        if (root.isWord())
            wordList.add(newPrefix);

        root.getChildren().values().forEach(node -> inOrderTraverse(node, wordList, newPrefix));
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("hello");
        trie.insert("world");
        trie.insert("wo");
        System.out.println(trie.findAll("w"));
    }
}
