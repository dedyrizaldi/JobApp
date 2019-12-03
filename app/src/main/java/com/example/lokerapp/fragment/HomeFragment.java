package com.example.lokerapp.fragment;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lokerapp.Adapter.AdapterListHome;
import com.example.lokerapp.DetailHomeActivity;
import com.example.lokerapp.Drawer;
import com.example.lokerapp.LoginActivity;
import com.example.lokerapp.R;
import com.example.lokerapp.koneksi.JSONParser;
import com.example.lokerapp.tools.DrawerBadge;
import com.example.lokerapp.tools.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

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

public class HomeFragment extends Fragment {



    JSONParser jsonParser = new JSONParser();
    String ip;
    int color;
    String listPosition;
    private static int save = -1;

    JSONArray myJSON = null;
    ArrayList<HashMap<String, String>> arrayList;
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    ListView listView;
    String idPengguna;
    String getBadge;
    int array_id;
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
    private static final String TAG_COMPANYNAME = "companyname";
    private static final String TAG_STATE = "state";
    String idUser, fullName,fisrtName, lastName, city, contact, passingYear, qualification ;
    TextView tvJobTitle, tvCompanyName, tvMinSalary, tvMaxSalary, keyIdJob, keyIdPost, keyIdUser;
    SwipeRefreshLayout sw;
    SharedPrefManager sharedPrefManager;
    View rootView;
    NavigationView navigationView;
    TextView gallery;
    String pk, x;
    String idColor;
    int posit=0;
    int[]arrayPos;
    ProgressBar prgLoading;
    AdapterListHome adapterListHome;

    public static ArrayList<String> arrIdCompany = new ArrayList<String>();
    public static ArrayList<Long> arrIdJob = new ArrayList<Long>();
    public static ArrayList<String> arrIdJobStr = new ArrayList<String>();
    public static ArrayList<String> arrIdUser = new ArrayList<String>();
    public static ArrayList<String> arrJobTitle = new ArrayList<String>();
    public static ArrayList<String> arrDesc = new ArrayList<String>();
    public static ArrayList<String> arrMaxSal = new ArrayList<String>();
    public static ArrayList<String> arrMinSal = new ArrayList<String>();
    public static ArrayList<String> arrCompanyName = new ArrayList<String>();
    public static ArrayList<String> arrState = new ArrayList<String>();
    public static ArrayList<String> arrQualification = new ArrayList<String>();
    SwipeRefreshLayout swipeRefreshLayout = null;
    int IOConnect = 0;
    TextView txtAlert;
    FrameLayout frameLayout;
    public HomeFragment() { }
    int array_place[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
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
            //tmpbadge  = sharedPref.getString("badge", "");
        }
        frameLayout = (FrameLayout)rootView.findViewById(R.id.frame);
        adapterListHome = new AdapterListHome(getActivity());
        tvJobTitle = (TextView)rootView.findViewById(R.id.tv_jobtitile);
        tvCompanyName = (TextView)rootView.findViewById(R.id.tv_companyname);
        tvMinSalary = (TextView)rootView.findViewById(R.id.tv_minsal);
        keyIdJob = (TextView)rootView.findViewById(R.id.key_idjob);
        keyIdPost = (TextView)rootView.findViewById(R.id.key_idcompany);
        keyIdUser = (TextView)rootView.findViewById(R.id.key_iduser);
        txtAlert = (TextView)rootView.findViewById(R.id.txtAlert);
        prgLoading = (ProgressBar) rootView.findViewById(R.id.prgLoading);
        sw = (SwipeRefreshLayout)rootView.findViewById(R.id.id_sw);


       // arrayList = new ArrayList<HashMap<String, String>>();
        listView = (ListView)rootView.findViewById(R.id.lv_home);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pk = ((TextView) view.findViewById(R.id.key_idjob)).getText().toString();
                x = ((TextView) view.findViewById(R.id.key_idcompany)).getText().toString();
                String namaPekerjaan = ((TextView) view.findViewById(R.id.tv_jobtitile)).getText().toString();
                String namaPerusahaan = ((TextView) view.findViewById(R.id.tv_companyname)).getText().toString();
                String gaji = ((TextView) view.findViewById(R.id.tv_minsal)).getText().toString();
                String lokasi = ((TextView) view.findViewById(R.id.tv_state)).getText().toString();
                String qualificaiton = ((TextView) view.findViewById(R.id.key_qualification)).getText().toString();
                String desc = ((TextView) view.findViewById(R.id.key_desc)).getText().toString();
                Intent intent = new Intent(getActivity(), DetailHomeActivity.class);
                intent.putExtra("pk", pk);
                intent.putExtra("x", x);
                intent.putExtra("namapekerjaan", namaPekerjaan);
                intent.putExtra("namaperusahaan", namaPerusahaan);
                intent.putExtra("gaji", gaji);
                intent.putExtra("lokasi", lokasi);
                intent.putExtra("qualification", qualificaiton);
                intent.putExtra("desc", desc);
                startActivityForResult(intent, 100);
                //new save().execute();
               // Toast.makeText(getActivity(), "Your ID "+i, Toast.LENGTH_LONG).show();
                Log.d("Clicked : ",String.valueOf(i));
                new color().execute();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id_jobpostsee", pk));
                params.add(new BasicNameValuePair("id_companysee", x));
                params.add(new BasicNameValuePair("id_user", idUser));
                params.add(new BasicNameValuePair("id_array", String.valueOf(i).trim()));
                String url=ip+"jobs-see.php";
                Log.v("add",url);
                JSONObject json = jsonParser.httpRequest(url,"POST", params);
                Log.d("add", json.toString());

