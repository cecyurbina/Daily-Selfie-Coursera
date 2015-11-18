package com.dailyselfie.coursera.dailyselfiec.view;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class ImageFolderActivity extends AppCompatActivity {
    private ListView listView;
    private List<RowData> listSelfies = new ArrayList<>();
    private CustomAdapter customAdapter;
    private String fileName;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder);
        Intent intent = getIntent();
        fileName = intent.getStringExtra("fileName");
        setTitle("Folder: "+ fileName);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorInFolder)));
        }
        initList();
    }


    public void initList(){
        listView = (ListView) findViewById(R.id.listViewImages);
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
                inflater.inflate(R.menu.menu_list_in_folder, menu);
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

    public void showImage(String aSelected){
        String selected = aSelected.replace(Constants.THUMBNAILS_FOLDER,Constants.ORIGINALS_FOLDER);

        Intent intent = new Intent(this, ImageFullScreen.class);
        intent.putExtra("uri", selected);
        startActivity(intent);
    }

    private void getDataInList() {
        File dir = ImageStorageUtils.getImagesByUserAndNameImage(getApplicationContext(), fileName);
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

}
