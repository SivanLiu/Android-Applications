package com.example.downloadDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 创建数据库
 * 
 */
public class DBhelper extends SQLiteOpenHelper{
	private static final String DB_NAME="download.db";
	private static final int VERSION=1;
	private static final String SQL_CREATE="create table thread_info(_id integer primary key autoincrement,"
			+ "thread_id integer,url text,start integer,end integer,finished integer)";
	private static final String SQL_DROP="drop table if exists thread_info";
	public DBhelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg0, int arg1) {
		db.execSQL(SQL_DROP);
		db.execSQL(SQL_CREATE);
	}

}
