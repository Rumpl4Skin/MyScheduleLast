package com.example.myschedule;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DbHelper extends SQLiteOpenHelper {

        static private final int DB_VERSION = 1;
        private static DbHelper mInstance = null;
        private Context mContext;

        DbHelper(Context context) {
            // конструктор суперкласса
            super(context, "kaDB", null, DB_VERSION);
            mContext = context;
        }

        public static DbHelper getInstance(Context ctx) {

            // Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
            // See this article for more information: http://bit.ly/6LRzfx
            if (mInstance == null) {
                mInstance = new DbHelper(ctx.getApplicationContext());
            }
            return mInstance;
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            {
                db.execSQL("create table points ("
                        + "id integer primary key,"
                        + "id_point integer,"
                        + "ord integer,"
                        + "city_id text,"
                        + "photo text,"
                        + "images text,"
                        + "logo text,"
                        + "lat text,"
                        + "lng text,"
                        + "lang text,"
                        + "sound text,"
                        + "text text,"
                        + "name text,"
                        + "type text,"
                        + "visible text,"
                        + "tags text,"
                        + "is_excursion text,"
                        + "isFave integer DEFAULT 0,"
                        + "isWas integer DEFAULT 0,"
                        + "excursion_order integer DEFAULT 0,"
                        + "last_edit_time integer DEFAULT 0" + ");");
                db.execSQL("create table cities ("
                        + "id_locale integer primary key,"
                        + "logo text,"
                        + "logo_cache text DEFAULT 0,"
                        + "logo_hash int DEFAULT 0,"
                        + "id text,"
                        + "lang text,"
                        + "name text,"
                        + "visible text,"
                        + "isSave integer DEFAULT 0,"
                        + "isFave integer DEFAULT 0,"
                        + "last_edit_time integer DEFAULT 0" + ");");
                db.execSQL("create table langs ("
                        + "id integer primary key,"
                        + "name text,"
                        + "key text" + ");");
                db.execSQL("create table tags ("
                        + "id integer primary key,"
                        + "tag_id integer,"
                        + "tag_label text,"
                        + "lang text" + ");");
                db.execSQL("create table savings ("
                        + "id integer primary key,"
                        + "point_id integer,"
                        + "audio_cache text,"
                        + "images_cache text,"
                        + "logo_cache text,"
                        + "audio_hash integer DEFAULT 0,"
                        + "images_hash integer DEFAULT 0,"
                        + "logo_hash integer DEFAULT 0" + ");");
                db.execSQL("create table tag_point_junc ("
                        + "id integer primary key autoincrement,"
                        + "tag_id integer,"
                        + "point_id integer,"
                        + "FOREIGN KEY(tag_id) REFERENCES tags(tag_id),"
                        + "FOREIGN KEY(point_id) REFERENCES points(id_point));");
                db.execSQL("create table excursions ("
                        + "id integer primary key,"
                        + "name text,"
                        + "logo text,"
                        + "lang text,"
                        + "author text,"
                        + "duration text,"
                        + "description text,"
                        + "excursion integer,"
                        + "points text,"
                        + "last_edit_time integer DEFAULT 0" + ");");
            }
            setDefaultContent(db);
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    void setDefaultContent(SQLiteDatabase db) {
            String jsonStr = jsonReader("langs.txt", mContext);
            ContentValues cv = new ContentValues();
            try {
                JSONArray langs = new JSONArray(jsonStr);
                for (int i = 0; i < langs.length(); i++) {
                    JSONObject c = langs.getJSONObject(i);
                    Cursor c0 = db.rawQuery("SELECT id FROM langs where id = " + c.getString("id"), null);
                    c0.moveToFirst();
                    cv.put("id", c.getString("id"));
                    cv.put("key", c.getString("key"));
                    cv.put("name", c.getString("name"));
                    db.beginTransaction();
                    if ((c0.getCount() > 0)) {
                        db.update("langs", cv, "id = ?", new String[]{c.getString("id")});
                    } else {
                        db.insert("langs ", null, cv);
                    }
                    c0.close();
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }
            } catch (final JSONException e) {
                e.printStackTrace();
            }

            jsonStr = jsonReader("city_sources.txt", mContext);

            cv = new ContentValues();
            try {
                JSONArray cities = new JSONArray(jsonStr);
                for (int i = 0; i < cities.length(); i++) {
                    JSONObject c = cities.getJSONObject(i);
                    Cursor c2 = db.rawQuery("SELECT id_locale FROM cities where id_locale = " + c.getString("id_locale"), null);
                    c2.moveToFirst();
                    cv.put("id", c.getString("id"));
                    cv.put("logo", c.getString("logo"));
                    cv.put("id_locale", c.getString("id_locale"));
                    cv.put("lang", c.getString("lang"));
                    cv.put("name", c.getString("name"));
                    cv.put("visible", c.getString("visible"));
                    cv.put("last_edit_time", c.getString("last_edit_time"));
                    db.beginTransaction();
                    if ((c2.getCount() > 0)) {
                        db.update("cities", cv, "id_locale = ?", new String[]{c.getString("id_locale")});
                    } else {
                        db.insert("cities ", null, cv);
                    }
                    c2.close();
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }
            } catch (final JSONException e) {
                e.printStackTrace();
            }

            jsonStr = jsonReader("point_sources.txt", mContext);

            cv = new ContentValues();
            try {
                JSONArray points = new JSONArray(jsonStr);
                for (int i = 0; i < points.length(); i++) {
                    JSONObject c = points.getJSONObject(i);
                    Cursor c1 = db.rawQuery("SELECT id FROM points where id = " + c.getString("id"), null);
                    c1.moveToFirst();

                    cv.put("id", c.getString("id"));
                    cv.put("id_point", c.getString("id_point"));
                    cv.put("photo", c.getString("photo"));
                    cv.put("logo", c.getString("logo"));
                    cv.put("ord", Integer.parseInt(c.getString("order")));
                    cv.put("lat", c.getString("lat"));
                    cv.put("lng", c.getString("lng"));
                    cv.put("lang", c.getString("lang"));
                    cv.put("sound", c.getString("sound"));
                    cv.put("images", c.getString("images"));
                    cv.put("text", c.getString("text"));
                    cv.put("name", c.getString("name"));
                    cv.put("visible", c.getString("visible"));
                    cv.put("city_id", c.getString("city_id"));
                    cv.put("last_edit_time", c.getString("last_edit_time"));
                    db.beginTransaction();
                    if ((c1.getCount() > 0)) {
                        db.update("points", cv, "id = ?", new String[]{c.getString("id")});
                    } else {
                        db.insert("points ", null, cv);
                    }
                    c1.close();
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
    public static String jsonReader(String jsonName, Context context) {
        String jsonStr;
        try {
            jsonStr = "";
            AssetManager am = context.getAssets();
            InputStream is = am.open(jsonName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                jsonStr += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonStr;

    }
}
