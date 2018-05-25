package com.bearcreekmining.proyectocorani.actividades;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bearcreekmining.proyectocorani.R;
import com.bearcreekmining.proyectocorani.Servicios.BleService;
import com.bearcreekmining.proyectocorani.fragmentos.EsperaFragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LlaveroActivity extends BaseActivity {

    private BleService mService = null;
    private BluetoothAdapter mBtAdapter = null;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String mDeviceName, mDeviceAddress;
    @BindView(R.id.btnKey)ImageButton btnKey;
    private String incomingMessage = "";
    private EsperaFragment esperaFragment;
    private BluetoothGattCharacteristic mDataMDLP, mControlMLDP;                        //The BLE characteristic used for MLDP data transfers
    @OnClick(R.id.btnKey)public void clicButton(){
        //mService.writeRXCharacteristic("a");
        sendString("#a$");
        Log.i("lol","wtfsalio?");
    }
    private final ServiceConnection mServiceConnection = new ServiceConnection() {        //Create new ServiceConnection interface to handle connection and disconnection

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {    //Service connects
            mService = ((BleService.LocalBinder) service).getService(); //Get a link to the service
            if (!mService.initialize()) {                                    //See if the service did not initialize properly
                Log.e("lol", "Unable to initialize Bluetooth");
                finish();                                                                //End the application
            }
            mService.connect(mDeviceAddress);                                //Connects to the device selected and passed to us by the DeviceScanActivity
            //mService.setNRF51822(true, false);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {                //Service disconnects
            mService = null;
            deleteFragment(esperaFragment);
            finish();
            //Not bound to a service
        }
    };


    @Override
    protected int getLayoutId() {
        return  R.layout.activity_llavero;
    }

    @Override
    public void initView() {
        super.initView();
        initBluetooth();
        initSharedPreferences();
        service_init();
        initFragment();
        showFragment(esperaFragment,R.id.fragment_esperar);
    }

    public void initFragment(){
        esperaFragment = new EsperaFragment();
    }

    public void initBluetooth(){
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.i("lol", "");
        if (mBtAdapter == null) {
            Log.i("lol", "Bluetooth is not available");
        }else {
            Log.i("lol", "Bconsegui un Btadapter");
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();//cierra la aplicacion con el button back
        mService.disconnect();
    }

    public void initSharedPreferences(){
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mDeviceAddress =  sharedpreferences.getString("direccion","");
        Log.i("lol", mDeviceAddress);
        mDeviceName =  sharedpreferences.getString("nombre","");
        Log.i("lol", mDeviceName);
    }

    private void service_init() {
        Intent bindIntent = new Intent(this, BleService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BleService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BleService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }
    public void sendString(String s) {
        try {
            sendBytes(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendBytes(byte[] bytes) {
        if (!mService.isConnected()) { return; }
        mDataMDLP.setValue(bytes);                     //Set value of MLDP characteristic to send die roll information
        mService.writeCharacteristic(mDataMDLP);                     //Call method to write the characteristic
        // mService.writeRXCharacteristic(bytes);
    }

    // ----------------------------------------------------------------------------------------------------------------
    // BroadcastReceiver handles various events fired by the BluetoothLeService service.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        String action;
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();                                   //Get the action that was broadcast by the intent that was received

            if (BleService.ACTION_GATT_CONNECTED.equals(action)) {              //Service has connected to BLE device
                //mConnected = true;                                                      //Record the new connection state
                //updateConnectionState(R.string.connected);                              //Update the display to say "Connected"
                //invalidateOptionsMenu();                                                //Force the Options menu to be regenerated to show the disconnect option
                Log.i("lol", "SE CONECTO");
            }
            if (BleService.ACTION_GATT_DISCONNECTED.equals(action)) {        //Service has disconnected from BLE device
                //mConnected = false;                                                     //Record the new connection state
                //updateConnectionState(R.string.disconnected);                           //Update the display to say "Disconnected"
                //invalidateOptionsMenu();                                                //Force the Options menu to be regenerated to show the connect option
                Log.i("lol", "SE DESCONECTO");
            }
            if (BleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //Service has discovered GATT services on BLE device
                findMldpGattService(mService.getSupportedGattServices());    //Show all the supported services and characteristics on the user interface
                Log.i("lol", "CREO Q DEBERIA DESCUBRIR SERVICIOS");
                if(esperaFragment.isVisible()){
                    deleteFragment(esperaFragment);
                }
                //mService.enableTXNotification();
            }
            if (BleService.ACTION_DATA_AVAILABLE.equals(action)) {         //Service has found new data available on BLE device
                String dataValue = intent.getStringExtra(BleService.EXTRA_DATA); //Get the value of the characteristic
                processIncomingPacket(dataValue);                                       //Process the data that was received
                Log.i("lol", "ACA NO SE QUE HAGO, TAL VEZ LEA O ESCRIBA");
            }
        }
    };
    // ----------------------------------------------------------------------------------------------------------------
    // Iterate through the supported GATT Services/Characteristics to see if the MLDP srevice is supported
    private void findMldpGattService(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {                                                     //Verify that list of GATT services is valid
            Log.i("lol", "findMldpGattService found no Services");
            return;
        }
        String uuid;                                                                    //String to compare received UUID with desired known UUIDs
        mDataMDLP = null;                                                               //Searching for a characteristic, start with null value
        Log.i("lol", "sin econtro al parecer :/");
        for (BluetoothGattService gattService : gattServices) {                         //Test each service in the list of services
            uuid = gattService.getUuid().toString();
            Log.i("lol","encontre: "+ uuid);//Get the string version of the service's UUID
            if (uuid.equals(BleService.CCCD.toString())) {                                    //See if it matches the UUID of the MLDP service
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics(); //If so then get the service's list of characteristics
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) { //Test each characteristic in the list of characteristics
                    uuid = gattCharacteristic.getUuid().toString();                     //Get the string version of the characteristic's UUID
                    Log.i("lol","caracteristicas: " + uuid);

                    if (uuid.equals(BleService.TX_CHAR_UUID.toString())) {                          //See if it matches the UUID of the MLDP data characteristic
                        mDataMDLP = gattCharacteristic;                                 //If so then save the reference to the characteristic
                        Log.i("lol", "Found MLDP data characteristics");
                    } else if (uuid.equals(BleService.RX_CHAR_UUID.toString())) {                  //See if UUID matches the UUID of the MLDP control characteristic
                        mControlMLDP = gattCharacteristic;                              //If so then save the reference to the characteristic
                        Log.i("lol", "Found MLDP control characteristics");
                    }
                    final int characteristicProperties = gattCharacteristic.getProperties(); //Get the properties of the characteristic
                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_NOTIFY)) > 0) { //See if the characteristic has the Notify property
                        mService.setCharacteristicNotification(gattCharacteristic, true); //If so then enable notification in the BluetoothGatt
                    }
                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_INDICATE)) > 0) { //See if the characteristic has the Indicate property
                        mService.setCharacteristicIndication(gattCharacteristic, true); //If so then enable notification (and indication) in the BluetoothGatt
                    }
                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_WRITE)) > 0) { //See if the characteristic has the Write (acknowledged) property
                        gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT); //If so then set the write type (write with acknowledge) in the BluetoothGatt
                    }
                    if ((characteristicProperties & (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) > 0) { //See if the characteristic has the Write (unacknowledged) property
                        gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE); //If so then set the write type (write with no acknowledge) in the BluetoothGatt
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mService = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mService != null) {
            final boolean result = mService.connect(mDeviceAddress);
            Log.i("lol", "Connect request result=" + result);
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

    }
    // ----------------------------------------------------------------------------------------------------------------
    // Look for message with switch pressed indicator "->S1\n\r"
    private void processIncomingPacket(String data) {
        String distancia;
        int indexStart, indexEnd;
        incomingMessage = incomingMessage.concat(data);//Add the new data to what is left of previous data
        if (incomingMessage.length() >= 2 && incomingMessage.contains("#") && incomingMessage.contains("$")) { //See if we have the right nessage
            indexStart = incomingMessage.indexOf("#");                                //Get the position of the matching characters
            indexEnd = incomingMessage.indexOf("$");                                 //Get the position of the end of frame "\r\n"
            if (indexEnd >0) {//Check that the packet does not have missing or extra characters
                distancia=incomingMessage.substring(indexStart+1,indexEnd);
                Log.i("DATAXD",incomingMessage);
                incomingMessage="";
            }                                                                            //Thow away everything up to and including "\n\r"
        }
    }

}
