package com.example.passin.encryption;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CtrMode {

    public final AesUtils aesUtils;

    public CtrMode(AesUtils aesUtils) {
        this.aesUtils = aesUtils;
    }

    public byte[] encrypt(byte[] iv,byte[] message,byte[] key) throws Exception {
        byte[] cipherIv;
        if(message.length<16){
            System.out.println("Message should be atleast 128 bits");
        }
        int length = message.length;
        int n = (length + 15)/16*16;
        byte[] cipher = new byte[n];

        if(length == 16){
            cipherIv =  aesUtils.encryptText(iv,key);
            cipher = aesUtils.XORBytes(cipherIv,message);
            return cipher;
        }

        int i = 0;
        int k = 0;
        while (i < length){
            byte[] block = new byte[16];
            byte[] result;
            int j = 0;
            for (; j < 16 && i < length; j++, i++) {
                block[j] = message[i];
            }
            while (j < 16) {
                /* pad with white spaces */
                block[j++] = 0x20;
            }

            cipherIv =  aesUtils.encryptText(iv,key);
            result = aesUtils.XORBytes(cipherIv,block);
            for (j = 0 ; j < 16 && k < cipher.length; j++, k++) {
                cipher[k] = result[j];
            }
        }
        return cipher;
    }

    public byte[] decrypt(byte[] iv,byte[] cipherText,byte[] key) throws Exception {
        System.out.println("iv"+Arrays.toString(iv));
        byte[] cipherIv;
        if(cipherText.length<16){
            System.out.println("Message should be atleast 128 bits");
        }
        int length = cipherText.length;
        int n = (length + 15)/16*16;
        byte[] plainText = new byte[n];

        if(length == 16){
            cipherIv =  aesUtils.encryptText(iv,key);
            plainText = aesUtils.XORBytes(cipherIv,cipherText);
            return plainText;
        }

        int i = 0;
        int k = 0;
        while (i < length){
            byte[] block = new byte[16];
            byte[] result;
            int j = 0;
            for (; j < 16 && i < length; j++, i++) {
                block[j] = cipherText[i];
            }
            while (j < 16) {
                /* pad with white spaces */
                block[j++] = 0x20;
            }

            cipherIv =  aesUtils.encryptText(iv,key);
            result = aesUtils.XORBytes(cipherIv,block);
            for (j = 0 ; j < 16 && k < plainText.length; j++, k++) {
                plainText[k] = result[j];
            }
        }
        return plainText;
    }
}
