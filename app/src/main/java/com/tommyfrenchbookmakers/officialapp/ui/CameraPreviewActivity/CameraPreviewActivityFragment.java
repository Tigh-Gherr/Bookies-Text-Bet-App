package com.tommyfrenchbookmakers.officialapp.ui.CameraPreviewActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.Global;
import com.tommyfrenchbookmakers.officialapp.ui.ResultPagerActivity.ResultPagerActivity;
import com.tommyfrenchbookmakers.officialapp.utils.NetworkUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class CameraPreviewActivityFragment extends Fragment {

    private TextureView mTextureView;

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
    private CameraModule mCameraModule;


    public CameraPreviewActivityFragment() {
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


        mCameraModule = new CameraModule(getActivity());

        mCameraModule.setInstantiatedListener(new CameraInstantiatedListener() {
            @Override
            public void onInstantiation() {
                mTextureView.setRotation(mCameraModule.getViewFinderRotation());
                mTextureView.setLayoutParams(mCameraModule.getLayoutParams());
            }
        });

        mCameraModule.setScanListener(new ScanSuccessfulListener() {
            @Override
            public void onScanSuccessful(String result) {
                processBarcode(result);
            }
        });

        mTextureView = (TextureView) v.findViewById(R.id.camera_preview);
        mTextureView.setSurfaceTextureListener(mCameraModule);

        mTextureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mCameraModule.focusCamera(event.getX(),
                                        event.getY(),
                                        mTextureView.getWidth(),
                                        mTextureView.getHeight());
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

        mToggleFlashImageButton = (AppCompatImageButton) v.findViewById(R.id.image_button_toggleFlash);
        if(!checkHasFlash(getActivity())) {
            mToggleFlashImageButton.setVisibility(View.GONE);
        }
        mToggleFlashImageButton.setOnTouchListener(mOnTouch);
        mToggleFlashImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                mCameraModule.setFlash(v.isSelected());

            }
        });

        return v;
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private boolean checkHasFlash(Context context) {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
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

}
