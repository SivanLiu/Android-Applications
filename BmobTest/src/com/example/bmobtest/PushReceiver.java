package com.example.bmobtest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.bmob.push.PushConstants;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class PushReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String message="";
		if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
			String msg=intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			Log.e("Push", msg);
			JSONTokener jsonTokener=new JSONTokener(msg);
			try {
				JSONObject object=(JSONObject)jsonTokener.nextValue();
				message=object.getString("alert");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			NotificationManager magager=(NotificationManager)context.getSystemService
					(context.NOTIFICATION_SERVICE);
			Notification notification	=new Notification(R.drawable.ic_launcher,
					"Test Bmob",System.currentTimeMillis());	
		notification.setLatestEventInfo(context, "Bmob Test", message, null);	
		magager.notify(R.drawable.ic_launcher,notification);
		
		}
	}

}
