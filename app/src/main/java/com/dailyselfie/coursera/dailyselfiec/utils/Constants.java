package com.dailyselfie.coursera.dailyselfiec.utils;

/**
 * Class that contains all the Constants required in our Video Upload
 * client App.
 */
public class Constants {
    /**
     * URL of the VideoWebService.  Please Read the Instructions in
     * README.md to set up the SERVER_URL.
     */
    public static final String SERVER_URL =
        "https://192.168.1.53:8443";
    
    /**
     * Define a constant for 1 MB.
     */
    public static final long MEGA_BYTE = 1024 * 1024;

    /**
     * Maximum size of Video to be uploaded in MB.
     */
    public static final long MAX_SIZE_MEGA_BYTE = 50 * MEGA_BYTE;

    /**
     * user key in shared preferences
     */
    public static final String USER = "user";

    /**
     * token key in shared preferences
     */
    public static final String TOKEN = "token";

    /**
     * private folder to images
     */
    public static final String IMAGES_FOLDER = "images";

    /**
     * private folder to original images
     */
    public static final String ORIGINALS_FOLDER = "originals";

    /**
     * private folder to thumbnails images
     */
    public static final String THUMBNAILS_FOLDER = "thumbnails";

    /**
     * public folder to images
     */
    public static final String FOLDER_APP = "DailySelfie";

    /**
     * name anonym user
     */
    public static final String ANONYMOUS_USER = "anonymous";


}
