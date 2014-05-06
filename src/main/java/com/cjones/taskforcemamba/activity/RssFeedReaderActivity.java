package com.cjones.taskforcemamba.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cjones.taskforcemamba.About;
import com.cjones.taskforcemamba.MainActivity;
import com.cjones.taskforcemamba.R;
import com.cjones.taskforcemamba.adapter.RssReaderListAdapter;
import com.cjones.taskforcemamba.helper.ReverseOrder;
import com.cjones.taskforcemamba.helper.RssFeedStructure;
import com.cjones.taskforcemamba.helper.SortingOrder;
import com.cjones.taskforcemamba.helper.XmlHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class RssFeedReaderActivity extends Activity {
    /** Called when the activity is first created. */
	
	private final String feed = "http://feeds.nytimes.com/nyt/rss/HomePage";
	//preppulating feed list with the same feeds since I don't have any others to use right now.
	private final List<String> feeds= new ArrayList<String>(){{
			add(feed); 
			add(feed); 
			add(feed);
		}};
	
	List<ListView> _rssFeedListViews = new ArrayList<ListView>();
	List<JSONObject> jobs ;
	List<List<RssFeedStructure>> rssStrs;
	String sorti = "";
	String mode = "";
	Button sort_Btn;
    public static final String PREFS_NAME = "MyPrefsFile";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .build();
        ImageLoader.getInstance().init(config);
        setContentView(R.layout.rssfeedreaderactivity);
        for(String s : feeds){
        	ListView lv = (ListView)findViewById(R.id.rssfeed_listview);
        	lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                                    long id) {
	                //Display the Title and Date to the user when they click on an episode
	//                Object o = _rssFeedListView.getItemAtPosition(position);
	//                RssFeedStructure obj_itemDetails = (RssFeedStructure)o;
	//                String objURL = obj_itemDetails.getImgLink();
	//                if (objURL != null){
	////                    Toast.makeText(getApplicationContext(), "objURL = " + objURL, Toast.LENGTH_LONG).show();
	//                }
	//                Intent showContent = new Intent(getApplicationContext(),
	//                MainActivity.class);
	//                showContent.setData(Uri.parse(objURL));
	//                startActivity(showContent);
	                Toast.makeText(getApplicationContext(), "Clicking on an item is a future enhancement, for now click the cloud icon to launch to the webview.", Toast.LENGTH_LONG).show();
	            }
	
			});
        	
        	//add to our list of views
        	_rssFeedListViews.add(lv);
        }

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
				  
				  
				  for(String s : feeds) { 
				  	XmlHandler rh = new XmlHandler();
				  	rssStrs.add(rh.getLatestArticles(s));
				  }
			  } catch (Exception e) {}
			return response;

		}

		@Override
		protected void onPostExecute(String result) {
			  if(sorti.equalsIgnoreCase("sort")){
				  sorti = "reverse";
				 for(List<RssFeedStructure> struct : rssStrs){
					 Collections.sort(struct, new SortingOrder());
					 }
			     
			     
			  }else if(sorti.equalsIgnoreCase("reverse")){
				  sorti = "normal";
				  Comparator comp = Collections.reverseOrder();
				  for(List<RssFeedStructure> struct : rssStrs){
						 Collections.sort(struct, new ReverseOrder());
				  }
			  }else{
				  sorti = "";
			  }
			  if(rssStrs != null){
			  	int adapterCt = 0;
			  	if(_rssFeedListViews == null) return;
			  	for(ListView lv : _rssFeedListViews){
			  		if(adapterCt < rssStrs.size()){
			  			lv.setAdapter(new RssReaderListAdapter(RssFeedReaderActivity.this, rssStrs.get(adapterCt)));
			  		}
			  	}
			  }
			  Dialog.dismiss();
		}
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_list, menu);
        return super.onCreateOptionsMenu(
                menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.apply:
                Log.i(TAG, "Login trigger");
                Intent showContent = new Intent(this, MainActivity.class);
                showContent.setData(Uri.parse("http://www.tfmamba.com/register.php"));
                startActivity(showContent);
//                myWebView.loadUrl("http://www.tfmamba.com/register.php");
                return true;
            case R.id.Web:
                Log.i(TAG, "Web trigger");
                Intent homeContent = new Intent(this, MainActivity.class);
                homeContent.setData(Uri.parse("http://www.tfmamba.com"));
                startActivity(homeContent);
//                myWebView.loadUrl("http://www.tfmamba.com");
                return true;
            case R.id.youtube:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=ILAT3qPB1FA")));
                return true;
            case R.id.about:
                Log.i(TAG, "About trigger");
                Intent AboutIntent = new Intent(this, About.class);
                startActivity(AboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static final String TAG = "Task Force Mamba";

  
}
