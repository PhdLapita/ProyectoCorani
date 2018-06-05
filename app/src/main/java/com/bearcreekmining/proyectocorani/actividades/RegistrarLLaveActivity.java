package com.bearcreekmining.proyectocorani.actividades;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bearcreekmining.proyectocorani.App;
import com.bearcreekmining.proyectocorani.R;
import com.bearcreekmining.proyectocorani.adapter.AdapterImagenInterface;
import com.bearcreekmining.proyectocorani.db.entidades.LlaveEntidad;
import com.bearcreekmining.proyectocorani.fragmentos.ElegirImagenFragment;
import com.bearcreekmining.proyectocorani.fragmentos.ElegirImagenFragmentInterface;
import com.bearcreekmining.proyectocorani.fragmentos.EmparejarFragment;
import com.bearcreekmining.proyectocorani.fragmentos.EmparejarFragmentInterface;
import com.bearcreekmining.proyectocorani.model.imagenModel;
import com.bearcreekmining.proyectocorani.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class RegistrarLLaveActivity extends BaseActivity implements EmparejarFragmentInterface, ElegirImagenFragmentInterface {
    private static final String TAG = "RegistrarLLaveActivity";
    @BindView(R.id.btnRegistrarBluetooth)Button btnRegistrarBluetooth;
    @BindView(R.id.fab)FloatingActionButton fab;
    @BindView(R.id.etDescripcion)EditText etDescripcion;
    @BindView(R.id.ibRegistrarImagen)ImageButton ibRegistrarImagen;
    private int hayDescripcion,hayImagen,hayBluetooth;
    private ElegirImagenFragment elegirImagenFragment;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;
    private BluetoothAdapter mBluetoothAdapter;    //adaptador que me permitira conocer el estado de mi bluettoh
    private EmparejarFragment emparejarFragment;    //creo una instancia de la clase EmparejarFragment
    int imagenElegida;
    String direccionBluettothElegido,nombreBluetoothElegido,descripcion;
    //List<Integer> listImagenes ;
    Constantes constantes= new Constantes();

    @OnClick(R.id.fab)public void clicBotonFlotante() {
        getData();
        final List<LlaveEntidad> listaDeLlaves = new ArrayList<>();
        LlaveEntidad llaveEntidad = new LlaveEntidad();
        llaveEntidad.setBleUuid(direccionBluettothElegido);
        llaveEntidad.setImagen(String.valueOf(imagenElegida));
        llaveEntidad.setDescripcion(descripcion);
        llaveEntidad.setNameBle(nombreBluetoothElegido);
        listaDeLlaves.add(llaveEntidad);

        new Thread(new Runnable(){
            @Override
            public void run() {
                App.get().getDB().llaveDao().insertAll(listaDeLlaves);
                List<LlaveEntidad> imprimirLlaves = App.get().getDB().llaveDao().getAll();
                for(int i=0;i<imprimirLlaves.size();i++){
                    Log.d("BASEDEDATOS", imprimirLlaves.get(i).toString());
                }
                //Log.d("IMPRIMIEDOXD", String.valueOf(imprimirLlaves.get(imprimirLlaves.indexOf(1))));
            }
        }).start();

        //irHacia(MisLlavesActivity.class);
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
         descripcion = text.toString();
        //Log.i(TAG, "feature name:" + featureName);
        if(descripcion.isEmpty()){
            hayDescripcion=0;
            }else {
            hayDescripcion=1;
            areYouReady();
        }
    }

    public void getData(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        imagenElegida = sharedPreferences.getInt("iamgenNumber", 0);
        Log.d(TAG, String.valueOf(imagenElegida));
        direccionBluettothElegido = sharedPreferences.getString("direccion", "");
        Log.d(TAG, direccionBluettothElegido);
        nombreBluetoothElegido = sharedPreferences.getString("nombre", "");
        Log.d(TAG,nombreBluetoothElegido);
    }

    public void initSharedPreferences(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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
        initSharedPreferences();
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
    public void verificarHayBluetooth() {
        hayBluetooth=1;
        getData();
        btnRegistrarBluetooth.setText(nombreBluetoothElegido);
        areYouReady();

    }

    @Override
    public void clicFondoxD() {
        deleteFragment(elegirImagenFragment);
    }

    @Override
    public void verificarHayImagen() {
        hayImagen=1;
        getData();
        ibRegistrarImagen.setImageResource(Constantes.listImagenes.get(imagenElegida));//jalo de mis constantes la imagen para mostrar en el boton
        areYouReady();
    }
/**
 * Pregunto si ya las 3 opciones de elegir imagen, elegir llave y poner descripción fueron seteados por el usuario**/
    public void areYouReady(){
        if(hayBluetooth == 1 && hayDescripcion == 1 && hayImagen == 1){
            fab.setEnabled(true);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
    }


}
