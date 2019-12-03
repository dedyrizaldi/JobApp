package com.example.lokerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lokerapp.koneksi.JSONParser;
import com.example.lokerapp.tools.SharedPrefManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    Button btnRegis, btnLogin;
    EditText edtEmail, edtPassword;
    JSONParser jsonParser = new JSONParser();
    String ip, getEmail, getPassword;
    String dbIdUser, dbFristName, dbLastName, dbEmail, dbPassword,dbAddress, dbCity, dbState, dbContact, dbQualification, dbStream, dbPassing, dbDob, dbAge, dbDesignation, dbResume;;
    int badge;
    int sukses;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ip=jsonParser.getIP();
        sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.getSPSudahLoginUser()){
            startActivity(new Intent(LoginActivity.this, Drawer.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        btnRegis = (Button)findViewById(R.id.btn_regis);
        btnLogin = (Button)findViewById(R.id.btn_login);
        edtEmail = (EditText)findViewById(R.id.edt_email);
        edtPassword = (EditText)findViewById(R.id.edt_password);


        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromEditText();
                if (getEmail.length()<1||getPassword.length()<1){
                    empty();
                }else{
                    new login().execute();
                }


            }
        });

    }

    private void getDataFromEditText(){
        getEmail = edtEmail.getText().toString();
        getPassword = edtPassword.getText().toString();
    }


    //fungsi untuk ambil data dari json php dengan http request
    class login extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... params) {
            try {

                List<NameValuePair> myparams = new ArrayList<NameValuePair>();
                myparams.add(new BasicNameValuePair("email", getEmail));
                myparams.add(new BasicNameValuePair("password", getPassword));
                String url=ip+"login-api.php";
                Log.v("detail",url);
                JSONObject json = jsonParser.httpRequest(url, "GET", myparams);
                Log.d("detail", json.toString());
                sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    JSONArray myObj = json.getJSONArray(TAG_record); // JSON Array
                    final JSONObject myJSON = myObj.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_LONG).show();
                                dbIdUser = myJSON.getString("id_user");
                                dbFristName = myJSON.getString("firstname");
                                dbLastName = myJSON.getString("lastname");
                                dbEmail = myJSON.getString("email");
                                dbPassword = myJSON.getString("password");
                                dbAddress= myJSON.getString("password");
                                dbCity = myJSON.getString("city");
                                dbState = myJSON.getString("state");
                                dbContact = myJSON.getString("contactno");
                                dbQualification = myJSON.getString("qualification");
                                dbStream = myJSON.getString("stream");
                                dbPassing = myJSON.getString("passingyear");
                                dbDob = myJSON.getString("dob");
                                dbAge = myJSON.getString("age");
                                dbDesignation = myJSON.getString("designation");
                                dbResume = myJSON.getString("resume");
                                badge = Integer.parseInt(myJSON.getString("badge"));
                            }
                            catch (JSONException e) {e.printStackTrace();}
                        }});
                }else if (sukses== 0 ){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gagal();
                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @SuppressLint("NewApi")
        protected void onPostExecute(String file_url) {
           // Log.v("SUKSES",dbIdUser);
            if(sukses==1){
                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    sharedPrefManager.saveSPBooleanUser(SharedPrefManager.SUDAH_LOGIN_USER, true);
                    editor.putBoolean("user", true);
                    editor.putString("id_user", dbIdUser);
                    editor.putString("firstname", dbFristName);
                    editor.putString("lastname", dbLastName);
                    editor.putString("email", dbEmail);
                    editor.putString("password", dbPassword);
                    editor.putString("address", dbAddress);
                    editor.putString("city", dbCity);
                    editor.putString("state", dbState);
                    editor.putString("contactno", dbContact);
                    editor.putString("qualification", dbQualification);
                    editor.putString("stream", dbStream);
                    editor.putString("passingyear", dbPassing);
                    editor.putString("dob", dbDob);
                    editor.putString("age", dbAge);
                    editor.putString("designation", dbDesignation);
                    editor.putString("resume", dbResume);
                    editor.putString("badge", String.valueOf(badge));

                    editor.apply();
                    Intent i = new Intent(getApplicationContext(),Drawer.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

            }else{

            }

        }
    }

    public void empty(){
        new AlertDialog.Builder(this)
                .setTitle("Maaf...")
                .setMessage("Email atau Password Tidak Boleh Kosong!!!")
                .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {

                    }})
                .show();
    }
    public void gagal(){
        new AlertDialog.Builder(this)
                .setTitle("Gagal Login")
                .setMessage("Email atau Password Tidak Cocok...")
                .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {

                    }})
                .show();
    }

}
