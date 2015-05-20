package com.example.howold;

import java.io.ByteArrayOutputStream;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

public class FaceppDetect {
	public interface Callback{
	
		void success(JSONObject result);
		void error(FaceppParseException exception);
	}
	protected static final String TAG = "Face";
	public static void detect(final Bitmap bm,final Callback callback){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			try {
				HttpRequests requests=new HttpRequests(Constat.KEY,Constat.SECRET, true, true);
				Bitmap bmSmall=Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
				ByteArrayOutputStream stream=new ByteArrayOutputStream();
				
				bmSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				
				byte[] arrays=stream.toByteArray();
				
				PostParameters params=new PostParameters();
				params.setImg(arrays);
				
				JSONObject jsonobject = requests.detectionDetect(params);
				Log.e(TAG, jsonobject.toString());
					if(callback!=null){
						callback.success(jsonobject);
					}
				} catch (FaceppParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(callback!=null)
					{
						callback.error(e);
					}
				}
			}
		}).start();
	}
}
