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

public class DetailSaveJobActivity extends AppCompatActivity {

    String ip;
    JSONParser jsonParser = new JSONParser();
    String idCompany, idSaved, idJob;
    SharedPrefManager sharedPrefManager;
    String idUser;
    Button btnDelete, btnApplySaveJob;

    String namaPekerjaan, namaPerusahaan, gaji, lokasi;
    TextView tvNamaPekerjaan, tvLokasi, tvGaji, tvNamaPerusahaan;
    private static String TAG_SUKSES="sukses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_save_job);
        ip=jsonParser.getIP();
        sharedPrefManager = new SharedPrefManager(DetailSaveJobActivity.this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(DetailSaveJobActivity.this);
        //Boolean RegisteredAdmin = sharedPref.getBoolean("admin", false);
        Boolean RegisteredUser = sharedPref.getBoolean("user", false);
        if (!RegisteredUser){
           finish();
        }else {
            idUser = sharedPref.getString("id_user", "");
        }

        getDataFromIntent();
        setButton();
        setTexView();
        setText();

    }

    private void setText(){
        tvNamaPerusahaan.setText(namaPerusahaan);
        tvNamaPekerjaan.setText(namaPekerjaan);
        tvGaji.setText(gaji);
        tvLokasi.setText(lokasi);
    }

    private void setTexView(){
        tvNamaPekerjaan = (TextView)findViewById(R.id.id_nama_pekerjaan);
        tvNamaPerusahaan = (TextView)findViewById(R.id.id_nama_perusahaan);
        tvGaji = (TextView)findViewById(R.id.id_gaji);
        tvLokasi = (TextView)findViewById(R.id.id_lokasi);
    }

    private void setButton(){

        btnDelete = (Button)findViewById(R.id.btn_delete_savejob);
        btnApplySaveJob = (Button) findViewById(R.id.btn_apply_savejob);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new deletejob().execute();
            }
        });

        btnApplySaveJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ApplyJob().execute();
            }
        });

    }

    private void getDataFromIntent(){

        Intent i = getIntent();
        idSaved = i.getStringExtra("idsaved");
        idJob = i.getStringExtra("idjob");
        idCompany = i.getStringExtra("idcompnay");
        namaPerusahaan = i.getStringExtra("namaperusahaan");
        namaPekerjaan = i.getStringExtra("namapekerjaan");
        gaji = i.getStringExtra("gaji");
        lokasi = i.getStringExtra("lokasi");
    }

    class ApplyJob extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_saved", idSaved));
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
                            Toast.makeText(DetailSaveJobActivity.this, "Lamaran Pekerjaan Berhasil", Toast.LENGTH_LONG).show();
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

    class deletejob extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_saved", idSaved));
            params.add(new BasicNameValuePair("id_user", idUser));
            String url=ip+"delete-savejob-api.php";
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
                            Toast.makeText(DetailSaveJobActivity.this, "Data Berhasil diHapus...", Toast.LENGTH_LONG).show();
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
