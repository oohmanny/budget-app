package com.example.budgetingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "budgeting.db"; // Nome del database
    private static final int DATABASE_VERSION = 1; // Versione del database

    // Creazione delle tabelle
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_TYPE = "type"; // Entrata o uscita
    private static final String COLUMN_DATE = "date";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_AMOUNT + " REAL, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_DATE + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE); // Esegui il comando SQL per creare la tabella
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS); // Elimina la tabella esistente
        onCreate(db); // Ricrea la tabella
    }
}
