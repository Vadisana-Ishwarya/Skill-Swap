package com.skillswap.model;
public class UserSkill {
 private int id,userId,skillId; private String skillName,type;
 public int getId(){return id;} public void setId(int v){id=v;}
 public int getUserId(){return userId;} public void setUserId(int v){userId=v;}
 public int getSkillId(){return skillId;} public void setSkillId(int v){skillId=v;}
 public String getSkillName(){return skillName;} public void setSkillName(String v){skillName=v;}
 public String getType(){return type;} public void setType(String v){type=v;}
}