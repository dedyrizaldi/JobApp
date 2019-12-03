package com.example.lokerapp.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lokerapp.R;
import com.example.lokerapp.koneksi.JSONParser;

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

public class SearchListViewFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SeekBar seekBar;
    TextView textView, textView1;
    EditText search;
    String getNumber, getData;
    Button btnSearch;
    JSONParser jsonParser = new JSONParser();
    String ip;
    JSONArray myJSON = null;
    ArrayList<HashMap<String, String>> arrayList;
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    ListView listView;

    private static final String TAG_IDJOB = "id_jobpost";
    private static final String TAG_IDCOMPANY = "id_company";
    private static final String TAG_JOBTITLE = "jobtitle";
    private static final String TAG_DESC = "description";
    private static final String TAG_MAXSAL = "maximumsalary";
    private static final String TAG_MINSAL = "minimumsalary";
    String data,salary;


    public SearchListViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_listview, container, false);
        ip=jsonParser.getIP();
        arrayList = new ArrayList<HashMap<String, String>>();
        listView = (ListView)view.findViewById(R.id.lv_search);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            data = bundle.getString("key");
            salary = bundle.getString("salary");
            if (data.equals("0000000")&&salary.equals("0000000")){
                data="";
                salary="";
            }

        }
        new load().execute();
        return view;
    }




    class load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("key", data));
            params.add(new BasicNameValuePair("sal", salary));
            //params.add(new BasicNameValuePair("key", salary));
            Log.d("show: ", params.toString());
            JSONObject json = jsonParser.httpRequest(ip+"search-job-api.php", "GET", params);
            Log.d("show: ", json.toString());
            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    myJSON = json.getJSONArray(TAG_record);
                    for (int i = 0; i < myJSON.length(); i++) {
                        JSONObject c = myJSON.getJSONObject(i);

                        String idJobPost = c.getString(TAG_IDJOB);
                        String idCompnay = c.getString(TAG_IDCOMPANY);
                        String jobTitle = c.getString(TAG_JOBTITLE);
                        String minSal = c.getString(TAG_MINSAL);
                        String maxSal = c.getString(TAG_MAXSAL);
                        if (maxSal.equals("")||minSal.equals("")){
                            maxSal="0";
                            minSal="0";
                        }


                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TAG_IDJOB,idJobPost);
                        map.put(TAG_IDCOMPANY,idCompnay);
                        map.put(TAG_JOBTITLE,jobTitle);
                        map.put(TAG_MINSAL,toRupiah(minSal)+" - "+toRupiah(maxSal));

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
                            R.layout.desain_view, new String[] {TAG_IDJOB,TAG_IDCOMPANY,TAG_JOBTITLE,TAG_MINSAL},
                            new int[] {R.id.key_idjob, R.id.key_idcompany, R.id.tv_jobtitile, R.id.tv_minsal});
                    listView.setAdapter(adapter);

                }
            });

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





}
