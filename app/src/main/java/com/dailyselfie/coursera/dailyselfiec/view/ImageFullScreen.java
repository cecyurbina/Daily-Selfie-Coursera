package com.dailyselfie.coursera.dailyselfiec.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.dailyselfie.coursera.dailyselfiec.R;

public class ImageFullScreen extends AppCompatActivity {
    ImageView imageView;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorInFolder)));
        }

        imageView = (ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        String message = intent.getStringExtra("uri");
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bMap = BitmapFactory.decodeFile(message, options);
        imageView.setImageBitmap(bMap);
    }
}
