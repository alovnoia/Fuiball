package com.example.minhkhai.fuiball;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.minhkhai.fuiball.libs.HoTro;
import com.example.minhkhai.fuiball.sancuatoi.TaoSanCuaToi;
import com.example.minhkhai.fuiball.timsan.SanBongFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager = getFragmentManager();
    Fragment fragment;
    String loadInterface = ""; //lưu tên fragment sẽ load
    FloatingActionsMenu fabMenu;
    FloatingActionButton fabTimSanTo, fabQuanhDay, fabSanCuaToi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        fabMenu = (FloatingActionsMenu) findViewById(R.id.fabParent);
        fabTimSanTo = (FloatingActionButton) findViewById(R.id.fabTimSanTo);
        fabQuanhDay = (FloatingActionButton) findViewById(R.id.fabQuanhDay);
        fabSanCuaToi = (FloatingActionButton) findViewById(R.id.fabSanCuaToi);

        fabMenu.collapse();

        fabTimSanTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("key", "SanBongFragment");
                startActivity(intent);
            }
        });

        fabSanCuaToi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new KiemTraChuSan().execute(HoTro.SERVER+"/ground/owner/"+HoTro.USER_ID_TEST);
                    }
                });
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,  R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        try {
            // lấy key để load đúng giao diện
            loadInterface = extras.getString("key");
        }catch(Exception e){

        }

        // tiến hành load giao diện
        switch (loadInterface) {
            case "SanBongFragment":
                fragment = new SanBongFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.framelayout, fragment).commit();
                toggle.setDrawerIndicatorEnabled(true);
                break;
            /*case "SanCuaToiFragment":
                fragment = new SanCuaToiFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.framelayout, fragment).commit();
                toggle.setDrawerIndicatorEnabled(true);
                break;*/
            default:
                fragment = new SanBongFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.framelayout, fragment).commit();
                toggle.setDrawerIndicatorEnabled(true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_san) {

        } else if (id == R.id.nav_tran) {

        } else if (id == R.id.nav_taiKhoan) {

        } else if (id == R.id.nav_dangXuat) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class KiemTraChuSan extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL myUrl = new URL(params[0]);

                JSONObject data = new JSONObject();

                return HoTro.POST_URL(myUrl, data);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (!s.equals("true")) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MainActivity.this);
                }
                builder.setTitle("Chưa có sân")
                        .setMessage("Bạn có muốn tạo sân của bạn?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, TaoSanCuaToi.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            } else {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("key", "SanCuaToiFragment");
                startActivity(intent);
            }

            super.onPostExecute(s);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fabMenu.collapse();
    }
}
