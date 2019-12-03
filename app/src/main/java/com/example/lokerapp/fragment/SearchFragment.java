package com.example.lokerapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class SearchFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
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
    String getNumber, getData, getSalary;
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


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ip=jsonParser.getIP();


        arrayList = new ArrayList<HashMap<String, String>>();
        listView = (ListView)view.findViewById(R.id.lv_search);

        textView =(TextView)view.findViewById(R.id.id_tv_salary);
        textView1 = (TextView)view.findViewById(R.id.jusnumber);
        btnSearch = (Button)view.findViewById(R.id.id_btn_search);
        search = (EditText)view .findViewById(R.id.id_search_job);

        seekBar = (SeekBar)view.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(40);

        btnSearch.setOnClickListener((View.OnClickListener) this);

        return view;
    }

    void getData(){

        getData = search.getText().toString().trim();
        getSalary = textView1.getText().toString().trim();
        if (getData.equals("")){
            getData = textView1.getText().toString().trim();
        }




    }




    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        textView.setText("Gaji Perbulan : Rp."+String.valueOf(i)+".000.000");
        getNumber = textView.getText().toString().replaceAll("[^-?0-9]+", "");
        textView1.setText(getNumber);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        getData();
        Fragment fragment ;
        switch (view.getId()) {
            case R.id.id_btn_search:
                fragment = new SearchListViewFragment();
                rep(fragment);
                Bundle bundle = new Bundle();
                bundle.putString("key",getData);
                bundle.putString("salary",getSalary);
                fragment.setArguments(bundle);
                break;

        }
    }

    public void rep(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }



    class load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("key", getData));
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
