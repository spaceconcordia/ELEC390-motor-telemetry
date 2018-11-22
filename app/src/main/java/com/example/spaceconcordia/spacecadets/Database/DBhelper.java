package com.example.spaceconcordia.spacecadets.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.spaceconcordia.spacecadets.Data_Types.BigData;
import com.example.spaceconcordia.spacecadets.Database.DBConfig;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


// Original SQLlite database helper by Tawfiq Jawhar

public class DBhelper extends SQLiteOpenHelper {

    private static final String TAG = "TelemetryDatabaseHelper";

    // All Static variables
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = DBConfig.DATABASE_NAME;

    private Context context = null;
    private SQLiteDatabase db;

    private char DELIMITER = ';';

    // Constructor
    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tables SQL execution
        String Create_Table = "CREATE TABLE " + DBConfig.TABLE_TELEMETRY + "("
                + DBConfig.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConfig.Time_ms + " LONG DEFAULT 0, "
                + DBConfig.Status + " STRING NOT NULL, "
                + DBConfig.sens1 + " SHORT DEFAULT 0, "
                + DBConfig.sens2 + " SHORT DEFAULT 0, "
                + DBConfig.sens3 + " SHORT DEFAULT 0, "
                + DBConfig.sens4 + " SHORT DEFAULT 0, "
                + DBConfig.sens5 + " SHORT DEFAULT 0, "
                + DBConfig.sens6 + " SHORT DEFAULT 0, "
                + DBConfig.sens7 + " SHORT DEFAULT 0, "
                + DBConfig.sens8 + " SHORT DEFAULT 0, "
                + DBConfig.sens9 + " SHORT DEFAULT 0, "
                + DBConfig.sens10 + " SHORT DEFAULT 0, "
                + DBConfig.sens11 + " SHORT DEFAULT 0, "
                + DBConfig.sens12 + " SHORT DEFAULT 0, "
                + DBConfig.sens13 + " SHORT DEFAULT 0, "
                + DBConfig.sens14 + " SHORT DEFAULT 0, "
                + DBConfig.sens15 + " SHORT DEFAULT 0, "
                + DBConfig.sens16 + " SHORT DEFAULT 0, "
                + DBConfig.sens17 + " SHORT DEFAULT 0, "
                + DBConfig.sens18 + " SHORT DEFAULT 0, "
                + DBConfig.sens19 + " SHORT DEFAULT 0, "
                + DBConfig.sens20 + " SHORT DEFAULT 0, "
                + DBConfig.sens21 + " SHORT DEFAULT 0, "
                + DBConfig.sens22 + " SHORT DEFAULT 0, "
                + DBConfig.sens23 + " SHORT DEFAULT 0, "
                + DBConfig.sens24 + " SHORT DEFAULT 0, "
                + DBConfig.sens25 + " SHORT DEFAULT 0, "
                + DBConfig.sens26 + " SHORT DEFAULT 0, "
                + DBConfig.sens27 + " SHORT DEFAULT 0, "
                + DBConfig.sens28 + " SHORT DEFAULT 0, "
                + DBConfig.sens29 + " SHORT DEFAULT 0"
                + ")";

        Log.d(TAG,"Table create SQL: " + Create_Table);

        db.execSQL(Create_Table);

