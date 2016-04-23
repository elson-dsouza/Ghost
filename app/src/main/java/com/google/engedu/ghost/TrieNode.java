package com.google.engedu.ghost;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    private char value;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
        value='\0';
    }

    public TrieNode(char c){
        children = new HashMap<>();
        isWord = false;
        value=c;
    }

    public void add(String s) {
        TrieNode cur=this;
        for (Character c:s.toCharArray()){
            if(cur.children.containsKey(c))
                cur=cur.children.get(c);
            else {
                cur.children.put(c, new TrieNode(c));
                cur=cur.children.get(c);
            }
        }
        cur.isWord=true;
    }

    public boolean isWord(String s) {
        TrieNode cur=this;
        for (Character c:s.toCharArray()){
            if(cur.children.containsKey(c))
                cur=cur.children.get(c);
            else {
                return false;
            }
        }
        return cur.isWord;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode cur=this;
        for (Character c:s.toCharArray()){
            if(cur.children.containsKey(c))
                cur=cur.children.get(c);
            else {
                return null;
            }
        }
        ArrayList<String>validWords = new ArrayList<>();
        findword(s,validWords,cur,true,0);
        if (validWords.isEmpty())
            return null;
        else {
            int r=(int)(Math.random()*validWords.size());
            return validWords.get(r);
        }
    }

    public void findword(String s,ArrayList<String> words,TrieNode cur,boolean easy,int i){
        for (TrieNode t: cur.children.values()){
            String temp=s+t.value;
            if(t.isWord&&easy)
                words.add(temp);
            else if (t.isWord&&i%2==0)
                words.add(temp);
            else if(t.isWord)
                return;
            findword(temp,words,t,easy,i+1);
        }
    }


    public String getGoodWordStartingWith(String s) {
        TrieNode cur=this;
        for (Character c:s.toCharArray()){
            if(cur.children.containsKey(c))
                cur=cur.children.get(c);
            else {
                return null;
            }
        }
        ArrayList<String> words=new ArrayList<>();
        findword(s,words,cur,false,0);
        if (words.isEmpty()){
            findword(s,words,cur,true,0);
            if (words.isEmpty())
                return null;
            else {
                char r =(char)((int)(Math.random()*26) +(int) 'a');
                return s+Character.toString(r);
            }
        }
        else {
            int r=(int)(Math.random()*words.size());
            return words.get(r);
        }
    }
}
