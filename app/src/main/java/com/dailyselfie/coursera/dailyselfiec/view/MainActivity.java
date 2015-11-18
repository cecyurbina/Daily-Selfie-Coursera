package com.dailyselfie.coursera.dailyselfiec.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dailyselfie.coursera.dailyselfiec.R;
import com.dailyselfie.coursera.dailyselfiec.utils.Constants;
import com.dailyselfie.coursera.dailyselfiec.utils.ImageStorageUtils;
import com.dailyselfie.coursera.dailyselfiec.view.ui.CustomAdapter;
import com.dailyselfie.coursera.dailyselfiec.view.ui.RowData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int LOGIN_REQUEST_CODE = 200;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private ListView listView;
    private List<RowData> listSelfies = new ArrayList<>();
    private CustomAdapter customAdapter;
    private AlertDialog dialogEffects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initList();


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

    public void initList(){
        listView = (ListView) findViewById(R.id.listView);
        getDataInList();
        customAdapter = new CustomAdapter(getApplicationContext(), listSelfies);

        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // selected item
                RowData selectedRow = listSelfies.get(position);
                String selected = ((TextView) view.findViewById(R.id.secondLine)).getText().toString();
                showImage(selected, selectedRow.getTitle());


            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int nr = 0;

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                menu.clear();
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_list, menu);
                mode.setTitle("Select Items");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_login:
                        Toast.makeText(getApplicationContext(), getString(R.string.title_activity_image),
                                Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_settings:
                        break;
                    case R.id.action_effect:
                        showDialogEffects();
                        break;

                }
                mode.finish();
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                nr = 0;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                RowData hi = customAdapter.getItem(position);
                if (checked) {
                    nr++;
                    //mSelectedRecords.add(hi.getId());

                } else {
                    nr--;
                    //mSelectedRecords.remove(mSelectedRecords.indexOf(form.getId()));

                }
                mode.setTitle(nr + " " + getString(R.string.title_activity_image));
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
                uploadDataInList();
                invalidateOptionsMenu();
            } else {
                Intent intent = new Intent(getApplication().getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
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
        if (requestCode == LOGIN_REQUEST_CODE) {
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
        File dir = ImageStorageUtils.getDirsByUser(getApplicationContext());
        File[] filelist = dir.listFiles();
        listSelfies.clear();
        try {
            for (File f : filelist) { // do your stuff here }
                RowData ld = new RowData();
                ld.setTitle(f.getName());
                ld.setDescription(f.getPath()+"/"+Constants.THUMBNAILS_FOLDER + "/"+f.getName());
                ld.setImgResId(f.getPath()+"/"+Constants.THUMBNAILS_FOLDER + "/"+f.getName());
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

    public void showImage(String aSelected, String fileName){
        Intent intent = new Intent(this, ImageFolderActivity.class);
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }

    private void showDialogEffects(){
        final CharSequence[] items = {" Easy "," Medium "," Hard "," Very Hard "};
        // arraylist to keep the selected items
        final ArrayList seletedItems=new ArrayList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplication().getBaseContext());
        builder.setTitle("Select The Difficulty Level");
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    // indexSelected contains the index of item (of which checkbox checked)
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            // write your code when user checked the checkbox
                            seletedItems.add(indexSelected);
                        } else if (seletedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            // write your code when user Uchecked the checkbox
                            seletedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel

                    }
                });

        dialogEffects = builder.create();//AlertDialog dialog;
        dialogEffects.show();
    }




}