        Log.d(TAG,"DB created!");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DBConfig.DATABASE_NAME);

        // Create tables again
        onCreate(db);
    }

    //Insert new assignement
    public long insertValues(BigData CurrentValues){

        long id = -1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        String Status =  "" + CurrentValues.GetEngineStatus();
        contentValues.put(DBConfig.Time_ms, System.currentTimeMillis() );
        contentValues.put(DBConfig.Status, Status);
        contentValues.put(DBConfig.sens1, CurrentValues.GetRawSensorValue(0));
        contentValues.put(DBConfig.sens2, CurrentValues.GetRawSensorValue(1));
        contentValues.put(DBConfig.sens3, CurrentValues.GetRawSensorValue(2));
        contentValues.put(DBConfig.sens4, CurrentValues.GetRawSensorValue(3));
        contentValues.put(DBConfig.sens5, CurrentValues.GetRawSensorValue(4));
        contentValues.put(DBConfig.sens6, CurrentValues.GetRawSensorValue(5));
        contentValues.put(DBConfig.sens7, CurrentValues.GetRawSensorValue(6));
        contentValues.put(DBConfig.sens8, CurrentValues.GetRawSensorValue(7));
        contentValues.put(DBConfig.sens9, CurrentValues.GetRawSensorValue(8));
        contentValues.put(DBConfig.sens10, CurrentValues.GetRawSensorValue(9));
        contentValues.put(DBConfig.sens11, CurrentValues.GetRawSensorValue(10));
        contentValues.put(DBConfig.sens12, CurrentValues.GetRawSensorValue(11));
        contentValues.put(DBConfig.sens13, CurrentValues.GetRawSensorValue(12));
        contentValues.put(DBConfig.sens14, CurrentValues.GetRawSensorValue(13));
        contentValues.put(DBConfig.sens15, CurrentValues.GetRawSensorValue(14));
        contentValues.put(DBConfig.sens16, CurrentValues.GetRawSensorValue(15));
        contentValues.put(DBConfig.sens17, CurrentValues.GetRawSensorValue(16));
        contentValues.put(DBConfig.sens18, CurrentValues.GetRawSensorValue(17));
        contentValues.put(DBConfig.sens19, CurrentValues.GetRawSensorValue(18));
        contentValues.put(DBConfig.sens20, CurrentValues.GetRawSensorValue(19));
        contentValues.put(DBConfig.sens21, CurrentValues.GetRawSensorValue(20));
        contentValues.put(DBConfig.sens22, CurrentValues.GetRawSensorValue(21));
        contentValues.put(DBConfig.sens23, CurrentValues.GetRawSensorValue(22));
        contentValues.put(DBConfig.sens24, CurrentValues.GetRawSensorValue(23));
        contentValues.put(DBConfig.sens25, CurrentValues.GetRawSensorValue(24));
        contentValues.put(DBConfig.sens26, CurrentValues.GetRawSensorValue(25));
        contentValues.put(DBConfig.sens27, CurrentValues.GetRawSensorValue(26));
        contentValues.put(DBConfig.sens28, CurrentValues.GetRawSensorValue(27));
        contentValues.put(DBConfig.sens29, CurrentValues.GetRawSensorValue(28));


        try {
            id = sqLiteDatabase.insert(DBConfig.TABLE_TELEMETRY, null, contentValues);
        } catch (SQLiteException e){
            Log.d(TAG,"Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    //Delete all Assignment with the same course code
    public void SaveToFile(FileOutputStream fos) {


        String Header = String.valueOf(System.currentTimeMillis()) +" : " + Calendar.getInstance().getTime() + "\n\r"+
                "ID" + DELIMITER + DBConfig.Time_ms + DELIMITER + DBConfig.Status + DELIMITER +
                DBConfig.sens1 + DELIMITER + DBConfig.sens2 + DELIMITER + DBConfig.sens3 + DELIMITER + DBConfig.sens4 + DELIMITER + DBConfig.sens5 + DELIMITER +
                DBConfig.sens6 + DELIMITER + DBConfig.sens7 + DELIMITER + DBConfig.sens8 + DELIMITER + DBConfig.sens9 + DELIMITER + DBConfig.sens10 + DELIMITER +
                DBConfig.sens11 + DELIMITER + DBConfig.sens12 + DELIMITER + DBConfig.sens13 + DELIMITER + DBConfig.sens14 + DELIMITER + DBConfig.sens15 + DELIMITER +
                DBConfig.sens16 + DELIMITER + DBConfig.sens17 + DELIMITER + DBConfig.sens18 + DELIMITER + DBConfig.sens19 + DELIMITER + DBConfig.sens20 + DELIMITER +
                DBConfig.sens21 + DELIMITER + DBConfig.sens22 + DELIMITER + DBConfig.sens23 + DELIMITER + DBConfig.sens24 + DELIMITER + DBConfig.sens25 + DELIMITER +
                DBConfig.sens26 + DELIMITER + DBConfig.sens27 + DELIMITER + DBConfig.sens28 + DELIMITER + DBConfig.sens29+ "\n\r";

        try {
            fos.write(Header.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBConfig.TABLE_TELEMETRY, null);

        String Buffer = "";

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Buffer = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBConfig.COLUMN_ID))) + DELIMITER +
                        String.valueOf(cursor.getLong(cursor.getColumnIndex(DBConfig.Time_ms))) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.Status)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens1)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens2)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens3)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens4)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens5)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens6)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens7)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens8)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens9)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens10)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens11)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens12)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens13)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens14)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens15)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens16)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens17)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens18)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens19)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens20)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens21)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens22)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens23)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens24)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens25)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens26)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens27)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens28)) + DELIMITER +
                        cursor.getString(cursor.getColumnIndex(DBConfig.sens29)) + "\n\r";

                try {
                    fos.write(Buffer.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                cursor.moveToNext();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();
        db.delete(DBConfig.TABLE_TELEMETRY,null,null);
        db.close();

    }
    }