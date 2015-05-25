package com.example.userlogin;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private EditText et_name,et_password;
	private Button bt_register,bt_login;
	private CheckBox cb_mempass,cb_autologin;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		sp=this.getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
		sp=this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		sp=this.getSharedPreferences("userinfo", Context.MODE_WORLD_WRITEABLE);
		Bmob.initialize(this, "8834f1713aec08e8b0bae05d0965fc2a");
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		et_name=(EditText) findViewById(R.id.et_name);
		et_password=(EditText) findViewById(R.id.et_password);
		bt_login=(Button) findViewById(R.id.bt_login);
		bt_register=(Button) findViewById(R.id.bt_register);
		cb_mempass=(CheckBox) findViewById(R.id.cb_mempass);
		cb_autologin=(CheckBox) findViewById(R.id.cb_autologin);
		
		//判断记住密码框的状态
		if(sp.getBoolean("ISCHECK", false)){
			//设置默认是记录密码状态
			cb_mempass.setChecked(true);
			et_name.setText(sp.getString("USER_NAME", ""));
			et_password.setText(sp.getString("PASSWORD", ""));
			//判断自动登录多选框状态
			if(sp.getBoolean("AUTO_ISCHECK", false)){
					//设置默认是自动登录状态
				cb_autologin.setChecked(true);
				final String username=et_name.getText().toString();
				final String PASSWORD=et_password.getText().toString();
				final BmobUser user=new BmobUser();
				user.setUsername(username);
				user.setPassword(PASSWORD);
					user.login(Login.this, new SaveListener() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), 
									"Login Successful", Toast.LENGTH_LONG).show();
			
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
						}
					});
			}
}
		
		
		bt_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String username=et_name.getText().toString();
				final String password=et_password.getText().toString();
				final BmobUser user=new BmobUser();
				user.setUsername(username);
				user.setPassword(password);
					user.login(Login.this, new SaveListener() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
							if(cb_mempass.isChecked()){
								//记住用户名、密码
								Editor editor=sp.edit();
								editor.putString("USER_NAME", username);
								editor.putString("PASSWORD", password);
								editor.commit();
							}
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
						}
					});
			}
			});
			bt_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Login.this,Register.class);
				startActivity(intent);
			}
		});
		cb_mempass.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(cb_mempass.isChecked()){
					System.out.println("记住密码已经选中");
					sp.edit().putBoolean("ISCHECK", true).commit();
				}else{
					System.out.println("记住密码没有选中");
					sp.edit().putBoolean("ISCHECK", false).commit();
				}
			}
		});
		cb_autologin.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(cb_autologin.isChecked()){
					System.out.println("自动登录已经选中");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
				}else{
					System.out.println("自动登录没有选中");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});			
	}	
}
