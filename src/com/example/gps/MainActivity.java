package com.example.gps;
 
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.os.AsyncTask;
//import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;
import android.content.Intent;
import android.widget.RadioGroup;






//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapController;
//import com.google.android.maps;
//import com.google.android.maps.MapView;


public class MainActivity extends Activity {//implements AdapterView.OnItemSelectedListener {//, 
	//RadioGroup.OnCheckedChangeListener {//implements LocationListener {
	//private boolean getService = false;
	//private MapView map;			//宣告google map物件
	//private MapController mapController;	//宣告google map控制物件
	public static final String[] items_type = {"中油加油站", "自營加油站", "加盟加油站"};
	private static final String[] items_city = {"台北市", "高雄市", "基隆市", "新竹市", 
												"台中市", "嘉義市", "台南市", "新北市",
												"桃園縣", "新竹縣", "苗栗縣", "彰化縣",
												"雲林縣", "南投縣", "嘉義縣", "屏東縣",
												"宜蘭縣", "花蓮縣", "台東縣", "澎湖縣",
												"連江縣", "金門縣"};
	
	private TextView selection_type;
	private TextView selection_city;
	
	private type_selection type_sel;
	private city_selection city_sel;
	
	RadioGroup orientation;
	RadioGroup self_oil;
	private self_Radio selfOil;
	private time_Radio timeOption;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		type_sel = new type_selection();
		selection_type=(TextView)findViewById(R.id.selection_type);
		Spinner spin_type=(Spinner)findViewById(R.id.spinner_type);
		
		spin_type.setOnItemSelectedListener(type_sel);
		
		ArrayAdapter<String> array_type=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                items_type);
		array_type.setDropDownViewResource(
		android.R.layout.simple_spinner_dropdown_item);
		spin_type.setAdapter(array_type);
		
		city_sel = new city_selection();
		selection_city=(TextView)findViewById(R.id.selection_city);
		Spinner spin_city=(Spinner)findViewById(R.id.spinner_city);
		
		spin_city.setOnItemSelectedListener(city_sel);
		
		ArrayAdapter<String> array_city=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                items_city);
		array_city.setDropDownViewResource(
		android.R.layout.simple_spinner_dropdown_item);
		spin_city.setAdapter(array_city);
		
		timeOption = new time_Radio();
		
		orientation=(RadioGroup)findViewById(R.id.orientation);
		orientation.setOnCheckedChangeListener(timeOption);
		
		selfOil = new self_Radio();
		
		self_oil=(RadioGroup)findViewById(R.id.self);
		self_oil.setOnCheckedChangeListener(selfOil);
		

/*
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
*/		
/*		
		map = (MapView) findViewById(R.id.map);	//載入google map物件
		mapController = map.getController();	//設定控制的map物件
		setupMap();
*/		
	}
