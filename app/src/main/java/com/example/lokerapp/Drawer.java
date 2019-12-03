package com.example.lokerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.lokerapp.fragment.HomeFragment;
import com.example.lokerapp.fragment.JobsAlreadySeenFragment;
import com.example.lokerapp.fragment.MyResumeFragment;
import com.example.lokerapp.fragment.RiwayatFragment;
import com.example.lokerapp.fragment.SavedJobFragment;
import com.example.lokerapp.fragment.SearchFragment;
import com.example.lokerapp.koneksi.JSONParser;
import com.example.lokerapp.tools.DrawerBadge;
import com.example.lokerapp.tools.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String idUser, fullName,fisrtName, lastName, city, contact, passingYear, qualification, email ;
    SharedPrefManager sharedPrefManager;
    ImageView image;
    TextView slideshow,gallery;
    View headerView;
    String ip;
    String imgPath;
    String urlImage;
    String imgDatabase;
    String getBadge;
    TextView messages;
    String color;
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    private static final String TAG_IMG = "gambar";
    JSONParser jsonParser = new JSONParser();
    String dbBadge;
    Handler handler;
    DrawerBadge badge;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ip = jsonParser.getIP();
        imgPath = jsonParser.getImage();
        sf();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navigation_header_name);
        TextView navEmail = (TextView) headerView.findViewById(R.id.nav_header_email);
        gallery=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_home));
        navUsername.setText(fisrtName+" "+lastName);
        navEmail.setText(email);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_home);
        initializeCountDrawer();
        //setNotifBadge();
        new load().execute();
        this.handler = new Handler();
        runnable.run();
    }

    private void sf(){

        sharedPrefManager = new SharedPrefManager(this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //Boolean RegisteredAdmin = sharedPref.getBoolean("admin", false);
        Boolean RegisteredUser = sharedPref.getBoolean("user", false);
        if (!RegisteredUser){
            finish();
        }else {
            idUser = sharedPref.getString("id_user", "");
            fisrtName = sharedPref.getString("firstname", "");
            lastName = sharedPref.getString("lastname", "");
            city  = sharedPref.getString("city", "");
            contact  = sharedPref.getString("contactno", "");
            passingYear  = sharedPref.getString("passingyear", "");
            qualification  = sharedPref.getString("qualification", "");
            fullName  = sharedPref.getString("designation", "");
            email  = sharedPref.getString("email", "");
            dbBadge  = sharedPref.getString("badge", "");
            color  = sharedPref.getString("color", "");
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sf();
            initializeCountDrawer();
            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Drawer.this);
            sharedPrefManager.saveSPBooleanUser(SharedPrefManager.SUDAH_LOGIN_USER, true);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("user", true);
            editor.putString("badge", String.valueOf(0));
            editor.apply();
            editor.commit();
            Drawer.this.handler.postDelayed(runnable,5000);

        }
    };

    private void initializeCountDrawer(){
        //Gravity property aligns the text
        gallery.setGravity(Gravity.CENTER_VERTICAL);
        gallery.setTypeface(null, Typeface.BOLD);
        gallery.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        gallery.setText(dbBadge);

    }

    private void setNotifBadge(){



//         badge = new DrawerBadge(Drawer.this, (NavigationView) findViewById(R.id.nav_view),
//                R.id.nav_home,
//                String.valueOf(Integer.parseInt(dbBadge) > 99 ? "+" + 99 : Integer.parseInt(dbBadge)),
//                "#FFFFFF","#FF0000","#FF0000");
//        badge.setSolidColor("#FF0000");
//



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

                String url=ip+"get-imageprofile.php";
                Log.v("detail",url);
                JSONObject json = jsonParser.httpRequest(url, "GET", params1);
                Log.d("detail", json.toString());
                sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    JSONArray myObj = json.getJSONArray(TAG_record); // JSON Array
                    final JSONObject myJSON = myObj.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {

                                imgDatabase=myJSON.getString(TAG_IMG);
                                getBadge=myJSON.getString("badge");

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
            new Drawer.DownloadImageTask((ImageView) headerView.findViewById(R.id.nv_header_image)).execute(urlImage);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;
        switch (itemId){
            case  R.id.nav_home:
                fragment = new HomeFragment();
                  break;
            case R.id.nav_seen:
                fragment = new JobsAlreadySeenFragment();
                break;
            case R.id.nav_search:
                fragment = new SearchFragment();
                break;
            case R.id.nav_person:
                fragment = new MyResumeFragment();
                break;
            case R.id.nav_riwayat:
                fragment = new RiwayatFragment();
                break;
            case R.id.nav_saved:
                fragment = new SavedJobFragment();
                break;
            case R.id.nav_logout:
                logout();
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    void logout(){
        sharedPrefManager.saveSPBooleanUser(SharedPrefManager.SUDAH_LOGIN_USER, false);
        sharedPrefManager.saveSPBooleanUser(SharedPrefManager.SUDAH_LOGIN_ADMIN, false);
        Intent intent = new Intent(Drawer.this, LoginActivity.class);

        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //int id = item.getItemId();
        displaySelectedScreen(item.getItemId());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Keluar Aplikasi")
                    .setMessage("Yakin Ingin Keluar Aplikasi!!")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Stop the activity
                            finish();
                        }

                    })
                    .setNegativeButton("Tidak", null)
                    .show();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
