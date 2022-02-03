package com.example.demo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Constants {
    String SALT = "hB*huGJ$LELrt57L";

    static String hashPass(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-384");
        byte[] data1 = pass.getBytes("UTF-8");
        byte[] data2 = Constants.SALT.getBytes("UTF-8");
        messageDigest.update(data1);
        messageDigest.update(data2);
        byte[] digest = messageDigest.digest();
        return new String(digest);
    }

    static boolean equalPass(String pass1, String pass2) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return pass1.equals(Constants.hashPass(pass2));
    }
}
