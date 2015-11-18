package com.dailyselfie.coursera.dailyselfiec.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.ImageServiceProxy;
import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.VideoServiceProxy;
import com.dailyselfie.coursera.dailyselfiec.utils.ImageStorageUtils;
import com.dailyselfie.coursera.dailyselfiec.utils.VideoStorageUtils;
import com.dailyselfie.coursera.dailyselfiec.view.MainActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by root on 17/11/15.
 */
public class ImageData extends AsyncTask<Void, Void, Void> {
    private ImageServiceProxy mImageServiceProxy;
    private Context mContext;
    private File mFile = null;
    private String mFileName = null;
    private int mEffect = 1;


    public ImageData(Context aContext, File aFile, ImageServiceProxy aImageServiceProxy,
                     int aEffect) {
        super();
        mFile = aFile;
        mImageServiceProxy = aImageServiceProxy;
        mFileName = mFile.getName();
        mContext = aContext;
        mEffect = aEffect;

    }

    @Override
    protected Void doInBackground(Void... voids) {
        TypedFile typedFile = new TypedFile("image/*", mFile);

        try {
            Response response = mImageServiceProxy.setImageData(mEffect, typedFile);
            mFile = ImageStorageUtils.storeVideoInExternalDirectory(mContext, response, "");
            System.out.println(response.getStatus());
        } catch (RetrofitError e) {
            System.out.println(e.getResponse().getStatus());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        ImageStorageUtils.saveImageFromCamera(mFile, mContext,
                ImageStorageUtils.getCurrentUser(mContext), mFileName);
        if (mFile != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(mFile));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(mFile), "image/*");
            mContext.startActivity(intent);
        }

    }


}
