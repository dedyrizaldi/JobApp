package com.example.lokerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lokerapp.koneksi.JSONParser;
import com.example.lokerapp.tools.SharedPrefManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailHomeActivity extends AppCompatActivity {

    private static final String TAG_SUKSES = "sukses";
    String idUser, idCompany, idJob, namaPekerjaan, namaPerusahaan, gaji, lokasi, qualification, desc;
    String ip;
    JSONParser jsonParser = new JSONParser();
    SharedPrefManager sharedPrefManager;
    TextView tvNamaPerusahaan,tvNamaPekerjaan, tvLokasiKerja, tvGaji, tvIdPekerjaan, tvIdPerusahaan, tvQualification, tvDesc;
    Button btnSave, btnApply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_home);
        ip=jsonParser.getIP();
        sharedPrefManager = new SharedPrefManager(this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //Boolean RegisteredAdmin = sharedPref.getBoolean("admin", false);
        Boolean RegisteredUser = sharedPref.getBoolean("user", false);
        if (!RegisteredUser){
         finish();
        }else {
            idUser = sharedPref.getString("id_user", "");

        }



        Intent i = getIntent();
        idCompany = i.getStringExtra("x");
        idJob = i.getStringExtra("pk");
        namaPekerjaan = i.getStringExtra("namapekerjaan");
        namaPerusahaan = i.getStringExtra("namaperusahaan");
        gaji = i.getStringExtra("gaji");
        lokasi = i.getStringExtra("lokasi");
        qualification = i.getStringExtra("qualification");
        desc = i.getStringExtra("desc");


        btnApply = (Button)findViewById(R.id.id_apply_job);
        btnSave = (Button)findViewById(R.id.id_save_job);
        tvIdPekerjaan = (TextView)findViewById(R.id.id_pekerjaan);
        tvIdPerusahaan = (TextView)findViewById(R.id.id_perusahaan);
        tvQualification = (TextView)findViewById(R.id.id_qualification);
        tvDesc = (TextView)findViewById(R.id.id_desc);

        tvNamaPerusahaan = (TextView)findViewById(R.id.id_nama_perusahaan);
        tvNamaPekerjaan = (TextView)findViewById(R.id.id_nama_pekerjaan);
        tvLokasiKerja = (TextView)findViewById(R.id.id_lokasi);
        tvGaji = (TextView)findViewById(R.id.id_gaji);

        tvIdPekerjaan.setText(idJob);
        tvIdPerusahaan.setText(idCompany);
        //=================================//
        tvNamaPerusahaan.setText(namaPerusahaan);
        tvNamaPekerjaan.setText(namaPekerjaan);
        tvGaji.setText(gaji);
        tvLokasiKerja.setText(lokasi);
        tvQualification.setText(qualification);
        tvDesc.setText(desc);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ApplyJob().execute();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SavedJob().execute();
            }
        });


    }

    class SavedJob extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_jobpost", idJob));
            params.add(new BasicNameValuePair("id_company", idCompany));
            params.add(new BasicNameValuePair("id_user", idUser));
            String url=ip+"saved-job-api.php";
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
                            Toast.makeText(DetailHomeActivity.this, "Data Berhasil Disimpan...", Toast.LENGTH_LONG).show();
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


    class ApplyJob extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_jobpost", idJob));
            params.add(new BasicNameValuePair("id_company", idCompany));
            params.add(new BasicNameValuePair("id_user", idUser));
            String url=ip+"apply-job-api.php";
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
                            Toast.makeText(DetailHomeActivity.this, "Lamaran Pekerjaan Berhasil", Toast.LENGTH_LONG).show();
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
