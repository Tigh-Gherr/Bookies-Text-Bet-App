package com.tommyfrenchbookmakers.officialapp.ui.BarcodeScannerActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.tighearnan.frenchsscanner.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tighearnan on 21/03/16.
 */
public class CameraModule implements TextureView.SurfaceTextureListener, Camera.PreviewCallback {

    private static final float NEXUS_5X_VIEW_ROTATION = 270.0f;
    private static final float STANDARD_VIEW_ROTATION = 90.0f;

    private Camera mCamera;
    private BarcodeProcessor mProcessor;
    private CameraHandlerThread mThread;

    private ScanSuccessfulListener mScanListener;
    private CameraInstantiatedListener mInstantiatedListener;

    public CameraModule(Context context) {
        mProcessor = new BarcodeProcessor(context.getApplicationContext());

        if(!mProcessor.isOperational()) {
            Toast.makeText(context,
                    R.string.error_barcode_decoder_not_operational,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void setScanListener(ScanSuccessfulListener scanListener) {
        mScanListener = scanListener;
    }

    public void setInstantiatedListener(CameraInstantiatedListener instantiatedListener) {
        mInstantiatedListener = instantiatedListener;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        openCameraInNewThread();
        mInstantiatedListener.onInstantiation();
        mCamera.setPreviewCallback(this);

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Already handled by Camera by default.
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
        // Cannot extract Bitmap from this method.
    }

    public FrameLayout.LayoutParams getLayoutParams() {
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();

        return new FrameLayout.LayoutParams(
                previewSize.width, previewSize.height, Gravity.CENTER
        );
    }

    public float getViewFinderRotation() {
        return Build.MODEL.equals("Nexus 5X") ?
                    NEXUS_5X_VIEW_ROTATION : STANDARD_VIEW_ROTATION;
    }

    public void setFlash(boolean turnOn) {
        Camera.Parameters params = mCamera.getParameters();
        params.setFlashMode(turnOn ?
                Camera.Parameters.FLASH_MODE_TORCH :
                Camera.Parameters.FLASH_MODE_OFF);

        mCamera.setParameters(params);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(mProcessor.scanPreviewForBarcode(getBitmapFromData(data))) { // Preview frame has barcode.
            mScanListener.onScanSuccessful(mProcessor.getBarcode());
        }
    }

    private Bitmap getBitmapFromData(byte[] data) {
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        int w = previewSize.width;
        int h = previewSize.height;
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, w, h, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, w, h), 80, byteArrayOutputStream);
        byte[] jdata = byteArrayOutputStream.toByteArray();
        return BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
    }

    private void instantiateCamera() {
        Camera camera = null;
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (RuntimeException e) {

        }

        mCamera = camera;
    }

    private void openCameraInNewThread() {
        if(mThread == null) {
            mThread = new CameraHandlerThread();
        }

        synchronized (mThread) {
            mThread.openCamera();
        }
    }

    public void focusCamera(float x, float y, int width, int height) {
        mCamera.cancelAutoFocus();

        Camera.Parameters params = mCamera.getParameters();

        if(params.getMaxNumMeteringAreas() < 1) {
            mCamera.autoFocus(null);
            return;
        }

        Rect focusRect = calculateTapArea(x, y, width, height);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        List<Camera.Area> metringAreas = new ArrayList<>();
        metringAreas.add(new Camera.Area(focusRect, 800));
        params.setFocusAreas(metringAreas);

        try {
            mCamera.setParameters(params);
            mCamera.autoFocus(null);
        } catch (RuntimeException e) {

        }
    }

    private Rect calculateTapArea(float x, float y, int width, int height) {
        final int FOCUS_AREA_SIZE = 300;
        int left = clamp(Float.valueOf((x / width) * 2000 - 1000).intValue(),
                FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / height) * 2000 - 1000).intValue(),
                FOCUS_AREA_SIZE);
        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if(Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if(touchCoordinateInCameraReper > 0) {
                result = 1000 + focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }

        return result;
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
