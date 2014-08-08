package com.cjones.taskforcemamba.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cjones.taskforcemamba.R;
import com.cjones.taskforcemamba.adapter.RssReaderListAdapter;
import com.cjones.taskforcemamba.helper.RssFeedStructure;
import com.cjones.taskforcemamba.helper.XmlHandler;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MediaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MediaFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MediaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FadingActionBarHelper mFadingHelper;
    ListView _rssFeedListView;
    private RssReaderListAdapter _adapter;
    List<RssFeedStructure> rssStr ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mediaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaFragment newInstance(String param1, String param2) {
        MediaFragment fragment = new MediaFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }
    public MediaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private class RssFeedTask extends AsyncTask<String, Void, String> {
        // private String Content;
        private ProgressDialog Dialog;
        String response = "";

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(getActivity());
            Dialog.setMessage("Rss Loading...");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                //String feed = "http://feeds.nytimes.com/nyt/rss/HomePage";

                String feed = "http://www.tfmamba.com/external.php?do=rss&type=newcontent&sectionid=153&days=120&count=10";
                XmlHandler rh = new XmlHandler();
                rssStr = rh.getLatestArticles(feed);
            } catch (Exception e) {
            }
            return response;

        }


        @Override
        protected void onPostExecute(String result) {

            if (rssStr != null) {
                _adapter = new RssReaderListAdapter(getActivity(), rssStr);
                _rssFeedListView.setAdapter(_adapter);
                Log.i("Task Force Mamba", "onPostExecute adapter = " + _adapter);
            }
            Dialog.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        } else {
            RssFeedTask rssTask = new RssFeedTask();
            rssTask.execute();
        }

        View v = inflater.inflate(R.layout.fragment_mediapage_list, container, false);
        _rssFeedListView = (ListView) v.findViewById(R.id.rssfeed_listview);
        _rssFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Display the Title and Date to the user when they click on an episode
                Object o = _rssFeedListView.getItemAtPosition(position);
                RssFeedStructure obj_itemDetails = (RssFeedStructure) o;
                String objURL = obj_itemDetails.getYouTubeLink();
                String Title = obj_itemDetails.getTitle();
                if (objURL != null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + objURL)));
                    Toast.makeText(getActivity(), "Loading " + Title + " ...", Toast.LENGTH_SHORT).show();
//
                } else {
                    Toast.makeText(getActivity(), "Clicking on an item is a future enhancement, for now click the cloud icon to launch to the webview.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        return v;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
