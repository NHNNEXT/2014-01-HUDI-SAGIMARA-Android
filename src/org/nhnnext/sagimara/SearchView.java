package org.nhnnext.sagimara;

import org.nhnnext.sagimara.noperation.HTTPPostOperation;
import org.nhnnext.sagimara.utility.SharedDatas;

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
		//TEST용도
		SharedDatas.PHONE_NUMBER = "3333";
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnClickListener{
		public Button searchButton, pushAlarmButton;
		public EditText searchPhoneNumberText;
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
			pushAlarmButton = (Button) getActivity().findViewById(R.id.pushAlarmButton);
			
			pushAlarmButton.setOnClickListener(this);
			searchButton.setOnClickListener(this);

		}

		@Override
		public void onClick(View v) {

			Log.i("sagimara", "Listener ON");
			Intent intent;
			switch (v.getId()) {
			case R.id.searchButton:
				Log.i("sagimara", "searchButton Click!");
				intent = new Intent(getActivity(), UserInfoView.class);
				intent.putExtra("phoneNumber", searchPhoneNumberText.getText().toString());
				startActivity(intent);
				break;
			
			case R.id.pushAlarmButton:
				Log.i("sagimara", "pushAlarmButton");
				intent = new Intent(getActivity(), VerificationSendView.class);
				startActivity(intent);
				break;
				
			default:
				break;
			}
			
		}

		@Override
		public void onResume() {
			super.onResume();
			new HTTPPostOperation(getActivity().getApplicationContext(), getView())
			.execute("getRequestAboutMe", SharedDatas.PHONE_NUMBER);
			
		}
	}
}
