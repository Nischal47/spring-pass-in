package com.example.passin.encryption;

import org.springframework.stereotype.Component;

@Component
public class Word {
    private byte[] word = null;
    private AesUtils aesUtils = new AesUtils();

    public Word() {
        word = new byte[4];
    }

    public Word(byte b0,byte b1,byte b2, byte b3){
        this();
        word[0] = b0;
        word[1] = b1;
        word[2] = b2;
        word[3] = b3;
    }

    public byte[] getWord() {
        return word;
    }

    public void setWord(byte[] word){
        this.word = word;
    }

    public static byte[] wordsToBytes(Word[] words){
        byte[] out = new byte[4 * words.length];
        for (int i = 0 ; i < words.length; i++){
            byte[] temp = words[i].getWord();
            out[4*i] = temp[0];
            out[4*i + 1] = temp[1];
            out[4*i + 2] = temp[2];
            out[4*i + 3] = temp[3];
        }
        return out;
    }

    public void rotWord(){
        byte[] temp = this.getWord();
        byte[] newWord = new byte[4];
        newWord[0] = temp[1];
        newWord[1] = temp[2];
        newWord[2] = temp[3];
        newWord[3] = temp[0];
        this.setWord(newWord);
    }

    public void subWord(){
        byte[] in = this.getWord();
        byte[] out = new byte[4];
        for (int i = 0 ; i < 4 ; i++){
            byte a = in[i];
            int row = ( a >> 4 ) & 0x000F;
            int col = a & 0x000F;
            out[i] = (byte) aesUtils.sBox[row * 16 + col];
        }
        for (int i = 0 ; i < 4 ; i++) {
            this.word[i] = out[i];
        }
    }

    public static Word XORWords(Word word1, Word word2) {
        Word outWord = new Word();
        byte[] in1 = word1.getWord();
        byte[] in2 = word2.getWord();
        byte[] out = new byte[4];
        for (int i = 0 ; i < 4 ; i++) {
            out[i] = (byte)((in1[i] ^ in2[i]) & 0xff);
        }
        outWord.setWord(out);
        return outWord;
    }

    public String toString() {
        return aesUtils.bytesToHex(this.getWord());
    }
}
