package agorbahn.peer_to_peer.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import agorbahn.peer_to_peer.Constants;
import agorbahn.peer_to_peer.R;
import agorbahn.peer_to_peer.helper.AESHelper;
import agorbahn.peer_to_peer.models.ChatMessage;

/**
 * Created by Guest on 12/21/16.
 */
public class MessageAdapter extends ArrayAdapter<ChatMessage> {
    public MessageAdapter(Context context, ArrayList<ChatMessage> chatMessages) {
        super(context, 0, chatMessages);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        ChatMessage chatMessage = getItem(position);

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.message, parent, false);
        }
        // Get references to the views of message.xml
        TextView messageText = (TextView)v.findViewById(R.id.message_text);
        TextView messageUser = (TextView)v.findViewById(R.id.message_user);
        TextView messageTime = (TextView)v.findViewById(R.id.message_time);

        AESHelper encryption = new AESHelper();

        try {
            String key = encryption.decrypt(Constants.ENCRYPT_SEED, chatMessage.getKey());
            String message =  encryption.decrypt(key, chatMessage.getMessageText());
            // Set their text
            messageText.setText(message);
            messageUser.setText(chatMessage.getMessageUser());

            // Format the date before showing it
            messageTime.setText(DateFormat.format("dd-MM-yyyy",
                    chatMessage.getMessageTime()));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }
}
