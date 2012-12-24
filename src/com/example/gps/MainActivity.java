package com.example.gps;
 
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;
import android.content.Intent;
//import com.google.android.maps;


public class MainActivity extends Activity implements LocationListener {
	private boolean getService = false;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//取得系統定位服務
		LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			//如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
			getService = true;	//確認開啟定位服務
			locationServiceInitial();
		} else {
			Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));	//開啟設定頁面
		}
	}
	
	public String getAddressByLocation(Location location) {
		String returnAddress = "";
		try {
			if (location != null) {
		    		Double longitude = location.getLongitude();	//取得經度
		    		Double latitude = location.getLatitude();	//取得緯度

		    		//建立Geocoder物件: Android 8 以上模疑器測式會失敗
		    		Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE); 	//地區:台灣
		    		//自經緯度取得地址

		    		List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);
		    		//List<Address> lstAddress = lstAddress = gc.getFromLocationName("地址", 3);	//輸入地址回傳Location物件

		    		//Toast.makeText(this, returnAddress, Toast.LENGTH_LONG).show();
		    		returnAddress = lstAddress.get(0).getAddressLine(0);
			}	
		}
		catch(Exception e) {
			Toast.makeText(this, "getAddress Failed!!!!!!!!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return returnAddress;
	}
	
	private void getLocation(Location location) {	//將定位資訊顯示在畫面中
		if(location != null) {
			TextView longitude_txt = (TextView) findViewById(R.id.longitude);
			TextView latitude_txt = (TextView) findViewById(R.id.latitude);
			TextView address_txt = (TextView) findViewById(R.id.addressprint);
 
			Double longitude = location.getLongitude();	//取得經度
			Double latitude = location.getLatitude();	//取得緯度
			
			longitude_txt.setText(String.valueOf(longitude));
			latitude_txt.setText(String.valueOf(latitude));
			address_txt.setText(getAddressByLocation(location));
		}
		else {
			Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
		}
	}
	
	private LocationManager lms;
	private String bestProvider = LocationManager.GPS_PROVIDER;	//最佳資訊提供者
	private void locationServiceInitial() {
		lms = (LocationManager) getSystemService(LOCATION_SERVICE);	//取得系統定位服務
		Criteria criteria = new Criteria();	//資訊提供者選取標準
		bestProvider = lms.getBestProvider(criteria, true);	//選擇精準度最高的提供者
		Location location = lms.getLastKnownLocation(bestProvider);
		getLocation(location);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(getService) {
			lms.requestLocationUpdates(bestProvider, 1000, 1, this);
			//服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
		}
	}
 
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(getService) {
			lms.removeUpdates(this);	//離開頁面時停止更新
		}
	}
 
	@Override
	public void onLocationChanged(Location location) {	//當地點改變時
		// TODO Auto-generated method stub
		getLocation(location);
	}
 
	@Override
	public void onProviderDisabled(String arg0) {	//當GPS或網路定位功能關閉時
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void onProviderEnabled(String arg0) {	//當GPS或網路定位功能開啟
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {	//定位狀態改變
		// TODO Auto-generated method stub
 
	}
}

/*
package com.example.gps;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
*/
