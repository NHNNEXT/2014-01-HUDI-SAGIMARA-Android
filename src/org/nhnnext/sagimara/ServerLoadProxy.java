package org.nhnnext.sagimara;

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

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServerLoadProxy extends AsyncTask<String, Integer, Long> {
	JSONObject jsonResult;
	private Context mContext;
	private View rootView;
	
	
	public ServerLoadProxy(Context mContext, View rootView) {
		this.mContext = mContext;
		this.rootView = rootView;
	}

	@Override
	protected Long doInBackground(String... params) {
		long result = 0;
		String phoneNumber = params[0];
		jsonResult = getUserProfileAsJSON(phoneNumber);
		
		return result;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(Long result) {
		super.onPostExecute(result);
		String profileStatus, profileLocation, profileWatch, profileNotify, profileInquiry;
		Log.i("Server Response", "[ServerLoadProxy] JSON RESULT : "+ jsonResult);
		try {
			profileNotify = jsonResult.getString("profileNotify");
			profileStatus = jsonResult.getString("profileStatus");
			profileLocation = jsonResult.getString("profileLocation");
			profileInquiry = jsonResult.getString("profileInquiry");
			profileWatch = jsonResult.getString("profileWatch");
			
			JSONArray profileInquirys = new JSONArray(profileInquiry);
			
			String today = profileInquirys.getString(4);
			
			TextView todayBlock = (TextView)rootView.findViewById(R.id.todayBlockValueText);
			todayBlock.setText(today);
			
			TextView notifyBlock = (TextView)rootView.findViewById(R.id.notifyBlockValueText);
			notifyBlock.setText(profileNotify);
			
			TextView locationBlock = (TextView)rootView.findViewById(R.id.locationBlockValueText);
			locationBlock.setText(profileLocation);
			
			TextView watchBlock = (TextView)rootView.findViewById(R.id.watchBlockValueText);
			watchBlock.setText(profileWatch);
			
			LinearLayout statusLayout = (LinearLayout)rootView.findViewById(R.id.status_layout);
			
			if(profileStatus.equals("0")){
				statusLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_trust));
			}else if(profileStatus.equals("1")){
				statusLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_warning));
			}else if(profileStatus.equals("2")){
				statusLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_dangerous));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * {
"profilePhone":"0000”,
"profileStatus":"0”,
"profileVerification":"true”,
"profileLocation":"경기도 성남시”,
"profileWatch":"0”,
"profileNotify":"0”,
"profileInquiry":["1","0","0","0","0"]}
		 */
		
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}	
	public JSONObject getUserProfileAsJSON(String phoneNumber) {
		Log.i("Server Response", "GetJSON Start");
		try {
			
			String url = SharedDatas.SERVER_URL + "/test";
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			
			//01048077749
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("id", phoneNumber));

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
}
