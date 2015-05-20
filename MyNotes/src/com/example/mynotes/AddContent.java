package com.example.mynotes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

public class AddContent extends Activity implements OnClickListener {

	private NoteDB noteDB;
	private SQLiteDatabase dbWriter;
	
	private String val;
	private Button savebtn,deletebtn;
	private EditText ettext;
	private ImageView c_img;
	private VideoView c_video;
	private File phoneFile;
	private File VideoFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_content);
		val=getIntent().getStringExtra("flag");
		noteDB=new NoteDB(this);
		dbWriter=noteDB.getWritableDatabase();
		
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		savebtn=(Button)findViewById(R.id.save);
		deletebtn=(Button) findViewById(R.id.delete);
		ettext=(EditText) findViewById(R.id.ettext);
		c_img=(ImageView) findViewById(R.id.c_img);
		c_video=(VideoView) findViewById(R.id.c_video);
		savebtn.setOnClickListener(this);
		deletebtn.setOnClickListener(this);
		if(val.equals("1")){
			c_img.setVisibility(View.GONE);
			c_video.setVisibility(View.GONE);
		}
		if(val.equals("2")){
			c_img.setVisibility(View.VISIBLE);
			c_video.setVisibility(View.GONE);
			Log.e("IMG","showed");
			Intent iimg=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			phoneFile=new File(Environment.getExternalStorageDirectory().
					getAbsoluteFile()+"/"+getTime()+".jpg");
			System.out.println(phoneFile.getAbsolutePath());
			iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
			startActivityForResult(iimg, 1);
		}
		if(val.equals("3")){
			c_img.setVisibility(View.GONE);
			c_video.setVisibility(View.VISIBLE);
			Intent iimg=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			phoneFile=new File(Environment.getExternalStorageDirectory().
					getAbsoluteFile()+"/"+getTime()+".mp4");
			
			iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
			startActivityForResult(iimg, 2);
		}
	}
	public void addDB(){
		ContentValues cv=new ContentValues();
		cv.put(NoteDB.CONTENT,ettext.getText().toString());
		cv.put(NoteDB.TIME,getTime());
		cv.put(NoteDB.PATH, phoneFile+"");
		cv.put(NoteDB.VIDEO,VideoFile+"");
		dbWriter.insert(NoteDB.TABLE_NAME, null, cv);
	}
	public String getTime(){
		SimpleDateFormat formate=new SimpleDateFormat("yyyyMMddHHMMSS");
		Date curDate=new Date(0);
		String str=formate.format(curDate);
		return str;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.save:
			addDB();
			finish();
			break;
		case R.id.delete:
			finish();
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1){
			Bitmap bitmap=BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
			c_img.setImageBitmap(bitmap);
			Log.e("Success","KO");
		}
		if(resultCode==2){
			c_video.setVideoURI(Uri.fromFile(VideoFile.getAbsoluteFile()));
			c_video.start();
		}
	}
	//将本地图片转化为Bitmap
	public Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
   // 设置为ture只获取图片大小
       opts.inJustDecodeBounds = true;
       opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
       BitmapFactory.decodeFile(path, opts);
       int width = opts.outWidth;
      int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
           // 缩放
           scaleWidth = ((float) width) / w;
           scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
      float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
       return Bitmap.createScaledBitmap(weak.get(), w, h, true);
   }
}
