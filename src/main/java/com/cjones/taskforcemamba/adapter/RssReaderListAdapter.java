package com.cjones.taskforcemamba.adapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.cjones.taskforcemamba.R;
import com.cjones.taskforcemamba.helper.RssFeedStructure;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.picasso.Picasso;

public class RssReaderListAdapter extends ArrayAdapter<RssFeedStructure> {
	List<RssFeedStructure> imageAndTexts1 =null;
public RssReaderListAdapter(Activity activity, List<RssFeedStructure> imageAndTexts) {
super(activity, 0, imageAndTexts);
imageAndTexts1 = imageAndTexts;
}


@Override
public View getView(int position, View convertView, ViewGroup parent) {

Activity activity = (Activity) getContext();
LayoutInflater inflater = activity.getLayoutInflater();
View rowView = inflater.inflate(R.layout.rssfeedadapter_layout, null);

//This sets up the listview items into TextView
TextView textView = (TextView) rowView.findViewById(R.id.feed_text);
TextView dtextView = (TextView) rowView.findViewById(R.id.feed_description);
TextView timeFeedText = (TextView) rowView.findViewById(R.id.feed_updatetime);
TextView feed_link = (TextView)rowView.findViewById(R.id.feed_link);
ImageView imageView = (ImageView) rowView.findViewById(R.id.feed_image);

//        try {
        	
        	Log.d("rssfeed", "imageAndTexts1.get(position).getImgLink() :: " +imageAndTexts1.get(position).getImgLink() +" :: " +imageAndTexts1.get(position).getTitle());
        	textView.setText(imageAndTexts1.get(position).getTitle());
            textView.setTypeface(null, Typeface.BOLD);
            dtextView.setText (Html.fromHtml(getItem(position).getDescription()));
            feed_link.setText(imageAndTexts1.get(position).getURLLink());
        	SpannableString content = new SpannableString(imageAndTexts1.get(position).getPubDate());
        	content.setSpan(new UnderlineSpan(), 0, 13, 0);

            timeFeedText.setText(content);
//            //ISSUE FIXED: Feed image isn't returning the URL's inside <img src> instead is returning the Title URL
            if(imageAndTexts1.get(position).getImgLink() !=null){
                String feedImage = imageAndTexts1.get(position).getImgLink().toString();
                String NewFeedImage = String.valueOf(feedImage);
                Log.i("Mamba", "feedImage = " + feedImage + "NewFeedImage = " + NewFeedImage);
                if(!feedImage.toString().equalsIgnoreCase("null")){
                    Picasso.with(getContext()).load(NewFeedImage).into(imageView);
                }
                else{
                    imageView.setBackgroundResource(R.drawable.ic_launcher);
                }
            }
    if(imageAndTexts1.get(position).getYouTubeLink() !=null){
        String feedImage = imageAndTexts1.get(position).getYouTubeLink().toString();
        String NewFeedImage = String.valueOf(feedImage);
        Log.i("Mamba", "feedImage = " + feedImage + "NewFeedImage = " + NewFeedImage);
        if(!feedImage.toString().equalsIgnoreCase("null")){
            Picasso.with(getContext()).load("http://img.youtube.com/vi/" + NewFeedImage + "/0.jpg").resize(1000,750).into(imageView);
        }
        else{
            imageView.setBackgroundResource(R.drawable.ic_launcher);
        }
    }


       
        	
//        } catch (MalformedURLException e) {
//
//        }
//        catch (IOException e) {
//
//        }


return rowView;

}
//    public class IMGFeedTask extends AsyncTask<String, Void, String> {
//        // private String Content;
//        String response = "";
//
//
//        @Override
//        protected String doInBackground(String... urls) {
//            try {
//////                String feed = "http://feeds.nytimes.com/nyt/rss/HomePage";
////                HttpURLConnection conn = (HttpURLConnection)feed.openConnection();
////                InputStream is = conn.getInputStream();
////                Bitmap img = BitmapFactory.decodeStream();
////                imageView.setImageBitmap(img);
//
//            } catch (Exception e) {
//            }
//            return response;
//
//        }



}