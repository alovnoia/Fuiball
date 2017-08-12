package com.example.minhkhai.fuiball.dangnhap;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.minhkhai.fuiball.R;
import com.example.minhkhai.fuiball.libs.HoTro;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class DangKy extends AppCompatActivity {

    MaterialEditText edtUsername, edtPassword, edtConfirm, edtName, edtPhone;
    Button btnDangKy;
    String username, password, confirm, name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        // quay lại giao diện đăng nhập
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AnhXa(); // Ánh xạ các view đã khai báo với layout

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DangKyTaiKhoan().execute(HoTro.SERVER + "/user");
                    }
                });
            }
        });

    }

    // đăng ký tài khoản
    private class DangKyTaiKhoan extends AsyncTask<String, String, String>{

        String validate = Validate(); // kiểm tra các trường trống, điền không đúng

        @Override
        protected String doInBackground(String... params) {

            if (validate.equals("empty")){
                return "";
            } else if (validate.equals("confirm")) {
                return "";
            } else {
                try {
                    URL myUrl = new URL(params[0]);

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("username", username);
                    postDataParams.put("password", password);
                    postDataParams.put("name", name);
                    postDataParams.put("phone", phone);
                    postDataParams.put("avatar", "");
                    //postDataParams.put("fbid", "");

                    return HoTro.POST_URL(myUrl, postDataParams);
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("")) {

            } else {
                try {
                    Log.i("thathu", s.toString());
                    JSONObject obj = new JSONObject(s);
                    if (obj.has("existed")) { // nếu json trả về có trường "existed" tức là đã tồn tại, đăng ký không thành
                        Toast.makeText(DangKy.this,
                                "Tài khoản đã tồn tại! Vui lòng chọn tài khoản khác", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("thathu1", s.toString());
                        Toast.makeText(DangKy.this,
                                "Đăng ký thành công! Hệ thống sẽ tự động đăng nhập", Toast.LENGTH_SHORT).show();
                    }

                    HoTro.DangNhapThanhCong(
                            DangKy.this,
                            obj.getString("_id"),
                            obj.getString("username"),
                            obj.getString("like")
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * validate các trường trong form đăng ký
     * @return "pass" nếu không có lỗi, "confirm" nếu xác nhận sai mật khẩu, "empty" nếu nhập thiếu trường
     */
    private String Validate() {

        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();
        confirm = edtConfirm.getText().toString();
        name = edtName.getText().toString();
        phone = edtPhone.getText().toString();
        
        if (username.isEmpty() ||
            password.isEmpty() ||
            confirm.isEmpty() ||
            name.isEmpty() ||
            phone.isEmpty() ) {
            Toast.makeText(DangKy.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return "empty";
        } else if (!password.equals(confirm)) {
            Toast.makeText(DangKy.this, "Xác nhận mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            return "confirm";
        }
        return "pass";
    }

    private void AnhXa() {
        edtUsername = (MaterialEditText) findViewById(R.id.edtDKTaiKhoan);
        edtPassword = (MaterialEditText) findViewById(R.id.edtDKMatKhau);
        edtConfirm = (MaterialEditText) findViewById(R.id.edtDKXacNhanMatKhau);
        edtName = (MaterialEditText) findViewById(R.id.edtDKTen);
        edtPhone = (MaterialEditText) findViewById(R.id.edtDKDienThoai);
        btnDangKy = (Button) findViewById(R.id.btnDKDangKy);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int idItem = item.getItemId();
        if (idItem == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
