package com.dailyselfie.coursera.dailyselfiec.model.mediator.webdata;

import retrofit.client.Response;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

/**
 * Created by root on 17/11/15.
 */
public interface ImageServiceProxy {
    /**
     * Used as Request Parameter for Image data.
     */
    public static final String DATA_PARAMETER = "data";

    /**
     * Used as Request Parameter for effects.
     */
    public static final String ID_EFFECT = "effect";

    /**
     * The path where we expect the image to live.
     */
    public static final String IMAGE_SVC_PATH = "/image";

    public static final String IMAGE_DATA_PATH =
            IMAGE_SVC_PATH
                    + "/{"
                    + ImageServiceProxy.ID_EFFECT
                    + "}/data";

    /**
     *
     * @param id
     * @param imageData
     * @return Response which contains the actual image data.
     */
    @Streaming
    @Multipart
    @POST(IMAGE_DATA_PATH)
    public Response setImageData(@Path(ID_EFFECT) long id,
                                    @Part(DATA_PARAMETER) TypedFile imageData);

    /**
     * This method uses Retrofit's @Streaming annotation to indicate
     * that the method is going to access a large stream of data
     * (e.g., the mpeg video data on the server).  The client can
     * access this stream of data by obtaining an InputStream from the
     * Response as shown below:
     *
     * VideoServiceProxy client = ... // use retrofit to create the client
     * Response response = client.getData(someVideoId);
     * InputStream videoDataStream = response.getBody().in();
     *
     * @param id
     * @return Response which contains the actual Video data.
     */


}
