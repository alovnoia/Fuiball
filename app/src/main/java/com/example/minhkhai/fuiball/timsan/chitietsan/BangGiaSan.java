package com.example.minhkhai.fuiball.timsan.chitietsan;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minhkhai.fuiball.Models.BangGia;
import com.example.minhkhai.fuiball.Models.SanCon;
import com.example.minhkhai.fuiball.R;
import com.example.minhkhai.fuiball.libs.HoTro;
import com.melnykov.fab.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import info.hoang8f.widget.FButton;

public class BangGiaSan extends Fragment {

    RecyclerView rvBangGia;
    MaterialEditText edtNgay;
    Spinner spSan;
    FButton btnXem;
    FloatingActionButton fabDatSan;
    String strSanCon = "";
    ArrayList<SanCon> lstSanCon = new ArrayList<>();
    ArrayAdapter<SanCon> adapterSanCon;
    GiaSanAdapter adapter;
    ArrayList<BangGia> lstBangGia;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bang_gia_san, container, false);

        rvBangGia = (RecyclerView) view.findViewById(R.id.rvBangGiaSan);
        edtNgay = (MaterialEditText) view.findViewById(R.id.edtNgay);
        spSan = (Spinner) view.findViewById(R.id.spSanCon);
        btnXem = (FButton) view.findViewById(R.id.btnXem);
        fabDatSan = (FloatingActionButton) view.findViewById(R.id.fabDatSan);

        edtNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HoTro.datePicker(edtNgay, getActivity());
            }
        });

        fabDatSan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_dat_san);

                TextView tvTenSan = (TextView) dialog.findViewById(R.id.tvTenSan);
                final MaterialEditText edtNgayDat = (MaterialEditText) dialog.findViewById(R.id.edtNgayDat);
                final MaterialEditText edtBatDau = (MaterialEditText) dialog.findViewById(R.id.edtBatDau);
                final MaterialEditText edtKetThuc = (MaterialEditText) dialog.findViewById(R.id.edtKetThuc);
                FButton btnDat = (FButton) dialog.findViewById(R.id.btnDat);

                tvTenSan.setText(spSan.getSelectedItem().toString());
                edtNgayDat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HoTro.datePicker(edtNgayDat, getActivity());
                    }
                });

                edtBatDau.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        HoTro.timePicker(edtBatDau, getActivity());
                    }
                });

                edtKetThuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        HoTro.timePicker(edtKetThuc, getActivity());
                    }
                });

                btnDat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( edtBatDau.getText().toString().equals("") ||
                             edtKetThuc.getText().toString().equals("") ||
                             edtNgayDat.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "Hãy nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        } else {
                            new datSan().execute(
                                    HoTro.SERVER + "/pitch/order/" + lstSanCon.get(spSan.getSelectedItemPosition()).getId(),
                                    edtNgayDat.getText().toString(),
                                    edtBatDau.getText().toString(),
                                    edtKetThuc.getText().toString()
                            );
                            //Tao doi tuong bang gia moi dat
                            BangGia bg = new BangGia(
                                    edtNgayDat.getText().toString(),
                                    edtBatDau.getText().toString(),
                                    edtKetThuc.getText().toString(),
                                    "0",
                                    "pending"
                            );
                            SanCon sanCon = (SanCon) spSan.getSelectedItem();
                            // them json object cua doi tuong bang gia moi dat
                            sanCon.newOrder(bg.toJsonObject());
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

        xuLyThongTin();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getListSanNho().execute(HoTro.SERVER + "/pitch/inground");
            }
        });

        HoTro.XuLyRecyclerView(getActivity(), rvBangGia);

        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SanCon sanCon = (SanCon) spSan.getSelectedItem();
                lstBangGia = new ArrayList<BangGia>();
                JSONArray arrBangGia = sanCon.getOrder();// lấy danh sách lịch đặt của pitch
                for (int i = 0; i < arrBangGia.length(); i++) {
                    // nếu chưa nhập ngày thì toast và bỏ qua for
                    if (edtNgay.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Nhập ngày muốn xem lịch", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    try {
                        JSONObject objBangGia = arrBangGia.getJSONObject(i);
                        Log.i("date", objBangGia.getString("date") + " " + edtNgay.getText());
                        // Nếu ngày không khớp hoặc trạng thái là từ chối thì không đưa vào danh sách
                        if (edtNgay.getText().toString().equals(objBangGia.getString("date")) &&
                                !objBangGia.getString("status").equals("reject")) {
                            lstBangGia.add(new BangGia(
                                    objBangGia.getString("date"),
                                    objBangGia.getString("from"),
                                    objBangGia.getString("to"),
                                    objBangGia.getString("price"),
                                    objBangGia.getString("user_id"),
                                    objBangGia.getString("status")
                            ));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new GiaSanAdapter(getContext(), lstBangGia);

                rvBangGia.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (lstBangGia.size() == 0) {
                    Toast.makeText(getActivity(), "Chưa có ai đặt trong ngày này!", Toast.LENGTH_SHORT).show();// đổi thành textview
                }


                /*adapter.setOnItemClickedListener(new SanBongAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClick(String address) {
                        Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });

        return view;
    }

    /**
     * lấy danh sách id các pitch để làm tham số đưa lên server
     */
    private void xuLyThongTin () {
        try {
            JSONObject obj = new JSONObject(getArguments().getString("thongtin"));// lấy data được gửi sang qua setArguments
            JSONArray menu = obj.getJSONArray("menu"); // lấy mảng các pitch_id trong field menu
            for (int i = 0; i < menu.length(); i++) { // lặp qua từng object để ghép id vào chuỗi
                JSONObject objSanCon = menu.getJSONObject(i);
                strSanCon += objSanCon.getString("pitch_id") + " ";// chuỗi sẽ được phân tích trên server
            }
            Log.i("sancon", strSanCon.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class getListSanNho extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            final JSONObject postDataParams = new JSONObject();

            try {
                URL myUrl = new URL(params[0]);
                postDataParams.put("lst_pitch", strSanCon.trim());

                return HoTro.POST_URL(myUrl, postDataParams);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray arrObj = new JSONArray(s);

                for (int i = 0; i < arrObj.length(); i++) {
                    JSONObject obj = arrObj.getJSONObject(i);

                    lstSanCon.add(new SanCon(
                            obj.getString("_id"),
                            obj.getString("name"),
                            obj.getJSONArray("order")
                    ));
                }
                // spinner
                adapterSanCon = new ArrayAdapter<SanCon>(getActivity(),
                        R.layout.spinner_item,
                        lstSanCon);
                adapterSanCon.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spSan.setAdapter(adapterSanCon);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("banggia", s + "");
        }
    }

    private class datSan extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            URL myUrl = null;
            try {
                myUrl = new URL(params[0]);
                SharedPreferences sp = getActivity().getSharedPreferences(
                        getString(R.string.USER_FILE),
                        Context.MODE_PRIVATE);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("date", params[1]);
                postDataParams.put("from", params[2]);
                postDataParams.put("to", params[3]);
                postDataParams.put("price", "0");
                //dong duoi dung khi chay that
                //postDataParams.put("user_id", sp.getString(getString(R.string.USER_ID), ""));
                //dong duoi dung de test
                postDataParams.put("user_id", HoTro.USER_ID_TEST);
                postDataParams.put("status", "pending");

                return HoTro.POST_URL(myUrl, postDataParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(getActivity(), "Yêu cầu của bạn đã được gửi đến chủ sân!", Toast.LENGTH_SHORT).show();
        }
    }
}
