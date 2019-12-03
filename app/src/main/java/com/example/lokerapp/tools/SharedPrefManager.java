package com.example.lokerapp.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dedy Rizaldi on 8/20/2019.
 */

public class SharedPrefManager {
    public static final String MYBANK = "lokerapp";

    public static final String NAMA = "nama_pengguna";
    public static final String EMAIL = "email_pengguna";

    public static final String SUDAH_LOGIN_ADMIN = "sudah_admin";
    public static final String SUDAH_LOGIN_USER = "user";
    public static final String STATUS = "status";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context) {
        sp = context.getSharedPreferences(MYBANK, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }


    public void saveSPString(String keySP, String value) {
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value) {
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBooleanAdmin(String keySP, boolean value) {
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public void saveSPBooleanUser   (String keySP, boolean value) {
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama() {
        return sp.getString(NAMA, "");
    }

    public String getSPEmail() {
        return sp.getString(EMAIL, "");
    }

    public String getSPStatus() {
        return sp.getString(EMAIL, "");
    }

    public Boolean getSPSudahLoginAdmin() {
        return sp.getBoolean(SUDAH_LOGIN_ADMIN, false);
    }

    public Boolean getSPSudahLoginUser() {
        return sp.getBoolean(SUDAH_LOGIN_USER, false);
    }

}