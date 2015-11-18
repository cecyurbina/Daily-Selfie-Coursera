package com.dailyselfie.coursera.dailyselfiec.model.mediator;

/**
 * Created by root on 15/11/15.
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.ImageServiceProxy;
import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.Video;
import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.VideoServiceProxy;
import com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata.VideoStatus;
import com.dailyselfie.coursera.dailyselfiec.oauth.SecuredRestBuilder;
import com.dailyselfie.coursera.dailyselfiec.oauth.UnsafeHttpsClient;
import com.dailyselfie.coursera.dailyselfiec.presenter.ImageData;
import com.dailyselfie.coursera.dailyselfiec.presenter.VideoData;
import com.dailyselfie.coursera.dailyselfiec.presenter.VideoRating;
import com.dailyselfie.coursera.dailyselfiec.utils.Constants;
import com.dailyselfie.coursera.dailyselfiec.utils.VideoMediaStoreUtils;
import com.dailyselfie.coursera.dailyselfiec.view.ui.VideoAdapter;

/**
 * Mediates communication between the Video Service and the local
 * storage on the Android device.  The methods in this class block, so
 * they should be called from a background thread (e.g., via an
 * AsyncTask).
 */
public class ImageDataMediator {
    /**
     * Status code to indicate that file is successfully
     * uploaded.
     */
    public static final String STATUS_UPLOAD_SUCCESSFUL =
            "Upload succeeded";

    /**
     * Status code to indicate that file upload failed
     * due to large video size.
     */
    public static final String STATUS_UPLOAD_ERROR_FILE_TOO_LARGE =
            "Upload failed: File too big";

    /**
     * Status code to indicate that file upload failed.
     */
    public static final String STATUS_UPLOAD_ERROR =
            "Upload failed";

    /**
     * Defines methods that communicate with the Video Service.
     */
    private VideoServiceProxy mVideoServiceProxy;
    private ImageServiceProxy mImageServiceProxy;


    /**
     * Constructor that initializes the VideoDataMediator.
     *
     */
    public ImageDataMediator() {

        mVideoServiceProxy = new SecuredRestBuilder()
                .setLoginEndpoint(Constants.SERVER_URL
                        + VideoServiceProxy.TOKEN_PATH)
                .setUsername("xxxxx")//secret!
                .setPassword("xxxxx")//secret!
                .setClientId("mobile")
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setEndpoint(Constants.SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL).build()
                .create(VideoServiceProxy.class);

        mImageServiceProxy = new SecuredRestBuilder()
                .setLoginEndpoint(Constants.SERVER_URL
                        + VideoServiceProxy.TOKEN_PATH)
                .setUsername("xxxxx")//secret!
                .setPassword("xxxxx")//secret!
                .setClientId("mobile")
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setEndpoint(Constants.SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL).build()
                .create(ImageServiceProxy.class);

    }

    public ImageDataMediator(String aUser, String aPass) {
        // Initialize the VideoServiceProxy.

        mVideoServiceProxy = new SecuredRestBuilder()
                .setLoginEndpoint(Constants.SERVER_URL
                        + VideoServiceProxy.TOKEN_PATH)
                .setUsername(aUser) //username from login activity
                .setPassword(aPass) //pass from login activity
                .setClientId("mobile")
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setEndpoint(Constants.SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL).build()
                .create(VideoServiceProxy.class);

        mImageServiceProxy = new SecuredRestBuilder()
                .setLoginEndpoint(Constants.SERVER_URL
                        + VideoServiceProxy.TOKEN_PATH)
                .setUsername(aUser) //username from login activity
                .setPassword(aPass) //pass from login activity
                .setClientId("mobile")
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setEndpoint(Constants.SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL).build()
                .create(ImageServiceProxy.class);

    }

    public VideoServiceProxy getmVideoServiceProxy(){
        return  mVideoServiceProxy;
    }

    /**
     * Uploads the Video having the given Id.  This Id is the Id of
     * Video in Android Video Content Provider.
     *
     * @param context
     *            Id of the Video to be uploaded.
     *
     * @return A String indicating the status of the video upload operation.
     */
    public String uploadVideo(Context context,
                              Uri videoUri) {
        // Get the path of video file from videoUri.
        String filePath =
                VideoMediaStoreUtils.getPath(context,
                        videoUri);

        // Get the Video from Android Video Content Provider having
        // the given filePath.
        Video androidVideo =
                VideoMediaStoreUtils.getVideo(context,
                        filePath);

        // Check if any such Video exists in Android Video Content
        // Provider.
        if (androidVideo != null) {
            // Prepare to Upload the Video data.

            // Create an instance of the file to upload.
            File videoFile = new File(filePath);

            // Check if the file size is less than the size of the
            // video that can be uploaded to the server.
            if (videoFile.length() < Constants.MAX_SIZE_MEGA_BYTE) {

                try {
                    // Add the metadata of the Video to the Video Service
                    // and get the resulting Video that contains
                    // additional meta-data (e.g., Id and ContentType)
                    // generated by the Video Service.
                    Video receivedVideo =
                            mVideoServiceProxy.addVideo(androidVideo);

                    // Check if the Server returns any Video metadata.
                    if (receivedVideo != null) {

                        // Finally, upload the Video data to the server
                        // and get the status of the uploaded video data.
                        VideoStatus status =
                                mVideoServiceProxy.setVideoData
                                        (receivedVideo.getId(),
                                                new TypedFile(receivedVideo.getContentType(),
                                                        videoFile));

                        // Check if the Status of the Video or not.
                        if (status.getState() == VideoStatus.VideoState.READY) {
                            // Video successfully uploaded.
                            return STATUS_UPLOAD_SUCCESSFUL;
                        }
                    }
                } catch (Exception e) {
                    // Error occured while uploading the video.
                    return STATUS_UPLOAD_ERROR;
                }
            } else
                // Video can't be uploaded due to large video size.
                return STATUS_UPLOAD_ERROR_FILE_TOO_LARGE;
        }

        // Error occured while uploading the video.
        return STATUS_UPLOAD_ERROR;
    }

    /**
     * Get the List of Videos from Video Service.
     *
     * @return the List of Videos from Server or null if there is
     *         failure in getting the Videos.
     */
    public List<Video> getVideoList() {
        try {
            return (ArrayList<Video>)
                    mVideoServiceProxy.getVideoList();
        } catch (Exception e) {
            return null;
        }
    }

    public void getData(Context context, File file){
        ImageData imageData = new ImageData(context, file, mImageServiceProxy);
        imageData.execute();
    }

    public void setRating(int position, Video video, Context context, VideoAdapter videoAdapter){
        VideoRating videoRating = new VideoRating(position, context, videoAdapter, mVideoServiceProxy);
        videoRating.execute(video);
    }
}
