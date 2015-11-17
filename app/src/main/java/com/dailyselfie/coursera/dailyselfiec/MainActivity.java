package com.dailyselfie.coursera.dailyselfiec;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dailyselfie.coursera.dailyselfiec.common.Utils;
import com.dailyselfie.coursera.dailyselfiec.utils.Constants;
import com.dailyselfie.coursera.dailyselfiec.utils.ImageStorageUtils;
import com.dailyselfie.coursera.dailyselfiec.view.CustomAdapter;
import com.dailyselfie.coursera.dailyselfiec.view.LoginActivity;
import com.dailyselfie.coursera.dailyselfiec.view.ui.ImageFullScreen;
import com.dailyselfie.coursera.dailyselfiec.view.ui.RowData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private ListView listView;
    private List<RowData> listSelfies = new ArrayList<>();
    private CustomAdapter customAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listView);
        getDataInList();
        customAdapter = new CustomAdapter(getApplicationContext(), listSelfies);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String selected = ((TextView) view.findViewById(R.id.secondLine)).getText().toString();
                showImage(selected);


            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showCameraIntent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_login){
            if (ImageStorageUtils.isLoggedIn(getApplicationContext())) {
                ImageStorageUtils.logout(getApplicationContext());
                invalidateOptionsMenu();
            } else {
                Intent intent = new Intent(getApplication().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void showCameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(ImageStorageUtils.getOutputMediaFile(type));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        Bitmap photoThumb;
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
                //mImageView.setImageBitmap(imageBitmap);
            Uri u = Uri.parse(fileUri.toString());
            File f = new File(u.getPath());

            //get token saved
            String user;
            user = ImageStorageUtils.getCurrentUser(getApplicationContext());
            ImageStorageUtils.saveImageFromCamera(f, getApplicationContext(), user);
            uploadDataInList();
        }
            /*photoThumb = Utilities.getThumbnailFromFile(fileUri.getPath().toString());
            Utilities.saveBitmapToFile(photoThumb, nameFile);
            uploadDataInList();*/
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        //change login to logout
        MenuItem someMenuItem = menu.findItem(R.id.action_login);
        if (ImageStorageUtils.isLoggedIn(getApplicationContext())){
            someMenuItem.setTitle(getString(R.string.action_logout));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void getDataInList() {
        File dir = ImageStorageUtils.getDirThumbs(getApplicationContext());
        File[] filelist = dir.listFiles();
        listSelfies.clear();
        try {
            for (File f : filelist) { // do your stuff here }
                RowData ld = new RowData();
                ld.setTitle(f.getName());
                ld.setDescription(f.getPath());
                ld.setImgResId(f.getPath());
                // Add this object into the ArrayList myList
                listSelfies.add(ld);

            }
        }catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private void uploadDataInList() {
        getDataInList();
        customAdapter.notifyDataSetChanged();
    }

    public void showImage(String aSelected){
        String selected = aSelected.replace(Constants.THUMBNAILS_FOLDER,Constants.ORIGINALS_FOLDER);
        /*Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + selected), "image/*");
        startActivity(intent);*/

        Intent intent = new Intent(this, ImageFullScreen.class);
        intent.putExtra("uri", selected);
        startActivity(intent);
    }

}
