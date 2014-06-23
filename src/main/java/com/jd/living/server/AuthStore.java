package com.jd.living.server;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import android.text.TextUtils;

public class AuthStore {

    private long time = 0;
    private String unique = "";

    public String getCallerId() {
        return "EasyLiving";
    }

    private String getPrivateKey() {
        return "fN9u8gmUFMvXCgNS8SAWkE96535Ttul5lNzpWeP2";
    }

    public long getTime() {
        if (time == 0) {
            time = System.currentTimeMillis();
        }
        return time;
    }

    public String getUnique(){
        if (TextUtils.isEmpty(unique)) {
            String result = UUID.randomUUID().toString().replace("-", "");
            unique = result.substring(0, 16);
        }
        return unique;
    }

    public String getHash() {
        return sha1(getCallerId() + getTime() + getPrivateKey() + getUnique());
    }

    private  String sha1(String s) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        digest.reset();
        byte[] data = digest.digest(s.getBytes());
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }
}
