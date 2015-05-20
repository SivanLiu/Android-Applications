package com.example.mynotes;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

	
	private Button textbtn,imgbtn,videobtn;
	private ListView lv;
	private Intent i;
	private MyAdapter myAdapter;
	private NoteDB noteDB;
	private SQLiteDatabase dbReader;
	private Cursor cursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		noteDB=new NoteDB(this);
		dbReader=noteDB.getReadableDatabase();
		
		initView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				cursor.moveToPosition(position);
				Intent i=new Intent(MainActivity.this,SelectAct.class);
				i.putExtra(NoteDB.ID, cursor.getInt(cursor.getColumnIndex(NoteDB.ID)));
				i.putExtra(NoteDB.CONTENT, cursor.getString(cursor.getColumnIndex(NoteDB.CONTENT)));
				i.putExtra(NoteDB.TIME, cursor.getString(cursor.getColumnIndex(NoteDB.TIME)));
				i.putExtra(NoteDB.PATH, cursor.getString(cursor.getColumnIndex(NoteDB.PATH)));
				i.putExtra(NoteDB.VIDEO, cursor.getString(cursor.getColumnIndex(NoteDB.VIDEO)));
				startActivity(i);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		lv=(ListView)findViewById(R.id.list);
		textbtn=(Button)findViewById(R.id.text);
		imgbtn=(Button)findViewById(R.id.img);
		videobtn=(Button)findViewById(R.id.video);
		textbtn.setOnClickListener(this);
		imgbtn.setOnClickListener(this);
		videobtn.setOnClickListener(this);
	}
	public void selectDB(){
		cursor=dbReader.query(NoteDB.TABLE_NAME, 
				null, null, null, null, null, null);
		myAdapter=new MyAdapter(this, cursor);
		lv.setAdapter(myAdapter);
	}
	protected void onResume() {
		super.onResume();
		selectDB();
	};
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	i=new Intent(this,AddContent.class);
	switch(v.getId()){
	case R.id.text:
		i.putExtra("flag", "1");
		startActivity(i);
		break;
	case R.id.img:
		i.putExtra("flag", "2");
		startActivity(i);
		break;
	case R.id.video:
		i.putExtra("flag", "3");
		startActivity(i);
		break;
	}
}

}
