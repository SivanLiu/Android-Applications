package com.example.downloadfile;

import com.example.entities.FileInfo;
import com.example.services.DownloadService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView mTvFileName=null;
	private ProgressBar mpbProgress=null;
	private Button mBtStop=null;
	private Button mBtStart=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//��ʼ�����
		mTvFileName=(TextView) findViewById(R.id.tv_FileName);
		mpbProgress=(ProgressBar) findViewById(R.id.pb_progressBar);
		mBtStop=(Button) findViewById(R.id.bt_stop);
		mBtStart=(Button) findViewById(R.id.bt_start);
		mpbProgress.setMax(100);

		//�����ļ���Ϣ����
		final FileInfo fileInfo=new FileInfo(0,
				"http://www.sqliteexpert.com/SQLiteExpertPersSetup.exe",
				"SQLiteExpertPersSetup.exe",0,0);
		//����¼�����
			mBtStart.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// ͨ��Intent���ݲ�����service
					Intent intent=new Intent(MainActivity.this,DownloadService.class);
					intent.setAction(DownloadService.ACTION_START);
					intent.putExtra("fileInfo", fileInfo);
					startService(intent);
				}
			});
			//����¼�����
			mBtStop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// ͨ��Intent���ݲ�����service
					Intent intent=new Intent(MainActivity.this,DownloadService.class);
					intent.setAction(DownloadService.ACTION_STOP);
					intent.putExtra("fileInfo", fileInfo);
					startService(intent);
				}
			});
			
			//ע��㲥������
			IntentFilter filter=new IntentFilter();
			filter.addAction(DownloadService.ACTION_UPDATE);
			registerReceiver(mReceiver, filter);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	/*
	 * ����UI�Ĺ㲥������
	 */
	BroadcastReceiver mReceiver=new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			if(DownloadService.ACTION_UPDATE.equals(intent.getAction())){
				int finished=intent.getIntExtra("finished", 0);
				mpbProgress.setProgress(finished);
			}
		}
	};

}
