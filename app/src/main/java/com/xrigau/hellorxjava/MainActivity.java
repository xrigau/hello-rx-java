package com.xrigau.hellorxjava;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xrigau.hellorxjava.rs.BitmapEffects;
import com.xrigau.hellorxjava.rs.ImageHelper;

import rx.Observer;
import rx.android.concurrency.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

    private static final float BLUR_RADIUS = 10f;

    private final ImageHelper imageHelper = new ImageHelper();

    private ImageView imageView;
    private Bitmap currentBitmap;
    private RenderScript renderScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        setUpViews();

        renderScript = RenderScript.create(getApplication());
        showNextBitmap();
    }

    private void setUpViews() {
        findViewById(R.id.blur).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapEffects.blurryBitmap(currentBitmap, BLUR_RADIUS, renderScript)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(makeShowBitmapObserver());
            }
        });
        findViewById(R.id.grayscale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapEffects.grayscaleBitmap(currentBitmap, renderScript)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(makeShowBitmapObserver());
            }
        });
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextBitmap();
            }
        });
    }

    private void showNextBitmap() {
        imageHelper.nextBitmap(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(makeShowBitmapObserver());
    }

    private Observer<Bitmap> makeShowBitmapObserver() {
        return new Observer<Bitmap>() {
            @Override
            public void onNext(Bitmap bitmap) {
                showImage(bitmap);
            }

            @Override
            public void onError(Throwable throwable) {
                showError(throwable);
            }

            @Override
            public void onCompleted() {

            }
        };
    }

    private void showImage(Bitmap bitmap) {
        currentBitmap = bitmap;
        imageView.setImageBitmap(bitmap);
    }

    private void showError(Throwable throwable) {
        Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

}
