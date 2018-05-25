package com.bearcreekmining.proyectocorani.fragmentos;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bearcreekmining.proyectocorani.R;
import com.bearcreekmining.proyectocorani.actividades.LlaveroActivity;
import com.bearcreekmining.proyectocorani.actividades.SensoresActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bear on 11/01/18.
 */

public class EmparejarFragment extends BaseFragment{
    @BindView(R.id.btn_buscar_devices)Button btnBuscarDevices;
    @BindView(R.id.btn_fondo)RelativeLayout btnFndo;
    @BindView(R.id.lvDevices)ListView lvDevices;
    String address;
    public static final String uuid_blenano = "713D0000-503E-4C75-BA94-3148F18D941E";
    public static final UUID uuid_blenano_xd = UUID.fromString("713D0000-503E-4C75-BA94-3148F18D941E");
    private final static String TAG = EmparejarFragment.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;                                                      //Keep track of whether there is a scan in progress
    private Handler mHandler;                                                                       //Handler used to stop scanning after time delay
    private static final int REQUEST_ENABLE_BT = 1;                                                 //Constant to identify response from Activity that enables Bluetooth
    private static final long SCAN_PERIOD = 10000;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    //private ScanCallback callback;
    private ScanCallback mCallback;

    private ArrayList mLeDevicesList = null;                                                        //List adapter to hold list of BLE devices from a scan
    private ArrayAdapter<String> mLeDevicesListAdapter = null;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private BluetoothLeScanner bluetoothLeScanner;
    EmparejarFragmentInterface interfaceEmparejar;

    @OnClick(R.id.btn_fondo)public void clicFondo(){
        interfaceEmparejar.clicFondo();
    }

    @OnClick(R.id.btn_buscar_devices)public void clicBuscar(){

        stopScan();
        mLeDevicesListAdapter.clear();                                                      //Clear list of BLE devices found
        scanRefresh();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.emparejar_fragment;
    }

    @Override
    public void initViewFragment() {
        super.initViewFragment();
        Log.e("LOL","OMG7");
        initBluetooth();
        permisosxD();
    }

    public void saveBluetooth(String mac,String name){
        SharedPreferences preferencias = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("direccion", mac);
        editor.putString("nombre",name);
        editor.commit();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        interfaceEmparejar=(EmparejarFragmentInterface) context;
    }
    // ----------------------------------------------------------------------------------------------------------------
    // Activity launched

    protected void initBluetooth() {
        mHandler = new Handler();                                                                   //Create Handler to stop scanning
        Log.e("LOL","OMG6");

        BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);   //Get the BluetoothManager
        mBluetoothAdapter = bluetoothManager.getAdapter();                                                          //Get a reference to the BluetoothAdapter (radio)

        //Instantiate LE devices list view, list and list adapter
        mLeDevicesList = new ArrayList();
        mLeDevicesListAdapter = new ArrayAdapter(getActivity(), R.layout.simple_list_item_devices, mLeDevicesList);



