package agorbahn.peer_to_peer.ui;

import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import agorbahn.peer_to_peer.Constants;
import agorbahn.peer_to_peer.R;
import agorbahn.peer_to_peer.adapters.BluetoothListDialogs;
import agorbahn.peer_to_peer.adapters.ChatController;
import agorbahn.peer_to_peer.adapters.LogDialogs;
import agorbahn.peer_to_peer.helper.AESHelper;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    private Handler mHandler;
    private ChatController chatController;
    private Menu mMenu;
    private MenuItem mBluetooth;
    private ArrayAdapter<String> mChatAdapter;
    private ArrayList<String> mChatMessages;
    @Bind(R.id.list_of_messages) ListView mListView;
    @Bind(R.id.send) Button mSend;
    @Bind(R.id.input) EditText mInput;
    FirebaseUser userFire;

    String mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available!", Toast.LENGTH_SHORT).show();
            finish();
        }

        userFire = FirebaseAuth.getInstance()
                .getCurrentUser();

        if (userFire != null) {
            mUser = FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getDisplayName();
        } else {
            mUser = "";
        }


        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.MESSAGE_STATE_CHANGE:
                        switch (msg.arg1) {
                            case Constants.STATE_CONNECTED:
                                setTitle(mDevice.getName());
                                break;
                            case Constants.STATE_CONNECTING:
                                setTitle("Connecting...");
                                break;
                            case Constants.STATE_LISTEN:
                            case Constants.STATE_NONE:
                                setTitle("Not connected");
                                break;
                        }
                        break;
                    case Constants.MESSAGE_WRITE:
                        byte[] writeBuf = (byte[]) msg.obj;
                        String writeMessage = new String(writeBuf);
                        jsonMessage(writeMessage);
                        break;
                    case Constants.MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;
                        String readMessage = new String(readBuf, 0, msg.arg1);
                        jsonMessage(readMessage);
                        break;
                    case Constants.MESSAGE_DEVICE_OBJECT:
                        mDevice = msg.getData().getParcelable(Constants.DEVICE_OBJECT);
                        Toast.makeText(getApplicationContext(), "Connected to " + mDevice.getName(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.MESSAGE_TOAST:
                        Toast.makeText(getApplicationContext(), msg.getData().getString("toast"),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        mSend.setOnClickListener(this);

        //set chat adapter
        mChatMessages = new ArrayList<>();
        mChatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mChatMessages);
        mListView.setAdapter(mChatAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_bluetooth) {
            bluetoothSearch();
            return true;
        }

        if (id == R.id.action_log) {
            LogDialogs test = new LogDialogs();
            FragmentManager manager = getFragmentManager();

            test.show(manager, "");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        super.onCreateOptionsMenu(menu);

        this.mMenu = menu;

        return true;
    }

    private void bluetoothSearch() {
        BluetoothListDialogs display = new BluetoothListDialogs(mBluetoothAdapter, chatController);
        display.show(this);
        chatController = display.getChatController();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BLUETOOTH);
        } else {
            chatController = new ChatController(this, mHandler);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (chatController != null) {
            if (chatController.getState() == Constants.STATE_NONE) {
                chatController.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatController != null)
            chatController.stop();
    }

    @Override
    public void onClick(View v) {
        if (mInput.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this, "Please input some texts", Toast.LENGTH_SHORT).show();
        } else {
            //TODO: here
            sendMessage(mInput.getText().toString());
            mInput.setText("");
        }
    }

    private void sendMessage(String message) {
        if (chatController.getState() != Constants.STATE_CONNECTED) {
            Toast.makeText(this, "Connection was lost!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.length() > 0) {

            byte[] send = makeJSON(message).getBytes();
            chatController.write(send);
        }
    }

    private String makeJSON(String message) {
        JSONObject json = new JSONObject();
        AESHelper encryption = new AESHelper();
        String random = encryption.randomKey();
        try {
            message = encryption.encrypt(random, message);
            random = encryption.encrypt(Constants.ENCRYPT_SEED, random);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            json.put("key", random);
            json.put("message", message);
            json.put("from", "Adam");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    private void jsonMessage(String jsonData) {
        AESHelper encryption = new AESHelper();
        try {
            JSONObject messageJSON = new JSONObject(jsonData);
            String name = messageJSON.get("from").toString();
            String key = encryption.decrypt(Constants.ENCRYPT_SEED, messageJSON.get("key").toString());
            String message =  encryption.decrypt(key, messageJSON.get("message").toString());
            mChatMessages.add(name + ":  " + message);
            mChatAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}