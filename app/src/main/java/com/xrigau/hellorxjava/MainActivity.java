package com.xrigau.hellorxjava;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.view.View;
import android.widget.ImageView;

import com.xrigau.hellorxjava.rs.BitmapEffects;
import com.xrigau.hellorxjava.rs.ImageHelper;

import rx.android.concurrency.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.util.functions.Action1;

public class MainActivity extends Activity {

    private final ImageHelper imageHelper = new ImageHelper();

    private ImageView imageView;
    private Bitmap currentBitmap;
    private RenderScript renderScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        renderScript = RenderScript.create(getApplication());

        setUpViews();

        showSelectedImage();
    }

    private void setUpViews() {
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapEffects.blurryBitmap(currentBitmap, 15f, renderScript)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Bitmap>() {
                            @Override
                            public void call(Bitmap bitmap) {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showNextImage();
                return true;
            }
        });
    }

    private void showNextImage() {
        showImage(imageHelper.getNextBitmap(this));
    }

    private void showSelectedImage() {
        showImage(imageHelper.getSelectedBitmapImage(this));
    }

    private void showImage(Bitmap bitmap) {
        currentBitmap = bitmap;
        imageView.setImageBitmap(bitmap);
    }

}
