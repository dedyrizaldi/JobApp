package com.example.lokerapp.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lokerapp.R;
import com.example.lokerapp.koneksi.JSONParser;
import com.example.lokerapp.tools.AppController;
import com.example.lokerapp.tools.SharedPrefManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.lokerapp.tools.AppController.TAG;

public class MyResumeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap,decoded;
    Handler mHandler;
    int bitmap_size = 60;
    ImageView imageViewChoose;
    int success;
    Button updateFoto;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "gambar";
    private String KEY_USER = "id_user";
    String tag_json_obj = "json_obj_req";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText edtEducation, edtExp, edtSkill, edtContact;
    TextView tvName, tvLocation;
    JSONParser jsonParser = new JSONParser();
    SharedPrefManager sharedPrefManager;
    String ip;
    String idUser, fullName,fisrtName, lastName, city, contact, passingYear, qualification ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String urlImage;
    String ip2;
    String imgDatabase;
    String imgPath;
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    private static final String TAG_IMG = "gambar";


    public MyResumeFragment() {
        // Required empty public constructor
    }

    public static MyResumeFragment newInstance(String param1, String param2) {
        MyResumeFragment fragment = new MyResumeFragment();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_myresume, container, false);
        ip=jsonParser.uploadImage();
        ip2=jsonParser.getIP();
        imgPath=jsonParser.getImage();
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

        //===================================//
        tvName = (TextView)view.findViewById(R.id.id_tv_name);
        tvLocation = (TextView)view.findViewById(R.id.id_tv_location);
        edtEducation = (EditText)view.findViewById(R.id.id_edt_education);
        edtExp = (EditText)view.findViewById(R.id.id_edt_exp);
        edtSkill = (EditText)view.findViewById(R.id.id_edt_skill);
        edtContact = (EditText)view.findViewById(R.id.id_edt_contac);
        imageViewChoose = (ImageView)view.findViewById(R.id.image_choose);
        updateFoto = (Button)view.findViewById(R.id.update_foto);
        updateFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            uploadImage();
            }
        });

        imageViewChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                    }else{
                        showFileChooser();

                    }
            }
        });
        //==================================//
        tvName.setText(fisrtName+" "+lastName);
        tvLocation.setText(city);
        edtEducation.setText(passingYear);
        edtExp.setText(fullName);
        edtSkill.setText(qualification);
        edtContact.setText(contact);
        new load().execute();
        return view;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MyResumeFragment.this.mHandler.postDelayed(runnable, 5000);
        }
    };

    private void showFileChooser() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 2000));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... params) {
            int sukses;
            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("id_user", idUser));

                String url=ip2+"get-imageprofile.php";
                Log.v("detail",url);
                JSONObject json = jsonParser.httpRequest(url, "GET", params1);
                Log.d("detail", json.toString());
                sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    JSONArray myObj = json.getJSONArray(TAG_record); // JSON Array
                    final JSONObject myJSON = myObj.getJSONObject(0);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {

                                imgDatabase=myJSON.getString(TAG_IMG);

                            }
                            catch (JSONException e) {e.printStackTrace();}
                        }});
                }
                else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {

            urlImage=imgPath+imgDatabase;
            new DownloadImageTask((ImageView) getActivity().findViewById(R.id.image_choose)).execute(urlImage);

        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }
            catch (Exception e) {Log.e("Error", e.getMessage());e.printStackTrace();}
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result); }
    }

    private void uploadImage() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());

                                Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                kosong();

                            } else {
                                Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_IMAGE, getStringImage(decoded));
                params.put(KEY_USER, idUser);

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };
        this.mHandler = new Handler();
        runnable.run();
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }
    private void kosong() {
        imageViewChoose.setImageResource(0);
    }
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageViewChoose.setImageBitmap(decoded);
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

//    class LoadProfileData extends AsyncTask<String, String, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//        protected String doInBackground(String... params) {
//            int sukses;
//            try {
//                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
//                params1.add(new BasicNameValuePair("id_user", idPengguna));
//                String url=ip+"profile-api.php";
//                Log.v("detail",url);
//                JSONObject json = jsonParser.httpRequest(url, "GET", params1);
//                Log.d("detail", json.toString());
//                sukses = json.getInt(TAG_SUKSES);
//                if (sukses == 1) {
//                    JSONArray myObj = json.getJSONArray(TAG_record); // JSON Array
//                    final JSONObject myJSON = myObj.getJSONObject(0);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @SuppressLint("SetTextI18n")
//                        public void run() {
//                            try {
//                                edtNama.setText(myJSON.getString("nama_pengguna"));
//                                edtEmail.setText(myJSON.getString("email_pengguna"));
//                                edtUsername.setText(myJSON.getString("username"));
//                                edtPassword.setText(myJSON.getString("password"));
//                                String strTmp = myJSON.getString("status");
//                                if (strTmp.equals("0")){
//                                    edtStatus.setText("admin");
//                                }else {
//                                    edtStatus.setText("user");
//                                }
//                            }
//                            catch (JSONException e) {e.printStackTrace();}
//                        }});
//                }
//                else{
//                    // jika id tidak ditemukan
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        protected void onPostExecute(String file_url) {}
//    }

}
