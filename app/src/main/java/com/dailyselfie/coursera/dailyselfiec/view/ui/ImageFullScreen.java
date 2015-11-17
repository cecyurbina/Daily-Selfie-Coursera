package com.dailyselfie.coursera.dailyselfiec.view.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.dailyselfie.coursera.dailyselfiec.R;

public class ImageFullScreen extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);
        imageView = (ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        String message = intent.getStringExtra("uri");
        Bitmap bMap = BitmapFactory.decodeFile(message);
        imageView.setImageBitmap(bMap);
    }
}
