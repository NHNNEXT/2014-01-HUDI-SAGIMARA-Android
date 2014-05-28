package org.nhnnext.sagimara.noperation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.nhnnext.sagimara.utility.SharedDatas;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class InsertPhohoForVerificationOperation implements NOperation{

	String operationURL;
	
	public InsertPhohoForVerificationOperation(String operationURL){
		this.operationURL = operationURL;
	}

	@Override
	public JSONObject run(String... params) {
		Log.i("Server Response", "insertPhotoForVerification params  : " + SharedDatas.PHONE_NUMBER);
		try {

			String url = SharedDatas.SERVER_URL + operationURL;
			String photoPath = params[1];
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			
			
			MultipartEntity entity = new MultipartEntity();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String date = dateFormat.format(cal.getTime());
			
			Bitmap bitmap;
			bitmap = SharedDatas.imageBitmap;
			File file = new File(Environment.getExternalStorageDirectory()+"/tmp.png");
			FileOutputStream fOut = new FileOutputStream(file);
			
			bitmap.compress(Bitmap.CompressFormat.PNG, 70, fOut);
			fOut.close();
			entity.addPart("id", new StringBody(SharedDatas.PHONE_NUMBER));
			entity.addPart("date", new StringBody(date));
			entity.addPart("photo", new FileBody(file));
			
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			JSONObject jObject = new JSONObject(result.toString());
			return jObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void doPostRun(JSONObject jsonResult, Context context, View view) {
		Log.i("RequestVerificationOperation", "doPostRun in RequestVerification");
	}

}
