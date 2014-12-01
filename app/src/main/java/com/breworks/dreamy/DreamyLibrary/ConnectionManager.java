package com.breworks.dreamy.DreamyLibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ryan Avila on 01/12/2014.
 */
public class ConnectionManager {
    Context context;

    public ConnectionManager(Context context){
        this.context = context;
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

}
