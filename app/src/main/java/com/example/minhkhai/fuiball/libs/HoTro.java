package com.example.minhkhai.fuiball.libs;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.minhkhai.fuiball.MainActivity;
import com.example.minhkhai.fuiball.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by minhkhai on 17/07/17.
 */

public class HoTro {

    //public static String SERVER = "http://10.0.3.2:3000";
    public static String SERVER = "http://192.168.1.201:3000";

    // dung de test chuc nang can phan quyen ma khong can dang nhap
    public static String USER_ID_TEST = "59716b6fd33953099c30dcce";
    public static String USERNAME_TEST = "oclhh";
    public static String USERNAME_LIKE_TEST = "59781d85633b2a00cc60fe69,597816e3633b2a00cc60fe65";
    public static String USERNAME_LIKE;

    // phương thức post cho api
    public static String POST_URL(URL url, JSONObject object) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        try {
            writer.write(getPostDataString(object));
        } catch (Exception e) {
            e.printStackTrace();
        }

        writer.flush();
        writer.close();
        os.close();

        int responseCode=conn.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {

            BufferedReader in=new BufferedReader(new
                    InputStreamReader(
                    conn.getInputStream()));

            StringBuffer sb = new StringBuffer("");
            String line="";

            while((line = in.readLine()) != null) {

                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();

        }
        else {
            return new String("false : "+responseCode);
        }
    }

    // phương thức lấy chuỗi từ object cho post
    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    // phương thức gét dùng cho api
    public static String GET_URL(String theURL) throws IOException {
        StringBuilder content = new StringBuilder();
        URL url = new URL(theURL);
        URLConnection urlConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null){
            content.append(line + "\n");
        }
        bufferedReader.close();

        return content.toString();
    }

    // hiển thị ngày trên editext
    public static void datePicker(final EditText editText, Context context){
        Calendar ngayGioHienTai = Calendar.getInstance();
        DatePickerDialog date = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // dùng string format để có thể lấy định dạng 01/01/2017 thay vì 1/1/2017
                        editText.setText(String.format("%02d-%02d-%02d", dayOfMonth, month+1, year));
                    }
                },
                //Định dạng ngày tháng năm
                ngayGioHienTai.get(Calendar.YEAR),
                ngayGioHienTai.get(Calendar.MONTH),
                ngayGioHienTai.get(Calendar.DAY_OF_MONTH));
        date.show();
    }

    public static void timePicker(final EditText editText, Context context){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText(String.format("%d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();
    }

    // đổi kiểu date từ YYYY/MM/dd -> dd/MM/YYYY
    public static String convertDate(String date){
        String[] ngay = date.split("-");
        return ngay[2] + "/" + ngay[1] + "/" + ngay[0];
    }

    // chuyển giao diện khi đăng nhập thành công
    public static void DangNhapThanhCong(Context context, String id, String username, String like){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.USER_FILE),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.USER_ID), id);
        editor.putString(context.getString(R.string.USER_NAME), username);
        editor.putString(context.getString(R.string.USER_LIKE), like);
        editor.commit();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * divider và orientation cho recyclerview
     * @param activity: activity chứa recyclerview
     * @param recyclerView recyclerview cần xử lý
     */
    public static void XuLyRecyclerView(Context activity, RecyclerView recyclerView){
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        // chia item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}
