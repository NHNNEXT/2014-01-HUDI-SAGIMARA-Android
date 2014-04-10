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

import android.util.Log;

public class ServerLoadProxy {

	public String getUserProfileAsJSON(String phoneNumber) {
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
			Log.i("Server Response", result.toString());
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
