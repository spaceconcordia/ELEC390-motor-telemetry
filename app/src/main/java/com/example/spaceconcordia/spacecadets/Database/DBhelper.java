package com.example.spaceconcordia.spacecadets.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.spaceconcordia.spacecadets.Data_Types.BigData;
import com.example.spaceconcordia.spacecadets.Database.DBConfig;


// Original SQLlite database helper by Tawfiq Jawhar

public class DBhelper extends SQLiteOpenHelper {

    private static final String TAG = "TelemetryDatabaseHelper";

    // All Static variables
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = DBConfig.DATABASE_NAME;

    private Context context = null;
    // Constructor
    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tables SQL execution
        String CREATE_ASSIGNMENT_TABLE = "CREATE TABLE " + DBConfig.TABLE_TELEMETRY + "("
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

        Log.d(TAG,"Table create SQL: " + CREATE_ASSIGNMENT_TABLE);

        db.execSQL(CREATE_ASSIGNMENT_TABLE);

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
        String Status = "" + CurrentValues.GetEngineStatus();
        contentValues.put(DBConfig.Status, Status);
        contentValues.put(DBConfig.Time_ms, System.currentTimeMillis() );
        contentValues.put(DBConfig.sens1, CurrentValues.GetSensorValue(0));
        contentValues.put(DBConfig.sens2, CurrentValues.GetSensorValue(1));
        contentValues.put(DBConfig.sens3, CurrentValues.GetSensorValue(2));
        contentValues.put(DBConfig.sens4, CurrentValues.GetSensorValue(3));
        contentValues.put(DBConfig.sens5, CurrentValues.GetSensorValue(4));
        contentValues.put(DBConfig.sens6, CurrentValues.GetSensorValue(5));
        contentValues.put(DBConfig.sens7, CurrentValues.GetSensorValue(6));
        contentValues.put(DBConfig.sens8, CurrentValues.GetSensorValue(7));
        contentValues.put(DBConfig.sens9, CurrentValues.GetSensorValue(8));
        contentValues.put(DBConfig.sens10, CurrentValues.GetSensorValue(9));
        contentValues.put(DBConfig.sens11, CurrentValues.GetSensorValue(10));
        contentValues.put(DBConfig.sens12, CurrentValues.GetSensorValue(11));
        contentValues.put(DBConfig.sens13, CurrentValues.GetSensorValue(12));
        contentValues.put(DBConfig.sens14, CurrentValues.GetSensorValue(13));
        contentValues.put(DBConfig.sens15, CurrentValues.GetSensorValue(14));
        contentValues.put(DBConfig.sens16, CurrentValues.GetSensorValue(15));
        contentValues.put(DBConfig.sens17, CurrentValues.GetSensorValue(16));
        contentValues.put(DBConfig.sens18, CurrentValues.GetSensorValue(17));
        contentValues.put(DBConfig.sens19, CurrentValues.GetSensorValue(18));
        contentValues.put(DBConfig.sens20, CurrentValues.GetSensorValue(19));
        contentValues.put(DBConfig.sens21, CurrentValues.GetSensorValue(20));
        contentValues.put(DBConfig.sens22, CurrentValues.GetSensorValue(21));
        contentValues.put(DBConfig.sens23, CurrentValues.GetSensorValue(22));
        contentValues.put(DBConfig.sens24, CurrentValues.GetSensorValue(23));
        contentValues.put(DBConfig.sens25, CurrentValues.GetSensorValue(24));
        contentValues.put(DBConfig.sens26, CurrentValues.GetSensorValue(25));
        contentValues.put(DBConfig.sens27, CurrentValues.GetSensorValue(26));
        contentValues.put(DBConfig.sens28, CurrentValues.GetSensorValue(27));
        contentValues.put(DBConfig.sens29, CurrentValues.GetSensorValue(28));


        try {
            id = sqLiteDatabase.insertOrThrow(DBConfig.TABLE_TELEMETRY, null, contentValues);
        } catch (SQLiteException e){
            Log.d(TAG,"Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    //Delete all Assignment with the same course code
    public void DropDB(String codeCourse) {
    }
    }