package org.nhnnext.sagimara;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class UserInfoView extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_search) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		String phoneNumber;
		Context mContext;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			Intent i = getActivity().getIntent();

			this.mContext = getActivity().getApplicationContext();
			phoneNumber = i.getStringExtra("phoneNumber");
			
			new HTTPPostOperation(getActivity().getApplicationContext(), rootView)
					.execute("getUserInfo", phoneNumber);
			
			Button reconfirmButton = (Button)rootView.findViewById(R.id.reconfirm_button);
			reconfirmButton.setOnClickListener(reconfirmClick);
			Button tradeHistoryButton = (Button)rootView.findViewById(R.id.trade_history_button);
			tradeHistoryButton.setOnClickListener(trade_history_button);
			
			return rootView;
		}
		
		Button.OnClickListener reconfirmClick = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(getActivity().getApplicationContext(), "인증요청 하셨습니다.", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
			}
		};
		Button.OnClickListener trade_history_button = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(getActivity().getApplicationContext(), "거래내역보기 클릭하셨습니다.", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
			}
		};
	}

}
