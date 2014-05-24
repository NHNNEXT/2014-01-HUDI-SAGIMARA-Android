package org.nhnnext.sagimara;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SearchView extends ActionBarActivity{
	public static Button searchButton;
	public static EditText searchPhoneNumberText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_main);
		Log.i("sagimara", "SearchView onCreate");
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		TelephonyManager telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		SharedDatas.PHONE_NUMBER = telManager.getLine1Number();
		
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnClickListener{
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.search_fragment,
					container, false);
			
			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			searchButton = (Button) getActivity().findViewById(R.id.searchButton);
			searchPhoneNumberText = (EditText) getActivity().findViewById(R.id.searchPhoneNumberText);
			
			searchButton.setOnClickListener(this);

		}

		@Override
		public void onClick(View v) {

			Log.i("sagimara", "Listener ON");
			switch (v.getId()) {
			case R.id.searchButton:
				Log.i("sagimara", "searchButton Click!");
				Intent i = new Intent(getActivity(), UserInfoView.class);
				i.putExtra("phoneNumber", searchPhoneNumberText.getText().toString());
				startActivity(i);
				break;

			default:
				break;
			}
			
		}
	}
}
