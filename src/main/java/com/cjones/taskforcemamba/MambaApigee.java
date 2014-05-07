package com.cjones.taskforcemamba;

import android.app.Application;

import com.apigee.sdk.ApigeeClient;

/**
 * Created by Caleb on 5/3/2014.
 */
public class MambaApigee extends Application{
    private ApigeeClient apigeeClient;

    public MambaApigee()
    {
        this.apigeeClient = null;
    }

    public ApigeeClient getApigeeClient()
    {
        return this.apigeeClient;
    }

    public void setApigeeClient(ApigeeClient apigeeClient)
    {
        this.apigeeClient = apigeeClient;
    }
}
