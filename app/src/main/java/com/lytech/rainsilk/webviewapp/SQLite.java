package com.lytech.rainsilk.webviewapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rainsilk on 2015/12/17.
 */
public class SQLite extends SQLiteOpenHelper {
    private static final int VERSION = 1; //SQL Version
    private static final String Database_Name="contact.db";
    private static final String Table_Contact = "contacts";

    //table columns
    private static final String KEY_ID ="id";
    private static final String KEY_NAME="name";
    private static final String KEY_EMAIL="email";
    private static final String KEY_UNAME="uname";
    private static final String KEY_PASS="pass";
    private static final String KEY_PHONE="phone";

    private static final String KEY_ADDRESS ="address";
    private static final String KEY_JOB="job";
    private static final String KEY_INTROPERSON="introperson";
    private static final String KEY_LINEID="lineid";

    //private static final String KEY_PH_NUM="phone_num";
    SQLiteDatabase db;
    private static final String TABLE_CREATE = "create table contacts (id integer PRIMARY KEY not null , " +
            "name text not null, email text not null ,uname text not null, pass text not null, phone text not null, address text not null, job text not null, introperson text not null, lineid text not null);";

    //constructor
    public SQLite(Context context) {
        super(context, Database_Name, null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table TABLENAME
         db.execSQL(TABLE_CREATE);
         this.db = db;
    }
    public void insertContact(Contact c)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        values.put(KEY_ID,count);
        values.put(KEY_NAME,c.getName());
        values.put(KEY_EMAIL,c.getEmail());
        values.put(KEY_UNAME,c.getUname());
        values.put(KEY_PASS,c.getPass());
        values.put(KEY_PHONE,c.getPhone_num());

        values.put(KEY_ADDRESS,c.getaddress());
        values.put(KEY_JOB,c.getjob());
        values.put(KEY_INTROPERSON,c.getintroperson());
        values.put(KEY_LINEID,c.getlineid());

        db.insert(Table_Contact, null, values);
        db.close();


    }
    public String searchPass(String uname)
    {
        db = this.getReadableDatabase();
        String query = "select uname, pass from "+Table_Contact;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;

        b="not found";
        if(cursor.moveToFirst())
        {
            do{
                a= cursor.getString(0);

                if(a.equals(uname))
                {
                    b=cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        return  b;

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+Table_Contact);
        this.onCreate(db);

        /*//add new contact
        public void addContact(Contact contact)
        {

        }
        //get contact fron db
        public Contact getContact(int id)
        {

        }
        //get alll contact
        public List<Contact> getAllContacts()
        {

        }
        public int getContactsCount()
        {

        }

        public int updateContact(Contact contact)
        {

        }

        //delete contact
        public void deleteContact(Contact contact)
        {

        }*/
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
