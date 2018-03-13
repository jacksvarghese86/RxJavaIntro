package com.jacksvarghese.rxsamples;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by jacksvarghese on 3/9/18.
 */

public class RxUtils {

    public static Observable<String> getEditTextObservable(TextInputLayout inputLayout) {
        final PublishSubject<String> subject = PublishSubject.create();

        inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                subject.onNext(editable.toString());
            }
        });
        return subject;
    }
}
