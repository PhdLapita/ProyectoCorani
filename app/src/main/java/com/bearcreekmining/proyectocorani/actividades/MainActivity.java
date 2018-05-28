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
        implements NavigationView.OnNavigationItemSelectedListener,EmparejarFragmentInterface {

    @BindView(R.id.drawer_layout)DrawerLayout drawer;
    @BindView(R.id.nav_view)NavigationView navigationView;
    private BluetoothAdapter mBluetoothAdapter;                                                      //Keep track of whether there is a scan in progress
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
        editor.commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();
       initNavigationDrawer();
        initBluetooth();
       initSharedPreferences();
        emparejarFragment=new EmparejarFragment();
    }


    public void enableDisableBT(){
        if(mBluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: ACABA DE PRENDER TODO XD");
                        showFragment(emparejarFragment, R.id.fragment_emparejando);

                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
    }

    public void initBluetooth(){
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);   //Get the BluetoothManager
        mBluetoothAdapter = bluetoothManager.getAdapter();                                                          //Get a reference to the BluetoothAdapter (radio)
        //mBluetoothAdapter.enable();
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
            /*if (emparejarFragment.isVisible()) {
                deleteFragment(emparejarFragment);
            } else {
                showFragment(emparejarFragment, R.id.fragment_emparejando);
                saveBoton("1");
            }*/
            // Handle the camera action
        } else if (id == R.id.nav_llave) {
            if (emparejarFragment.isVisible()) {
                deleteFragment(emparejarFragment);
            } else {
                //initBluetooth();
                enableDisableBT();
                //showFragment(emparejarFragment, R.id.fragment_emparejando);
                saveBoton("2");
            }
        }
         else if (id == R.id.nav_hiladora) {

        } else if (id == R.id.nav_calidad) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void clicFondo() {

        deleteFragment(emparejarFragment);
    }

}
