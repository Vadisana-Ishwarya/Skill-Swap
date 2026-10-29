package com.skillswap.model;
public class User {
 private int id; private String name,email,password,bio;
 public User(){} public User(String n,String e,String p,String b){name=n;email=e;password=p;bio=b;}
 public int getId(){return id;} public void setId(int v){id=v;}
 public String getName(){return name;} public void setName(String v){name=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getPassword(){return password;} public void setPassword(String v){password=v;}
 public String getBio(){return bio;} public void setBio(String v){bio=v;}
}