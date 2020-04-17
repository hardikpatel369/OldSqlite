package com.example.sqlite.Features;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class FeatureDatabase extends SQLiteOpenHelper {

    private static final String databaseName = "myFeatureData";
    private static final String tableName = "myFeature";
    private static final String name = "Name";
    private static final String description = "Description";
    private static final int version = 1;
    private static final String id = "Id";
    private static final String createTable = "CREATE TABLE " + tableName + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + name + " TEXT unique ," + description + " VARCHAR(200) ); ";
    private static final String drop_table = "DROP TABLE IF EXISTS " + tableName;
    private Context context;

    public FeatureDatabase(Context context) {
        super(context, databaseName, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(createTable);
        } catch (Exception e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(drop_table);
            onCreate(db);
        } catch (Exception e) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public long InsertData(String Name, String Description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(name, Name);
        contentValues.put(description, Description);
        long rowId = sqLiteDatabase.insert(tableName, null, contentValues);
        return rowId;
    }

    public ArrayList<FeatureModelClass> getData() {
        ArrayList<FeatureModelClass> info = new ArrayList<FeatureModelClass>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            FeatureModelClass featureModelClass = new FeatureModelClass(id, name, description);
            info.add(featureModelClass);
        }
        return info;
    }

    public void Update(int ID, String Name, String Description) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(name, Name);
        contentValues.put(description, Description);
        database.update(tableName, contentValues, "id=" + ID, null);
    }

    public void Delete(int ID) {
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(tableName, "id=" + ID, null);
    }
}
