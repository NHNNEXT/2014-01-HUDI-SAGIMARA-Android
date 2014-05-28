package org.nhnnext.sagimara.noperation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nhnnext.sagimara.R;
import org.nhnnext.sagimara.utility.SharedDatas;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GetRequestsAboutMeOperation implements NOperation {

	String operationURL;
	
	public GetRequestsAboutMeOperation(String operationURL){
		this.operationURL = operationURL;
	}

	@Override
	public JSONObject run(String... params) {
		Log.i("Server Response", "GetJSON Start");
		try {
			String url = SharedDatas.SERVER_URL + this.operationURL;
			String phoneNumber = params[1];
			Log.e("GetUserInfoOperation", "URL : "+ url + " : "+phoneNumber);
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("userPhone", phoneNumber));

			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println(result);
			JSONArray jArray = new JSONArray(result.toString());
			
			JSONObject jObject = new JSONObject().put("data", jArray);
			return jObject;
		} catch (Exception e) {
			Log.e("GetRequestsAboutMe", "Error;;;;;+_+_+_+_+");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void doPostRun(JSONObject jsonResult, Context context, View view) {
		//Log.e("GetRequestsAboutMe", jsonResult.toString());
		Log.e("GetRequestsAboutMe", "[GetRequestAboutMe] [ " + jsonResult + " ]");
		JSONArray requestArray;
		int numOfRequest;
		try {
			requestArray = jsonResult.getJSONArray("data");
			numOfRequest = requestArray.length();
			Button pushAlarmButton = (Button)view.findViewById(R.id.pushAlarmButton);
			pushAlarmButton.setText(""+numOfRequest);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}
}
