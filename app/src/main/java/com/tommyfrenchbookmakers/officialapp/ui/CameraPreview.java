package com.tommyfrenchbookmakers.officialapp.ui;

import android.content.Context;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by Tíghearnán on 09/02/2016.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Context mContext;
    private Camera mCamera;
    private List<Camera.Size> mSupportedPreviewSizes;
    private Camera.Size mPreviewSize;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mContext = context;

        mCamera = camera;
        if(mCamera != null) mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
        if(mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(mHolder.getSurface() == null) return;

        try {
            mCamera.stopPreview();
        } catch (Exception e) {

        }

        Camera.Parameters parameters = mCamera.getParameters();

        Display display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0) {
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            mCamera.setDisplayOrientation(90);
        }

        if(display.getRotation() == Surface.ROTATION_90) {
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        }

        if(display.getRotation() == Surface.ROTATION_180) {
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        }

        if(display.getRotation() == Surface.ROTATION_270) {
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            mCamera.setDisplayOrientation(180);
        }

        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;

        if(sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;
        int len = sizes.size();
        for(int i = 0; i < len; i++) {
            Camera.Size s = sizes.get(i);
            double ratio = (double) s.width / s.height;
            if(Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if(Math.abs(s.height  - targetHeight) < minDiff) {
                optimalSize = s;
                minDiff = Math.abs(s.height - targetHeight);
            }
        }

        if(optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for(int i = 0; i < len; i++) {
                Camera.Size s = sizes.get(i);
                if(Math.abs(s.height - targetHeight) < minDiff) {
                    optimalSize = s;
                    minDiff = Math.abs(s.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }
}
