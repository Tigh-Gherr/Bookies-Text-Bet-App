package com.tommyfrenchbookmakers.officialapp.enumerators;

/**
 * Created by Tíghearnán on 31/08/2015.
 */
public enum DownloadType {
    BARCODE,
    ACCOUNT_AND_REFERENCE;

    public static String intentKey() {
        return "DOWNLOAD_TYPE";
    }
}
