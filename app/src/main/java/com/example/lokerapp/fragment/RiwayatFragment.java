package com.example.lokerapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lokerapp.DetailHomeActivity;
import com.example.lokerapp.R;
import com.example.lokerapp.koneksi.JSONParser;
import com.example.lokerapp.tools.SharedPrefManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiwayatFragment extends Fragment {



    JSONParser jsonParser = new JSONParser();
    String ip;
    JSONArray myJSON = null;
    ArrayList<HashMap<String, String>> arrayList;
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    ListView listView;
    String idPengguna;

    private static final String TAG_IDUSER = "id_user";
    private static final String TAG_IDJOB = "id_jobpost";
    private static final String TAG_IDCOMPANY = "id_company";
    private static final String TAG_JOBTITLE = "jobtitle";
    private static final String TAG_DESC = "description";
    private static final String TAG_MAXSAL = "maximumsalary";
    private static final String TAG_MINSAL = "minimumsalary";
    private static final String TAG_EXP = "experience";
    private static final String TAG_QUALIFICATION = "qualification";
    private static final String TAG_DATE = "createdat";
    private static final String TAG_COMPANYNAME = "createdat";
    private static final String TAG_STATE = "state";
    private static final String TAG_STATUSDOWNLAOD = "status_download";
    String idUser, fullName,fisrtName, lastName, city, contact, passingYear, qualification ;
    TextView tvJobTitle, tvCompanyName, tvMinSalary, tvMaxSalary, keyIdJob, keyIdPost, keyIdUser;
    SwipeRefreshLayout sw;
    SharedPrefManager sharedPrefManager;

    public RiwayatFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_riwayat, container, false);
        ip=jsonParser.getIP();
        sharedPrefManager = new SharedPrefManager(getActivity());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //Boolean RegisteredAdmin = sharedPref.getBoolean("admin", false);
        Boolean RegisteredUser = sharedPref.getBoolean("user", false);
        if (!RegisteredUser){
            getActivity().finish();
        }else {
            idUser = sharedPref.getString("id_user", "");
            fisrtName = sharedPref.getString("firstname", "");
            lastName = sharedPref.getString("lastname", "");
            city  = sharedPref.getString("city", "");
            contact  = sharedPref.getString("contactno", "");
            passingYear  = sharedPref.getString("passingyear", "");
            qualification  = sharedPref.getString("qualification", "");
            fullName  = sharedPref.getString("designation", "");
        }
        tvJobTitle = (TextView)rootView.findViewById(R.id.tv_jobtitile);
        tvCompanyName = (TextView)rootView.findViewById(R.id.tv_companyname);
        tvMinSalary = (TextView)rootView.findViewById(R.id.tv_minsal);
        keyIdJob = (TextView)rootView.findViewById(R.id.key_idjob);
        keyIdPost = (TextView)rootView.findViewById(R.id.key_idcompany);
        keyIdUser = (TextView)rootView.findViewById(R.id.key_iduser);
        arrayList = new ArrayList<HashMap<String, String>>();
        listView = (ListView)rootView.findViewById(R.id.lv_riwayat);
        //keyIdUser.setText(idUser);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String pk = ((TextView) view.findViewById(R.id.key_idjob)).getText().toString();
//                String x = ((TextView) view.findViewById(R.id.key_idcompany)).getText().toString();
//                String namaPekerjaan = ((TextView) view.findViewById(R.id.tv_jobtitile)).getText().toString();
//                String namaPerusahaan = ((TextView) view.findViewById(R.id.tv_companyname)).getText().toString();
//                String gaji = ((TextView) view.findViewById(R.id.tv_minsal)).getText().toString();
//                String lokasi = ((TextView) view.findViewById(R.id.tv_state)).getText().toString();
//                String qualificaiton = ((TextView) view.findViewById(R.id.key_qualification)).getText().toString();
//                String desc = ((TextView) view.findViewById(R.id.key_desc)).getText().toString();
//                Intent intent = new Intent(getActivity(), DetailHomeActivity.class);
//                intent.putExtra("pk", pk);
//                intent.putExtra("x", x);
//                intent.putExtra("namapekerjaan", namaPekerjaan);
//                intent.putExtra("namaperusahaan", namaPerusahaan);
//                intent.putExtra("gaji", gaji);
//                intent.putExtra("lokasi", lokasi);
//                intent.putExtra("qualification", qualificaiton);
//                intent.putExtra("desc", desc);
//                startActivityForResult(intent, 100);
//            }
//        });
        sw = (SwipeRefreshLayout)rootView.findViewById(R.id.id_sw);
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sw.setRefreshing(true);
                arrayList.clear();
                new load().execute();
            }
        });

        new load().execute();
        return rootView;
    }

    class load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_user", idUser));
            Log.d("show: ", params.toString());
            JSONObject json = jsonParser.httpRequest(ip+"riwayat-job-api.php", "POST", params);
            Log.d("show: ", json.toString());
            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    myJSON = json.getJSONArray(TAG_record);
                    for (int i = 0; i < myJSON.length(); i++) {
                        JSONObject c = myJSON.getJSONObject(i);

                        String jobTitle = c.getString(TAG_JOBTITLE);
                        String minSal = c.getString(TAG_MINSAL);
                        String maxSal = c.getString(TAG_MAXSAL);
                        String companyName = c.getString(TAG_COMPANYNAME);
                        String qualification = c.getString(TAG_QUALIFICATION);
                        String state = c.getString(TAG_STATE);
                        String statusDownload = c.getString("status_download");


                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TAG_JOBTITLE,jobTitle);
                        map.put(TAG_MINSAL,toRupiah(minSal)+" - "+toRupiah(maxSal));
                        map.put(TAG_COMPANYNAME,companyName);
                        map.put(TAG_QUALIFICATION,qualification);
                        map.put(TAG_STATE,state);
                        if (statusDownload.equals("1")){
                            map.put(TAG_STATUSDOWNLAOD,"DILIHAT");
                        }else{
                            map.put(TAG_STATUSDOWNLAOD,"BELUM DILIHAT");
                        }

                        arrayList.add(map);
                    }
                }
                else {


                }
            }
            catch (JSONException e) {e.printStackTrace();}
            return null;
        }
        protected void onPostExecute(String file_url) {

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    final ListAdapter adapter = new SimpleAdapter(
                            getActivity(), arrayList,
                            R.layout.riwayat_view_apply, new String[] {TAG_IDUSER,TAG_IDJOB,TAG_IDCOMPANY,TAG_JOBTITLE,TAG_MINSAL,TAG_COMPANYNAME,TAG_STATE, TAG_QUALIFICATION, TAG_DESC, TAG_STATUSDOWNLAOD  },
                            new int[] {R.id.key_iduser,R.id.key_idjob, R.id.key_idcompany, R.id.tv_jobtitile, R.id.tv_minsal, R.id.tv_companyname, R.id.tv_state, R.id.key_qualification, R.id.key_desc, R.id.tv_status_riwayat});
                    listView.setAdapter(adapter);
                    sw.setRefreshing(false);
                }
            });

        }

    }

    private String toRupiah(String nominal){
        String hasil = "";
        DecimalFormat toRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatAngka = new DecimalFormatSymbols();
        formatAngka.setCurrencySymbol("Rp. ");
        formatAngka.setMonetaryDecimalSeparator(',');
        toRupiah.setDecimalFormatSymbols(formatAngka);
        hasil = toRupiah.format(Double.valueOf(nominal));
        return hasil;
    }




}
