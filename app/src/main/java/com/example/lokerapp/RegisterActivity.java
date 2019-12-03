package com.example.lokerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lokerapp.koneksi.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG_SUKSES = "sukses";
    Button btnSubmit;
    EditText edtFirstName, edtLastName, edtEmail, edtPassword;
    JSONParser jsonParser = new JSONParser();
    String ip;
    String firstname, lastname, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ip=jsonParser.getIP();


        btnSubmit = (Button)findViewById(R.id.btn_submit);
        edtFirstName = (EditText)findViewById(R.id.edt_firstname);
        edtLastName = (EditText)findViewById(R.id.edt_lastname);
        edtEmail = (EditText)findViewById(R.id.edt_email);
        edtPassword = (EditText)findViewById(R.id.edt_password);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                new Register().execute();
            }
        });

    }

    private void getData(){

        firstname = edtFirstName.getText().toString();
        lastname = edtLastName.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

    }

    class Register extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("firstname", firstname));
            params.add(new BasicNameValuePair("lastname", lastname));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password ));
            String url=ip+"daftar-api.php";
            Log.v("add",url);
            JSONObject json = jsonParser.httpRequest(url,"POST", params);
            Log.d("add", json.toString());

            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    Intent i = getIntent();
                    setResult(100, i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_LONG).show();
                        }
                    });
                    finish();
                } else {
                    // gagal update data
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }
        protected void onPostExecute(String file_url) {


        }
    }
}
