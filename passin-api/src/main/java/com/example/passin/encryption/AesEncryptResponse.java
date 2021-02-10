package com.example.passin.encryption;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AesEncryptResponse {
    private byte[] cipherText;
    private byte[] iv;
}
