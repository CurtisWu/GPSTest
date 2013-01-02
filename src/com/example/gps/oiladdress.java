/**
 * 
 */
/**
 * @author smallhau
 *
 */
package com.example.gps;

//import java.io.IOException;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
public class oiladdress extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oiladdress);
		Intent i = getIntent();
		String[] array = i.getStringArrayExtra("com.example.gps.TEST");
		try {
			ShowAndSetAddress(array);
		}
		catch(Exception e) {
			Toast.makeText(this, "showAddress Failed!!!!!!!!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	private TextView oilAddr1_txt;
	private TextView oilAddr2_txt;
	private TextView oilAddr3_txt;
	
	public void ShowAndSetAddress(String[] array) {
        
        oilAddr1_txt = (TextView) findViewById(R.id.addr1);
        oilAddr2_txt = (TextView) findViewById(R.id.addr2);
        oilAddr3_txt = (TextView) findViewById(R.id.addr3);
        
        oilAddr1_txt.setText(array[0]);
        oilAddr2_txt.setText(array[1]);
        oilAddr3_txt.setText(array[2]);
	}
}*/

public class oiladdress extends ListActivity {
	  String[] array_phone = null;
	  String[] array_address = null;
	  public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    //setContentView(R.layout.oiladdress);
	    Intent i = getIntent();
		array_address = i.getStringArrayExtra("com.example.gps.ADDRESS");
		array_phone = i.getStringArrayExtra("com.example.gps.PHONE");
	    // Use your own layout
		setListAdapter(new ArrayAdapter<String>(this, R.layout.oiladdress, array_address));
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
	  }

	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
		  //intent.putExtra("com.example.gps.ADDRESSForSearch", [(int)id]);
		  Intent intent = new Intent(oiladdress.this, SearchByAddress.class);
		  intent.putExtra("com.example.gps.ADDRESSForSearch", array_address[(int)id]);
		  startActivity(intent);
		  //Toast.makeText(this, array_phone[(int)id], Toast.LENGTH_LONG).show();
	  }
} 