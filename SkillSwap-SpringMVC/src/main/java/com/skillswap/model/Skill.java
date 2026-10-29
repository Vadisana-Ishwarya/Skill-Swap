package com.skillswap.model;
public class Skill {
 private int id; private String name;
 public Skill(){} public Skill(int i,String n){id=i;name=n;}
 public int getId(){return id;} public void setId(int v){id=v;}
 public String getName(){return name;} public void setName(String v){name=v;}
}