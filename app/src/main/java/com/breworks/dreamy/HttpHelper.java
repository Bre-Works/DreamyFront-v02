package com.breworks.dreamy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.breworks.dreamy.model.dreamyAccount;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static android.widget.Toast.*;

/**
 * Created by Ryan Avila on 19/11/2014.
 */
public class HttpHelper {

    private String TAG = this.getClass().getSimpleName();
    Context _context;
    String AccountUrl = "http://dreamy-server.herokuapp.com/api/accounts";
    String LogUrl = "http://dreamy-server.herokuapp.com/api/logs";

    public HttpHelper(){
    }

    public HttpHelper(Context context){
        this._context = context;
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
            jsonObject.accumulate("LastAccess", person.getLastAccess());
            Log.e("LastAccess", jsonObject.getString("LastAccess"));
            jsonObject.accumulate("id", person.getId());
            Log.e("id",jsonObject.getString("id"));

        }catch (JSONException ex){
            Log.e("JSON","Error when building JSON");
        }
        try {
            // 1. create HttpClient
            DefaultHttpClient httpclient = new DefaultHttpClient();

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
            HttpResponse httpResponse = httpclient.execute(httpPost);

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
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

    public dreamyAccount findAccountByUserName(String username){
        List<dreamyAccount>acc = reqAccount(AccountUrl+"?filter[where][Username]="+username);
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

    public List<dreamyAccount> reqAccount(String url){
        RequestQueue queue;
        queue =  Volley.newRequestQueue(_context);
        final List<dreamyAccount> result = new ArrayList<dreamyAccount>();
                JsonArrayRequest req = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // Parsing json array response
                                // loop through each json object
                                parseJSONtoAccount(response,result);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
        queue.add(req);
        return result;
    }

    private void parseJSONtoAccount(JSONArray response,List<dreamyAccount> acc){
        try{
            for (int i = 0; i < response.length(); i++) {

                JSONObject person = (JSONObject) response
                        .get(i);

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

                acc.add(am);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
            Log.e("errorProblem",
                    "Error: " + e.getMessage());
        }
    }

}
