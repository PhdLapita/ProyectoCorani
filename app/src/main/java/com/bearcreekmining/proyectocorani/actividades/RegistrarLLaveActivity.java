package com.bearcreekmining.proyectocorani.actividades;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.bearcreekmining.proyectocorani.R;
import com.bearcreekmining.proyectocorani.fragmentos.ElegirImagenFragment;
import com.bearcreekmining.proyectocorani.fragmentos.ElegirImagenFragmentInterface;
import com.bearcreekmining.proyectocorani.fragmentos.EmparejarFragment;
import com.bearcreekmining.proyectocorani.fragmentos.EmparejarFragmentInterface;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class RegistrarLLaveActivity extends BaseActivity implements EmparejarFragmentInterface, ElegirImagenFragmentInterface {
    private static final String TAG = "RegistrarLLaveActivity";
    @BindView(R.id.fab)FloatingActionButton fab;
    @BindView(R.id.etDescripcion)EditText etDescripcion;

    private ElegirImagenFragment elegirImagenFragment;

    private BluetoothAdapter mBluetoothAdapter;    //adaptador que me permitira conocer el estado de mi bluettoh
    private EmparejarFragment emparejarFragment;    //creo una instancia de la clase EmparejarFragment

    @OnClick(R.id.fab)public void clicBotonFlotante() {
        irHacia(MisLlavesActivity.class);
    }

    @OnClick(R.id.btnRegistrarBluetooth)public void clicElegirBle(){
        enableDisableBT();
    }

    @OnClick(R.id.ibRegistrarImagen)public void elegirImagen(){
        if(emparejarFragment.isVisible()) {
            deleteFragment(emparejarFragment);
        } else {
            showFragment(elegirImagenFragment,R.id.fragment_emparejando);
        }
    }

    @OnTextChanged(R.id.etDescripcion)protected void onTextChanged(CharSequence text) {
        String featureName = text.toString();
        //Log.i(TAG, "feature name:" + featureName);
        if(featureName.isEmpty()){
            fab.setEnabled(false);
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }else {
            fab.setEnabled(true);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_registrar_llave;
    }

    @Override
    public void initView() {
        super.initView();
        initBluetooth();            //obtengo el bluetoothAdapter para cuando lo use en el clicElegirBle()
        initFragment();             //inicio los fragments
        initInterface();
    }

    public void initFragment(){
        emparejarFragment=new EmparejarFragment(); //creo un objeto de la clase EmparejarFragment
        elegirImagenFragment = new ElegirImagenFragment(); //creo un objeto de la clase ElegirImagenFragment
    }

    public void initInterface(){
        fab.setEnabled(false);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
    }

    public void initBluetooth(){
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);   //Get the BluetoothManager
        mBluetoothAdapter = bluetoothManager.getAdapter();                                                          //Get a reference to the BluetoothAdapter (radio)
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
            if(emparejarFragment.isVisible()) {
                deleteFragment(emparejarFragment);
            } else {
                showFragment(emparejarFragment, R.id.fragment_emparejando);
            }
        }

    }



    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
    }


    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
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
    public void clicFondo() {
        deleteFragment(emparejarFragment);
    }

    @Override
    public void clicFondoxD() {
        deleteFragment(elegirImagenFragment);
    }
}
