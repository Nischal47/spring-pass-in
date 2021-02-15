package com.example.passin.encryption;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitializationVector {
    private byte[] iv;
    private byte[] nonce;
}
