package agorbahn.peer_to_peer.adapters;

/**
 * Created by Adam on 2/28/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

import agorbahn.peer_to_peer.R;

public class CameraDialog extends AppCompatActivity {
    private Dialog mDialog;
    private Context mContext;
    private CameraSource cameraSource;
    private Button getpicture;
    private SurfaceView cameraView;
    private Bitmap mImage;
    private String mSave = "f";

    public String getSave() {
        return mSave;
    }

    public void setSave(String mSave) {
        this.mSave = mSave;
    }

    public void show(Context context) {
        mContext = context;
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.layout_camera);

        cameraView = (SurfaceView) mDialog.findViewById(R.id.surface_view);

        getpicture = (Button) mDialog.findViewById(R.id.getpicture);

        FaceDetector detector = new FaceDetector.Builder(context)
                .setProminentFaceOnly(true)
                .build();

        cameraSource = new CameraSource.Builder(context, detector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1020, 720)
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
                cameraSource.takePicture(null, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {
                        mSave = "t";
                        mImage = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
                    }
                });

            }
        });

        mDialog.show();

    }

    public void setImage(Bitmap mImage) {
        this.mImage = mImage;
    }

    public Bitmap getImage() {
        return mImage;
    }


}