package app.utils.trie.TrieNode;

import java.util.HashMap;

public class TrieNode {
    private final HashMap<Character, TrieNode> children;
    private final String content;
    private boolean isWord;

    public TrieNode(String content){
        this.content = content;
        this.children = new HashMap<>();
    }

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public String getContent() {
        return content;
    }

    public boolean isWord() {
        return isWord;
    }

    public void setIsWord(boolean isWord) {
        this.isWord = isWord;
    }
}
