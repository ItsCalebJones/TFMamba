package com.cjones.taskforcemamba.activity;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.apigee.sdk.ApigeeClient;
import com.apigee.sdk.data.client.DataClient;
import com.apigee.sdk.data.client.callbacks.ApiResponseCallback;
import com.apigee.sdk.data.client.callbacks.DeviceRegistrationCallback;
import com.apigee.sdk.data.client.entities.Device;
import com.apigee.sdk.data.client.response.ApiResponse;
import com.cjones.taskforcemamba.About;
import com.cjones.taskforcemamba.R;
import com.cjones.taskforcemamba.adapter.RssReaderListAdapter;
import com.cjones.taskforcemamba.fragments.AboutFragment;
import com.cjones.taskforcemamba.fragments.AnnouncementFragment;
import com.cjones.taskforcemamba.fragments.BlogFragment;
import com.cjones.taskforcemamba.fragments.MediaFragment;
import com.cjones.taskforcemamba.fragments.WebViewFragment;
import com.cjones.taskforcemamba.helper.MambaApigee;
import com.cjones.taskforcemamba.helper.RssFeedStructure;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NavDrawer extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    //Variables needed for the listview
    ListView _rssFeedListView;
    List<JSONObject> jobs ;
    List<RssFeedStructure> rssStr ;
    private RssReaderListAdapter _adapter;
    private MambaApigee YourApp;

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String TAG = "Task Force Mamba";
    private static final String ORGNAME = "caman9119";
    private static final String APPNAME = "TASKFORCEMAMBA";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static MambaApigee entityApplication;
    public static String TAGPlayService = "PlayServiceReg";
    public static String API_URL = "https://api.usergrid.com";
    static final String NOTIFIER = "Mamba";
    public String feed = "http://www.tfmamba.com/external.php?do=rss&type=newcontent&sectionid=155&days=120&count=10";
    public String mediaFeed = "http://www.tfmamba.com/external.php?do=rss&type=newcontent&sectionid=153&days=120&count=10";
    public String announcementFeed = "http://www.tfmamba.com/external.php?do=rss&type=newcontent&sectionid=155&days=120&count=10";
    public String blogFeed = "http://www.tfmamba.com/blog_external.php?type=RSS2";


    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;
    String regid;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "529244210304";

    //Initialize the Apigee stuff
    private static DataClient client;
    private static Device device;
    static synchronized DataClient getClient(Context context) {
        if (client == null) {
            if (ORGNAME.equals("TASKFORCEMAMBA")) {
                Log.e(TAG, "ORG value has not been set.");
            } else {
                ApigeeClient apigeeClient = new ApigeeClient(ORGNAME,APPNAME,API_URL,context);
                client = apigeeClient.getDataClient();
            }
        }
        return client;
    }
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        context = getApplicationContext();

        //Init Apigee SDK
        initializeSDK ();
        setTitle("Announcement");

        //Setup UIL
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .build();
        ImageLoader.getInstance().init(config);

        //Check for Google Play Services
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.

            Log.d(TAGPlayService, "Registration starting...");
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            Log.d(TAGPlayService, "Registration context = " + context + " Current RegID = " + regid);
            if (regid.isEmpty()) {
                Log.d(TAGPlayService, "Registering in the background...");
                Toast.makeText(getApplicationContext(), "Registering for Push Notifications...", Toast.LENGTH_SHORT).show();
                registerInBackground();
                Log.d(TAGPlayService, "Registering in the background completed.");
            }
            sendRegistrationIdToBackend();
            Log.d(TAGPlayService, "Registering completed: " + regid);
        } else {
            Toast.makeText(getApplicationContext(), "Error initiating Google Play services, please download valid Play Services APK.", Toast.LENGTH_LONG).show();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private final void initializeSDK () {
        // This creates an instance of the Apigee.Client class which initializes the SDK
        ApigeeClient apigeeClient = new ApigeeClient(ORGNAME, APPNAME, this.getBaseContext());
        setApplicationApigeeClient (apigeeClient);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getFragmentManager();

        Fragment fragment = new AnnouncementFragment();
        FragmentManager fragmentManager = getFragmentManager();
        switch (position){
            case 0:
                fragment = new AnnouncementFragment();
                break;
            case 1:
                fragment = new MediaFragment();
                break;
            case 2:
                fragment = new BlogFragment();
                break;
            case 3:
                fragment = new AboutFragment();
                break;
            case 4:
                fragment = new WebViewFragment();
                break;
        }
                fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        onSectionAttached(position);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    public void onSectionAttached(int position) {
        switch (position) {
            case 0:
                mTitle = getString(R.string.title_section1);
                getActionBar().setTitle(mTitle);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                getActionBar().setTitle(mTitle);
                break;
            case 2:
                mTitle = "Blog";
                getActionBar().setTitle(mTitle);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                getActionBar().setTitle(mTitle);
                break;
            case 4:
                mTitle = "Website";
                getActionBar().setTitle("Website");
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_list, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.apply:
                Log.i(TAG, "Web trigger");
                Intent homeContent = new Intent(this, WebActivity.class);
                startActivity(homeContent);
                return true;
            case R.id.home:
                Log.i(TAG, "Home trigger");
                Intent intent = new Intent(this, NavDrawer.class);
                startActivity(intent);
                return true;
            case R.id.Web:
                Intent Webintent = new Intent(this, WebActivity.class);
                startActivity(Webintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Sets the Apigee.Client class in the application class so
    // that it is accessible to other activities in the app
    private final void setApplicationApigeeClient (ApigeeClient apigeeClient) {
        entityApplication = (MambaApigee) getApplication();
        entityApplication.setApigeeClient(apigeeClient);
    }
    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                Log.i(TAGPlayService, "doInBackground started...");
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;


                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

                Toast.makeText(getApplicationContext(), "Registered for Mamba Alerts!", Toast.LENGTH_LONG).show();
            }
        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        //Get an instance of the Apigee DataClient class from the ApigeeClient object
        DataClient dataClient = getClient(context);

        if (dataClient != null) {

            dataClient.registerDeviceForPushAsync(dataClient.getUniqueDeviceID(), NOTIFIER, regid, null, new DeviceRegistrationCallback() {
                @Override
                public void onResponse(Device device) {
                    Log.i(TAG, "register response: " + device);
                    DataClient dataClient = getClient(context);

                    if (dataClient != null) {
                        // connect Device to current User - if there is one
                        if (dataClient.getLoggedInUser() != null) {
                            dataClient.connectEntitiesAsync("users", dataClient.getLoggedInUser().getUuid().toString(),
                                    "devices", device.getUuid().toString(),
                                    new ApiResponseCallback() {
                                        @Override
                                        public void onResponse(ApiResponse apiResponse) {
                                            Log.i(TAG, "connect response: " + apiResponse);
                                        }

                                        @Override
                                        public void onException(Exception e) {
                                            Log.i(TAG, "connect exception: " + e);
                                        }
                                    });
                        }
                    } else {
                        Log.e(TAG,"data client is null, did you set ORG value in Settings.java?");
                    }
                }

                @Override
                public void onException(Exception e) {
                }

                @Override
                public void onDeviceRegistration(Device device) { /* this won't ever be called */ }
            });
        } else {

        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(RssFeedReaderActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_nav_drawer, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((NavDrawer) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    }
}


