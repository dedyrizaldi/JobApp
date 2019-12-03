package com.example.lokerapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lokerapp.R;
import com.example.lokerapp.fragment.HomeFragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class AdapterListHome extends BaseAdapter {

    private Activity activity;

    public AdapterListHome(Activity act) {
        this.activity = act;
    }

    public int getCount() {
        return HomeFragment.arrIdJob.size();

    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.desain_view, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvJobTitle = (TextView) convertView.findViewById(R.id.tv_jobtitile);
        holder.tvCompanyName = (TextView) convertView.findViewById(R.id.tv_companyname);
        holder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
        holder.tvSalary = (TextView) convertView.findViewById(R.id.tv_minsal);

        //for key
        holder.keyIdUser = (TextView) convertView.findViewById(R.id.key_iduser);
        holder.keyIdJob = (TextView) convertView.findViewById(R.id.key_idjob);
        holder.keyIdCompany = (TextView) convertView.findViewById(R.id.key_idcompany);
        holder.keyQualification = (TextView) convertView.findViewById(R.id.key_qualification);
        holder.keyDesc = (TextView) convertView.findViewById(R.id.key_desc);

        //set
        holder.tvJobTitle.setText(HomeFragment.arrJobTitle.get(position));
        holder.tvCompanyName.setText(HomeFragment.arrCompanyName.get(position));
        holder.tvState.setText(HomeFragment.arrState.get(position));
        holder.tvSalary.setText(toRupiah(HomeFragment.arrMinSal.get(position))+" - "+toRupiah(HomeFragment.arrMaxSal.get(position)));
        //set for key
//
        holder.keyIdJob.setText(HomeFragment.arrIdJobStr.get(position));
        holder.keyIdCompany.setText(HomeFragment.arrIdCompany.get(position));
        holder.keyQualification.setText(HomeFragment.arrQualification.get(position));
        holder.keyDesc.setText(HomeFragment.arrDesc.get(position));


        //   Picasso.get().load(Config.PATH_URL_IMG_CAT+ ListMenuActivity.IMAGE.get(position)).into(holder.imgThumb);

        return convertView;
    }


    static class ViewHolder {
        TextView tvJobTitle, tvCompanyName, tvState, tvSalary;
        TextView keyIdUser, keyIdJob, keyIdCompany, keyQualification, keyDesc;
        String data;
        //ImageView imgThumb;
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
