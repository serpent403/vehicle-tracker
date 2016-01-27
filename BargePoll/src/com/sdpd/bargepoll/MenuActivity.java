package com.sdpd.bargepoll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MenuActivity extends Activity {
	
	 Button b_queryall;
	 ListView lv;
	 List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();		
   	 String[] from = new String[] {"row_id" , "col_1", "col_2", "col_3"};
   	 int[] to = new int[] {R.id.item1 , R.id.item2 , R.id.item3 , R.id.item4};
   	SimpleAdapter adapter;
	 
	 // List<Barge> bargeList = new ArrayList<Barge>();
	 	 
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
  
     lv = (ListView)findViewById(R.id.listview);
   	

     b_queryall = (Button)findViewById(R.id.queryall);
      b_queryall.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//to remove any previous data in the hash list
			fillMaps.clear();
			
			BufferedReader in = null;
			
			//this string saves the json format string echoed by php script 
			String result="";
			try {
				
				HttpClient client = new DefaultHttpClient();
				
				HttpGet request = new HttpGet();
				
				try {
					Log.i("url","http://www.goyalrajat.in/barge/bargeloc.php");
					request.setURI(new URI("http://www.goyalrajat.in/barge/bargeloc.php"));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				Log.e("passedurl", "http://www.goyalrajat.in/barge/bargeloc.php");				
				//the HTTP response info or code generated after the request is executed
				HttpResponse response = client.execute(request);
				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				result = sb.toString();
				//System.out.println(page);
			} catch (IOException e) {
				e.printStackTrace();
		} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			//parse the json string
	        try{
	                JSONArray jArray = new JSONArray(result);
	                for(int i=0;i<jArray.length();i++){
	                		//with the help of this json object obtain all the query records
	                		//one by one by looping
	                		JSONObject json_data = jArray.getJSONObject(i);
	                        Log.i("log_tag","id: "+json_data.getInt("id")+
	                                ", name: "+json_data.getString("name")+
	                                ", lng: "+json_data.getString("lng")+
	                                ", lat: "+json_data.getString("lat")
	                        );
	                        
	                        //adding the (id,name) of all barges one by one to the List<Barge> 
	                        //bargeList.add(new Barge(json_data.getInt("id") , json_data.getString("name")));
	        	        	HashMap<String, String> map = new HashMap<String, String>();
	        	        	map.put("row_id", "" + i);
	        	        	map.put("col_1", "" + json_data.getString("name"));
	        	        	map.put("col_2", "" + json_data.getString("lng"));
	        	        	map.put("col_3", "" + json_data.getString("lat"));
	        	        	fillMaps.add(map);
	        				
	                }
	        }
	        catch(JSONException e){
	                Log.e("log_tag", "Error parsing data "+e.toString());
	        }
     
	        //This is used to map the data to the listView
	        adapter = new SimpleAdapter(MenuActivity.this, fillMaps, R.layout.customlist, from, to);
	        lv.setAdapter(adapter);
		
		}
	});
     
        
    }
}

