package com.cjones.taskforcemamba;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.cjones.taskforcemamba.R;
import java.lang.Override;

/**
 * Created by Caleb on 1/22/14.
 */
public class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_main);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
