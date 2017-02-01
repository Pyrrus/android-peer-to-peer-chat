package agorbahn.peer_to_peer.command;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;

import java.io.IOException;

/**
 * Created by Adam on 1/17/2017.
 */
public class Command {

    private int startFrom = 0;
    private int endAt = 4000;
    private MediaPlayer mMediaPlayer;

    private Runnable stopAt = new Runnable(){
        @Override
        public void run() {
            mMediaPlayer.pause();
        }};

    public String type(String message, Context context) {
        message = message.toLowerCase();
        if (message.equals("ringtone") || message.equals("ringtone ")) {
            playRigntone(context);
        } else if (message.equals("alarm") || message.equals("alarm ")) {
            playAlarm(context);
        } else if (message.equals("vibrate") || message.equals("vibrate ")) {
            playVibrate(context);
        } else if (message.equals("notification") || message.equals("notification ")) {
            playNotification(context);
        } else if (message.equals("show") || message.equals("show ")) {
            return "true";
        }

        return "false";
    }

    public void playSound(Context context, Uri soundUri) throws IllegalArgumentException,
            SecurityException,
            IllegalStateException,
            IOException {

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(context, soundUri);
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            Handler handler = new Handler();
            handler.postDelayed(stopAt, endAt);
        }
    }

    public void playVibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(endAt);
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