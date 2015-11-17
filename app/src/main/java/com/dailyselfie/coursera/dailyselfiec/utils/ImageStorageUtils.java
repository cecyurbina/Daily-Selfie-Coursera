package com.dailyselfie.coursera.dailyselfiec.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 15/11/15.
 */
public class ImageStorageUtils {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static int ORIGINAL = 0;
    public static int THUMB = 1;

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DailySelfie");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
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
        String root = "images/" + String.valueOf(user);
        if (type == ORIGINAL) {
            mediaStorageDir = new File(context.getFilesDir(),
                    root + "/originals");
        } else if (type == THUMB) {
            mediaStorageDir = new File(context.getFilesDir(),
                    root + "/thumbnails");
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

    public static File saveImageFromCamera(File f, Context context, String user){
        File mediaFile = getOutputMediaFile(ORIGINAL, f.getName(), context, user);
        File mediaFileThumb = getOutputMediaFile(THUMB, f.getName(), context, user);

        FileOutputStream fos = null;
        try {
            Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath());
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bmResized = BitmapFactory.decodeFile(f.getPath(),options);
            Bitmap resized = ThumbnailUtils.extractThumbnail(bmResized, 50, 50);
            writeFile(resized, mediaFileThumb);
            writeFile(bm, mediaFile);
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return mediaFile;
    }

    public static void writeFile(Bitmap bm, File mediaFile){
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
    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
            {
                child.delete();
                deleteRecursive(child);
            }

        fileOrDirectory.delete();
    }

    public static boolean isLoggedIn(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                context);
        //get token saved
        String user = prefs.getString("user", null);
        if (user != null) {
            return true;
        }
        return false;
    }

    public static String getCurrentUser(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                context);
        String user = prefs.getString("user", null);
        if (user != null) {
            return user;
        }
        return Constants.ANONYMOUS_USER;
    }

    public static void logout(Context context){
        //erase token
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                context);
        prefs.edit().putString(Constants.TOKEN, null).commit();
        prefs.edit().putString(Constants.USER, null).commit();
    }

    public static File getDirThumbs(Context context){
        String user = getCurrentUser(context);
        File mediaStorageDir = null;
        String root = "images/" + String.valueOf(user);
        mediaStorageDir = new File(context.getFilesDir(),
                    root + "/thumbnails");
        return mediaStorageDir;
    }
}
