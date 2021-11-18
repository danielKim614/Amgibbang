package com.cookandroid.amgibbang;

public class Word {
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
}
