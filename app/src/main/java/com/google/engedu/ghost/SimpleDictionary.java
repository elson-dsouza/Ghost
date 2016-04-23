package com.google.engedu.ghost;

import android.provider.UserDictionary;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix == ""){
            int r = (int) (Math.random()*words.size());
            return words.get(r-1);
        }
        else
        {
            int h = words.size(),l = 0,m;
            while (l <= h){
                m = (l + h) / 2;
                String currentWord = words.get(m);
                if(currentWord.startsWith(prefix)){
                    return currentWord;
                }else if (currentWord.compareTo(prefix) < 0){
                    l = m + 1;
                }else if (currentWord.compareTo(prefix) > 0) {
                    h = m - 1;
                }
            }
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }

}
