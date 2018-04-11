package com.jacksvarghese.rxsamples;

import android.databinding.ObservableField;
import android.databinding.Observable.OnPropertyChangedCallback;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

/**
 * Created by jacksvarghese on 4/10/18.
 */

public class FieldUtils {

    /**
     * Converts an ObservableField to an Observable.
     * @return Observable that contains the latest value in the ObservableField
     */
    @NonNull
    public static <T> Observable<T> toObservable(@NonNull final ObservableField<T> field) {

        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(final ObservableEmitter<T> emitter) throws Exception {
                T initialValue = field.get();
                if (initialValue != null) {
                    //Emit initial value
                    emitter.onNext(initialValue);
                }
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        //Emit value whenever there is change in observableField
                        emitter.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        //Remove property change listener when observable is no longer required
                        field.removeOnPropertyChangedCallback(callback);
                    }
                });
            }
        });
    }
}
