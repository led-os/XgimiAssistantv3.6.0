package com.xgimi.zhushou.aes;


import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by liuyang on 2016/7/15.
 */
public class AesUtil {

    public static String KEY = "5GDESKE491LANX5R5UONSQRQ9T2H4IAG";

    /**
     * 解密
     * @param encryptData
     * @return
     */
    public static String AES_Decrypt(String encryptData) {
        byte[] decrypt = null;
        try {
            Key key = generateKey(KEY);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypt = cipher.doFinal(Base64.decode(encryptData, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (decrypt == null) {
            return null;
        }
        return new String(decrypt).trim();
    }

    /**
     * 加密
     * @param plainText
     * @return
     */
    public static String AES_Encode(String plainText) {
        try {
            Key key = generateKey(KEY);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptData = cipher.doFinal(plainText.getBytes("utf-8"));
            return Base64.encodeToString(encryptData, Base64.DEFAULT);
        } catch (Exception e) {

        }
        return null;
    }

    private static Key generateKey(String key) throws Exception {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            return keySpec;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
