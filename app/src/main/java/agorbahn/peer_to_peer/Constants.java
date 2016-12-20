package agorbahn.peer_to_peer;

import java.util.UUID;

/**
 * Created by Adam on 12/19/2016.
 */

public class Constants {
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_OBJECT = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_OBJECT = "device_name";
    public static final int REQUEST_ENABLE_BLUETOOTH = 1;
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    public static final UUID MY_UUID = UUID.fromString("4800fe5d-f6f1-4aab-9cd6-7aebd87eb306");
}
