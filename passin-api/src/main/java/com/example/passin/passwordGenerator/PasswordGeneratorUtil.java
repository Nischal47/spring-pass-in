package com.example.passin.passwordGenerator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGeneratorUtil {

    SecureRandom secureRandom = new SecureRandom();

    private final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private final char[] L_CASE = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private final char[] U_CASE = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'M', 'N', 'O', 'p', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final char[] SYMBOLS = {'@', '#', '$', '%', '=', ':', '?', '.', '|', '~', '*', '(', ')'};

    public String generateRandomPassword(int minLength,int maxLength){

        int passwordLength = secureRandom.nextInt(maxLength - minLength + 1) + minLength;
        int digitCount = secureRandom.nextInt((passwordLength - 6) - 2 + 1) + 2;
        int lCaseCount = secureRandom.nextInt((passwordLength - digitCount - 4) - 2 + 1) + 2;
        int uCaseCount = secureRandom.nextInt((passwordLength - digitCount - lCaseCount - 2) - 2 + 1) + 2;
        int symbolCount = secureRandom.nextInt((passwordLength - digitCount - lCaseCount - uCaseCount) - 2 + 1) + 2;

        return generatePassword(digitCount,lCaseCount,uCaseCount,symbolCount);

    }

    private String generatePassword(int digitCount,int lCaseCount,int uCaseCount, int symbolCount){
        String tempPassword;
        StringBuilder randomDigits= new StringBuilder();
        StringBuilder randomLCaseCharacters = new StringBuilder();
        StringBuilder randomUCaseCharacters= new StringBuilder();
        StringBuilder randomSymbols= new StringBuilder();

        for(int i = 0;i< digitCount;i++){
            int random = secureRandom.nextInt(DIGITS.length);
            char randomDigit = DIGITS[random];
            randomDigits.append(randomDigit);
        }

        for(int i = 0;i< lCaseCount;i++){
            int random = secureRandom.nextInt(L_CASE.length);
            char randomLCase = L_CASE[random];
            randomLCaseCharacters.append(randomLCase);
        }

        for(int i = 0;i< uCaseCount;i++){
            int random = secureRandom.nextInt(U_CASE.length);
            char randomUCase = U_CASE[random];
            randomUCaseCharacters.append(randomUCase);
        }

        for(int i = 0;i< symbolCount;i++){
            int random = secureRandom.nextInt(SYMBOLS.length);
            char randomSymbol= SYMBOLS[random];
            randomSymbols.append(randomSymbol);
        }

        tempPassword = randomDigits.toString() + randomLCaseCharacters.toString() + randomUCaseCharacters.toString() + randomSymbols.toString();
        return shufflePassword(tempPassword);
    }

    public String shufflePassword(String text) {
        char[] characters = text.toCharArray();
        int characterLength = characters.length;
        for (int i = 0; i < characterLength; i++) {
            int randomIndex = secureRandom.nextInt(characterLength);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }
}
