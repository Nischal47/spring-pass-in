package com.example.passin.encryption;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AesUtils {

    private static final int BITS = 16;
    private static final int ROUNDS = 14;
    private static final int NO_OF_WORDS_IN_KEY = 60;
    private static final int KEY_LENGTH = 32;
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private byte[] word  = new byte[NO_OF_WORDS_IN_KEY];;
    int[] RC = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36, 0x6C, 0xD8, 0xAB, 0x4D, 0x9A};
    Word[] Rcon = new Word[ROUNDS];

    public void setRcon() {
        for (int i = 0; i < ROUNDS; i++) {
            Rcon[i] = new Word();
            byte[] temp = new byte[4];
            temp[0] = (byte) (RC[i] & 0xff);
            temp[1] = 0;
            temp[2] = 0;
            temp[3] = 0;
            Rcon[i].setWord(temp);
        }
    }

    static final int[] sBox = {
            0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
            0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
            0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
            0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
            0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
            0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
            0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
            0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
            0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
            0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
            0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
            0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
            0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
            0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
            0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
            0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16
    };

    private byte[] getRoundKey(int round) {
        return Arrays.copyOfRange(word, 16 * round, 16 * round + 16);
    }

    public byte[] XOR(byte[] a, byte[] b) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) ((a[i] ^ b[i]) & 0xff);
        }
        return out;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public byte[] hexStringToByteArray(String string) {
        int length = string.length();
        int n = (int) Math.ceil((length + 1) / 2);
        byte[] result = new byte[n];
        for (int i = length - 1; i >= 0; i -= 2) {
            if (i == 0) {
                result[i / 2] = (byte) ((Character.digit('0', 16) << 4)
                        + Character.digit(string.charAt(i), 16));
            } else {
                result[i / 2] = (byte) ((Character.digit(string.charAt(i - 1), 16) << 4)
                        + Character.digit(string.charAt(i), 16));
            }
        }
        return result;
    }

    byte gmul(byte a, byte b) {
        byte p = 0;
        int counter;
        byte high_bit_set;
        byte byte0x80 = hexStringToByteArray("80")[0];
        for (counter = 0; counter < 8; counter++) {
            if ((b & 0x01) == 1) {
                p = (byte) ((p ^ a) & 0xff);
            }
            high_bit_set = (byte) (a & 0x80);
            a <<= 1;
            if (high_bit_set == byte0x80) {
                a = (byte) ((a ^ 0x1b) & 0xff);
            }
            b = (byte) ((b >> 1) & 0x7f);
        }
        return p;
    }

    byte gmul(byte a, int b) {
        byte t = (byte) (b & 0xff);
        return gmul(a, t);
    }

    /* Substitute Bytes */
    private byte[] substituteBytes(byte[] in) {
        byte[] out = new byte[BITS];
        for (int i = 0 ; i < BITS ; i++) {
            byte a = in[i];
            int row = (a >> 4) & 0x000F;
            int col = a & 0x000F;
            out[i] = (byte) sBox[row * 16 + col];
        }
        return out;
    }

    /* Shift Rows */
    private byte[] shiftRows(byte[] in) {
        byte[] out = new byte[BITS];
        byte[] temp = new byte[BITS];
        for (int i = 0; i < BITS / 4; i++) {
            for (int j = 0; j < BITS / 4; j++) {
                temp[4 * j + i] = in[4 * i + j];
            }
        }
        for (int i = 0; i < BITS / 4; i++) {
            byte[] a = Arrays.copyOfRange(temp, (4 * i), (4 * i + 4));
            byte[] b = leftShift(a, i);
            in[4 * i] = b[0];
            in[4 * i + 1] = b[1];
            in[4 * i + 2] = b[2];
            in[4 * i + 3] = b[3];
        }
        for (int i = 0; i < BITS / 4; i++) {
            for (int j = 0; j < BITS / 4; j++) {
                out[4 * j + i] = in[4 * i + j];
            }
        }
        return out;
    }

    private byte[] leftShift(byte[] in, int times) {
        byte[] out = new byte[4];
        out = Arrays.copyOfRange(in, 0, 4);
        for (int i = 0; i < times; i++) {
            out[0] = in[1];
            out[1] = in[2];
            out[2] = in[3];
            out[3] = in[0];
            in = Arrays.copyOfRange(out, 0, 4);
        }
        return out;
    }

    /* Mix Columns */
    private byte[] mixColumns(byte[] in) {
        byte[] out = new byte[BITS];
        byte[] temp = new byte[BITS];
        for (int i = 0; i < BITS / 4; i++) {
            for (int j = 0; j < BITS / 4; j++) {
                temp[4 * j + i] = in[4 * i + j];
            }
        }
        in = temp;
        for (int j = 0; j < BITS / 4; j++) {
            out[4 * 0 + j] = (byte) ((gmul(in[4 * 0 + j], 2) ^ gmul(in[4 * 1 + j], 3) ^ in[4 * 2 + j] ^ in[4 * 3 + j]) & 0xff);
            out[4 * 1 + j] = (byte) ((in[4 * 0 + j] ^ gmul(in[4 * 1 + j], 2) ^ gmul(in[4 * 2 + j], 3) ^ in[4 * 3 + j]) & 0xff);
            out[4 * 2 + j] = (byte) ((in[4 * 0 + j] ^ in[4 * 1 + j] ^ gmul(in[4 * 2 + j], 2) ^ gmul(in[4 * 3 + j], 3)) & 0xff);
            out[4 * 3 + j] = (byte) ((gmul(in[4 * 0 + j], 3) ^ in[4 * 1 + j] ^ in[4 * 2 + j] ^ gmul(in[4 * 3 + j], 2)) & 0xff);
        }
        for (int i = 0; i < BITS / 4; i++) {
            for (int j = 0; j < BITS / 4; j++) {
                temp[4 * j + i] = out[4 * i + j];
            }
        }
        out = temp;
        return out;
    }

    private byte[] expandKey(byte[] key) throws Exception {
        if (key.length != KEY_LENGTH) {
            throw new Exception("Key should be of length, 256 bits");
        }
        Word[] w = new Word[NO_OF_WORDS_IN_KEY];
        Word temp;
        for (int i = 0; i < 8; i++) {
            w[i] = new Word(key[4 * i], key[4 * i + 1], key[4 * i + 2], key[4 * i + 3]);
        }

        for (int i = 8; i < NO_OF_WORDS_IN_KEY; i++) {
            temp = w[i - 1];
            Word temp1 = new Word();
            temp1.setWord(temp.getWord());
            if (i % 8 == 0) {
                temp1.rotWord();
                temp1.subWord();
                temp1 = Word.XORWords(temp1, Rcon[(i / 4) - 1]);
            }
            w[i] = Word.XORWords(w[i - 8], temp1);
        }
        return Word.wordsToBytes(w);
    }

    public byte[] encryptText(byte[] plainText, byte[] key) throws Exception {
        setRcon();
        this.word = expandKey(key);
        byte[] roundKey = getRoundKey(0);
        byte[] cipher = XOR(plainText, roundKey);

        for (int i = 1; i < ROUNDS; i++) {
            cipher = substituteBytes(cipher);
            cipher = shiftRows(cipher);
            cipher = mixColumns(cipher);
            roundKey = getRoundKey(i);
            cipher = XOR(cipher, roundKey);
        }

        cipher = substituteBytes(cipher);
        cipher = shiftRows(cipher);
        roundKey = getRoundKey(ROUNDS);
        cipher = XOR(cipher, roundKey);
        return cipher;
    }
}
