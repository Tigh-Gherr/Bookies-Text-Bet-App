package com.tommyfrenchbookmakers.officialapp.utils;

/**
 * Created by Tíghearnán on 02/09/2015.
 */
public interface DataDownloadListener {
    void onDownloadStart();
    void onDownloadComplete(Boolean success, String downloadedData);
}
