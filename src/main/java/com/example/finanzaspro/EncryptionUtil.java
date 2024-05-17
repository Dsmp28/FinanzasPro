package com.example.finanzaspro;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MySuperSecretKey".getBytes(); // Debe ser de 16 bytes para AES-128

    public static String encrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedValue = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public static String decrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue = Base64.getDecoder().decode(value);
        byte[] decValue = cipher.doFinal(decryptedValue);
        return new String(decValue);
    }

    private static Key generateKey() {
        return new SecretKeySpec(KEY, ALGORITHM);
    }

    public static void main(String[] args) {
        try {
            String original = "HelloWorld";
            String encrypted = encrypt(original);
            System.out.println("Encrypted: " + encrypted);
            String decrypted = decrypt(encrypted);
            System.out.println("Decrypted: " + decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


