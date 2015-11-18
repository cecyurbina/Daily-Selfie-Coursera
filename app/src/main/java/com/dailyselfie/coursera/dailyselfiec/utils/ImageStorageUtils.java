package com.dailyselfie.coursera.dailyselfiec.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.client.Response;

/**
 * Created by root on 15/11/15.
 */
public class ImageStorageUtils {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static int ORIGINAL = 0;
    public static int THUMB = 1;

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DailySelfie");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    /**
     * Create a File for saving an image in internal folders
     */
    public static File getOutputMediaFile(int type, String name, Context context, String user) {
        File mediaStorageDir = null;
        String root = Constants.IMAGES_FOLDER + "/" + String.valueOf(user) + "/" + name;
        if (type == ORIGINAL) {
            mediaStorageDir = new File(context.getFilesDir(),
                    root + "/" + Constants.ORIGINALS_FOLDER);
        } else if (type == THUMB) {
            mediaStorageDir = new File(context.getFilesDir(),
                    root + "/" + Constants.THUMBNAILS_FOLDER);
        }

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CreateDirectory", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                name);
        return mediaFile;
    }

    /**
     * Get directory by name and user
     */
    public static File getOutputMediaFileDir(String name, String user) {
        File mediaStorageDir = null;
        String root = Constants.IMAGES_FOLDER + "/" + String.valueOf(user) + "/" + name;
        mediaStorageDir = new File(root);
        return mediaStorageDir;
    }

    /**
     * Create a File for saving an image in internal folders
     */
    public static File getOutputMediaFile(int type, String originalName, Context context,
                                          String user, String newName) {
        File mediaStorageDir = null;
        String root = Constants.IMAGES_FOLDER + "/" + String.valueOf(user) + "/" + originalName;
        if (type == ORIGINAL) {
            mediaStorageDir = new File(context.getFilesDir(),
                    root + "/" + Constants.ORIGINALS_FOLDER);
        } else if (type == THUMB) {
            mediaStorageDir = new File(context.getFilesDir(),
                    root + "/" + Constants.THUMBNAILS_FOLDER);
        }

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CreateDirectory", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                newName);
        return mediaFile;
    }

    /**
     * save image from camera
     *
     * @param f
     * @param context
     * @param user
     * @return
     */
    public static File saveImageFromCamera(File f, Context context, String user) {
        File mediaFile = getOutputMediaFile(ORIGINAL, f.getName(), context, user);
        File mediaFileThumb = getOutputMediaFile(THUMB, f.getName(), context, user);

        FileOutputStream fos = null;
        try {
            Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath());
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bmResized = BitmapFactory.decodeFile(f.getPath(), options);
            Bitmap resized = ThumbnailUtils.extractThumbnail(bmResized, 50, 50);
            writeFile(resized, mediaFileThumb);
            writeFile(bm, mediaFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mediaFile;
    }

    /**
     * save image from camera
     *
     * @param f
     * @param context
     * @param user
     * @return
     */
    public static File saveImageFromCamera(File f, Context context, String user, String originalName) {
        File mediaFile = getOutputMediaFile(ORIGINAL, originalName, context, user, f.getName());
        File mediaFileThumb = getOutputMediaFile(THUMB, originalName, context, user, f.getName());

        FileOutputStream fos = null;
        try {
            Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath());
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bmResized = BitmapFactory.decodeFile(f.getPath(), options);
            Bitmap resized = ThumbnailUtils.extractThumbnail(bmResized, 50, 50);
            writeFile(resized, mediaFileThumb);
            writeFile(bm, mediaFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mediaFile;
    }

    /**
     * @param bm
     * @param mediaFile
     */
    public static void writeFile(Bitmap bm, File mediaFile) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mediaFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * delete a folder with children
     *
     * @param fileOrDirectory
     */
    public static void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                child.delete();
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    /**
     * @param context
     * @return
     */
    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                context);
        //get token saved
        String user = prefs.getString(Constants.USER, null);
        if (user != null) {
            return true;
        }
        return false;
    }

    /**
     * get current username
     *
     * @param context
     * @return
     */
    public static String getCurrentUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                context);
        String user = prefs.getString(Constants.USER, null);
        if (user != null) {
            return user;
        }
        return Constants.ANONYMOUS_USER;
    }

    /**
     * locally logout
     *
     * @param context
     */
    public static void logout(Context context) {
        //erase token
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                context);
        prefs.edit().putString(Constants.TOKEN, null).commit();
        prefs.edit().putString(Constants.USER, null).commit();
    }

    /**
     * get all folders by user
     *
     * @param context
     * @return
     */
    public static File getDirsByUser(Context context) {
        String user = getCurrentUser(context);
        File mediaStorageDir = null;
        String root = Constants.IMAGES_FOLDER + "/" + String.valueOf(user);
        mediaStorageDir = new File(context.getFilesDir(),
                root);
        /*mediaStorageDir = new File(context.getFilesDir(),
                root + "/"+Constants.THUMBNAILS_FOLDER);*/
        return mediaStorageDir;
    }

    /**
     * get all images in a folder by user
     *
     * @param context
     * @param nameImage
     * @return
     */
    public static File getImagesByUserAndNameImage(Context context, String nameImage) {
        String user = getCurrentUser(context);
        File mediaStorageDir = null;
        String root = Constants.IMAGES_FOLDER + "/" + String.valueOf(user) + "/" + nameImage +
                "/" + Constants.THUMBNAILS_FOLDER;
        mediaStorageDir = new File(context.getFilesDir(),
                root);
        /*mediaStorageDir = new File(context.getFilesDir(),
                root + "/"+Constants.THUMBNAILS_FOLDER);*/
        return mediaStorageDir;
    }


    /**
     * save response video in DailySelfie folder
     *
     * @param context
     * @param response
     * @param videoName
     * @return
     */
    public static File storeVideoInExternalDirectory(Context context,
                                                     Response response,
                                                     String videoName) {
        // Try to get the File from the Directory where the Video
        // is to be stored.
        final File file =
                getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            try {
                // Get the InputStream from the Response.
                final InputStream inputStream =
                        response.getBody().in();

                // Get the OutputStream to the file
                // where Video data is to be written.
                final OutputStream outputStream =
                        new FileOutputStream(file);

                // Write the Video data to the File.
                IOUtils.copy(inputStream,
                        outputStream);

                // Close the streams to free the Resources used by the
                // stream.
                outputStream.close();
                inputStream.close();

                // Always notify the MediaScanners after Downloading
                // the Video, so that it is immediately available to
                // the user.
                notifyMediaScanners(context,
                        file);
                return file;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * Notifies the MediaScanners after Downloading the Video, so it
     * is immediately available to the user.
     */
    private static void notifyMediaScanners(Context context,
                                            File videoFile) {
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile
                (context,
                        new String[]{videoFile.toString()},
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path,
                                                        Uri uri) {
                            }
                        });
    }
}
