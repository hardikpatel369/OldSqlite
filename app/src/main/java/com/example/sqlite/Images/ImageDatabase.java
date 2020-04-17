package com.example.sqlite.Images;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class ImageDatabase extends SQLiteOpenHelper {

    private static final String databaseName = "myImageData";
    private static final String tableName = "myImage";
    private static final String filePath = "FilePath";
    private static final String fileName = "FileName";
    private static final String serviceID = "ServiceID";
    private static final String displayOrder = "DisplayOrder";
    private static final int version = 1;
    private static final String id = "Id";
    private static final String createTable = "CREATE TABLE " + tableName + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + filePath + "  VARCHAR(500) ," + fileName + " VARCHAR(100) ," + serviceID + " INTEGER ," + displayOrder + " INTEGER); ";
    private static final String drop_table = "DROP TABLE IF EXISTS " + tableName;
    private Context context;

    public ImageDatabase(Context context) {
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

    public long InsertData(String FilePath, String FileName, int ServiceID, int DisplayOrder) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(filePath, FilePath);
        contentValues.put(fileName, FileName);
        contentValues.put(serviceID, ServiceID);
        contentValues.put(displayOrder, DisplayOrder);
        long rowId = sqLiteDatabase.insert(tableName, null, contentValues);
        return rowId;
    }

    public ArrayList<ImageModelClass> getData() {
        ArrayList<ImageModelClass> info = new ArrayList<ImageModelClass>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String filePath = cursor.getString(1);
            String fileName = cursor.getString(2);
            String serviceID = cursor.getString(3);
            String displayOrder = cursor.getString(4);
            ImageModelClass imageModelClass = new ImageModelClass(id, filePath, fileName, serviceID, displayOrder);
            info.add(imageModelClass);
        }
        return info;
    }

//    public void Update(int ID, String Name, String Description) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(name, Name);
//        contentValues.put(description, Description);
//        database.update(tableName, contentValues, "id=" + ID, null);
//    }

    public void Delete(int ID) {
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(tableName, "id=" + ID, null);
    }
}
