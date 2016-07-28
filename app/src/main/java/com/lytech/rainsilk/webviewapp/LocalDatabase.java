package com.lytech.rainsilk.webviewapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by rainsilk on 2016/1/22.
 */
public class LocalDatabase {
    public static final String SP_NAME ="UserDetails";
    SharedPreferences localDatabase;

    public LocalDatabase(Context context)
    {
        localDatabase = context.getSharedPreferences(SP_NAME , 0);
    }
    public void storeData(Contact contact)
    {
        SharedPreferences.Editor spEditor = localDatabase.edit();
        spEditor.putString("Name",contact.name);
        spEditor.putString("Email",contact.email);
        spEditor.putString("Username",contact.uname);
        spEditor.putString("Password",contact.pass);
        spEditor.putString("Phone",contact.phone_num);
        
        spEditor.putString("Address",contact.address);
        spEditor.putString("Job",contact.job);
        spEditor.putString("Introperson",contact.introperson);
        spEditor.putString("Lineid",contact.lineid);
        spEditor.putString("ID",contact.id);
        spEditor.putString("Count",contact.count);
        spEditor.putString("Giftount",contact.giftcount);
        spEditor.putString("Carount",contact.carcount);
        spEditor.putString("Evaluaion",contact.evaluaion);
        spEditor.putString("Performance",contact.performance);
        spEditor.putString("Level",contact.level);

        Log.e("rainsilkinfo","storeData");
        spEditor.commit();
    }
    public Contact getLoggedInUser()
    {
        String name = localDatabase.getString("Name","");
        String email = localDatabase.getString("Email","");
        String username = localDatabase.getString("Username","");
        String password = localDatabase.getString("Password","");
        String phonenum = localDatabase.getString("Phone","");

        String address = localDatabase.getString("Address","");
        String assoiation= localDatabase.getString("Assoiation","");
        String job= localDatabase.getString("Job","");

        String introperson = localDatabase.getString("Introperson","");
        String lineid = localDatabase.getString("Lineid","");

        String id = localDatabase.getString("ID","");
        String count = localDatabase.getString("Count","");
        String giftcount = localDatabase.getString("Giftount","");
        String carcount = localDatabase.getString("Carount","");
        String evaluation = localDatabase.getString("Evaluation","");
        String performance = localDatabase.getString("Performance","");
        String level = localDatabase.getString("Level","");

        Contact storedContact = new Contact(name,email,username,password,phonenum,address,assoiation,job,introperson,lineid,id,count,giftcount,carcount,evaluation,performance,level);
        return  storedContact;

    }
    public void setUserLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor spEditor = localDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
        Log.e("rainsilkinfo", "setUserLoggedIn commit");
    }
    public boolean getUserLoggedIn()
    {
        Log.e("rainsilkinfo", "getUserLoggedIn boolean");
        if(localDatabase.getBoolean("loggedIn",false))
            return true;
        else
            return false;

    }
    public void cleanData()
    {
        SharedPreferences.Editor spEditor = localDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

}
