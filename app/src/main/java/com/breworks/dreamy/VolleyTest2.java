package com.breworks.dreamy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ryan Avila on 15/11/2014.
 */
public class VolleyTest2 extends Activity {
    private String TAG = this.getClass().getSimpleName();
    private LinearLayout line;
    private RequestQueue queue;
    private ArrayList<accountModel> acc;
    private ProgressDialog pd;
    private String jsonResponse;

    TextView bigUsername;
    TextView username;
    TextView password;
    TextView id;
    TextView lastname;
    TextView firstname;
    TextView email;
    TextView lastAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley2);
        acc = new ArrayList<accountModel>();
        line = (LinearLayout) findViewById(R.id.linearLo);
        queue =  Volley.newRequestQueue(this);

        String url = "https://dreamy-server.herokuapp.com/api/Accounts";
        pd = ProgressDialog.show(this, "Please Wait...", "Please Wait...");
        try{
            Thread.sleep(2000);
        }catch(Exception e){
        }
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
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
                            Log.e("fuck", acc.get(0).getLastAccess());
                            makeitThere(acc);
                            pd.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }  }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        /*JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
                Log.e("kok","Masuk COY");
                parseJSON(response);
                pd.dismiss();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,error.getMessage());
            }
        });*/

        queue.add(req);
    }

    public void makeitThere(ArrayList<accountModel> acc) {

        Log.e("size", String.valueOf(acc.size()));

        if(acc.size() > 0){
            for(accountModel ac : acc){

                Log.e("username",ac.getUsername());

                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);

                final View addView = inflater.inflate(R.layout.volley_row, null);

                bigUsername = (TextView) addView.findViewById(R.id.bigUsername);
                bigUsername.setText(ac.getUsername());

                password = (TextView) addView.findViewById(R.id.password);
                password.setText(ac.getPassword());

                id = (TextView) addView.findViewById(R.id.Id);
                id.setText(String.valueOf(ac.getId()));

                lastname = (TextView) addView.findViewById(R.id.lastName);
                lastname.setText(ac.getLastName());

                firstname = (TextView) addView.findViewById(R.id.firstName);
                firstname.setText(ac.getFirstName());

                email = (TextView) addView.findViewById(R.id.email);
                email.setText(ac.getEmail());

                lastAccess = (TextView) addView.findViewById(R.id.lastAccess);
                lastAccess.setText(ac.getLastAccess());

                line.addView(addView);
            }
        }
    }

    private void parseJSON(JSONObject json){
        try{
            JSONObject value = json.getJSONObject("value");
            JSONArray items = value.getJSONArray("items");
            for(int i=0;i<items.length();i++){

                JSONObject item = items.getJSONObject(i);
                accountModel am = new accountModel();
                am.setFirstName(item.optString("firstName"));

                am.setLastName(item.optString("LastName"));
                am.setUsername(item.optString("Username"));
                am.setPassword(item.optString("Password"));
                am.setEmail(item.optString("Email"));
                am.setId(item.optInt("id"));
                am.setLastAccess(item.optString("LastAccess"));
                acc.add(am);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }
}

class accountModel{
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String lastAccess;
    private String email;
    private int id;

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    void setLastName(String lastName){
        this.lastName = lastName;
    }

    void setUsername(String username){
        this.username = username;
    }

    void setPassword(String password){
        this.password = password;
    }

    void setLastAccess(String lastAccess){
        this.lastAccess = lastAccess;
    }

    void setEmail(String email){
        this.email = email;
    }

    void setId(int id){
        this.id = id;
    }

    String getFirstName(){
        return firstName;
    }

    String getLastName(){
        return lastName;
    }

    String getEmail(){
        return email;
    }

    String getUsername(){
        return username;
    }

    String getPassword(){
        return password;
    }

    String getLastAccess(){
        return lastAccess;
    }

    int getId(){
        return id;
    }
}
