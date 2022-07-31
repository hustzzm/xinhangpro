package com.pig.basic.util;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
    static final int KEY_LENGTH_BYTE = 32;
    static final int TAG_LENGTH_BIT = 128;
    private final byte[] aesKey;

    public AesUtil(byte[] key) {
        if (key.length != 32) {
            throw new IllegalArgumentException("无效的ApiV3Key，长度必须为32个字节");
        } else {
            this.aesKey = key;
        }
    }

    public String decryptToString(byte[] associatedData, byte[] nonce, String ciphertext) throws GeneralSecurityException, IOException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec key = new SecretKeySpec(this.aesKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(128, nonce);
            cipher.init(2, key, spec);
            cipher.updateAAD(associatedData);
            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), "utf-8");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException var7) {
            throw new IllegalStateException(var7);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException var8) {
            throw new IllegalArgumentException(var8);
        }
    }
}