                try {
                    int sukses = json.getInt(TAG_SUKSES);
                    if (sukses == 1) {
                        Intent is = getActivity().getIntent();
                        getActivity().setResult(100, is);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listView != null && listView.getChildCount() > 1) {
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                sw.setEnabled(enable);
            }
        });
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sw.setRefreshing(false);
                IOConnect = 0;
                listView.invalidateViews();
                clearData();
                new load().execute();
                new color().execute();
                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPrefManager.saveSPBooleanUser(SharedPrefManager.SUDAH_LOGIN_USER, true);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("user", true);
                editor.putString("badge", String.valueOf(getBadge));
                editor.apply();
                editor.commit();

            }
        });
        clearData();
        new load().execute();
        new color().execute();
        adapterListHome.notifyDataSetChanged();
        return rootView;
    }





    class color extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_user", idUser));
            Log.d("show: ", params.toString());
            JSONObject json = jsonParser.httpRequest(ip+"color-status.php", "GET", params);
            Log.d("show: ", json.toString());
            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    myJSON = json.getJSONArray(TAG_record);
                    for (int i = 0; i < myJSON.length(); i++) {
                        JSONObject c = myJSON.getJSONObject(i);
                         final int position_array = c.getInt("id_array");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ListView lv = (ListView) rootView.findViewById(R.id.lv_home);
                                    lv.getChildAt(position_array-1).setBackgroundColor(Color.GRAY);
                                    adapterListHome.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }


                            }
                        });




                    }
                    //posit=14;
                  //  arrayPos=new int[posit];
                    //for (int i = 0; i < posit; i++) {
                        //JSONObject c = myJSON.getJSONObject(i);
                      //  final int no = c.getInt("id_array");
                      //  arrayPos[i]=no;
                        //Log.v("Baca","ADA# "+no);
                   // }
                }

            }
            catch (JSONException e) {e.printStackTrace();}
            return null;
        }
        protected void onPostExecute(String file_url) {


        }

    }

    class load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            Log.d("show: ", params.toString());
            JSONObject json = jsonParser.httpRequest(ip+"list-home-api.php", "GET", params);
            Log.d("show: ", json.toString());
            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    myJSON = json.getJSONArray(TAG_record);
                    for (int i = 0; i < myJSON.length(); i++) {
                        JSONObject c = myJSON.getJSONObject(i);

                        arrIdJob.add(Long.parseLong(c.getString("id_jobpost")));
                        arrIdJobStr.add(c.getString("id_jobpost"));
                        arrIdCompany.add(c.getString("id_company"));
                        arrJobTitle.add(c.getString("jobtitle"));
                        arrCompanyName.add(c.getString("companyname"));
                        arrDesc.add(c.getString("description"));
                        arrMinSal.add(c.getString("minimumsalary"));
                        arrMaxSal.add(c.getString("maximumsalary"));
                        arrQualification.add(c.getString("qualification"));
                        arrState.add(c.getString("state"));

                    }
                }
                else {


                }
            }
            catch (JSONException e) {e.printStackTrace();}
            return null;
        }
        protected void onPostExecute(String file_url) {

            prgLoading.setVisibility(View.GONE);

            // if internet connection and data available show data on list
            // otherwise, show alert text
            if((arrIdCompany.size() > 0) && (IOConnect == 0)){
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(adapterListHome);
                adapterListHome.notifyDataSetChanged();
            }else{
                txtAlert.setVisibility(View.VISIBLE);
            }

        }

    }

    public void clearData(){
        arrIdJob.clear();
        arrIdJobStr.clear();
        arrIdCompany.clear();
        arrJobTitle.clear();
        arrCompanyName.clear();
        arrDesc.clear();
        arrMinSal.clear();
        arrMaxSal.clear();
        arrQualification.clear();
        arrState.clear();


    }

    class save extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_jobpostsee", pk));
            params.add(new BasicNameValuePair("id_companysee", x));
            params.add(new BasicNameValuePair("id_user", idUser));
            params.add(new BasicNameValuePair("id_array", String.valueOf(array_id)));
            String url=ip+"jobs-see.php";
            Log.v("add",url);
            JSONObject json = jsonParser.httpRequest(url,"POST", params);
            Log.d("add", json.toString());

            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    Intent i = getActivity().getIntent();
                    getActivity().setResult(100, i);

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
