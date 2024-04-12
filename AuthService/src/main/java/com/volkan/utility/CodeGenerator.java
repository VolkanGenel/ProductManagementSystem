package com.volkan.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static java.security.MessageDigest.getInstance;

public class CodeGenerator {

    public static  String generateCode(){
        String code = UUID.randomUUID().toString();
        String [] data = code.split("-");
        String newCode = "";
        for(String string:data){
            newCode+=string.charAt(0);
            newCode+=string.charAt(1);
        }
        return newCode;
    }

    public static String encode(String password) {
        MessageDigest md = null;
        try {
            md = getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }

        String hashedPassword = sb.toString();
        return hashedPassword;
    }

}
