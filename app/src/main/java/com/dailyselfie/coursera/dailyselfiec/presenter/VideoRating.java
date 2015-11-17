package com.dailyselfie.coursera.dailyselfiec.presenter;

import android.content.Context;
import android.os.AsyncTask;


import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.AverageVideoRating;
import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.Video;
import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.VideoServiceProxy;
import com.dailyselfie.coursera.dailyselfiec.view.ui.VideoAdapter;


/**
 * Created by Cecilia Urbina on 15/07/15.
 */
public class VideoRating extends AsyncTask<Video, Void, Void> {
    private VideoServiceProxy mVideoServiceProxy;
    private Context mContext;
    private VideoAdapter mVideoAdapter;

    private int id;
    private int position;
    private AverageVideoRating response;
    /**
     * Used to enable garbage collection.
     */

    //initiate vars
    public VideoRating(int aPosition, Context aContext, VideoAdapter videoAdapter, VideoServiceProxy aVideoServiceProxy) {
        super();
        //my params here
        mContext = aContext;
        mVideoServiceProxy = aVideoServiceProxy;
        mVideoAdapter = videoAdapter;
        position = aPosition;
    }

    @Override
    protected Void doInBackground(Video... params) {
        //Response response = mVideoServiceProxy.getData(params[0].getId());

        long i = 1;
        id = (int) params[0].getId();
        //VideoStatus vs = mVideoServiceProxy.setVideoRating(params[0].getId(), params[0].getRating());
        response = mVideoServiceProxy.setVideoRating(params[0].getId(), params[0].getRating());
        return null;
    }


    @Override
    protected void onPostExecute(Void result) {
        //do stuff
        mVideoAdapter.showToast();
        mVideoAdapter.update(position, (int) response.getRating());

    }


}