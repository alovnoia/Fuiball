package com.example.minhkhai.fuiball.dangnhap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.minhkhai.fuiball.R;
import com.example.minhkhai.fuiball.libs.HoTro;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.xw.repo.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class DangNhap extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    ImageView ivLogo;
    XEditText edtTaiKhoan, edtMatKhau;
    Button btnDangKy, btnDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_dang_nhap);
        getSupportActionBar().hide();

        edtTaiKhoan = (XEditText) findViewById(R.id.edtTaiKhoan);
        edtMatKhau = (XEditText) findViewById(R.id.edtMatKhau);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        btnDangKy = (Button) findViewById(R.id.btnDangKy);
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveup);
        ivLogo.startAnimation(animation); //animation cho logo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAllVisible();
            }
        }, 1000); // sau 1s hiển thị form đăng nhập

        logInFb(); // login với facebook

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DangNhapTaiKhoan().execute(HoTro.SERVER + "/user/login");
                    }
                });
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhap.this, DangKy.class);
                startActivity(intent);
            }
        });
    }

    // Kiểm tra tài khoản đã đăng ký và tiến hành đăng nhập
    private class DangNhapTaiKhoan extends AsyncTask<String, String, String>{
        String taiKhoan = edtTaiKhoan.getText().toString();
        String matKhau = edtMatKhau.getText().toString();
        /*String taiKhoan = "oclhh1";
        String matKhau = "123456";*/

        @Override
        protected String doInBackground(String... params) {
            if (taiKhoan.equals("") || matKhau.equals("")){ // không nhập thông tin thì báo lỗi
                return "Nhập sai thông tin";
            } else {
                try {
                    URL myUrl = new URL(params[0]); // lấy url API được truyền vào

                    JSONObject postDataParams = new JSONObject();// lưu danh sách các trường cho phương thức top
                    postDataParams.put("username", taiKhoan);
                    postDataParams.put("password", matKhau);

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

            if (s.equals("Nhập sai thông tin")) {
                doSwing();// animation báo lỗi
                Toast.makeText(DangNhap.this, "Bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (s.equals("[]")){
                doSwing();
                Toast.makeText(DangNhap.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }else {
                try {
                    s = s.substring(1, s.length()-1);
                    JSONObject obj = new JSONObject(s); // lấy đối tượng json từ kết quả của doInBackground
                    Log.i("thathu", obj.length() + "");
                    // nếu object trả về khác rỗng thì cho phép đăng nhập
                    //Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    HoTro.DangNhapThanhCong(DangNhap.this,
                            obj.getString("_id"),
                            obj.getString("username"),
                            obj.getString("like")
                    );
                } catch (JSONException e) {
                    Log.i("thathu", s.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    // Kiểm tra id fb đã đăng ký nếu chưa thì thêm vào db cho phép đăng nhập
    private class DangNhapFb extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(final String... params) {
            try {
                URL myUrl = new URL(params[0]);
                String fbid = params[1];
                String fbname = params[2];

                //Log.i("fbname", params[2] + " mtp");

                final JSONObject postDataParams = new JSONObject();

                postDataParams.put("fbid", fbid);
                postDataParams.put("name", fbname);

                Log.i("fbres", postDataParams.toString() + " mtp");
                return HoTro.POST_URL(myUrl, postDataParams);
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                Log.i("thathu", s.toString());
                // nếu object trả về khác rỗng thì cho phép đăng nhập
                if (!s.equals("[]")){
                    s = s.substring(1, s.length()-1);

                    //Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    JSONObject obj = new JSONObject(s);
                    HoTro.DangNhapThanhCong(DangNhap.this, obj.getString("_id"), "", obj.getString("like"));
                } else {
                    Toast.makeText(DangNhap.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*accessTokenTracker.stopTracking();
        profileTracker.stopTracking();*/
    }

    // chứa sự kiện cho button đăng nhập fb
    private void logInFb(){
        loginButton.setReadPermissions("public_profile"); // quyền xem thông tin fb
        // sự kiện khi click button
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                //nếu đăng nhập bằng fb thành công thì lấy các thông tin cần thiết để tiến hành đăng nhập hệ thống
                GraphRequest graphRequest = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                Log.i("fbres", response.getJSONObject().toString());
                                try {
                                    new DangNhapFb().execute(HoTro.SERVER + "/user/loginfb",
                                            object.getString("id"),
                                            object.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");// các trường mong muốn api fb trả về
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

                //HoTro.DangNhapThanhCong(DangNhap.this );

            }

            @Override
            public void onCancel() {
                Toast.makeText(DangNhap.this,"Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(DangNhap.this,"Login attempt failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doSwing(){
        YoYo.with(Techniques.Swing)
                .duration(400)
                .repeat(0)
                .playOn(edtTaiKhoan);
        YoYo.with(Techniques.Swing)
                .duration(400)
                .repeat(0)
                .playOn(edtMatKhau);
    }

    private void setAllVisible(){
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        edtMatKhau.startAnimation(anim);
        edtTaiKhoan.startAnimation(anim);
        btnDangKy.startAnimation(anim);
        btnDangNhap.startAnimation(anim);
        loginButton.startAnimation(anim);
        edtTaiKhoan.setVisibility(View.VISIBLE);
        edtMatKhau.setVisibility(View.VISIBLE);
    }

}
