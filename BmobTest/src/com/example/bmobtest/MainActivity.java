package com.example.bmobtest;

import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
private EditText mName;
private EditText mFeedback,mQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Bmob.initialize(this, "c46e57eee7d6ad446cde668a39f26269");
		BmobInstallation.getCurrentInstallation(this).save();;
		BmobPush.startWork(this, "c46e57eee7d6ad446cde668a39f26269");
		mName=(EditText)findViewById(R.id.username);
		mFeedback=(EditText)findViewById(R.id.feedback);
		mQuery=(EditText)findViewById(R.id.feedbackone);
	}
	public void PushAll(View view){
		BmobPushManager push=new BmobPushManager(MainActivity.this);
		push.pushMessageAll("Test");
	}
	public void query_feedback(View view){
		String str=mQuery.getText().toString();
		if(str.equals("")){
			return;
		}else{
			BmobQuery<FeedBack> query=new BmobQuery<FeedBack>();
			query.addWhereEqualTo("name", str);
			//query.addWhereNotEqualTo("name", str);
			query.findObjects(this, new FindListener<FeedBack>() {
				
				@Override
				public void onSuccess(List<FeedBack> feedBacks) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
					builder.setTitle("Query");
					String str="";
					for(FeedBack feedBack:feedBacks){
						str+=feedBack.getName()+":"+feedBack.getFeedback()+"\n";
						
					}
					
					builder.setMessage(str);
					builder.create().show();
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	public void query(View view){
		BmobQuery<FeedBack> query=new BmobQuery<FeedBack>();
		query.findObjects(this, new FindListener<FeedBack>() {
			
			@Override
			public void onSuccess(List<FeedBack> feedBacks) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Query");
				String str="";
				for(FeedBack feedBack:feedBacks){
					str+=feedBack.getName()+":"+feedBack.getFeedback()+"\n";
					
				}
				
				builder.setMessage(str);
				builder.create().show();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void submit(View view){
		String name=mName.getText().toString();
		String feedback=mFeedback.getText().toString();
		if(feedback.equals("")||name.equals("")){
			return;
		}else{
			FeedBack feedbackObject=new FeedBack();
			feedbackObject.setName(name);
			feedbackObject.setFeedback(feedback);
			feedbackObject.save(MainActivity.this,new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
				}
			});
			
		}
	}
}
