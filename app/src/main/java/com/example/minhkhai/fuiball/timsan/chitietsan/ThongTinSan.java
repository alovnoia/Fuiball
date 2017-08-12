package com.example.minhkhai.fuiball.timsan.chitietsan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minhkhai.fuiball.R;
import com.example.minhkhai.fuiball.libs.HoTro;
import com.example.minhkhai.fuiball.libs.myLocation;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import info.hoang8f.widget.FButton;

/**
 * Created by minhkhai on 28/07/17.
 */

public class ThongTinSan extends android.support.v4.app.Fragment {

    String id;
    TextView tvCTTenSan, tvCTDiaChi, tvCTLoaiSan, tvCTDienThoai;
    FButton btnBanDo, btnGoiDien;
    ShineButton sbtnLike;
    myLocation viTriSan;
    String thongTin;
    SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_thong_tin_san, container, false);

        // lay id san dc gui tu danh sach san
        Bundle extra = getActivity().getIntent().getExtras();
        id = extra.getString("id");

        // nhan thong tin tu activity cha
        thongTin = getArguments().getString("thongtin");
        //Toast.makeText(getActivity(), thongTin, Toast.LENGTH_SHORT).show();

        tvCTTenSan = (TextView) view.findViewById(R.id.tvChiTietTenSan);
        tvCTDiaChi = (TextView) view.findViewById(R.id.tvChiTietDiaChi);
        tvCTLoaiSan = (TextView) view.findViewById(R.id.tvChiTietLoaiSan);
        tvCTDienThoai = (TextView) view.findViewById(R.id.tvChiTietDienThoai);
        btnBanDo = (FButton) view.findViewById(R.id.btnBanDo);
        btnGoiDien = (FButton) view.findViewById(R.id.btnGoiDien);
        sbtnLike = (ShineButton) view.findViewById(R.id.sbtnLike);

        //xu ly thong tin nhan dc tu activity cha
        xuLyThongTin(thongTin);

        btnGoiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+tvCTDienThoai.getText()));
                //Kiểm tra permission
                //thiết bị chạy android 6 với api > 23 thì ứng dụng sẽ yêu cầu người dùng cấp quyền gọi
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE}, 10);  //request specific permission from user
                    return;
                }else {     //have got permission
                    try{
                        startActivity(i);  //call activity and make phone call
                    }
                    catch (android.content.ActivityNotFoundException ex){
                        Toast.makeText(getActivity(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnBanDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BanDoSan.class);
                intent.putExtra("lat", viTriSan.getLat());
                intent.putExtra("long", viTriSan.getLongi());
                intent.putExtra("ten", tvCTTenSan.getText());
                startActivity(intent);
            }
        });

        //final String[] arrLike = HoTro.USER_LIKE_TEST.split(",");

        sbtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO lay user bang shareprefence
                // chay test
                if (HoTro.USERNAME_LIKE_TEST.contains(id)) {
                    new like().execute(HoTro.SERVER+"/user/unlike/"+HoTro.USER_ID_TEST+"/"+id);
                } else {
                    new like().execute(HoTro.SERVER+"/user/like/"+HoTro.USER_ID_TEST+"/"+id);
                }

                // dung khi chay that
                /*if (sp.getString(getString(R.string.USER_LIKE), "").contains(id)) {
                    new like().execute(HoTro.SERVER+"/user/unlike/"+HoTro.USER_ID_TEST+"/"+id);
                } else {
                    new like().execute(HoTro.SERVER+"/user/like/"+HoTro.USER_ID_TEST+"/"+id);
                }*/
            }
        });

        return view;
    }

    private void xuLyThongTin (String s) {
        try {
            sp = getActivity().getSharedPreferences(getString(R.string.USER_FILE), Context.MODE_PRIVATE);
            JSONObject obj = new JSONObject(s);

            tvCTTenSan.setText("Sân bóng " + obj.getString("name"));
            tvCTDiaChi.setText(obj.getString("address")+", "+obj.getString("area")+", "+obj.getString("city"));
            tvCTDienThoai.setText(obj.getString("phone"));
            tvCTLoaiSan.setText(obj.getString("ground_type"));

            //TODO lay user bang shareprefence
            // chay test
            if (HoTro.USERNAME_LIKE_TEST.contains(obj.getString("_id"))) {
                sbtnLike.setChecked(true);
            }
            // dung khi chay that
            /*if (sp.getString(getString(R.string.USER_LIKE), "").contains(id)) {
                sbtnLike.setChecked(true);
            }*/

            String strViTri = obj.getString("location");
            JSONObject objViTri = new JSONObject(strViTri.substring(1, strViTri.length()-1));
            Log.i("viTriSan", objViTri.toString());
            viTriSan = new myLocation(objViTri.getString("lat"), objViTri.getString("long"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class like extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL myUrl = new URL(params[0]);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("cs", "cs");
                return HoTro.POST_URL(myUrl, postDataParams);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            //TODO lay user bang shareprefence
            // chay test
            HoTro.USERNAME_LIKE_TEST = s.substring(1, s.length()-1).replace("\"", "");// xu ly doan nay de lau dc chuoi dang"id1,id2"
            Log.i("ktRong", HoTro.USERNAME_LIKE_TEST+"csa");
            // dung khi chay that
            /*SharedPreferences.Editor editor = sp.edit();
            editor.putString(getString(R.string.USER_LIKE), s.substring(1, s.length()-1).replace("\"", ""));
            editor.commit();*/
            super.onPostExecute(s);
        }
    }

}
