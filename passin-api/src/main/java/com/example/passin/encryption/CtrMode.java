package com.example.passin.encryption;

import org.springframework.stereotype.Component;

@Component
public class CtrMode {

    public final AesUtils aesUtils;

    public CtrMode(AesUtils aesUtils) {
        this.aesUtils = aesUtils;
    }

    public byte[] encrypt(byte[] iv,byte[] message,byte[] key) throws Exception {
        return getBytes(iv, message, key);
    }

    public byte[] decrypt(byte[] iv,byte[] cipherText,byte[] key) throws Exception {
        return getBytes(iv, cipherText, key);
    }

    private byte[] getBytes(byte[] iv, byte[] message, byte[] key) throws Exception {
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
            increment(iv);
        }
        return cipher;
    }

    public static byte[] increment(byte[] a) {
        for (int i = a.length - 1; i >= 0; --i) {
            if (++a[i] != 0) {
                return a;
            }
        }
        throw new IllegalStateException("Counter overflow");
    }
}
