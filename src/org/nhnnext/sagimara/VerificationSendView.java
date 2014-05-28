package org.nhnnext.sagimara;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.nhnnext.sagimara.noperation.HTTPPostOperation;
import org.nhnnext.sagimara.noperation.InsertLocationForVerificationOperation;
import org.nhnnext.sagimara.utility.SharedDatas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class VerificationSendView extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification_send_view);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.verification_send_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			LocationListener, OnClickListener {
		LocationManager lm = null;
		String provider = null;
		static final int REQUEST_IMAGE_CAPTURE = 1;
		static final int REQUEST_TAKE_PHOTO = 1;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_verification_send_view, container, false);
			
			ImageButton mImageButton = (ImageButton)rootView.findViewById(R.id.verification_image_view);
			mImageButton.setOnClickListener(this);
			
			Button imageVerificationButton = (Button)rootView.findViewById(R.id.verification_image_button);
			imageVerificationButton.setOnClickListener(this);
			Button locationverificationButton = (Button)rootView.findViewById(R.id.verification_location_button);
			locationverificationButton.setOnClickListener(this);
			
			lm = (LocationManager) getActivity().getSystemService(
					Context.LOCATION_SERVICE);

			Criteria c = new Criteria();
			provider = lm.getBestProvider(c, true);
			if (provider == null || !lm.isProviderEnabled(provider)) {
				List<String> list = lm.getAllProviders();

				for (int i = 0; i < list.size(); i++) {
					String temp = list.get(i);

					if (lm.isProviderEnabled(temp)) {
						provider = temp;
						break;
					}
				}
			}

			Location location = lm.getLastKnownLocation(provider);

			if (location == null) {
				Toast.makeText(getActivity(), "사용가능한 위치 정보 제공자가 없습니다.",
						Toast.LENGTH_SHORT).show();
			} else {
				onLocationChanged(location);
			}
			
			return rootView;
		}

		@Override
		public void onPause() {
			super.onPause();
			lm.removeUpdates(this);
		}

		@Override
		public void onResume() {
			super.onResume();
			lm.requestLocationUpdates(provider, 500, 1, this);
		}

		@Override
		public void onLocationChanged(Location location) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		public String getAddress(double lat, double lng) {
			String address = null;
			List<Address> list = null;

			Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

			try {
				list = geocoder.getFromLocation(lat, lng, 1);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (list == null) {
				Log.e("Verficiation", "위치정보 없음");
				return null;
			}

			if (list.size() > 0) {
				Address addr = list.get(0);
				address = addr.getCountryName() + " : " + addr.getPostalCode()
						+ " : " + addr.getLocality() + " : "
						+ addr.getThoroughfare() + " : "
						+ addr.getFeatureName();
			}

			return address;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.verification_image_view:
				Log.d("Camera", "imageButton Click!");
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, 1);
				break;
				
			case R.id.verification_image_button:
				new HTTPPostOperation(getActivity().getApplicationContext(), getView())
				.execute("insertPhoto", mCurrentPhotoPath );
				break;
				
			case R.id.verification_location_button:
				new HTTPPostOperation(getActivity().getApplicationContext(), getView())
				.execute("insertLocation");
				
				break;
				
			default:
				break;
			}
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				SharedDatas.imageBitmap = (Bitmap) extras.get("data");
				ImageButton mImageView = (ImageButton)getView().findViewById(R.id.verification_image_view);
				mImageView.setImageBitmap(SharedDatas.imageBitmap);
			}
		}
		
		private void dispatchTakePictureIntent() {
		    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    // Ensure that there's a camera activity to handle the intent
		    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
		        // Create the File where the photo should go
		        File photoFile = null;
		        try {
		            photoFile = createImageFile();
		        } catch (IOException ex) {
		            // Error occurred while creating the File
		            //...
		        }
		        // Continue only if the File was successfully created
		        if (photoFile != null) {
		            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
		                    Uri.fromFile(photoFile));
		            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
		        }
		    }
		}

		String mCurrentPhotoPath;

		private File createImageFile() throws IOException {
		    // Create an image file name
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		    String imageFileName = "JPEG_" + timeStamp + "_";
		    File storageDir = Environment.getExternalStoragePublicDirectory(
		            Environment.DIRECTORY_PICTURES);
		    File image = File.createTempFile(
		        imageFileName,  /* prefix */
		        ".jpg",         /* suffix */
		        storageDir      /* directory */
		    );

		    // Save a file: path for use with ACTION_VIEW intents
		    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		    Log.e("CreateImageFile", mCurrentPhotoPath);
		    return image;
		}
	}

}
