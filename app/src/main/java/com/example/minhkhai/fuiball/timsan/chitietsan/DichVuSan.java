package com.example.minhkhai.fuiball.timsan.chitietsan;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.minhkhai.fuiball.Models.DichVu;
import com.example.minhkhai.fuiball.Models.ThanhPho;
import com.example.minhkhai.fuiball.R;
import com.example.minhkhai.fuiball.libs.HoTro;
import com.example.minhkhai.fuiball.timsan.SanBongAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DichVuSan extends Fragment {

    String thongTin;
    ArrayList<DichVu> lstDichVu;
    DichVuSanAdapter adapter;
    RecyclerView rvDichVu;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dich_vu_san, container, false);
        thongTin = getArguments().getString("thongtin");
        //Toast.makeText(getActivity(), thongTin, Toast.LENGTH_SHORT).show();

        rvDichVu = (RecyclerView) view.findViewById(R.id.rvGiaDichVu);
        xuLyThongTin(thongTin);

        return view;
    }

    private void xuLyThongTin (String s) {
        try {
            JSONObject obj = new JSONObject(s);

            JSONArray arrDichVu = obj.getJSONArray("service");
            lstDichVu = new ArrayList();
            for (int i = 0; i < arrDichVu.length(); i++) {
                JSONObject objDichVu = new JSONObject(arrDichVu.getString(i));
                lstDichVu.add(new DichVu(
                        objDichVu.getString("_id"),
                        objDichVu.getString("name"),
                        objDichVu.getString("price")
                ));
            }
            Log.i("dv", lstDichVu.size() + "");
            adapter = new DichVuSanAdapter(getContext(), lstDichVu);

            HoTro.XuLyRecyclerView(getActivity(), rvDichVu);

            rvDichVu.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            /*adapter.setOnItemClickedListener(new SanBongAdapter.OnItemClickedListener() {
                @Override
                public void onItemClick(String address) {
                    Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
                }
            });*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
