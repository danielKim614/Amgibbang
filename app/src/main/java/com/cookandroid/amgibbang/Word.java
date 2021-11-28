package com.cookandroid.amgibbang;

public class Word {
    boolean checkBox;
    String explanation;
    boolean isRight = false;  // 채점 결과 화면에서 쓸 변수
    String meaning;
    String word;

    public Word() {}

    public Word(String explanation, String meaning, String word, boolean checkBox) {
        this.explanation = explanation;
        this.meaning = meaning;
        this.word = word;
        this.checkBox=checkBox;
    }

    public Word(String explanation, String meaning, String word) {
        this.explanation = explanation;
        this.meaning = meaning;
        this.word = word;
    }

    public Word(String meaning, String word) {
        this.meaning = meaning;
        this.word = word;
        this.checkBox=checkBox;
    }

    public Word(Word word, boolean isRight) {
        this.explanation = word.explanation;
        this.meaning = word.meaning;
        this.word = word.word;
        this.isRight = isRight;
    }

    public void setIsRight(boolean isRight){
        this.isRight = isRight;
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

    public boolean getCheckBox() {
        return checkBox;
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

    public void cancelCheck(Boolean check){
        this.checkBox=check;
    }

}
