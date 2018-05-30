package com.bearcreekmining.proyectocorani.actividades;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bearcreekmining.proyectocorani.R;
import com.bearcreekmining.proyectocorani.fragmentos.EmparejarFragment;
import com.bearcreekmining.proyectocorani.fragmentos.EmparejarFragmentInterface;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)DrawerLayout drawer;
    @BindView(R.id.nav_view)NavigationView navigationView;
    EmparejarFragment emparejarFragment;
    private static final int REQUEST_ENABLE_BT = 1;                                                 //Constant to identify response from Activity that enables Bluetooth
    private static final String TAG = "MainActivity";
    int fragment1;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;
    @OnClick(R.id.btnMasInfo)public void clicMinera(){
        Uri uri = Uri.parse("http://www.bearcreekmining.com/s/Home.asp"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void initSharedPreferences(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveBoton(String dato){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("numero", dato);
        editor.apply();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();
        initNavigationDrawer();
        //initBluetooth();
        initSharedPreferences();
        emparejarFragment=new EmparejarFragment();
    }

    public void initNavigationDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if(emparejarFragment.isVisible()){
            deleteFragment(emparejarFragment);
            fragment1=1;
        } else {fragment1=2;}
        if(fragment1==2){
            super.onBackPressed();//cierra la aplicacion con el button back
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_polo) {
            if (emparejarFragment.isVisible()) {
                deleteFragment(emparejarFragment);
            } else {
                //showFragment(emparejarFragment, R.id.fragment_emparejando);
                saveBoton("1");
                //enableDisableBT();
            }
        } else if (id == R.id.nav_llave) {
            if (emparejarFragment.isVisible()) {
                deleteFragment(emparejarFragment);
            } else {
                saveBoton("2");
                irHacia(RegistrarLLaveActivity.class);
                //enableDisableBT();
            }
        }
         else if (id == R.id.nav_hiladora) {
            if (emparejarFragment.isVisible()) {
                deleteFragment(emparejarFragment);
            } else {
                saveBoton("3");
                //enableDisableBT();
            }
        } else if (id == R.id.nav_calidad) {
            if (emparejarFragment.isVisible()) {
                deleteFragment(emparejarFragment);
            } else {
                saveBoton("4");
                //enableDisableBT();
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
