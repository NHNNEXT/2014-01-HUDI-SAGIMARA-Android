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
import org.json.JSONException;
import org.json.JSONObject;
import org.nhnnext.sagimara.R;
import org.nhnnext.sagimara.SharedDatas;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GetUserInfoOperation implements NOperation {

	String operationURL;
	
	public GetUserInfoOperation(String operationURL){
		this.operationURL = operationURL;
	}

	@Override
	public JSONObject run(String... params) {
		Log.i("Server Response", "GetJSON Start");
		try {
			String url = SharedDatas.SERVER_URL + this.operationURL;
			Log.e("GetUserInfoOperation", "URL : "+url);
			String phoneNumber = params[1];
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			// 01048077749
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
			Log.e("GetUserInfoOperation", "Error;;;;;+_+_+_+_+");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void doPostRun(JSONObject jsonResult, Context context, View view) {
		String profileStatus, profileLocation, profileWatch, profileNotify;
		JSONObject profileInquiry;
		int[] dayAgoDatas = { 0, 0, 0, 0, 0 };
		Log.i("Server Response", "[ServerLoadProxy] JSON RESULT : "
				+ jsonResult);
		try {
			profileNotify = jsonResult.getString("profileNotify");
			profileStatus = jsonResult.getString("profileStatus");
			profileLocation = jsonResult.getString("profileLocation");
			profileWatch = jsonResult.getString("profileWatch");
			profileInquiry = jsonResult.getJSONObject("profileInquiry");

			dayAgoDatas[0] = profileInquiry.getInt("today");
			dayAgoDatas[1] = profileInquiry.getInt("oneDayAgo");
			dayAgoDatas[2] = profileInquiry.getInt("twoDayAgo");
			dayAgoDatas[3] = profileInquiry.getInt("threeDayAgo");
			dayAgoDatas[4] = profileInquiry.getInt("fourDayAgo");

			TextView todayBlock = (TextView) view
					.findViewById(R.id.todayBlockValueText);
			todayBlock.setText("" + dayAgoDatas[0]);

			TextView notifyBlock = (TextView) view
					.findViewById(R.id.notifyBlockValueText);
			notifyBlock.setText(profileNotify);

			TextView locationBlock = (TextView) view
					.findViewById(R.id.locationBlockValueText);
			locationBlock.setText(profileLocation);

			TextView watchBlock = (TextView) view
					.findViewById(R.id.watchBlockValueText);
			watchBlock.setText(profileWatch);

			LinearLayout statusLayout = (LinearLayout) view
					.findViewById(R.id.status_layout);

			if (profileStatus.equals("0")) {
				statusLayout.setBackgroundColor(context.getResources()
						.getColor(R.color.color_trust));
			} else if (profileStatus.equals("1")) {
				statusLayout.setBackgroundColor(context.getResources()
						.getColor(R.color.color_warning));
			} else if (profileStatus.equals("2")) {
				statusLayout.setBackgroundColor(context.getResources()
						.getColor(R.color.color_dangerous));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
