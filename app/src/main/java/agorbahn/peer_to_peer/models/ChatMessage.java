package agorbahn.peer_to_peer.models;

import java.util.Date;

/**
 * Created by Adam on 12/19/2016.
 */

public class ChatMessage {
    private String messageText;
    private long messageTime;
    private String messageUser;
    private String key;
    private boolean by;
    private String imageBitmap;

    public ChatMessage(String messageText, String messageUser, String key, boolean by, String imageBitmap) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.by = by;
        this.key = key;
        messageTime = new Date().getTime();
        this.imageBitmap = imageBitmap;
    }

    public boolean getBy() { return by; }

    public void setBy(boolean by) {this.by = by;}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessageText() {
        return messageText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public String getImageBitmap() { return imageBitmap; }

    public void setImageBitmap(String imageBitmap) { this.imageBitmap = imageBitmap; }

    public boolean isBy() { return by; }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public ChatMessage(){

    }
}
