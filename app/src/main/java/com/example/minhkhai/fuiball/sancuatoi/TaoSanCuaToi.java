package com.example.minhkhai.fuiball.sancuatoi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minhkhai.fuiball.MainActivity;
import com.example.minhkhai.fuiball.Models.ThanhPho;
import com.example.minhkhai.fuiball.R;
import com.example.minhkhai.fuiball.libs.HoTro;
import com.example.minhkhai.fuiball.libs.myLocation;
import com.example.minhkhai.fuiball.timsan.SanBongFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.sackcentury.shinebuttonlib.R.id.home;

public class TaoSanCuaToi extends AppCompatActivity {

    MaterialEditText edtTenSan, edtDienThoai, edtLoaiSan, edtDiaChi, edtToaDo;
    Spinner spKhuVuc, spThanhPho;
    com.melnykov.fab.FloatingActionButton fabTaoSan;

    ArrayList<ThanhPho> lstThanhPho = new ArrayList<>();
    ArrayList<String> lstKhuVuc;
    ArrayAdapter<ThanhPho> adapterThanhPho;
    ArrayAdapter<String> adapterKhuVuc;

    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_san_cua_toi);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtTenSan = (MaterialEditText) findViewById(R.id.edtThemTenSan);
        edtDienThoai = (MaterialEditText) findViewById(R.id.edtThemSDT);
        edtLoaiSan = (MaterialEditText) findViewById(R.id.edtThemLoaiSan);
        edtDiaChi = (MaterialEditText) findViewById(R.id.edtThemDiaChi);
        edtToaDo = (MaterialEditText) findViewById(R.id.edtThemToaDo);
        spKhuVuc = (Spinner) findViewById(R.id.spThemKhuVuc);
        spThanhPho = (Spinner) findViewById(R.id.spThemThanhPho);
        fabTaoSan = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fabTaoSan);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getListThanhPho().execute(HoTro.SERVER + "/city"); // lấy dữ liệu thành phố để đổ vào spinner
            }
        });

        spThanhPho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ThanhPho tp = (ThanhPho) parent.getSelectedItem();
                List<String> kv = tp.getLstKhuVuc(); // lấy list khu vực tương ứng tp
                lstKhuVuc = new ArrayList<>(); // gán list mới để recycler view ko bị đè dữ liệu
                for (int i = 0; i < kv.size(); i++) {// đưa dữ liệu khu vực và lstKhuVuc
                    lstKhuVuc.add(kv.get(i));
                }

                Log.i("kv", lstKhuVuc.toString() + "sc");

                // khi chọn xong thì tiến hành lấy dữ liệu cho spinner khu vực
                adapterKhuVuc = new ArrayAdapter<String>(TaoSanCuaToi.this,
                        R.layout.spinner_item,
                        lstKhuVuc);
                adapterKhuVuc.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spKhuVuc.setAdapter(adapterKhuVuc);
                adapterKhuVuc.notifyDataSetChanged();// đánh dấu thay đổi RV
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtToaDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    Intent intent = builder.build(TaoSanCuaToi.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        fabTaoSan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new TaoSan().execute(HoTro.SERVER+"/ground");
                    }
                });
            }
        });

    }

    private class TaoSan extends AsyncTask<String, String, String> {

        String tenSan = edtTenSan.getText().toString();
        String diaChi = edtDiaChi.getText().toString();
        String sdt = edtDienThoai.getText().toString();
        String loaiSan = edtLoaiSan.getText().toString();
        String khuVuc = spKhuVuc.getSelectedItem().toString();
        String thanhPho = spThanhPho.getSelectedItem().toString();

        String toaDo = edtToaDo.getText().toString();

        @Override
        protected String doInBackground(String... params) {
            if (!KiemTraHopLe()) {
                return null;
            }
            String[] arrToaDo = toaDo.split(",");
            String lat = arrToaDo[0];
            String longi = arrToaDo[1];
            try {
                URL myUrl = new URL(params[0]);

                JSONObject data = new JSONObject();

                data.put("name", tenSan);
                data.put("address", diaChi);
                data.put("phone", sdt);
                data.put("area", khuVuc);
                data.put("city", thanhPho);
                data.put("ground_type", loaiSan);
                data.put("desc", "");
                data.put("lat", lat);
                data.put("long", longi);
                //TODO lay user bang shareprefence
                data.put("owner", HoTro.USER_ID_TEST);

                return HoTro.POST_URL(myUrl, data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("") || s.length() < 3) {
                Toast.makeText(TaoSanCuaToi.this, "Có lỗi xảy ra! Chưa tạo được sân", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TaoSanCuaToi.this, "Sân mới đã được tạo! Hãy bổ sung các thông tin cần thiết!", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean KiemTraHopLe () {
            if (tenSan.equals("") ||
                diaChi.equals("") ||
                sdt.equals("") ||
                loaiSan.equals("") ||
                toaDo.equals("")) {
                return false;
            }
            return true;
        }
    }

    private class getListThanhPho extends AsyncTask<String, String, String> {

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
                adapterThanhPho = new ArrayAdapter<ThanhPho>(TaoSanCuaToi.this,
                        R.layout.spinner_item,
                        lstThanhPho);
                adapterThanhPho.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spThanhPho.setAdapter(adapterThanhPho);
            } catch (JSONException e) {
                Log.i("listArea", "csav");
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String lat = place.getLatLng().toString();
                edtToaDo.setText(lat.substring(10, lat.length()-1));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
