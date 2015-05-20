package com.example.howold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final int PICK_CODE = 0x110;
	private ImageView mPhoto;
	private Button mGetImage;
	private Button mDetect;
	private TextView mTip;
	private View mWaitting;
	
	private String mCurrentPhotoStr;
	private Bitmap mPhotoImage;
	
	private Paint mPaint;
	private static final int MSG_SUCCESS=0x111;
	private static final int MSG_ERROR=0x112;
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_SUCCESS:
				mWaitting.setVisibility(View.GONE);
				JSONObject rs=(JSONObject)msg.obj;
				Log.e("Before", "prepareRsBitmap");
				prepareRsBitmap(rs);
				Log.e("After", "prepareRsBitmap");
				mPhoto.setImageBitmap(mPhotoImage);
				break;
			case MSG_ERROR:
				mWaitting.setVisibility(View.GONE);
				String errorMsg=(String)msg.obj;
				if(TextUtils.isEmpty(errorMsg)){
					mTip.setText("Error.");
				}
				else{
					mTip.setText(errorMsg);
				}
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initViews();
		initEvents();
		mPaint=new Paint();
	}
	private void initViews() {
		// TODO Auto-generated method stub
		mPhoto=(ImageView)findViewById(R.id.id_photo);
		mGetImage=(Button)findViewById(R.id.id_getImage);
		
		mDetect=(Button)findViewById(R.id.id_detect);
		mTip=(TextView)findViewById(R.id.id_tip);
		
		mWaitting=(View)findViewById(R.id.id_waitting);
		
	}
	private void initEvents() {
		// TODO Auto-generated method stub
		mGetImage.setOnClickListener(this);
		mDetect.setOnClickListener(this);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		
		if(requestCode==PICK_CODE)
		{
			if(intent!=null)
			{
				Uri uri=intent.getData();
				Cursor cursor=getContentResolver().query(uri, null, null, null, null);
				cursor.moveToFirst();
				
				int idx=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
				mCurrentPhotoStr =cursor.getString(idx);
				cursor.close();
				
				resizePhote();
				
				mPhoto.setImageBitmap(mPhotoImage);
				mTip.setText("Click Detect ==>");
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);
	
	}
	private void resizePhote() {
		// TODO Auto-generated method stub
		
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		
		BitmapFactory.decodeFile(mCurrentPhotoStr,options);
		
		double ratio=Math.max(options.outWidth*1.0/1024f, options.outHeight*1.0d/1024f);
		options.inSampleSize=(int)Math.ceil(ratio);
		
		options.inJustDecodeBounds=false;
		mPhotoImage=BitmapFactory.decodeFile(mCurrentPhotoStr,options);
		Log.e("resizePhoto", "Have Done!"); 
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.id_getImage:
		
			Intent intent=new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, PICK_CODE);
			
		break;
		case R.id.id_detect:
			mWaitting.setVisibility(View.VISIBLE);
			
			if(mCurrentPhotoStr!=null&&!mCurrentPhotoStr.trim().equals("")){
				resizePhote();
			}else{
				mPhotoImage=BitmapFactory.decodeResource(getResources(), R.drawable.t4);
			}
			FaceppDetect.detect(mPhotoImage,new FaceppDetect.Callback() {
				
				@Override
				public void success(JSONObject result) {
					// TODO Auto-generated method stub
					Message msg=Message.obtain();
					msg.what=MSG_SUCCESS;
					msg.obj=result;
					mHandler.sendMessage(msg);
				}
				
				@Override
				public void error(FaceppParseException exception) {
					// TODO Auto-generated method stub
					Message msg=Message.obtain();
					msg.what=MSG_ERROR;
					msg.obj=exception.getErrorMessage();
					mHandler.sendMessage(msg);
				}
			});
		break;
		}
	}
	protected void prepareRsBitmap(JSONObject rs) {
		// TODO Auto-generated method stub
		Bitmap bitmap=Bitmap.createBitmap(mPhotoImage.getWidth(),
				mPhotoImage.getHeight(),mPhotoImage.getConfig());
		Log.e("Detect", "detect");
		Canvas canva=new Canvas(bitmap);
		canva.drawBitmap(mPhotoImage, 0,0, null);
		try {
			JSONArray faces=rs.getJSONArray("face");
			int faceCount=faces.length();
			mTip.setText("find"+faceCount);
			
			for(int i=0;i<faceCount;++i){
				//拿到单独的face对象
				JSONObject face=faces.getJSONObject(i);
				JSONObject posObj=face.getJSONObject("position");
				
				float x=(float)posObj.getJSONObject("center").getDouble("x");
				float y=(float)posObj.getJSONObject("center").getDouble("y");
				
				float w=(float)posObj.getDouble("width");
				float h=(float)posObj.getDouble("height");
				
				x=x/100*bitmap.getWidth();
				y=y/100*bitmap.getHeight();
				
				w=w/100*bitmap.getWidth();
				h=h/100*bitmap.getHeight();
				
				
				//画Box
				mPaint.setColor(0xffffffff);
				mPaint.setStrokeWidth(3);
				Log.e("mPaint","color");
				canva.drawLine(x-w/2, y-h/2, x-w/2, y+h/2,mPaint);
				canva.drawLine(x-w/2, y-h/2, x+w/2, y-h/2,mPaint);
				canva.drawLine(x+w/2, y-h/2, x+w/2, y+h/2,mPaint);
				canva.drawLine(x-w/2, y+h/2, x+w/2, y+h/2,mPaint);
				
				//get age and gender
				int age=face.getJSONObject("attribute").getJSONObject("age").getInt("value");
				String gender=face.getJSONObject("attribute").getJSONObject("gender").getString("value");
				
				Bitmap ageBitmap=buildAgeBitmp(age,"Male".equals(gender));
				
				int ageWidth=ageBitmap.getWidth();
				int ageHeight=ageBitmap.getHeight();
				
				if(bitmap.getWidth()<mPhoto.getWidth()&&bitmap.getHeight()<mPhoto.getHeight()){
					float ratio=Math.max(bitmap.getWidth()*1.0f/mPhoto.getWidth(), bitmap.getHeight()*1.0f/mPhoto.getHeight());
					ageBitmap=Bitmap.createScaledBitmap(ageBitmap,(int)(ageWidth*ratio),(int)(ageHeight*ratio),false);
					
				}
				canva.drawBitmap(ageBitmap,x-ageBitmap.getWidth()/2,y-h/2-ageBitmap.getHeight(),null);
				mPhotoImage=bitmap;
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Bitmap buildAgeBitmp(int age, boolean isMale) {
		// TODO Auto-generated method stub
		TextView tv=(TextView)mWaitting.findViewById(R.id.id_age_and_gender);
		if(isMale){
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male), null, null, null);	
		}else{
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female), null, null, null);
		}
		
		tv.setDrawingCacheEnabled(true);
		Bitmap bitmap=Bitmap.createBitmap(tv.getDrawingCache());
		tv.destroyDrawingCache();
		return bitmap;
	}
}
