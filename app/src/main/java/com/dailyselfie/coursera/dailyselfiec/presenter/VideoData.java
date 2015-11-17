package com.dailyselfie.coursera.dailyselfiec.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.Video;
import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.VideoServiceProxy;
import com.dailyselfie.coursera.dailyselfiec.utils.VideoStorageUtils;

import java.io.File;

import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Cecilia Urbina on 15/07/15.
 */
public class VideoData extends AsyncTask<Video, Void, Void> {
    private VideoServiceProxy mVideoServiceProxy;
    private Context mContext;
    private File mFile =  null;
    /**
     * Used to enable garbage collection.
     */

    //initiate vars
    public VideoData(Context aContext, VideoServiceProxy aVideoServiceProxy) {
        super();
        //my params here
        mContext = aContext;
        //TODO: change this
        mVideoServiceProxy = aVideoServiceProxy;

    }

    @Override
    protected Void doInBackground(Video... params) {
        try {
            Response response = mVideoServiceProxy.getData(params[0].getId());
            String fileName = params[0].getTitle();
            mFile = VideoStorageUtils.storeVideoInExternalDirectory(mContext.getApplicationContext(), response, fileName);
        } catch (RetrofitError e) {
            System.out.println(e.getResponse().getStatus());
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void result) {
        if (mFile != null){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(mFile));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(mFile), "video/mp4");
            mContext.startActivity(intent);
        }

    }


}