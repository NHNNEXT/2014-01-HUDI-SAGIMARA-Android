package org.nhnnext.sagimara;

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
import org.json.JSONException;
import org.json.JSONObject;
import org.nhnnext.sagimara.noperation.GetUserInfoOperation;
import org.nhnnext.sagimara.noperation.NOperation;
import org.nhnnext.sagimara.noperation.OperationMapping;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HTTPPostOperation extends AsyncTask<String, Integer, JSONObject> {
	JSONObject jsonResult;
	private Context mContext;
	private View rootView;
	NOperation operation;
	
	public HTTPPostOperation(Context mContext, View rootView) {
		this.mContext = mContext;
		this.rootView = rootView;
	}

	// params[0] 연산 종류
	// params[...] 인자값
	@Override
	protected JSONObject doInBackground(String... params) {
		//JSONObject result = new JSONObject();
		//String phoneNumber = params[0];
		//jsonResult = getUserProfileAsJSON(phoneNumber);
		OperationMapping operationMapping = new OperationMapping();
		String operationName = params[0];
		Log.e("HTTPPostOperation", "opreation name : "+operationName);
		operation = operationMapping.getNOperation(operationName);
		JSONObject result = operation.run(params);
		return result;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		operation.doPostRun(result, this.mContext, this.rootView);
		
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

	public JSONObject requestVerification(String phoneNumber) {
		Log.i("Server Response", "requestVerification ");
		try {

			String url = SharedDatas.SERVER_URL + "/test";

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("from", phoneNumber));
			urlParameters.add(new BasicNameValuePair("to",
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
}
