package com.weiliao.kinnek.net;

import com.weiliao.kinnek.utils.Base64;

import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class TripleDES {

    //定义加密算法，有DES、DESede(即3DES)、Blowfish
    private static final String Algorithm = "DESede";
    private static final String KEY = "EpQxLka+um1ASTffJgvi5OQ==";
    private static final String IV = "01234567";

    static {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
    }

    private static final String MCRYPT_TRIPLEDES = "DESede";
    private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";

    public static byte[] decrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MCRYPT_TRIPLEDES);
        SecretKey sec = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec IvParameters = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters);
        return cipher.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey sec = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec IvParameters = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, sec, IvParameters);
        return cipher.doFinal(data);
    }

    public static String encryptMode(String src) {
        return encryptMode(src.getBytes());
    }

    public static String encryptMode(byte[] src) {
        String result = "";
        byte[] keyByte = KEY.getBytes();
        byte[] ivByte = IV.getBytes();

        try {
            byte[] temp = encrypt(src, keyByte, ivByte);
            result = new String(Base64.encode(temp), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decryptMode(String src) {
        return decryptMode(src.getBytes());
    }

    public static String decryptMode(byte[] src) {
        String result = "";
        byte[] keyByte = KEY.getBytes();
        byte[] ivByte = IV.getBytes();

        try {
            result = new String(decrypt(Base64.decode(src), keyByte, ivByte), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
