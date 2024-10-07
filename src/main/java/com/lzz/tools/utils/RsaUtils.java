package com.lzz.tools.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

/**
 * 通常使用PKCS#8格式进行编码。
 * 密钥对象通常转换为PEM格式字符串使用
 */
public class RsaUtils {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    public static String publicKeyToString(PublicKey publicKey) {
        try {
            // 获取密钥编码
            byte[] encodedKey = publicKey.getEncoded();
            // Base64 编码
            String base64EncodedKey = Base64.getEncoder().encodeToString(encodedKey);
            // 转换为 PEM 格式
            return "-----BEGIN PUBLIC KEY-----\n" +
                    formatPEM(base64EncodedKey) +
                    "-----END PUBLIC KEY-----";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String privateKeyToString(PrivateKey privateKey) {
        try {
            // 获取密钥编码
            byte[] encodedKey = privateKey.getEncoded();
            // Base64 编码
            String base64EncodedKey = Base64.getEncoder().encodeToString(encodedKey);
            // 转换为 PEM 格式
            return "-----BEGIN PRIVATE KEY-----\n" +
                    formatPEM(base64EncodedKey) +
                    "-----END PRIVATE KEY-----";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // 格式化为PEM，确保每行不超过64个字符
    private static String formatPEM(String encodedKey) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < encodedKey.length(); i += 64) {
            int endIndex = Math.min(i + 64, encodedKey.length());
            formatted.append(encodedKey, i, endIndex).append("\n");
        }
        return formatted.toString();
    }

    public static void main(String[] args) {
        try {
            // Generate Key Pair
            KeyPair keyPair = generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 将 PublicKey 转换为字符串
            String publicKeyStr = publicKeyToString(publicKey);
            System.out.println("PublicKey Text: " +publicKeyStr);

            // 将 PrivateKey 转换为字符串
            String privateKeyStr = privateKeyToString(privateKey);
            System.out.println("PrivateKey Text: " +privateKeyStr);

            // Original text
            String originalText = "Hello, RSA!";

            // Encrypt
            String encryptedText = encrypt(originalText, publicKey);
            System.out.println("Encrypted Text: " + encryptedText);

            // Decrypt
            String decryptedText = decrypt(encryptedText, privateKey);
            System.out.println("Decrypted Text: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