        if (lvDevices != null) {
            lvDevices.setAdapter(mLeDevicesListAdapter);                                   //Set adapter list view

            lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {       // Device selected in list adapter
                @Override
                // Start DeviceControl and pass the BLE device name and address to the activity
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the device MAC address, the last 17 chars in the View
                    String info = ((TextView) view).getText().toString();
                    address = info.substring(info.length() - 17);
                    Log.i("SCANNER",address);
                    stopScan();
                    final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);      // Get the Bluetooth device from the Bluetooth adapter
                    //bluetoothGattDevice = device.connectGatt(this,false,new ());

                    if (!(device == null)) {                                                        // Ignore if device is not valid
                        final Intent intent1 = new Intent(getActivity(), SensoresActivity.class);     //Create Intent to start the DeviceControl
                        final Intent intent2 = new Intent(getActivity(), LlaveroActivity.class);     //Create Intent to start the DeviceControl
                        //final Intent intent3 = new Intent(getActivity(), LlaveroActivity.class);     //Create Intent to start the DeviceControl
                        //intent.putExtra(EXTRAS_DEVICE_NAME, device.getName());        //Add BLE device name to the intent (for info, not needed)
                        //intent.putExtra(EXTRAS_DEVICE_ADDRESS, de vice.getAddress());  //Add BLE device address to the intent
                        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        String activity=sharedpreferences.getString("numero","");
                        Log.e("LOL",activity);
                        if(TextUtils.equals(activity,"1")){
                            startActivity(intent1);
                        }
                        if(TextUtils.equals(activity,"2")){
                            startActivity(intent2);
                        }
                       // if(TextUtils.equals(activity,"3")){
                        //    startActivity(intent3);
                        //}
                        Log.i("SCANNER","name:"+device.getName()+" dir:"+device.getAddress());
                        saveBluetooth(device.getAddress(),device.getName());
                    }
                }
            });
        }
    }


    public void permisosxD(){
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION))
            {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else
            {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        11);
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopScan();
    }
    // ----------------------------------------------------------------------------------------------------------------
    // Activity resumed
    // Enable BT if not already enabled, initialize list of BLE devices, start scan for BLE devices
    @Override
    public void onResume() {
        super.onResume();
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {           //Check if BLE is supported
            Toast.makeText(getActivity(), "Bluetooth no soportado ado", Toast.LENGTH_LONG).show();                 //Message that BLE not supported
            getActivity().finish();                                                                             //End the app
        }
        scanRefresh();
        if (!mBluetoothAdapter.isEnabled()) {                                                       //Check if BT is not enabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);             //Create an intent to get permission to enable BT
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);                              //Fire the intent to start the activity that will return a result based on user response
        }

    }

    // ----------------------------------------------------------------------------------------------------------------
    // Activity paused
    // Stop scan and clear device list
    @Override
    public void onPause() {
        super.onPause();
        stopScan();
        mLeDevicesListAdapter.clear();                                                              //Clear the list of BLE devices found during the scan
    }
    /**
     * Scan for BLE devices with Android API 21 and up

     */

    @RequiresApi(21)
    public void startScan(String serviceUUID){
        Log.e("LOL","OMG12");
        ParcelUuid uuid = new ParcelUuid(UUID.fromString(serviceUUID));

        mCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {

                super.onScanResult(callbackType, result);
                Log.e("LOL","OMG15");
                if (result != null){
                    BluetoothDevice bluetoothDevice = result.getDevice();
                    int rssixD= result.getRssi();
                    Log.i("rssi",bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress()+"--------"+result.getRssi());
                    if(!mLeDevicesList.contains(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress())){        //First check that it is a new device not in the list
                        Log.d(TAG, "Found Bluetooth device: "
                                + bluetoothDevice.getName() + " - " + bluetoothDevice.getAddress());                      //Debug information to log the devices as they are found
                        mLeDevicesListAdapter.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());       //Add the device to the list adapter that will show all the available devices
                        mLeDevicesListAdapter.notifyDataSetChanged();                                   //Tell the list adapter that it needs to refresh the view
                    }
                    Log.e("LOL","OMG9");
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.e("LOL","OMG13");
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.e("TAG", "Scan failed " + errorCode);
                Log.e("LOL","OMG11");
                Log.e("LOL",errorCode+"lol");
            }
        };
        bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        List<ScanFilter> filters = new ArrayList<>();

        ScanFilter filter = new ScanFilter.Builder()
                //.setServiceUuid(uuid)
                .build();
        filters.add(filter);

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        mHandler.postDelayed((new Runnable() {
            @Override
            public void run() {
                Log.e("LOL","OMGscanperiod");
                //swipeRefreshLayout.setRefreshing(false);

                bluetoothLeScanner.stopScan(mCallback);
            }}), SCAN_PERIOD);


        bluetoothLeScanner.startScan(filters, settings, mCallback);
        Log.e("LOL","scaneando");
        Log.i("LOL","scaneandoando");


    }
    /**
     * Scan BLE devices on Android API 18 to 20
     *
     * @param enable Enable scan
     */
    private void scanLeDevice18(boolean enable) {

        mLeScanCallback =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice bluetoothDevice, int rssi,
                                         byte[] scanRecord) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!mLeDevicesList.contains(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress())){        //First check that it is a new device not in the list
                                    Log.d(TAG, "Found Bluetooth device: "
                                            + bluetoothDevice.getName() + " - " + bluetoothDevice.getAddress());                      //Debug information to log the devices as they are found
                                    mLeDevicesListAdapter.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());       //Add the device to the list adapter that will show all the available devices
                                    mLeDevicesListAdapter.notifyDataSetChanged();                                   //Tell the list adapter that it needs to refresh the view
                                }
                            }
                        });
                    }
                };
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed((new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }),SCAN_PERIOD);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    public void stopScan(){
        Log.d("LOL","OMG5");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try{
                bluetoothLeScanner.stopScan(mCallback);
            } catch (NullPointerException e) {
            }
            mCallback=null;
        }

        else {

            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mLeScanCallback=null;
        }
    }

    private void scanRefresh(){
        Log.e("LOL","OMG3");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startScan(uuid_blenano);
            Log.e("LOL","OMG1");
        } else {
            scanLeDevice18(true);
            Log.e("LOL","OMG2");
        }
    }
}
