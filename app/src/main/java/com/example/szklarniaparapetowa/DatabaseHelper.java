package com.example.szklarniaparapetowa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final Integer NUMBER_OF_COLUMNS = 7;
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "RawData";
    private static final String COL0_ID = "ID";
    private static final String COL1_TIME = "TIME";
    private static final String COL2_TEMPERATURE = "TEMPERATURE";
    private static final String COL3_TEMPERATURE_REFERENCE_SIGNAL = "TEMPERATURE_REFERENCE_SIGNAL";
    private static final String COL4_LIGHT_INTENSITY = "LIGHT_INTENSITY";
    private static final String COL5_LIGHT_INTENSITY_REFERENCE_SIGNAL = "LIGHT_INTENSITY_REFERENCE_SIGNAL";
    private static final String COL6_HUMIDITY = "HUMIDITY";


    public DatabaseHelper(Context context)
    {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL0_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1_TIME + " DATETIME DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')), " +
                COL2_TEMPERATURE + " REAL, " +
                COL3_TEMPERATURE_REFERENCE_SIGNAL + " REAL, " +
                COL4_LIGHT_INTENSITY + " REAL, " +
                COL5_LIGHT_INTENSITY_REFERENCE_SIGNAL + " REAL, " +
                COL6_HUMIDITY + " REAL)";


        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    /**
     * Put data in database
     * @param data - data set
     * @return true if successfully added
     */
    public boolean putData(double[] data)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        //TODO chwilo z palca wpisywane
        contentValues.put(COL2_TEMPERATURE, data[0]);
        contentValues.put(COL3_TEMPERATURE_REFERENCE_SIGNAL, data[1]);
        contentValues.put(COL4_LIGHT_INTENSITY, data[2]);
        contentValues.put(COL5_LIGHT_INTENSITY_REFERENCE_SIGNAL, data[3]);
        contentValues.put(COL6_HUMIDITY, data[4]);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);


        return (result == -1);
    }


    /**
     * Get data from database
     * @param columnName - column name which will be returned
     * @return cursor with data
     */
    public Cursor getData(String columnName)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query;

        query = "SELECT " /*+ COL1_TIME + " "*/ + columnName + " FROM " + TABLE_NAME + " ORDER BY " + COL1_TIME;

        return sqLiteDatabase.rawQuery(query, null);
    }

    public boolean putDataFromJson(double temp, double light, double humidity)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        //TODO chwilo z palca wpisywane
        contentValues.put(COL2_TEMPERATURE, temp);
        //contentValues.put(COL3_TEMPERATURE_REFERENCE_SIGNAL, data[1]);
        contentValues.put(COL4_LIGHT_INTENSITY, light);
        //contentValues.put(COL5_LIGHT_INTENSITY_REFERENCE_SIGNAL, data[3]);
        contentValues.put(COL6_HUMIDITY, humidity);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);


        return (result == -1);
    }

    public void removeData()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);
        String query2 = "DELETE FROM sqlite_sequence WHERE name = 'RawData'";
        sqLiteDatabase.execSQL(query2);
    }
}
