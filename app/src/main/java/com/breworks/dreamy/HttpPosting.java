//package com.breworks.dreamy;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONObject;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.app.Activity;
//import java.io.InputStream;
//import java.sql.Timestamp;
//import java.util.Date;
//
///**
// * Created by Ryan Avila on 18/11/2014.
// */
//public class HttpPosting extends Activity implements OnClickListener{
//
//    private EditText usernameEnter;
//    private EditText password;
//    private EditText firstName;
//    private EditText lastName;
//    private EditText email;
//    private EditText id;
//    private Person person;
//    private Button tesBut;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.post_test);
//        usernameEnter = (EditText) findViewById(R.id.usernameEnter);
//        password = (EditText) findViewById(R.id.password);
//        firstName = (EditText) findViewById(R.id.firstname);
//        lastName = (EditText) findViewById(R.id.lastname);
//        email = (EditText) findViewById(R.id.email);
//        id = (EditText) findViewById(R.id.id);
//        tesBut = (Button) findViewById(R.id.test);
//
//        tesBut.setOnClickListener(this);
//    }
//
//    public static String POST(String url, Person person){
//        InputStream inputStream = null;
//        String result = "";
//        try {
//
//            // 1. create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//
//            // 2. make POST request to the given URL
//            HttpPost httpPost = new HttpPost(url);
//
//            String json = "";
//
//            // 3. build jsonObject
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.accumulate("Username", person.getUsername());
//            jsonObject.accumulate("Password", person.getPassword());
//            jsonObject.accumulate("Email", person.getEmail());
//            jsonObject.accumulate("firstName", person.getFirstName());
//            jsonObject.accumulate("LastName", person.getLastName());
//            jsonObject.accumulate("LastAccess", person.getLastAccess());
//            jsonObject.accumulate("id", person.getId());
//
//            // 4. convert JSONObject to JSON to String
//            json = jsonObject.toString();
//
//            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
//            // ObjectMapper mapper = new ObjectMapper();
//            // json = mapper.writeValueAsString(person);
//
//            // 5. set json to StringEntity
//            StringEntity se = new StringEntity(json);
//
//            // 6. set httpPost Entity
//            httpPost.setEntity(se);
//
//            // 7. Set some headers to inform server about the type of the content
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            // 8. Execute POST request to the given URL
//            HttpResponse httpResponse = httpclient.execute(httpPost);
//
//            // 9. receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // 10. convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//
//        // 11. return result
//        return result;
//    }
//
//    @Override
//    public void onClick(View view) {
//        Log.e("Tesat","lol");
//        switch(view.getId()){
//
//
//            case R.id.test:
//                // call AsynTask to perform network operation on separate thread
//                new HttpAsyncTask().execute("http://dreamy-server.herokuapp.com/api/accounts");
//                break;
//        }
//    }
//    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//
//            long offset = Timestamp.valueOf("2012-01-01 00:00:00").getTime();
//            long end = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
//            long diff = end - offset + 1;
//            Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
//
//            person = new Person();
//            person.setUsername(usernameEnter.getText().toString());
//            person.setPassword(password.getText().toString());
//            person.setFirstName(firstName.getText().toString());
//            person.setLastName(lastName.getText().toString());
//            person.setLastAccess(rand.toString());
//            person.setEmail(email.getText().toString());
//            person.setId(Integer.parseInt(id.getText().toString()));
//
//            return POST(urls[0],person);
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
//        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
//        String line = "";
//        String result = "";
//        while((line = bufferedReader.readLine()) != null)
//            result += line;
//
//        inputStream.close();
//        return result;
//
//    }
//}
//
//class Person{
//    private String firstName;
//    private String lastName;
//    private String username;
//    private String password;
//    private String lastAccess;
//    private String email;
//    private int id;
//
//    void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    void setLastName(String lastName){
//        this.lastName = lastName;
//    }
//
//    void setUsername(String username){
//        this.username = username;
//    }
//
//    void setPassword(String password){
//        this.password = password;
//    }
//
//    void setLastAccess(String lastAccess){
//        this.lastAccess = lastAccess;
//    }
//
//    void setEmail(String email){
//        this.email = email;
//    }
//
//    void setId(int id){
//        this.id = id;
//    }
//
//    String getFirstName(){
//        return firstName;
//    }
//
//    String getLastName(){
//        return lastName;
//    }
//
//    String getEmail(){
//        return email;
//    }
//
//    String getUsername(){
//        return username;
//    }
//
//    String getPassword(){
//        return password;
//    }
//
//    String getLastAccess(){
//        return lastAccess;
//    }
//
//    int getId(){
//        return id;
//    }
//}
