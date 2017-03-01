package agorbahn.peer_to_peer.adapters;

/**
 * Created by Adam on 2/28/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import agorbahn.peer_to_peer.R;

public class CameraDialog extends AppCompatActivity {
    private Dialog mDialog;
    private Context mContext;
    private CameraSource cameraSource;
    private Button getpicture;
    private SurfaceView cameraView;
    private String mImage;

    public void show(Context context) {
        mContext = context;
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.layout_camera);

        cameraView = (SurfaceView) mDialog.findViewById(R.id.surface_view);

        getpicture = (Button) mDialog.findViewById(R.id.getpicture);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                Log.d("Main", "receiveDetections");
            }
        });

        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available.");
        }

        cameraSource = new CameraSource.Builder(context, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    //noinspection MissingPermission
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        mDialog.findViewById(R.id.getpicture).setBackgroundResource(R.color.colorAccent);

        mDialog.findViewById(R.id.getpicture).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePic();
                mDialog.dismiss();
            }
        });

        mDialog.setCancelable(false);
        mDialog.show();

    }

    public void takePic() {
//        Toast.makeText(mContext, "in takePic" , Toast.LENGTH_SHORT).show();
        cameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                Toast.makeText(mContext, "in taken image" , Toast.LENGTH_SHORT).show();
            }
        });

//        new CameraSource.PictureCallback() {
//            @Override
//            public void onPictureTaken(byte[] bytes) {
//                Toast.makeText(mContext, "in taken image" , Toast.LENGTH_SHORT).show();
//                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                encodeBitmap(bmp);
//            }
//        });

    }



    public void encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        mImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    public String getImage() {
        return mImage;
    }

}