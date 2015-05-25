package com.example.services;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.RandomAccess;
import org.apache.http.HttpStatus;
import com.example.entities.FileInfo;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class DownloadService extends Service {

	public static final String ACTION_START="ACTION_START";
	public static final String ACTION_STOP="ACTION_STOP";
	public static final String ACTION_UPDATE="ACTION_UPDATE";
	private DownloadTask mTask=null;
	public static final String DOWNLOAD_PATH=
			Environment.getExternalStorageDirectory().getAbsolutePath()+"/Filedownloads/";
	public static final int MSG_INIT=0;
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		//���Activity�������Ĳ���
		
		
			if(ACTION_START.equals(intent.getAction())){
				FileInfo fileInfo=(FileInfo)intent.getSerializableExtra("fileInfo");
				Log.i("test", "Start:"+fileInfo.toString());
				//������ʼ���߳�
				new InitTread(fileInfo).start();
			}else if(ACTION_STOP.equals(intent.getAction())){
				FileInfo fileInfo=(FileInfo)intent.getSerializableExtra("fileInfo");
				//��ͣ�߳�
				Log.i("test", "Stop:"+fileInfo.toString());
				if(mTask!=null){
					mTask.isPause=true;
				}
			}
		
		
		return super.onStartCommand(intent, flags, startId);
	};
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	Handler mHandler=new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch(msg.what){
			case MSG_INIT:
				FileInfo fileInfo=(FileInfo)msg.obj;
				Log.i("test","Init:"+fileInfo);
				//������������
				mTask=new DownloadTask(DownloadService.this,fileInfo);
				mTask.download();
				break;
	}
			return true;
}});
	/**
	 * ��ʼ�����߳�
	 */
	class InitTread extends Thread{
		private FileInfo mFileInfo=null;
		public InitTread(FileInfo mFileInfo){
			this.mFileInfo=mFileInfo;
		}
		public void run(){
			HttpURLConnection conn=null;
			RandomAccessFile raf=null;
			try {
				//���������ļ�
				URL url=new URL(mFileInfo.getUrl());
				conn=(HttpURLConnection)url.openConnection();
				conn.setConnectTimeout(3000);
				/**
				 * ��Ϊ�Ǵӷ����������ļ�����������ѡ��GET��ʽ����ȡ���ݡ�
				 * �������ز������⣬����һ����post��ʽ
				 */
				conn.setRequestMethod("GET");
				int length=-1;
				if(conn.getResponseCode()==HttpStatus.SC_OK){
					//����ļ�����
					length=conn.getContentLength();
				}
				if(length<=0){
					return ;
				}
				File dir=new File(DOWNLOAD_PATH);
				if(!dir.exists()){
					dir.mkdir();
				}
				//�ڱ��ش����ļ�
				File file=new File(dir,mFileInfo.getFileName());
				raf=new RandomAccessFile(file, "rwd");
				//�����ļ�����
				raf.setLength(length);
				mFileInfo.setLength(length);
				mHandler.obtainMessage(MSG_INIT,mFileInfo).sendToTarget();
			} catch (Exception e) {
			}finally{
				try {
					raf.close();
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
