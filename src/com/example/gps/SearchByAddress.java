package com.example.gps;

/* import程式略 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Size;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class SearchByAddress extends MapActivity
{
	private TextView mTextView01;
	private LocationManager mLocationManager01;
	private String strLocationProvider = "";
	private Location mLocation01;
	private MapController mMapController01;
	private MapView mMapView01;
	private Button mButton01,mButton02,mButton03;
	private EditText mEditText01; private int intZoomLevel=0;
	
	private static final String MAP_URL = "file:///home/smallhau/Android/GPS/assets/googleMap.html";
	private WebView webView;
	 private EditText addressText;
	 private Button submit;
	 private boolean webviewReady = false; 
	
	private GeoPoint fromGeoPoint, toGeoPoint;
	private GeoPoint [] toGeoPoint_array = null; 
	private GeoPoint [] nearGeoPoint_array = null;
	

	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.searchbyaddress); 
/*		
		mTextView01 = (TextView)findViewById(R.id.myTextView1);
		mEditText01 = (EditText)findViewById(R.id.myEditText1);
		mEditText01.setText
		(
			getResources().getText
			(R.string.str_default_address).toString()
		);
*/		
		/* 建立 MapView物件 */
		mMapView01 = (MapView)findViewById(R.id.gmap);
		mMapController01 = mMapView01.getController();
		// 設定 MapView的顯示選項（衛星、街道）
		mMapView01.setSatellite(true);
		mMapView01.setBuiltInZoomControls(true);  // MapView預設縮放控制列
		mMapView01.displayZoomControls(true); 
		
		// 放大的層級 intZoomLevel = 15;
		mMapController01.setZoom(15);
		
		/* 建立 LocationManager物件取得系統 LOCATION服務 */
		mLocationManager01 = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		/* 自訂函數，存取 Location Provider，並將之儲存在 strLocationProvider當中  */
		getLocationProvider();

		/* 傳入 Location物件，顯示於 MapView */
		fromGeoPoint = getGeoByLocation(mLocation01);
		refreshMapViewByGeoPoint(fromGeoPoint, mMapView01, intZoomLevel);

		/* 建立 LocationManager物件，監聽Location變更時事件，更新 MapView*/
		mLocationManager01.requestLocationUpdates
		(strLocationProvider, 2000, 10, mLocationListener01);
		
		
		
		//mButton01 = (Button)findViewById(R.id.myButton1);
		//mButton01.setOnClickListener(new Button.OnClickListener()
//		{
//			@Override public void onClick(View v)
//			{
				new GetGeo().execute();

//			}
//		}
		//);
		
		/* 放大地圖 */
/*
		mButton02 = (Button)findViewById(R.id.streetViewBtn);
		mButton02.setOnClickListener(new Button.OnClickListener()
		{
		@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub 
				intZoomLevel++;
				if(intZoomLevel>mMapView01.getMaxZoomLevel())
				{
					intZoomLevel = mMapView01.getMaxZoomLevel();
				}
				mMapController01.setZoom(intZoomLevel);
			}
		});
*/
		
		/* 縮小地圖 */
