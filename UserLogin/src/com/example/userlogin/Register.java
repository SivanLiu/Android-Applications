package com.example.userlogin;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Register extends Activity implements OnClickListener {

	private EditText et_username;
	private EditText et_password;
	private EditText ets_password;
	private EditText et_sex;
	private EditText et_age;
	private EditText et_address;
	private Button bt_submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//Bmob.initialize(this, "8834f1713aec08e8b0bae05d0965fc2a");
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		et_username=(EditText) findViewById(R.id.et_username);
		et_password=(EditText) findViewById(R.id.et_password);
		ets_password=(EditText) findViewById(R.id.ets_password);
		et_sex=(EditText) findViewById(R.id.et_sex);
		et_age=(EditText) findViewById(R.id.et_age);
		et_address=(EditText) findViewById(R.id.et_address);
		bt_submit=(Button) findViewById(R.id.bt_submit);
		bt_submit.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String username=et_username.getText().toString();
		String password=et_password.getText().toString();
		String spassword=ets_password.getText().toString();
		String sex=et_sex.getText().toString();
		String age=et_age.getText().toString();
		String address=et_address.getText().toString();
		final RegisteUser uuser=new RegisteUser();
		uuser.setUsername(username);
		uuser.setPassword(password);
		uuser.setNumber(spassword);
		uuser.setAge(Integer.parseInt(age));
		uuser.setSex(sex);
		uuser.setAddress(address);
		switch(v.getId()){
		case R.id.bt_submit:
			uuser.signUp(this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), 
							"Register Successful", Toast.LENGTH_LONG).show();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), 
							"Register Failed", Toast.LENGTH_LONG).show();
				}
			});
			finish();
			break;
		
			
		}
	}
}
