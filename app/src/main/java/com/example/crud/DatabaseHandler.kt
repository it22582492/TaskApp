package com.example.crud

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION)  {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "TaskDatabase"
        private val TABLE_CONTACTS = "TaskTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_DETAILS = "details"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DETAILS + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }


    //method to insert data
    fun addTask(task: TaskModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, task.taskId)
        contentValues.put(KEY_NAME, task.taskName)
        contentValues.put(KEY_DETAILS,task.taskDetails )
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)

        db.close() // Closing database connection
        return success
    }
    //method to read data
    @SuppressLint("Range")
    fun viewTask():List<TaskModelClass>{
        val taskList:ArrayList<TaskModelClass> = ArrayList<TaskModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var taskId: Int
        var taskName: String
        var taskDetails: String
        if (cursor.moveToFirst()) {
            do {
                taskId = cursor.getInt(cursor.getColumnIndex("id"))
                taskName = cursor.getString(cursor.getColumnIndex("name"))
                taskDetails = cursor.getString(cursor.getColumnIndex("details"))
                val task= TaskModelClass(taskId = taskId, taskName = taskName, taskDetails = taskDetails)
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        return taskList
    }
    //method to update data
    fun updateTask(task: TaskModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, task.taskId)
        contentValues.put(KEY_NAME, task.taskName)
        contentValues.put(KEY_DETAILS,task.taskDetails)

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+task.taskId,null)

        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteTask(task: TaskModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, task.taskId) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"id="+task.taskId,null)

        db.close() // Closing database connection
        return success
    }
}