/*	
	private void setupMap() {
		setupMap(120.2126014372952, 22.99724179778664);	//設定地圖預設值
	}
	
	private void setupMap(double longitude, double latitude) {
		GeoPoint station = new GeoPoint(
					(int)(latitude * 1000000),
					(int)(longitude * 1000000)
		);			//設定地圖座標值:緯度,經度
 
		map.setTraffic(true);				//設定地圖檢示模式
		//.setTraffic(true)：一般地圖
		//.setSatellite(true)：衛星地圖
		//.setStreetView：街景圖
 
		mapController.setZoom(17);			//設定放大倍率1(地球)-21(街景)
		mapController.animateTo(station);	//指定地圖中央點
	}
*/
/*	
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
*/	
/*	
	public void onItemSelected(AdapterView<?> parent,
            View v, int position, long id) {

			//Toast.makeText(this,String.valueOf(position)+String.valueOf(id), Toast.LENGTH_LONG).show();

	}
	public void onNothingSelected(AdapterView<?> parent) {
		selection_type.setText("");
		//selection_city.setText("");
	}
*/	
/*		
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		
	  switch (checkedId) {
	    case R.id.time_normal:
	    case R.id.time_24HR:
	      //orientation.setOrientation(LinearLayout.HORIZONTAL);
    	break;
      
	    case R.id.self_yes:
	    case R.id.self_no:
	      //self_oil.setOrientation(LinearLayout.HORIZONTAL);
	      break;
	      
	  }
	}
*/
	public int typeofStation;
	public class type_selection implements AdapterView.OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent,
	            View v, int position, long id) {
			typeofStation = (int)id;
		}
		public void onNothingSelected(AdapterView<?> parent) {
			selection_type.setText("");
		}
	}
	
	public int cityLocation;
	public class city_selection implements AdapterView.OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent,
	            View v, int position, long id) {
			cityLocation = (int)id;
		}
		public void onNothingSelected(AdapterView<?> parent) {
			selection_city.setText("");
		}
	}
	
	public boolean oiltime = false;
	public class time_Radio implements RadioGroup.OnCheckedChangeListener {
		public void onCheckedChanged(RadioGroup group, int checkedId) {
		  switch (checkedId) {		      
		    case R.id.time_normal:
		    	oiltime = false;
		    break;
		    case R.id.time_24HR:
		    	oiltime = true;
		    break;
		  }
		}
	}
	
	public boolean oilbyself = true;
	public class self_Radio implements RadioGroup.OnCheckedChangeListener {
		public void onCheckedChanged(RadioGroup group, int checkedId) {
		  switch (checkedId) {		      
		    case R.id.self_yes:
		    	oilbyself = true;
		    break;
		    case R.id.self_no:
		    	oilbyself = false;
		    break;
		  }
		}
	}
	
	public ProgressDialog dialog = null;
	
	public class SearchOILStation extends AsyncTask<Void, Void, Void>{
		URL url = null;
        HttpURLConnection urlConnection = null;
        String parameters = null;
        String[] address_list = null;
        String[] phone_list = null;
		@Override
		protected Void doInBackground(Void... arg0) {
	        try {
	        	parameters = "pno=65" +
		                "&type1="+URLEncoder.encode(items_type[typeofStation],"big5")+ //%A6%DB%C0%E7%A5%5B%AAo%AF%B8
		                 "&city1="+URLEncoder.encode(items_city[cityLocation],"big5");
	        	
	        }catch (Exception e)
		    {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
		    }
	        
	        if (oiltime) {
	        	parameters = parameters+"&C1=ON";
        	}
	        
	        if (oilbyself) {
	        	parameters = parameters+"&CD=ON";
	        }
	        
	        Log.e("smallhau", parameters);
	        
	        try {
	             url = new URL("http://www.cpc.com.tw/big5_bd/tmtd/station/searchstn-1.asp");
		    } catch (MalformedURLException e)
		    {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		    }

	     try {
	    	int count = 0;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            urlConnection.setFixedLengthStreamingMode(parameters.getBytes().length);

            //send the POST out
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parameters);
            out.close();

            int response = urlConnection.getResponseCode();
            // if resonse = HttpURLConnection.HTTP_OK = 200, then it worked.

            InputStream in = new
            BufferedInputStream(urlConnection.getInputStream());
            
            //resultString = readStream(in);
            
            Document doc = Jsoup.parse(in, "big5", "");
            Elements addressInfo = doc.select("tr[bgcolor=#E1E4F4]");
            //Elements addressInfo1 = doc.select("td[width=6%]"); //td valign="top" width="6%" align="left"
            address_list = new String[addressInfo.size()];
            phone_list = new String[addressInfo.size()];
            for (Element index : addressInfo) {
           	 //Log.d("Smallhau", index.text());
           	 	Elements els = index.select("td");
           	 	Log.d("Smallhau", els.get(0).text()+els.get(1).text()+els.get(3).text());
           	 	address_list[count] = els.get(0).text()+els.get(1).text()+els.get(3).text();
           	 	phone_list[count] = els.get(2).text()+"\n"+els.get(4).text();
           	 	count++;
            }

			} catch( Exception e ){
				Log.v("Smallhau", "Exception error!!!!!!!!!!!!");
			    e.printStackTrace();
			}
			finally {
			    urlConnection.disconnect();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			Intent intent = new Intent(MainActivity.this, oiladdress.class);
			//intent.setClass(MainActivity.this, oiladdress.class);
			intent.putExtra("com.example.gps.ADDRESS", address_list);
			intent.putExtra("com.example.gps.PHONE", phone_list);
			startActivity(intent);	//呼叫新的Activity
		}
        

	}

	private TextView longitude_txt;
	private TextView latitude_txt;
	private TextView address_txt;
	
/*	
	private void getLocation(Location location) {	//將定位資訊顯示在畫面中
		if(location != null) {
			
			//longitude_txt = (TextView) findViewById(R.id.longitude);
			latitude_txt = (TextView) findViewById(R.id.latitude);
			address_txt = (TextView) findViewById(R.id.addressprint);
 
			Double longitude = location.getLongitude();	//取得經度
			Double latitude = location.getLatitude();	//取得緯度
			
			longitude_txt.setText(String.valueOf(longitude));
			latitude_txt.setText(String.valueOf(latitude));
			address_txt.setText("No Address");
		}
		else {
			Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
		}
	}
*/	

	public void showMe(View v) {
		//String _lat=latitude_txt.getText().toString();
		//String lon=longitude_txt.getText().toString();
		
		SearchOILStation test1 = new SearchOILStation();
		test1.execute();
		dialog = new ProgressDialog(this);
		dialog.show();
		

		
		//Uri uri=Uri.parse("geo:"+_lat+","+lon);
		//startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}
	
/*	
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
*/	
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
