package at.ac.tgm.hit.headupdisplayprototyp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import at.ac.tgm.hit.headupdisplayprototyp.MainActivity;
import at.ac.tgm.hit.headupdisplayprototyp.exceptions.BluetoothException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.Arrays;

public class Utils {
    private static final String TAG = MainActivity.class.getName();
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final ArrayList<String> devicenames = new ArrayList<>(Arrays.asList("CBT", "KONNWEI","CAN OBDII","JBL Clip 2"));

    public static BluetoothDevice getDevice() throws BluetoothException {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice bldevice = null;
        if (bluetoothAdapter == null) {
            throw new BluetoothException("Device doesn't support Bluetooth!");
        }
        else if (!bluetoothAdapter.isEnabled()){
            throw new BluetoothException("Bluetooth disabled");
        }
        else  {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                boolean found = false;
                for (BluetoothDevice device : pairedDevices) { //iterate through paired devices
                    if(devicenames.contains(device.getName())){
                        found = true;
                        bldevice = device;
                    }
                }
                if(found == false) throw new BluetoothException("Der Dongle ist nicht gepaired oder heißt nicht richtig");
            }else{
                throw new BluetoothException("Der Dongle ist nicht gepaired oder heißt nicht richtig");
            }
        }
        System.out.println("Bluetothdevicename: " + bldevice.getName());
        return bldevice;
    }

    public static BluetoothSocket getSocket(BluetoothDevice dev) throws IOException {
        BluetoothSocket sock = null;
        BluetoothSocket sockFallback = null;

        Log.d(TAG, "Starting Bluetooth connection..");
        try {
            sock = dev.createRfcommSocketToServiceRecord(MY_UUID);
            sock.connect();
            System.out.println(sock.getRemoteDevice().getName());
        } catch (Exception e1) {
            Log.e(TAG, "There was an error while establishing Bluetooth connection. Falling back..", e1);
            Class<?> clazz = sock.getRemoteDevice().getClass();
            Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
            try {
                Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                Object[] params = new Object[]{Integer.valueOf(1)};
                sockFallback = (BluetoothSocket) m.invoke(sock.getRemoteDevice(), params);
                sockFallback.connect();
                sock = sockFallback;
            } catch (Exception e2) {
                Log.e(TAG, "Couldn't fallback while establishing Bluetooth connection.", e2);
                throw new IOException(e2.getMessage());
            }
        }
        return sock;
    }
}