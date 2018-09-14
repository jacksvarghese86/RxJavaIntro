package com.jacksvarghese.rxsamples;

import android.databinding.BindingConversion;
import android.view.View;

import io.reactivex.functions.Action;

public class BindingUtils {

    @BindingConversion
    public static View.OnClickListener toOnClickListener(final Action listener) {
        if (listener != null) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        listener.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        } else {
            return null;
        }
    }
}
