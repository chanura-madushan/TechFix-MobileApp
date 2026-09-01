package com.example.techfix.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
public final class PasswordUtils {
 private PasswordUtils(){}
 public static String hash(String password){
  try{byte[] bytes=MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8)); StringBuilder sb=new StringBuilder(); for(byte b:bytes) sb.append(String.format("%02x",b)); return sb.toString();}catch(Exception e){throw new RuntimeException(e);}
 }
 public static boolean verify(String password,String hashedPassword){return hash(password).equals(hashedPassword);}
}