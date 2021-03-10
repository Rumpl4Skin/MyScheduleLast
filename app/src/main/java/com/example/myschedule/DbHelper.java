package com.example.myschedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myschedule.data.Admins;
import com.example.myschedule.data.Docs;
import com.example.myschedule.data.Subject;
import com.example.myschedule.data.model.LoggedInUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 9;
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

    private static final String TABLE_ADMINS = "admins";
    private static final String ID_ADMINS = "id_admins";
    private static final String ADMINS_FIO = "fio_admins";
    private static final String ADMINS_DOLJN = "doljn";
    private static final String ADMINS_IMG = "admins_img";
    private static final String ADMINS_PHONE = "phone";

    private static final String TABLE_DOCS = "Docs";
    private static final String ID_DOC = "id_doc";
    private static final String NAME_DOC = "name_doc";
    private static final String IMG_DOC = "doc_img";

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
    public boolean userIsExistAny(LoggedInUser user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,new String[] {ID_USER,USER_FIO,MAIL,ID_GROUP,PASSWORD},
                MAIL+"= ? OR "+ID_USER+"= ? ",
                new String[] {user.getMail(),""+user.getIdUser()},
                null, null, null);
        //cursor.close();
        if(cursor.getCount()>0)
            return true;
        else return false;
    }
    public boolean adminIsExistAny(Admins admin) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMINS,new String[] {ID_ADMINS,ADMINS_FIO,ADMINS_DOLJN,ADMINS_IMG},
                ADMINS_FIO+"= ? OR "+ID_ADMINS+"= ? ",
                new String[] {admin.getFio(),""+admin.getId_admins()},
                null, null, null);
        //cursor.close();
        if(cursor.getCount()>0)
            return true;
        else return false;
    }
    public boolean userIsExistWithMail(String mail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,new String[] {ID_USER,USER_FIO,MAIL,ID_GROUP,PASSWORD},
                MAIL+"= ? ",
                new String[] {mail},
                null, null, null);
        if(cursor.getCount()>0)
            return true;
        else return false;
    }

    public boolean adminIsExistWithFio(String fio) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMINS,new String[] {ID_ADMINS,ADMINS_FIO},
                ADMINS_FIO+"= ? ",
                new String[] {fio},
                null, null, null);
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
        cursor.moveToFirst();
        //cursor.close();
        if(cursor.getCount()>0)
            return true;
        else return false;
    }
    public int getGroupId(LoggedInUser user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GROUPS,new String[] {ID_GROUP},
                GROUP_NAME+"= ? ",
                new String[] {user.getGroupName().toString()},
                null, null, null);
        cursor.moveToFirst();
            if(cursor.getCount()>0)
            return cursor.getInt(0);
            else return 0;
    }
    public int getGroupId(String groupName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GROUPS,new String[] {ID_GROUP},
                GROUP_NAME+"= ? ",
                new String[] {groupName},
                null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0)
            return cursor.getInt(0);
        else return 0;
    }
    public String getGroupName(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GROUPS,new String[] {GROUP_NAME},
                ID_GROUP+"= ? ",
                new String[] {id.toString()},
                null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0)
            return cursor.getString(0);
        else return "NONE";
    }

    public void addUser(LoggedInUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
// Задайте значения для каждой строки.
        values.put(USER_FIO, user.getFIO());
        values.put(PASSWORD, user.getPassword());
        values.put(MAIL, user.getMail());
        //добавление и проверка группы
        if(this.groupIsExist(user)){//если группа существует
            values.put(ID_GROUP,this.getGroupId(user));
            db.insert(TABLE_USERS, null, values);
        }
        else {//создание группы и добавление пользователя

            values1.put(GROUP_NAME, user.getGroupName());
            db.insert(TABLE_GROUPS, null, values1);
            values.put(ID_GROUP,this.getGroupId(user));
            db.insert(TABLE_USERS, null, values);
        }

        values.clear();
        values1.clear();
    }

    public LoggedInUser getUser(String mail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,new String[] {ID_USER,USER_FIO,MAIL,PASSWORD,ID_GROUP},
                MAIL+"= ? ",
                new String[] {mail},
                null, null, null);
        cursor.moveToFirst();
        LoggedInUser user=new LoggedInUser(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                this.getGroupName(cursor.getInt(4)));
        cursor.close();
        return user;
    }
   final public LoggedInUser[] getAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USERS,new String[] {ID_USER,USER_FIO,MAIL,PASSWORD,ID_GROUP}, null,
                null,
                null, null, null);
        cursor.moveToFirst();
        LoggedInUser[] users=new LoggedInUser[cursor.getCount()];
        for(int i =0;i<cursor.getCount();i++){
            LoggedInUser user=new LoggedInUser(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    this.getGroupName(cursor.getInt(4))
                   );
                         users[i]=new LoggedInUser(user);
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

   /* final public Subject[] getAllDisc() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_ADMINS,new String[] {ID_ADMINS,ADMINS_FIO,ADMINS_DOLJN,ADMINS_IMG,ADMINS_PHONE}, null,
                null,
                null, null, null);
        cursor.moveToFirst();
        Subject[] subjects=new Subject[cursor.getCount()];
        for(int i =0;i<cursor.getCount();i++){
            Subject subject=new Subject(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            subjects[i]=new Subject(subject);
            cursor.moveToNext();
        }
        cursor.close();
        return admins;
    }*/

    final public Admins[] getAllAdmins() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_ADMINS,new String[] {ID_ADMINS,ADMINS_FIO,ADMINS_DOLJN,ADMINS_IMG,ADMINS_PHONE}, null,
                null,
                null, null, null);
        cursor.moveToFirst();
        Admins[] admins=new Admins[cursor.getCount()];
        for(int i =0;i<cursor.getCount();i++){
            Admins admin=new Admins(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            admins[i]=new Admins(admin);
            cursor.moveToNext();
        }
        cursor.close();
        return admins;
    }

    final public Docs[] getAllDocs() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_DOCS,new String[] {ID_DOC,NAME_DOC,IMG_DOC}, null,
                null,
                null, null, null);
        cursor.moveToFirst();
        Docs[] docs=new Docs[cursor.getCount()];
        for(int i =0;i<cursor.getCount();i++){
            Docs doc=new Docs(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            docs[i]=new Docs(doc);
            cursor.moveToNext();
        }
        cursor.close();
        return docs;
    }

    public void userUpdate(LoggedInUser user,LoggedInUser new_user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(USER_FIO,new_user.getFIO());
        values.put(MAIL,new_user.getMail());
        values.put(PASSWORD,new_user.getPassword());
        values.put(ID_GROUP,this.getGroupId(new_user));

        db.update(TABLE_USERS,
                values,
                MAIL+"=?",
                new String[] {user.getMail()});

    }
    public String[] getAllGroupName() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GROUPS,new String[] {GROUP_NAME}, null,
                null,
                null, null, null);
        cursor.moveToFirst();
        String[] groupNames=new String[cursor.getCount()];
        for(int i =0;i<cursor.getCount();i++){
            groupNames[i]=new String(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return groupNames;
    }
    public boolean groupNameExist() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GROUPS,new String[] {GROUP_NAME}, null,
                null,
                null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0)
        return true;
        else  return false;
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
