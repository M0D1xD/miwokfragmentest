package com.example.m0d1x.miwokfragmentest;


public class dictionary {
    private String Word, Translate;
    private int PicID, SoundID;

    public String getWord() {
        return Word;
    }

    public dictionary(String word, String translate, int picID, int soundID) {
        Word = word;
        Translate = translate;
        PicID = picID;
        SoundID = soundID;
    }

    public void setWord(String word) {
        Word = word;
    }

    public String getTranslate() {
        return Translate;
    }

    public void setTranslate(String translate) {
        Translate = translate;
    }

    public int getPicID() {
        return PicID;
    }

    public void setPicID(int picID) {
        PicID = picID;
    }

    public int getSoundID() {
        return SoundID;
    }

    public void setSoundID(int soundID) {
        SoundID = soundID;
    }
}
