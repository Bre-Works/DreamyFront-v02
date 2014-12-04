package com.breworks.dreamy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.breworks.dreamy.model.Logs;
import com.breworks.dreamy.model.dreamyAccount;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;

/**
 * Created by Ryan Avila on 19/11/2014.
 */
public class HttpHelper {

    private String TAG = this.getClass().getSimpleName();
    Context _context;
    static String AccountUrl = "http://dreamy-server.herokuapp.com/api/accounts";
    static String LogUrl = "http://dreamy-server.herokuapp.com/api/logs";
    List<dreamyAccount> accounts;
    ProgressDialog pDialog;
    HttpClient client;

    public HttpHelper(){
        client = new DefaultHttpClient();
    }

    public String POSTtoAccount(dreamyAccount person){
        InputStream inputStream = null;
        String result = "";
        JSONObject jsonObject = new JSONObject();
        try{
            // 3. build jsonObject
            jsonObject = new JSONObject();
            jsonObject.accumulate("Username", person.getUsername());
            Log.e("username", jsonObject.getString("Username"));
            jsonObject.accumulate("Password", person.getPassword());
            Log.e("pass", jsonObject.getString("Password"));
            jsonObject.accumulate("Email", person.getEmail());
            Log.e("email", jsonObject.getString("Email"));
            jsonObject.accumulate("firstName", person.getFirstName());
            Log.e("firstName", jsonObject.getString("firstName"));
            jsonObject.accumulate("LastName", person.getLastName());
            Log.e("LastName", jsonObject.getString("LastName"));
            jsonObject.accumulate("LastAccess", person.getLastAccess().toString());
            Log.e("LastAccess", jsonObject.getString("LastAccess"));
            jsonObject.accumulate("id", person.getId());
            Log.e("id",jsonObject.getString("id"));

        }catch (JSONException ex){
            Log.e("JSON","Error when building JSON");
        }
        try {

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(AccountUrl);
            String json = "";

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = client.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.e("InputStream",e.toString());
        }
        return result;
    }

    public String POSTtoLogs(Logs logs){
        InputStream inputStream = null;
        String Result = "";

        try {

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(LogUrl);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("userID", logs.getUsername());
            jsonObject.accumulate("accessDate", logs.getAccessDate().toString());
            jsonObject.accumulate("id", logs.getId());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = client.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                Result = convertInputStreamToString(inputStream);
            else
                Result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return Result;

        }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }

    public void POSTAccount(dreamyAccount dc){
        new JSONAccount().execute(dc);
    }

    private class JSONAccount extends AsyncTask<dreamyAccount,Void,String>{
        @Override
        protected String doInBackground(dreamyAccount... dc) {

            return POSTtoAccount(dc[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.e("DATA SENT","SENT LHO YA");
        }
    }

    public void POSTLogs(Logs lg){
        new JSONLogs().execute(lg);
    }

    private class JSONLogs extends AsyncTask<Logs,Void,String>{
        @Override
        protected String doInBackground(Logs... lg) {

            return POSTtoLogs(lg[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.e("DATA SENT","SENT LHO YA");
        }
    }

    public ArrayList<dreamyAccount> reqAccount(String url){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String result = getJSONCommand(url);
        ArrayList<dreamyAccount> param = new ArrayList<dreamyAccount>();
        try {
            JSONArray js = new JSONArray(result);

            for(int i = 0;i < js.length();i++){
                JSONObject person = (JSONObject) js.get(i);

                dreamyAccount am = new dreamyAccount();

                am.setFirstName(person.getString("firstName"));
                Log.e("firstName",person.getString("firstName"));

                am.setLastName(person.getString("LastName"));
                am.setUsername(person.getString("Username"));
                Log.e("username",am.getUsername());

                am.setPassword(person.getString("Password"));
                am.setEmail(person.getString("Email"));
                am.setId(person.getLong("id"));
                Log.e("ID", String.valueOf(am.getId()));

                am.setLastAccess(person.getString("LastAccess"));

                param.add(am);
                Log.e("TROLL", String.valueOf(js.get(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return param;
    }

    public String getJSONCommand(String url){
        HttpGet request = new HttpGet(url);
        HttpResponse response;
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        String result = null;
        try {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result = convertInputStreamToString(instream);
                // now you have the string representation of the HTML request
                Log.e("RESPONSE: ","this is = " + result);
                instream.close();
            }
            // Headers
            org.apache.http.Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
        } catch (ClientProtocolException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return result;
    }

    // Usable Code
    public List<dreamyAccount> findAllAccount(){
        List<dreamyAccount>acc = reqAccount(AccountUrl);
        return acc;
    }

    public dreamyAccount findAccountByUserName(String username){
        List<dreamyAccount>acc = reqAccount(AccountUrl+"?filter[where][Username]="+username);
        Log.e("SIZE", String.valueOf(acc.size()));
        if(acc.size() != 0){
            return acc.get(0);
        }
        return null;
    }

    public dreamyAccount findAccountByID(Long id){
        List<dreamyAccount> acc = reqAccount(AccountUrl+"?filter[where][id]="+id);
        if(acc.size() != 0){
            return acc.get(0);
        }
        return null;
    }

    public dreamyAccount searchAccountByUsername(List<dreamyAccount> acc,String username){
        for(dreamyAccount dc : acc){
            if(dc.getUsername().equals(username)){
                return dc;
            }
        }
        return null;
    }



}
