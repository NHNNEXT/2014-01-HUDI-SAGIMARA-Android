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
import org.json.JSONException;
import org.json.JSONObject;

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

}
