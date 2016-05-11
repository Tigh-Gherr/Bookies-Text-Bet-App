package com.tommyfrenchbookmakers.officialapp.ui.BarcodeScannerActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

/**
 * Created by tighearnan on 21/03/16.
 */
public class BarcodeProcessor {
    private BarcodeDetector mDetector;
    private String mBarcode;

    public BarcodeProcessor(Context context) {
        mDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS).build();
    }

    public boolean isOperational() {
        return mDetector.isOperational();
    }

    public String getBarcode() {
        return mBarcode;
    }

    public boolean scanPreviewForBarcode(Bitmap preview) {
        Frame frame = new Frame.Builder().setBitmap(preview).build();
        SparseArray<Barcode> barcodes = mDetector.detect(frame);

        if(barcodes.size() > 0) {
            mBarcode = barcodes.valueAt(0).rawValue;
            return true;
        }

        return false;
    }
}
