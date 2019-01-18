/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            int length = word.length();
            if (sizeToWords.containsKey(length)) {
                /*ArrayList<String> words = */sizeToWords.get(length).add(word);
                /*words.add(word);
                sizeToWords.put(length, words);*/
            }
            else {
                ArrayList<String> words = new ArrayList<>();
                words.add(word);
                sizeToWords.put(length, words);
            }

            String sortedWord = sortLetters(word);
            if (lettersToWord.containsKey(sortedWord)) {
                /*ArrayList<String> anagrams = */lettersToWord.get(sortedWord).add(word);
                /*anagrams.add(word);
                lettersToWord.put(sortedWord, anagrams);*/
            }
            else {
                ArrayList<String> anagrams = new ArrayList<>();
                anagrams.add(word);
                lettersToWord.put(sortedWord, anagrams);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    /*public boolean isAnagram(String word, String base) {
        String sortWord = sortLetters(word);
        String sortBase = sortLetters(base);
        return word.length() == base.length() && sortWord.equalsIgnoreCase(sortBase);
    }*/

    public List<String> getAnagrams(String targetWord) {
        return lettersToWord.get(sortLetters(targetWord));
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (char letter : alphabets) {
            if (lettersToWord.containsKey(sortLetters(word + letter))) {
                ArrayList<String> tempArray = lettersToWord.get(sortLetters(word + letter));
                for (String anagramWord : tempArray) {
                    result.add(anagramWord);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int numAnagrams = 0;
        int wordLength = DEFAULT_WORD_LENGTH;
        while (wordLength <= MAX_WORD_LENGTH) {
            ArrayList<String> sameLengthW = sizeToWords.get(wordLength);
            while (numAnagrams < MIN_NUM_ANAGRAMS) {
                String word = sameLengthW.get(random.nextInt(sameLengthW.size()));
                numAnagrams = lettersToWord.get(sortLetters(word)).size();
                if (numAnagrams >= MIN_NUM_ANAGRAMS) {
                    return word;
                }
            }
            wordLength++;
        }   /*numAnagrams < MIN_NUM_ANAGRAMS) {
            String word = wordList.get(random.nextInt(wordList.size()));
            numAnagrams = lettersToWord.get(sortLetters(word)).size();
            wordLength = word.length();
            if (wordLength <= MAX_WORD_LENGTH && numAnagrams >= MIN_NUM_ANAGRAMS) {
                return word;
            }
            wordLength++;
        }*/
        return "stop";
    }

    static String sortLetters(String input) {
        char[] letters = input.toCharArray();
        Arrays.sort(letters);
        String sorted = new String(letters);
        return sorted;
    }
}
