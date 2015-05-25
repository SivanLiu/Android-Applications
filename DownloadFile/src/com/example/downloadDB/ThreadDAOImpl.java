package com.example.downloadDB;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.entities.ThreadInfo;
/**
 * ���ݷ��ʽӿ�ʵ��
 *
 */
public class ThreadDAOImpl implements ThreadDAOs{

	private DBhelper mHelper=null;
	
	public  ThreadDAOImpl(Context context) {
		mHelper=new DBhelper(context);
	}
	
	@Override
	public void insertThread(ThreadInfo threadInfo) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=mHelper.getWritableDatabase();
		db.execSQL(
				"insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
				new Object[]{threadInfo.getId(),threadInfo.getUrl(),threadInfo.getStart(),
						threadInfo.getEnd(),threadInfo.getFinished()}
				);
		db.close();
	}
	@Override
	public void deleteThread(String url, int thread_id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=mHelper.getWritableDatabase();
		db.execSQL(
				"delete from thread_info where url=? and thread_id=?",
				new Object[]{url,thread_id}
				);
		db.close();
	}
	@Override
	public void updateThread(String url, int thread_id,int finished) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=mHelper.getWritableDatabase();
		db.execSQL(
				"update thread_info set finished=? where url=? and thread_id=?",
				new Object[]{finished,url,thread_id}
				);
		db.close();
	}
	
	@Override
	public List<ThreadInfo> getThreads(String url) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=mHelper.getWritableDatabase();
		List<ThreadInfo> list=new ArrayList<ThreadInfo>();
		Cursor cursor=db.rawQuery("select * from thread_info where url=?", new String[]{url});
		while(cursor.moveToNext()){
			ThreadInfo thread=new ThreadInfo();
			thread.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
			thread.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			thread.setStart(cursor.getInt(cursor.getColumnIndex("start")));
			thread.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
			thread.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
			list.add(thread);
		}
		cursor.close();
		db.close();
		return list;
	}
	@Override
	public boolean isExists(String url, int thread_id) {
			SQLiteDatabase db=mHelper.getWritableDatabase();
			Cursor cursor=db.rawQuery("select * from thread_info where url=? and thread_id=?", 
					new String[]{url,thread_id+""});
				boolean exist=cursor.moveToNext();
				cursor.close();
				db.close();
				return exist;
		
	}
	
}
