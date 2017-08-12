package com.example.minhkhai.fuiball.timsan;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minhkhai.fuiball.Models.SanBong;
import com.example.minhkhai.fuiball.Models.ThanhPho;
import com.example.minhkhai.fuiball.R;
import com.example.minhkhai.fuiball.libs.HoTro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;


public class SanBongFragment extends android.app.Fragment {

    ImageButton ibTimSan;
    TextView tvMoTaIb, tvThanhPhoDangChon, tvKhuVucDangChon;
    Spinner spCity, spArea;
    ConstraintLayout clTimKiem;
    RecyclerView rvSanBong;
    SanBongAdapter sanBongAdapter;
    ArrayList<SanBong> lstSanBong;
    ArrayList<SanBong> lstSanBongGoc;
    ArrayList<ThanhPho> lstThanhPho = new ArrayList<>();
    ArrayList<String> lstKhuVuc;
    ArrayAdapter<ThanhPho> adapterThanhPho;
    ArrayAdapter<String> adapterKhuVuc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_san_bong, container, false);

        ibTimSan = (ImageButton) view.findViewById(R.id.ibTimSan);
        tvMoTaIb = (TextView) view.findViewById(R.id.tvMotaIb);
        clTimKiem = (ConstraintLayout) view.findViewById(R.id.clTimKiem);
        rvSanBong = (RecyclerView) view.findViewById(R.id.rvSanBong);
        tvThanhPhoDangChon = (TextView) view.findViewById(R.id.tvThanhPhoDangChon);
        tvKhuVucDangChon = (TextView) view.findViewById(R.id.tvKhuVucDangChon);

        setHasOptionsMenu(true);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new getListYeuThich().execute(HoTro.SERVER+"/ground/like");
                }
            });

        final Dialog dialog = new Dialog(getActivity());
        makeDialog(dialog); // khai báo, ánh xạ các view, đổ dữ liệu và gán sự kiện trong dialog

        clTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        }); // sự kiện cho thanh tìm kiếm

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sanBongAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sanBongAdapter.filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    // asynctask chỉ chạy đc ở main thread nên ko cho vào onClick của ibTimsan đc ma phải load trước ở on create
    private void makeDialog(final Dialog dialog){
        dialog.setContentView(R.layout.dialog_tim_san);
        spCity = (Spinner) dialog.findViewById(R.id.spCity);
        spArea = (Spinner) dialog.findViewById(R.id.spArea);
        FButton btnTimKiem = (FButton) dialog.findViewById(R.id.btnTimKiem);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getListThanhPho().execute(HoTro.SERVER + "/city"); // lấy dữ liệu thành phố để đổ vào spinner
            }
        });
        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tp = spCity.getSelectedItem().toString();
                final String kv = spArea.getSelectedItem().toString();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("kvv", HoTro.SERVER + "/ground/" + tp + "/" + kv);
                        new getListSanBong().execute(HoTro.SERVER + "/ground/condition", tp, kv);
                    }
                }); // lấy dữ liệu sân bóng tương ứng với điều kiện đã chọn
                ibTimSan.setVisibility(View.INVISIBLE);
                tvMoTaIb.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                tvThanhPhoDangChon.setText(tp);
                tvKhuVucDangChon.setText(kv);
            }
        });
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("kv", "sc");
                //ThanhPho tp = (ThanhPho) parent.getSelectedItem();
                List<String> kv = ((ThanhPho) parent.getSelectedItem()).getLstKhuVuc(); // lấy list khu vực tương ứng tp
                lstKhuVuc = new ArrayList<>(); // gán list mới để recycler view ko bị đè dữ liệu
                lstKhuVuc.add("--"); // option nếu muốn lấy hết các khu vực trong 1 TP
                for (int i = 0; i < kv.size(); i++) {// đưa dữ liệu khu vực và lstKhuVuc
                    lstKhuVuc.add(kv.get(i));
                }

                Log.i("kv", lstKhuVuc.toString() + "sc");

                // khi chọn xong thì tiến hành lấy dữ liệu cho spinner khu vực
                adapterKhuVuc = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_item,
                        lstKhuVuc);
                adapterKhuVuc.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spArea.setAdapter(adapterKhuVuc);
                adapterKhuVuc.notifyDataSetChanged();// đánh dấu thay đổi RV
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // lấy danh sách các thành phố qua api
    private class getListThanhPho extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {

            try {
                Log.i("thathu", HoTro.GET_URL(params[0]));
                return HoTro.GET_URL(params[0]);
            } catch (IOException e) {
                Log.i("thathu", "cas");
                e.printStackTrace();
            }
            Log.i("thathu", "fafvas");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("thathu", "1");
            Log.i("listAreas", s);
            try {
                JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    Log.i("listAreaS", object.getJSONArray("area").toString());
                    JSONArray arrArea = object.getJSONArray("area"); // lấy mảng khu vực
                    List<String> area = new ArrayList<>();
                    for (int j = 0; j< arrArea.length(); j++) {
                        area.add(arrArea.getString(j));
                    }

                    Log.i("listAreaF", area.toString());

                    lstThanhPho.add(new ThanhPho(
                            object.getString("_id"),
                            object.getString("name"),
                            area
                    ));
                }
                //đưa dữ liệu vào spinner TP
                adapterThanhPho = new ArrayAdapter<ThanhPho>(getActivity(),
                        R.layout.spinner_item,
                        lstThanhPho);
                adapterThanhPho.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spCity.setAdapter(adapterThanhPho);
            } catch (JSONException e) {
                Log.i("listArea", "csav");
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }

    private class getListSanBong extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                URL myUrl = new URL(params[0]);

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("city", params[1]);
                postDataParams.put("area", params[2]);
                return HoTro.POST_URL(myUrl, postDataParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.i("thathus",s + "cs");
                JSONArray array = new JSONArray(s);
                lstSanBongGoc = new ArrayList<>();
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    lstSanBongGoc.add(new SanBong(
                            object.getString("_id"),
                            object.getString("address"),
                            object.getString("name"),
                            object.getString("area"),
                            object.getString("city")
                    ));
                }

                XuLyRecyclerView();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    private class getListYeuThich extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {

            JSONObject data = new JSONObject();
            try {
                URL myUrl = new URL(params[0]);

                //TODO lay user bang shareprefence
                data.put("lst_ground", HoTro.USERNAME_LIKE_TEST);

                return HoTro.POST_URL(myUrl, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.i("thathus",s + "cs");
                JSONArray array = new JSONArray(s);
                lstSanBongGoc = new ArrayList<>();
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    lstSanBongGoc.add(new SanBong(
                            object.getString("_id"),
                            object.getString("address"),
                            object.getString("name"),
                            object.getString("area"),
                            object.getString("city")
                    ));
                }

                XuLyRecyclerView();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    private void XuLyRecyclerView() {
        lstSanBong = new ArrayList<>(lstSanBongGoc);

        if (lstSanBong.size() == 0) { // nếu không có sân nào ở khu vực tương ứng thì hiện icon
            ibTimSan.setVisibility(View.VISIBLE);
            tvMoTaIb.setVisibility(View.VISIBLE);
        }

        // đưa dữ liệu sân bóng vào RV
        sanBongAdapter = new SanBongAdapter(getActivity(), lstSanBong, lstSanBongGoc);

        HoTro.XuLyRecyclerView(getActivity(), rvSanBong);

        rvSanBong.setAdapter(sanBongAdapter);
        sanBongAdapter.notifyDataSetChanged();

        sanBongAdapter.setOnItemClickedListener(new SanBongAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(String address) {
                Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
