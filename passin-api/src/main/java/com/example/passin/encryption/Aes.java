package com.example.passin.encryption;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class Aes {
    private final CtrMode ctrMode;
    private final SecureRandom secureRandom;


    public Aes(CtrMode ctrMode) throws NoSuchAlgorithmException {
        this.ctrMode = ctrMode;
        this.secureRandom = SecureRandom.getInstance("SHA1PRNG");
    }

    public byte[] createIvVector(){
        byte[] iv = new byte[16];
        byte[] nonce = new byte[12];
        secureRandom.nextBytes(nonce);
        System.arraycopy(nonce, 0, iv, 0, nonce.length);
        return iv;
    }

    public AesEncryptResponse encrypt(byte[] plainText,byte[] key) throws Exception {
        byte[] iv = createIvVector();
        byte[] plainTextByteArray = plainText;
        AesEncryptResponse aesEncryptResponse = new AesEncryptResponse();
        aesEncryptResponse.setIv(iv);
        byte[] cipherTextByteArray = ctrMode.encrypt(iv,plainTextByteArray,key);
        aesEncryptResponse.setCipherText(cipherTextByteArray);
        return aesEncryptResponse;
    }

    public byte[] decrypt(byte[] cipherText,byte[] key,byte[] iv) throws Exception {
        byte[] cipherTextByteArray = cipherText;
        byte[] plainTextByteArray = ctrMode.decrypt(iv,cipherTextByteArray,key);
        return plainTextByteArray;
    }
}
