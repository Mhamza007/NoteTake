package com.mhamza007.notetake

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {

    val dbName = "MyNotes"
    val dbTable = "Notes"
    val colId = "ID"
    val colTitle = "Title"
    val colDes = "Description"
    val dbVersion = 1
//  CREATE TABLE IF NOT EXISTS Notes (ID INTEGER PRIMARY KEY, Title TEXT, Description TEXT);"
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS $dbTable ($colId INTEGER PRIMARY KEY," +
            "$colTitle TEXT,$colDes TEXT);"
    var sqlDB : SQLiteDatabase? = null

    constructor(context: Context){
        var db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }


    inner class DatabaseHelperNotes : SQLiteOpenHelper {

        var context : Context? = null

        constructor(context : Context) : super(context, dbName, null, dbVersion){
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "Database Created", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS $dbTable")
        }
    }

    //CRUD

    //adding new note
    fun insert(value : ContentValues) : Long? {

        val ID = sqlDB?.insert(dbTable, "", value)
        return ID
    }

    //view notes
    fun query(projection : Array<String>, selection : String, selectionArgs : Array<String>, sortOrder : String) : Cursor {

        val db = SQLiteQueryBuilder()
        db.tables = dbTable

        return db.query(sqlDB, projection, selection , selectionArgs, null, null, sortOrder)
    }

    //update notes
    fun update(values : ContentValues, selection: String, selectionArgs: Array<String>) : Int? {

        return sqlDB?.update(dbTable, values, selection, selectionArgs)
    }

    //delete notes
    fun delete(selection : String, selectionArgs : Array<String>) : Int? {

        return sqlDB?.delete(dbTable, selection, selectionArgs)
    }

}