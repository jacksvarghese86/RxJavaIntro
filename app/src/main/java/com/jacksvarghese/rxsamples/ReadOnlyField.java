package com.jacksvarghese.rxsamples;

import android.databinding.ObservableField;
import android.util.Log;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * An extension of ObservableField which listens to the observable its tied to and updates the value automatically
 * when the observable changes. This field will automatically disconnects from the observable once the view is
 * destroyed. So you don't have to manually unsubscribe.
 * @param <T>
 */
public class ReadOnlyField<T> extends ObservableField<T> {

    private static final String TAG = "ReadOnlyField";

    //The source that we are going to tie to this observable field.
    private final Observable<T> source;
    private final HashMap<OnPropertyChangedCallback, Disposable> subscriptions = new HashMap<>();

    public static <U> ReadOnlyField<U> create(@NonNull Observable<U> source) {
        return new ReadOnlyField<>(source);
    }

    private ReadOnlyField(@NonNull Observable<T> source){
        super();
        /**
         * Readonly field is updated from the latest value of observable.
         */
        this.source = source.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                ReadOnlyField.super.set(t);
            }
        }).share();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.addOnPropertyChangedCallback(callback);
        Log.d(TAG, "addOnPropertyChangedCallback");
        //subscribing to the observable
        subscriptions.put(callback, source.subscribe());
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        Log.d(TAG, "removeOnPropertyChangedCallback");
        //unsubscribing from the observable
        Disposable subscription = subscriptions.remove(callback);
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        super.removeOnPropertyChangedCallback(callback);
    }


}
