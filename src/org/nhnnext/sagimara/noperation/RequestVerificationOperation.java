package org.nhnnext.sagimara.noperation;

import java.io.BufferedReader;
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
import org.nhnnext.sagimara.SharedDatas;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class RequestVerificationOperation implements NOperation{

	String operationURL;
	
	public RequestVerificationOperation(String operationURL){
		this.operationURL = operationURL;
	}

	@Override
	public JSONObject run(String... params) {
		Log.i("Server Response", "requestVerification params : "+params[1] + " : " + SharedDatas.PHONE_NUMBER);
		try {

			String url = SharedDatas.SERVER_URL + operationURL;
			String phoneNumber = params[1];
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("to", phoneNumber));
			urlParameters.add(new BasicNameValuePair("from",
					SharedDatas.PHONE_NUMBER));

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
		Log.i("RequestVerificationOperation", "doPostRun in RequestVerification");
	}

}
