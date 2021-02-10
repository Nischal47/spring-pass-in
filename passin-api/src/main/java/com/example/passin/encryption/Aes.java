package com.example.passin.encryption;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

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
        byte[] bKey = new byte[]{79, 86, 82, 110, 90, 110, 107, 49, 84, 106, 99, 53, 101, 70, 82, 50, 100, 68, 86, 79, 86, 50, 52, 118, 81, 85, 120, 69, 85, 84, 48, 57};
        byte[] iv = createIvVector();
        byte[] plainTextByteArray = plainText;
        byte[] keyByteArray = key;
        byte[] cipherTextByteArray = ctrMode.encrypt(iv,plainTextByteArray,bKey);
        AesEncryptResponse aesEncryptResponse = new AesEncryptResponse();
        aesEncryptResponse.setCipherText(cipherTextByteArray);
        aesEncryptResponse.setIv(iv);
        return aesEncryptResponse;
    }

    public byte[] decrypt(byte[] cipherText,byte[] key,byte[] iv) throws Exception {
        byte[] bKey = new byte[]{79, 86, 82, 110, 90, 110, 107, 49, 84, 106, 99, 53, 101, 70, 82, 50, 100, 68, 86, 79, 86, 50, 52, 118, 81, 85, 120, 69, 85, 84, 48, 57};
        byte[] cipherTextByteArray = cipherText;
        byte[] keyByteArray = key;
        byte[] plainTextByteArray = ctrMode.decrypt(iv,cipherTextByteArray,bKey);
        return plainTextByteArray;
    }
}