/*
		mButton03 = (Button)findViewById(R.id.satelliteViewBtn);
		mButton03.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				intZoomLevel--;
				if(intZoomLevel<1)
				{
					intZoomLevel = 1;
				}
				mMapController01.setZoom(intZoomLevel);
			}
		});	
*/		
	}
	/** Sets up the WebView object and loads the URL of the page **/
	/*
	 private void setupWebView(){
		 Log.d("Smallhau", "10.!!!!!!!!!!!!");
	   webView = (WebView) findViewById(R.id.gmap);
	   Log.d("Smallhau", "11.!!!!!!!!!!!!");
	   webView.getSettings().setJavaScriptEnabled(true);
	   Log.d("Smallhau", "12.!!!!!!!!!!!!");
	   //Wait for the page to load then send the location information
	   webView.setWebViewClient(new WebViewClient(){
	     @Override
	     public void onPageFinished(WebView view, String url)
	     {
	       //webView.loadUrl(centerURL);
	     webviewReady = true;//webview已經載入完畢
	     Log.d("Smallhau", "13.!!!!!!!!!!!!");
	     webView.loadUrl("javascript:resizeMap("+ (webView.getHeight()) + ")");
	     Log.d("Smallhau", "14.!!!!!!!!!!!!");
	     }

	   });
	   webView.loadUrl(MAP_URL);    
	 }
	 */
	/* 捕捉當手機GPS 座標更新時的事件 */
	public final LocationListener mLocationListener01 =
	new LocationListener()
	{
		@Override
		public void onLocationChanged(Location location)
		{
			// TODO Auto-generated method stub
			/* 當手機收到位置變更時，將location 傳入getMyLocation */
			mLocation01 = location;
			fromGeoPoint = getGeoByLocation(location);
			refreshMapViewByGeoPoint(fromGeoPoint,
			mMapView01, intZoomLevel);
		}
		@Override
		public void onProviderDisabled(String provider)
		{
			// TODO Auto-generated method stub
			mLocation01 = null;
		}
		@Override
		public void onProviderEnabled(String provider)
		{
			// TODO Auto-generated method stub
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
			// TODO Auto-generated method stub
		}
	};
	/* 傳入Location 物件，取回其GeoPoint 物件 */
	private GeoPoint getGeoByLocation(Location location)
	{
		GeoPoint gp = null;
		try
		{
			/* 當Location 存在 */
			if (location != null)
			{
				double geoLatitude = location.getLatitude()*1E6;
				double geoLongitude = location.getLongitude()*1E6;
				gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return gp;
	}
	/* 輸入地址，取得其GeoPoint 物件 */
	private GeoPoint getGeoByAddress(String strSearchAddress)
	{
		GeoPoint gp = null;
		try
		{	
			if(strSearchAddress!="")
			{
				Geocoder mGeocoder01 = new Geocoder
				(SearchByAddress.this, Locale.TAIWAN);

				List<Address> lstAddress = mGeocoder01.getFromLocationName
				(strSearchAddress, 1);

				if (!lstAddress.isEmpty())
				{
					Address adsLocation = lstAddress.get(0);
					double geoLatitude = adsLocation.getLatitude()*1E6;
					double geoLongitude = adsLocation.getLongitude()*1E6;
					gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return gp;
	}
	/* 傳入geoPoint 更新MapView 裡的Google Map */
	public static void refreshMapViewByGeoPoint
	(GeoPoint gp, MapView mapview, int zoomLevel)
	{
		try
		{
			mapview.displayZoomControls(true);
			MapController myMC = mapview.getController();
			myMC.animateTo(gp);
			myMC.setCenter(gp);
			myMC.setZoom(zoomLevel);
			mapview.setSatellite(false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/* 傳入經緯度更新MapView 裡的Google Map */
	public static void refreshMapViewByCode
	(double latitude, double longitude,
	MapView mapview, int zoomLevel)
	{
		try
		{
			GeoPoint p = new GeoPoint((int) latitude, (int) longitude);
			mapview.displayZoomControls(true);
			MapController myMC = mapview.getController();
			myMC.animateTo(p);
			myMC.setZoom(zoomLevel);
			mapview.setSatellite(false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/* 將GeoPoint 裡的經緯度以String,String 回傳 */
	private String GeoPointToString(GeoPoint gp)
	{
		String strReturn="";
		try
		{
			/* 當Location 存在 */
			if (gp != null)
			{
			double geoLatitude = (int)gp.getLatitudeE6()/1E6;
			double geoLongitude = (int)gp.getLongitudeE6()/1E6;
			strReturn = String.valueOf(geoLatitude)+","+
			String.valueOf(geoLongitude);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strReturn;
	}
	/* 取得LocationProvider */
	public void getLocationProvider()
	{
		try
		{
			Criteria mCriteria01 = new Criteria();
			mCriteria01.setAccuracy(Criteria.ACCURACY_FINE);
			mCriteria01.setAltitudeRequired(false);
			mCriteria01.setBearingRequired(false);
			mCriteria01.setCostAllowed(true);
			mCriteria01.setPowerRequirement(Criteria.POWER_LOW);
			strLocationProvider =
			mLocationManager01.getBestProvider(mCriteria01, true);
			mLocation01 = mLocationManager01.getLastKnownLocation
			(strLocationProvider);
		}
		catch(Exception e)
		{
			mTextView01.setText(e.toString());
			e.printStackTrace();
		}
	}
	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	private final double EARTH_RADIUS = 6378137.0;

	private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
		+ Math.cos(radLat1) * Math.cos(radLat2)
		* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	public class GetGeo extends AsyncTask<Void, Void, Void>{
		String dst_address = null;
		String [] neibor_address = null;
		int defvalue, option;
		@Override
		protected Void doInBackground(Void... arg0) {
			
			Intent i = getIntent();
			option = i.getIntExtra("com.example.gps.ADDRESSOption", defvalue);

			if (option == 0)
			{
				neibor_address = i.getStringArrayExtra("com.example.gps.ADDRESSListForSearch");
				dst_address = null;
			}
			else
				dst_address = i.getStringExtra("com.example.gps.ADDRESSForSearch");

			// TODO Auto-generated method stub 
			if((dst_address !="") && (dst_address != null ))
			{
				//webView.loadUrl("javascript:codeAddress("+ dst_address + ")");
				//setupWebView();//設定webview 
				// 取得 User要前往地址的 GeoPoint物件
				toGeoPoint = getGeoByAddress(dst_address);
				/* 路徑規劃 Intent */  
				//Intent intent = new Intent();
			}
			else
			{
				toGeoPoint_array = new GeoPoint[neibor_address.length-1];
				double [] distance = new double[neibor_address.length-1];

				Map<String, Double> map_Data = new HashMap<String, Double>();
				
				double fromgeoLatitude = (int)fromGeoPoint.getLatitudeE6()/1E6;
				double fromgeoLongitude = (int)fromGeoPoint.getLongitudeE6()/1E6;

				for (int index = 0; index < neibor_address.length-1; index++)
				{
					toGeoPoint_array[index] = getGeoByAddress(neibor_address[index+1]);
					double togeoLatitude = (int)toGeoPoint_array[index].getLatitudeE6()/1E6;
					double togeoLongitude = (int)toGeoPoint_array[index].getLongitudeE6()/1E6;
					distance[index] = gps2m(togeoLatitude, togeoLongitude ,fromgeoLatitude, fromgeoLongitude);
					map_Data.put(neibor_address[index+1], distance[index]);
				}
				
				List<Map.Entry<String, Double>> list_Data = 
						new ArrayList<Map.Entry<String, Double>>(map_Data.entrySet());
				
				Collections.sort(list_Data, new Comparator<Map.Entry<String, Double>>()
				{
					public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2)
					{
						return (int)(o1.getValue() - o2.getValue());
					}
				});
				
				nearGeoPoint_array = new GeoPoint[5];
				for (int ind = 0; ind < 5; ind++)
					nearGeoPoint_array[ind] = getGeoByAddress(list_Data.get(ind).getKey());
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);

			/* 傳入路徑規劃所需要的地標位址 */
			if (option != 0)
			{
				Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr="+
						GeoPointToString(fromGeoPoint)+
						"&daddr="+GeoPointToString(toGeoPoint)+
						"&hl=tw");
				intent.setData
				(
					uri
				);
			}
			else
			{
				MyLocationOverlay m_MyLocationOverlay;
				m_MyLocationOverlay = new MyLocationOverlay(SearchByAddress.this, mMapView01); 
				mMapView01.getOverlays().add(m_MyLocationOverlay);
				 MyOverlay myOverlay = new MyOverlay(getResources().getDrawable(R.drawable.ic_gas));
			     //try {
			     	List<Overlay> overlays = mMapView01.getOverlays();
			     	for(int ax=0;ax<5;ax++){
			     		OverlayItem ovi = new OverlayItem(nearGeoPoint_array[ax], "oil", "station");
			     		myOverlay.addOverley(ovi);
			     	}
			     	overlays.add(myOverlay);
			     	myOverlay = new MyOverlay(getResources().getDrawable(R.drawable.ic_ironman));
			     	OverlayItem ovi = new OverlayItem(fromGeoPoint, "Man", "Man");
		     		myOverlay.addOverley(ovi);
			     	overlays.add(myOverlay);
			     	mMapController01.setZoom(15);
			}
			startActivity(intent);
		}
		
		class MyOverlay extends ItemizedOverlay{
		 	public int type;
		 	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		 	public MyOverlay(Drawable defaultMarker) {
		 		super(boundCenterBottom(defaultMarker));
		 	}
		 	@Override
		 	public void draw(Canvas canvas, MapView mapView,boolean shadow){
		 		super.draw(canvas, mapView, shadow);
		 	}
		 	@Override
		 	protected OverlayItem createItem(int i) {
		 		return mOverlays.get(i);
		 	}
		 	@Override
		 	public int size() {
		 		return mOverlays.size();
		 	}
		 	public void addOverley(OverlayItem overley){
		 		mOverlays.add(overley);
		 		populate();
		 	}
		 	
		 	protected boolean onTap(int index){
		 		if (index != 5)
		 		{
			 		Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr="+
							GeoPointToString(fromGeoPoint)+
							"&daddr="+GeoPointToString(nearGeoPoint_array[index])+
							"&hl=tw");
					intent.setData
					(
						uri
					);
					startActivity(intent);
		 		}
		 		return true;
		 	}
		 }
	}
}
