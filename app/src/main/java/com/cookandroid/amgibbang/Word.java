package com.cookandroid.amgibbang;

import java.io.Serializable;

public class Word implements Serializable {
    String explanation;
    String meaning;
    String word;

    public Word() {}

    public Word(String explanation, String meaning, String word) {
        this.explanation = explanation;
        this.meaning = meaning;
        this.word = word;
    }

    public Word(String meaning, String word) {
        this.meaning = meaning;
        this.word = word;
    }


    public String getExplanation() {
        return explanation;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getWord() {
        return word;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
