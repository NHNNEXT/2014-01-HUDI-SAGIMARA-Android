package org.nhnnext.sagimara.noperation;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.nhnnext.sagimara.utility.SharedDatas;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class InsertLocationForVerificationOperation implements NOperation {

	String operationURL;

	public InsertLocationForVerificationOperation(String operationURL) {
		this.operationURL = operationURL;
	}

	@Override
	public JSONObject run(String... params) {
		Log.i("Server Response", "insertPhotoForVerification params : ");
		try {

			String url = SharedDatas.SERVER_URL + operationURL;
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("id",
					SharedDatas.PHONE_NUMBER));
			urlParameters.add(new BasicNameValuePair("location", SharedDatas.LOCATION));

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String date = dateFormat.format(cal.getTime());
			urlParameters.add(new BasicNameValuePair("date", date));
			Log.i("Server Response", "Date : " + date);
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

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
		Log.i("RequestVerificationOperation",
				"doPostRun in RequestVerification");
	}

}
