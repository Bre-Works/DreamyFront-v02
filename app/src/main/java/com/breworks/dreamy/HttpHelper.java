package com.breworks.dreamy;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.breworks.dreamy.model.dreamyAccount;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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

/**
 * Created by Ryan Avila on 19/11/2014.
 */
public class HttpHelper {

    private String TAG = this.getClass().getSimpleName();
    static String AccountUrl = "http://dreamy-server.herokuapp.com/api/accounts";
    String LogUrl = "http://dreamy-server.herokuapp.com/api/logs";

    public static String POSTtoAccount(Person person){
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(AccountUrl);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Username", person.getUsername());
            jsonObject.accumulate("Password", person.getPassword());
            jsonObject.accumulate("Email", person.getEmail());
            jsonObject.accumulate("firstName", person.getFirstName());
            jsonObject.accumulate("LastName", person.getLastName());
            jsonObject.accumulate("LastAccess", person.getLastAccess());
            jsonObject.accumulate("id", person.getId());

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
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return result;
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

    public accountModel findAccountByName(Context context,String username){
        List<accountModel>acc = reqAccount(context,AccountUrl+"?filter[where][Username]="+username);
        if(acc.size() == 0){
            return acc.get(0);
        }
        return null;
    }

    public accountModel findAccountByID(Context context, int id){
        List<accountModel> acc = reqAccount(context,AccountUrl+"?filter[where][id]="+id);
        if(acc.size() == 0){
            return acc.get(0);
        }
        return null;
    }

    public List<accountModel> reqAccount(Context context,String url){
        RequestQueue queue;
        queue =  Volley.newRequestQueue(context);
        final List<accountModel> result = new ArrayList<accountModel>();
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

    private void parseJSONtoAccount(JSONArray response,List<accountModel> acc){
        try{
            for (int i = 0; i < response.length(); i++) {

                JSONObject person = (JSONObject) response
                        .get(i);

                accountModel am = new accountModel();

                am.setFirstName(person.getString("firstName"));
                Log.e("firstName",person.getString("firstName"));

                am.setLastName(person.getString("LastName"));
                am.setUsername(person.getString("Username"));
                Log.e("username",am.getUsername());

                am.setPassword(person.getString("Password"));
                am.setEmail(person.getString("Email"));
                am.setId(person.getInt("id"));
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
