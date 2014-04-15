package com.cjones.taskforcemamba.activity;

import java.net.URL;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;

import com.cjones.taskforcemamba.MainActivity;
import com.cjones.taskforcemamba.R;
import com.cjones.taskforcemamba.adapter.RssReaderListAdapter;
import com.cjones.taskforcemamba.helper.SortingOrder;
import com.cjones.taskforcemamba.helper.ReverseOrder;
import com.cjones.taskforcemamba.helper.RssFeedStructure;
import com.cjones.taskforcemamba.helper.XmlHandler;
import com.cjones.taskforcemamba.adapter.RssReaderListAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class RssFeedReaderActivity extends Activity {
    /** Called when the activity is first created. */
	
	ListView _rssFeedListView;
	List<JSONObject> jobs ;
	List<RssFeedStructure> rssStr ;
	private RssReaderListAdapter _adapter;
	String sorti = "";
	String mode = "";
	Button sort_Btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rssfeedreaderactivity);
       _rssFeedListView = (ListView)findViewById(R.id.rssfeed_listview);
//       sort_Btn = (Button)findViewById(R.id.sort);
//       sort_Btn.setText("Change Sorting Mode");
//       sort_Btn.setOnClickListener(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			if(sorti.equalsIgnoreCase("")){
//				 sorti = "sort";
//			}
//			if(sorti.equalsIgnoreCase("sort")){
//		     sorti = "sort";
//		     sort_Btn.setText("Change Reverse Mode");
//			 RssFeedTask rssTask = new RssFeedTask();
//		     rssTask.execute();
//			}
//			else if(sorti.equalsIgnoreCase("reverse")){
//				 sorti = "reverse";
//				 sort_Btn.setText("Change Normal Mode");
//				 RssFeedTask rssTask = new RssFeedTask();
//			     rssTask.execute();
//			}
//			else if(sorti.equalsIgnoreCase("normal")){
//				sort_Btn.setText("Change Sorting Mode");
//				 RssFeedTask rssTask = new RssFeedTask();
//			     rssTask.execute();
//			}
//    }
        _rssFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Display the Title and Date to the user when they click on an episode
                Object o = _rssFeedListView.getItemAtPosition(position);
                RssFeedStructure obj_itemDetails = (RssFeedStructure)o;
                String objURL = obj_itemDetails.getImgLink();
                if (objURL != null){
//                    Toast.makeText(getApplicationContext(), "objURL = " + objURL, Toast.LENGTH_LONG).show();
                }
                Intent showContent = new Intent(getApplicationContext(),
                TutViewerActivity.class);
                showContent.setData(Uri.parse(objURL));
                startActivity(showContent);
                Toast.makeText(getApplicationContext(), "Loading URL...", Toast.LENGTH_LONG).show();
            }

        });

       RssFeedTask rssTask = new RssFeedTask();
       rssTask.execute();


    }
    private class RssFeedTask extends AsyncTask<String, Void, String> {
		// private String Content;
		private ProgressDialog Dialog;
		String response = "";

		@Override
		protected void onPreExecute() {
			Dialog = new ProgressDialog(RssFeedReaderActivity.this);
			Dialog.setMessage("Rss Loading...");
			Dialog.show();
		
		}

		@Override
		protected String doInBackground(String... urls) {
			  try {
				  //String feed = "http://feeds.nytimes.com/nyt/rss/HomePage";
				  
				  String feed = "http://www.tfmamba.com/external.php?do=rss&type=newcontent&sectionid=155&days=120&count=5";
				  XmlHandler rh = new XmlHandler();
				  rssStr = rh.getLatestArticles(feed);  
			        } catch (Exception e) {
			        }
			return response;

		}

		@Override
		protected void onPostExecute(String result) {
			  if(sorti.equalsIgnoreCase("sort")){
				  sorti = "reverse";
			     Collections.sort(rssStr, new SortingOrder());
			     
			  }else if(sorti.equalsIgnoreCase("reverse")){
				  sorti = "normal";
				  Comparator comp = Collections.reverseOrder();
				  Collections.sort(rssStr, new ReverseOrder());
			  }else{
				  sorti = "";
			  }
			  if(rssStr != null){
			    _adapter = new RssReaderListAdapter(RssFeedReaderActivity.this,rssStr);
		        _rssFeedListView.setAdapter(_adapter);
			  }
		        Dialog.dismiss();
		}
	}

  
}