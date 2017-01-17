package agorbahn.peer_to_peer.command;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import java.io.IOException;

/**
 * Created by Adam on 1/17/2017.
 */
public class Command {

    public void type(String message, Context context) {
        message = message.toLowerCase();
        if (message.equals("rigntone")) {
            playRigntone(context);
        } else if (message.equals("alarm")) {
            playAlarm(context);
        } else if (message.equals("vibrate")) {
            playVibrate(context);
        } else if (message.equals("notification")) {
            playNotification(context);
        }
    }

    public void playSound(Context context, Uri soundUri) throws IllegalArgumentException,
            SecurityException,
            IllegalStateException,
            IOException {

        MediaPlayer mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(context, soundUri);
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }
    }

    public void playVibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    public void playNotification(Context context) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            playSound(context, soundUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playAlarm(Context context) {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if(alert == null){
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if(alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        try {
            playSound(context, alert);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playRigntone(Context context) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        try {
            playSound(context, soundUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}