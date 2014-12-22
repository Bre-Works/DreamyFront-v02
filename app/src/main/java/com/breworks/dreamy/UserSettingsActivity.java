package com.breworks.dreamy;

/**
 * Created by Gregorius Adrian on 12/4/2014.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.breworks.dreamy.DreamyLibrary.DreamyActivity;
import com.breworks.dreamy.model.dreamyAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSettingsActivity extends DreamyActivity {

    SessionManager session;
    dreamyAccount login;
    HttpHelper http;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);
        session = new SessionManager(this);
        login = session.getUser();
        http = new HttpHelper();

        ArrayList<SettingsItem> arr = new ArrayList<SettingsItem>();
        arr.add(new SettingsItem("Email","Change your Email"));
        arr.add(new SettingsItem("Password","Change your Password"));
        arr.add(new SettingsItem("First Name","Change your First Name"));
        arr.add(new SettingsItem("Last Name","Change your Last Name"));

        MyAdapter adapt = new MyAdapter(this,arr);

        ListView lv = (ListView) findViewById(R.id.containSet);
        lv.setAdapter(adapt);
        final Context context = this.getApplicationContext();

        final TableLayout.LayoutParams params = new TableLayout.LayoutParams();
        params.setMargins(5, 5, 5, 5);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserSettingsActivity.this);
                        builder.setTitle(login.getEmail());

                        // Set up the input
                        final EditText input = new EditText(UserSettingsActivity.this);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT );
                        input.setHint("Input your new Email");
                        input.setLayoutParams(params);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (input.getText().toString().equals("")) {
                                    Toast.makeText(getApplicationContext(), "Insert your New Email please !", Toast.LENGTH_SHORT).show();
                                } else if (checkEmail(input.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Please insert a valid email", Toast.LENGTH_SHORT).show();
                                } else {
                                    login.setEmail(input.getText().toString());
                                    http.PUTAccount(login);
                                    Toast.makeText(getApplicationContext(), "Your Email has been changed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                        break;
                    case 1 :
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(UserSettingsActivity.this);
                        builder1.setTitle("Change Your Password");

                        // Set up the input
                        final EditText input1 = new EditText(UserSettingsActivity.this);
                        final EditText inputConf2 = new EditText(UserSettingsActivity.this);
                        final EditText inputConf = new EditText(UserSettingsActivity.this);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        LinearLayout lin = new LinearLayout(UserSettingsActivity.this);
                        lin.setOrientation(LinearLayout.VERTICAL);
                        input1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        input1.setHint("Insert your Old Password");
                        input1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        inputConf.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        inputConf.setHint("Insert your New Password");
                        inputConf.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        inputConf2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        inputConf2.setHint("Repeat your New Password");
                        inputConf2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        //lin.setPadding(20, 20, 20, 40);
                        input1.setLayoutParams(params);
                        inputConf.setLayoutParams(params);
                        inputConf2.setLayoutParams(params);
                        lin.addView(input1);
                        lin.addView(inputConf);
                        lin.addView(inputConf2);
                        builder1.setView(lin);

                        // Set up the buttons
                        builder1.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                if(input1.getText().toString().equals("")){
                                    Toast.makeText(getApplicationContext(), "Insert your New Password please !", Toast.LENGTH_SHORT).show();
                                }else if(checkPass(inputConf.getText().toString())){
                                    Toast.makeText(getApplicationContext(), "Password should have at least 6 character, uppercase, lowercase, and number", Toast.LENGTH_SHORT).show();
                                }else if(!inputConf.getText().toString().equals(inputConf2)){
                                    Toast.makeText(getApplicationContext(), "Password confirmation doesn't match", Toast.LENGTH_SHORT).show();
                                }else if(PasswordHash.validatePassword(input1.getText().toString(),login.getPassword())){
                                    Toast.makeText(getApplicationContext(), "Old Password doesn't match", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    login.setPasswordChange(inputConf.getText().toString());
                                    http.PUTAccount(login);
                                    Toast.makeText(getApplicationContext(), "Your Password is Now Changed", Toast.LENGTH_SHORT).show();
                                }
                            }catch(Exception e){
                                    Log.e("ERROR",e.toString());
                                }
                            }
                        });
                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder1.show();
                        break;
                    case 2 :
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(UserSettingsActivity.this);
                        builder2.setTitle(login.getFirstName());

                        // Set up the input
                        final EditText input2 = new EditText(UserSettingsActivity.this);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input2.setInputType(InputType.TYPE_CLASS_TEXT);
                        input2.setHint("Insert your new First Name");
                        input2.setLayoutParams(params);
                        builder2.setView(input2);


                        // Set up the buttons
                        builder2.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(input2.getText().toString().equals("")){
                                    Toast.makeText(getApplicationContext(), "Insert your New FirstName please !", Toast.LENGTH_SHORT).show();
                                }else{
                                     login.setFirstName(input2.getText().toString());
                                     http.PUTAccount(login);
                                     Toast.makeText(getApplicationContext(), "Your First Name has been changed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder2.show();
                        break;
                    case 3 :
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(UserSettingsActivity.this);
                        builder3.setTitle(login.getLastName());

                        // Set up the input
                        final EditText input3 = new EditText(UserSettingsActivity.this);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input3.setInputType(InputType.TYPE_CLASS_TEXT);
                        input3.setHint("Insert your new Last Name");
                        input3.setLayoutParams(params);
                        builder3.setView(input3);

                        // Set up the buttons
                        builder3.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(input3.getText().toString().equals("")){
                                    Toast.makeText(getApplicationContext(), "Insert your New LastName please !", Toast.LENGTH_SHORT).show();
                                }else{
                                    login.setLastName(input3.getText().toString());
                                    http.PUTAccount(login);
                                    Toast.makeText(getApplicationContext(), "Your Last Name has been Changed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder3.show();
                        break;
                }
            }
        });}

    private boolean checkEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean checkPass(String password) {
        String pattern = "((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,})";
        Pattern p = Pattern.compile(pattern);

        Matcher m = p.matcher(password);
        if (m.find()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



class MyAdapter extends ArrayAdapter<SettingsItem>{

    public MyAdapter(Context context,List<SettingsItem> item){
        super(context,0,item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        SettingsItem its = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.settings_row,parent,false);
        }
        TextView Title = (TextView) convertView.findViewById(R.id.TextGede);
        TextView Sub = (TextView) convertView.findViewById(R.id.TextKecil);

        Title.setText(its.MenuItem);
        Sub.setText(its.Subs);

        return convertView;
    }
}

class SettingsItem {
    String MenuItem;
    String Subs;

    public SettingsItem(String MenuItem, String Subs){
        this.MenuItem = MenuItem;
        this.Subs = Subs;
    }
}