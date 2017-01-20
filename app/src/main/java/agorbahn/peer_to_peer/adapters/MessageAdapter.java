package agorbahn.peer_to_peer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import agorbahn.peer_to_peer.Constants;
import agorbahn.peer_to_peer.R;
import agorbahn.peer_to_peer.helper.AESHelper;
import agorbahn.peer_to_peer.models.ChatMessage;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 12/21/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<ChatMessage> mChat = new ArrayList<>();
    private Context mContext;

    public MessageAdapter(Context context, ArrayList<ChatMessage> chat) {
        mContext = context;
        mChat = chat;
    }

    @Override
    public MessageAdapter.MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        MessageHolder viewHolder = new MessageHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageHolder holder, int position) {
        holder.bindChat(mChat.get(position));
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class MessageHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.message_text) TextView mText;
        @Bind(R.id.message_user) TextView mUser;
        @Bind(R.id.message_time) TextView mTime;
        private Context mContext;
        AESHelper encryption;


        public MessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            encryption = new AESHelper();

        }

        public void bindChat(ChatMessage chatMessage) {
            try {
                String key = encryption.decrypt(Constants.ENCRYPT_SEED, chatMessage.getKey());
                String message =  encryption.decrypt(key, chatMessage.getMessageText());
                // Set their text
                mText.setText(message);

                if (chatMessage.getBy() == true) {
                    mTime.setText(chatMessage.getMessageUser());

                    // Format the date before showing it
                    mUser.setText(DateFormat.format("dd-MM-yyyy",
                            chatMessage.getMessageTime()));
                } else {
                    mUser.setText(chatMessage.getMessageUser());

                    // Format the date before showing it
                    mTime.setText(DateFormat.format("dd-MM-yyyy",
                            chatMessage.getMessageTime()));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
