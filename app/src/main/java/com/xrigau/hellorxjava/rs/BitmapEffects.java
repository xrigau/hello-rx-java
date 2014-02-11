package com.xrigau.hellorxjava.rs;

import android.graphics.Bitmap;
import android.renderscript.RenderScript;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class BitmapEffects {

    public static Observable<Bitmap> blurryBitmap(final Bitmap source, final float blurRadius, final RenderScript renderScript) {
        return Observable.create(new Observable.OnSubscribeFunc<Bitmap>() {
            @Override
            public Subscription onSubscribe(Observer<? super Bitmap> observer) {
                try {
                    observer.onNext(new BlurEffect(renderScript, blurRadius).apply(source));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        });
    }

    public static Observable<Bitmap> grayscaleBitmap(final Bitmap source, final RenderScript renderScript) {
        return Observable.create(new Observable.OnSubscribeFunc<Bitmap>() {
            @Override
            public Subscription onSubscribe(Observer<? super Bitmap> observer) {
                try {
                    observer.onNext(new GrayscaleEffect(renderScript).apply(source));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        });
    }
}
