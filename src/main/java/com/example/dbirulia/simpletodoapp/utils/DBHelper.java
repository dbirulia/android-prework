package com.example.dbirulia.simpletodoapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dbirulia.simpletodoapp.models.ToDoItem;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDoList.db";
    public static final String TODOLIST_TABLE_NAME = "todos";
    public static final String TODO_COLUMN_ID = "id";
    public static final String TODO_COLUMN_NAME = "name";
    public static final String TODO_COLUMN_DETAILS = "details";
    public static final String TODO_COLUMN_PRIORITY = "priority";
    public static final String TODO_COLUMN_DUEDATE = "duedate";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS todos " +
                        "(id integer primary key, name text, details text, priority text, duedate int)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS todos");
        onCreate(db);
    }

    public long insertToDoItem (String name, String details, String priority, Integer duedate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("details", details);
        contentValues.put("priority", priority);
        contentValues.put("duedate", duedate);
        long id = db.insert("todos", null, contentValues);
        return id;
    }

    public boolean updateToDoItem (Long id, String name, String details, String priority, Integer duedate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("details", details);
        contentValues.put("priority", priority);
        contentValues.put("duedate", duedate);
        db.update("todos", contentValues, "id = ? ", new String[] { Long.toString(id) } );
        return true;
    }

    public Integer deleteToDoItem (Long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("todos",
                "id = ? ",
                new String[] { Long.toString(id) });
    }

    public ArrayList<ToDoItem> getAllToDos()
    {
        ArrayList<ToDoItem> todos_list = new ArrayList<ToDoItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from todos", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String name = res.getString(res.getColumnIndex(TODO_COLUMN_NAME));
            long id = res.getLong(res.getColumnIndex(TODO_COLUMN_ID));
            String details = res.getString(res.getColumnIndex(TODO_COLUMN_DETAILS));
            String priority = res.getString(res.getColumnIndex(TODO_COLUMN_PRIORITY));
            int duedate = res.getInt(res.getColumnIndex(TODO_COLUMN_DUEDATE));
            todos_list.add(new ToDoItem(id, name, details, priority, duedate));
            res.moveToNext();
        }
        return todos_list;
    }

}