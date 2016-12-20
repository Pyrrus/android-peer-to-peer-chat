package agorbahn.peer_to_peer.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import agorbahn.peer_to_peer.R;
import agorbahn.peer_to_peer.adapters.BluetoothListDialogs;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    private Handler mHandler;
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check device support bluetooth or not
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available!", Toast.LENGTH_SHORT).show();
            finish();
        }

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            return true;
        }

        if (id == R.id.action_bluetooth) {
            bluetoothSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tap, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void bluetoothSearch() {
        BluetoothListDialogs test = new BluetoothListDialogs(mBluetoothAdapter);
        test.show(this);
    }
}