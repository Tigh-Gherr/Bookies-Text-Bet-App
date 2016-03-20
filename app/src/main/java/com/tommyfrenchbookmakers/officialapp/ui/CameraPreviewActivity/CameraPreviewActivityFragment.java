package com.tommyfrenchbookmakers.officialapp.ui.CameraPreviewActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.tighearnan.frenchsscanner.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.ui.ResultPagerActivity.ResultPagerActivity;
import com.tommyfrenchbookmakers.officialapp.utils.NetworkUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class CameraPreviewActivityFragment extends Fragment
                        implements Camera.PreviewCallback, TextureView.SurfaceTextureListener {

    private Camera mCamera;
    private BarcodeDetector mDetector;
    private TextureView mTextureView;
    private CameraHandlerThread mThread;

    private ImageView mBackButtonImageView;
    private AppCompatImageButton mToggleFlashImageButton;

    private View.OnTouchListener mOnTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().setDuration(100)
                            .scaleX(0.75f)
                            .scaleY(0.75f);
                    break;
                case MotionEvent.ACTION_UP:
                    v.animate().setDuration(100)
                            .scaleX(1f)
                            .scaleY(1f);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return false;
        }
    };


    public CameraPreviewActivityFragment() {
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private boolean checkHasFlash(Context context) {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void instantiateCamera() {
        mCamera = null;
        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e) {

        }
    }

    private void scanPreviewForBarcode(final Bitmap preview) {
        Frame frame = new Frame.Builder().setBitmap(preview).build();
        SparseArray<Barcode> barcodes = mDetector.detect(frame);
        if(barcodes.size() > 0) {
            processBarcode(barcodes.valueAt(0).rawValue);
        }
    }

    private void processBarcode(String barcode) {
        if (!barcode.startsWith("0")) {
            Snackbar.make(getView(),
                    barcode + " is not a docket!",
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
        } else {
            barcode = barcode.substring(1);
        }

        if(NetworkUtils.networkIsAvailable(getActivity())) {
            Intent i = new Intent(getActivity(), ResultPagerActivity.class);
            i.putExtra(Global.INTENT_KEY_DOWNLOAD_TYPE, Global.DOWNLOAD_TYPE_BARCODE);
            i.putExtra(Global.INTENT_KEY_BARCODE, barcode);
            i.putExtra(Global.INTENT_KEY_SENDER, ((CameraPreviewActivity) getActivity()).getSelfNavDrawerItem());
            startActivity(i);
        } else {
            Snackbar.make(getView(),
                    R.string.error_message_no_internet,
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mToggleFlashImageButton.setSelected(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera_preview, container, false);

        if (!checkCameraHardware(getActivity())) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No On Device!")
                    .setMessage("Going back...")
                    .show();

            getActivity().finish();
        }

        mDetector = new BarcodeDetector.Builder(getActivity().getApplicationContext())
                                                .setBarcodeFormats(Barcode.ALL_FORMATS)
                                                .build();
        if(!mDetector.isOperational()) {
            Toast.makeText(getActivity(),
                    "Error.",
                    Toast.LENGTH_SHORT)
                    .show();
        }

        mTextureView = (TextureView)v.findViewById(R.id.camera_preview);
        mTextureView.setSurfaceTextureListener(this);

        mTextureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(mCamera == null) {
                        return false;
                    }

                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if(!success) {
                                Snackbar.make(getView(),
                                        "Autofocus failed.",
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }
                return false;
            }
        });

        mBackButtonImageView = (ImageView) v.findViewById(R.id.image_view_backButton);
        mBackButtonImageView.setOnTouchListener(mOnTouch);
        mBackButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        });

        if(!checkHasFlash(getActivity())) {
            mToggleFlashImageButton.setVisibility(View.GONE);
        }

        mToggleFlashImageButton = (AppCompatImageButton) v.findViewById(R.id.image_button_toggleFlash);
        mToggleFlashImageButton.setOnTouchListener(mOnTouch);
        mToggleFlashImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());

                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(v.isSelected() ?
                        Camera.Parameters.FLASH_MODE_TORCH :
                        Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            }
        });

        return v;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // Generate Bitmap from raw data.
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        int w = previewSize.width;
        int h = previewSize.height;
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21,
                w, h, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, w, h), 80, byteArrayOutputStream);
        byte[] jdata = byteArrayOutputStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);

        scanPreviewForBarcode(bitmap);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        openCameraInNewThread();
        mCamera.setPreviewCallback(this);

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(
                previewSize.width, previewSize.height, Gravity.CENTER
        ));

        try {
            mCamera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mTextureView.setRotation(270.0f);
        mCamera.startPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private void openCameraInNewThread() {
        if (mThread == null) {
            mThread = new CameraHandlerThread();
        }

        synchronized (mThread) {
            mThread.openCamera();
        }
    }

    private class CameraHandlerThread extends HandlerThread {
        Handler mHandler;

        public CameraHandlerThread() {
            super("CameraHandlerThread");
            start();
            mHandler = new Handler(getLooper());
        }

        synchronized void notifyCameraOpened() {
            notify();
        }

        void openCamera() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    instantiateCamera();
                    notifyCameraOpened();
                }
            });

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
