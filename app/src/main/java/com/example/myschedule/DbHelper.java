package com.example.myschedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myschedule.data.model.LoggedInUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "1.sqlite";
    private static String DB_PATH = null;
    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    private static final String TABLE_USERS = "users";
    private static final String ID_USER = "id_user";
    private static final String USER_FIO = "fio";
    private static final String MAIL = "mail";
    private static final String ID_GROUP = "id_group";
    private static final String PASSWORD = "password";

    private static final String TABLE_GROUPS = "groups";
    private static final String GROUP_NAME = "group_name";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }


    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }

    public boolean passIsRight(LoggedInUser user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,new String[] {ID_USER,USER_FIO,MAIL,ID_GROUP,PASSWORD},
                PASSWORD+"= ? ",
                new String[] {user.getPassword()},
                null, null, null);
        //cursor.close();
        if(cursor.getCount()>0)
            return true;
        else return false;
    }

    public boolean userIsExist(LoggedInUser user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,new String[] {ID_USER,USER_FIO,MAIL,ID_GROUP,PASSWORD},
        MAIL+"= ? ",
                new String[] {user.getMail()},
                null, null, null);
        //cursor.close();
       if(cursor.getCount()>0)
        return true;
       else return false;
    }

    public boolean groupIsExist(LoggedInUser user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GROUPS,new String[] {ID_GROUP},
                GROUP_NAME+"= ? ",
                new String[] {user.getGroupName()},
                null, null, null);
        //cursor.close();
        if(cursor.getCount()>0)
            return true;
        else return false;
    }
    public int getGroupId(LoggedInUser user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GROUPS,new String[] {ID_GROUP},
                GROUP_NAME+"= ? ",
                new String[] {user.getGroupName()},
                null, null, null);
            if(cursor.getCount()>0)
            return cursor.getInt(0);
            else return 0;
    }

    public void addUser(LoggedInUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
// Задайте значения для каждой строки.
        values.put(USER_FIO, user.getFIO());
        values.put(PASSWORD, user.getPassword());
        //добавление и проверка группы
        if(this.groupIsExist(user)){
            values.put(ID_GROUP,this.getGroupId(user));
            db.insert(TABLE_USERS, null, values);
            ;
        }
        else {

            values1.put(GROUP_NAME, user.getGroupName());
            db.insert(TABLE_GROUPS, null, values1);
        }
        // Inserting Row
        values.put(ID_GROUP,this.getGroupId(user));
        db.insert(TABLE_USERS, null, values);

        db.close(); // Closing database connection
    }

    public LoggedInUser getUser(String mail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,new String[] {USER_FIO,MAIL,ID_GROUP,ID_USER},
                MAIL+"= ? ",
                new String[] {mail},
                null, null, null);
        cursor.moveToFirst();
        LoggedInUser user=new LoggedInUser(cursor.getString(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getInt(3));
        cursor.close();
        return user;
    }
    public ArrayList getAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USERS,new String[] {USER_FIO,MAIL,ID_GROUP,ID_USER}, null,
                null,
                null, null, null);
        cursor.moveToFirst();
        //ArrayList<LoggedInUser> users[]=new LoggedInUser[cursor.getCount()];
        for(int i =0;i<cursor.getCount();i++){

        }
        LoggedInUser user=new LoggedInUser(cursor.getString(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getInt(3));
        cursor.close();
        return users[];
    }
  /*  // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }*/

}
