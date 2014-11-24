package com.breworks.dreamy;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

/**
 * Created by Ryan Avila on 24/11/2014.
 */
public class ProggressD extends AsyncTask<Void, String, Void> {
    private Context context;
    ProgressDialog dialog;

    public ProggressD(Context cxt) {
        context = cxt;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        dialog.setTitle("Please wait");
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... unused) {
        SystemClock.sleep(2000);
        return (null);
    }

    @Override
    protected void onPostExecute(Void unused) {
        dialog.dismiss();
    }
}