package com.example.ecotrack_v1;

public class ProfileModel {
    private String name;
    private String email;

    public ProfileModel(String name,String email)
    {
        this.name = name;
        this.email = email;

    }
    public  String getName ()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getEmail()
     {
           return email;
     }
    
    
}
