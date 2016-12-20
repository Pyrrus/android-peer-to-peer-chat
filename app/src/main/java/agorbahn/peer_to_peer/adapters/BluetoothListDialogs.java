package agorbahn.peer_to_peer.adapters;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

import agorbahn.peer_to_peer.R;

/**
 * Created by Adam on 12/19/2016.
 */

public class BluetoothListDialogs {
    private Dialog mDialog;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mDiscoveredDevices;
    private BroadcastReceiver mDiscovery;

    public void show(Context context) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.layout_bluetooth);
        mDialog.setTitle("Bluetooth Devices");

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();

        ArrayAdapter<String> pairedDevicesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        mDiscoveredDevices = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);

        ListView paired = (ListView) mDialog.findViewById(R.id.paired);
        ListView discover = (ListView) mDialog.findViewById(R.id.discovered);
        paired.setAdapter(pairedDevicesAdapter);
        discover.setAdapter(mDiscoveredDevices);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mDiscovery, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mDiscovery, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            pairedDevicesAdapter.add("No devices have been paired");
        }

        paired.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBluetoothAdapter.cancelDiscovery();
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);

                connectToDevice(address);
                mDialog.dismiss();
            }

        });

        discover.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mBluetoothAdapter.cancelDiscovery();
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);

                connectToDevice(address);
                mDialog.dismiss();
            }
        });

        mDialog.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setCancelable(false);
        mDialog.show();
    }


    public BluetoothListDialogs(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
        mDiscovery = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        mDiscoveredDevices.add(device.getName() + "\n" + device.getAddress());
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    if (mDiscoveredDevices.getCount() == 0) {
                        mDiscoveredDevices.add("No devices have been paired");
                    }
                }
            }
        };
    }

    private void connectToDevice(String deviceAddress) {
        mBluetoothAdapter.cancelDiscovery();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);

    }

}