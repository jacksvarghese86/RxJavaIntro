package com.jacksvarghese.rxsamples;

import android.databinding.ObservableField;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function3;

/**
 * Created by jacksvarghese on 4/10/18.
 */

public class LoginViewModel {

    private static final String TAG = "LoginViewModel";

    public ObservableField<String> userName  = new ObservableField<>();
    public ObservableField<String> password  = new ObservableField<>();
    public ObservableField<String> email  = new ObservableField<>();
    public ObservableField<String> userNameErr  = new ObservableField<>();
    public ObservableField<String> passwordErr  = new ObservableField<>();
    public ObservableField<String> emailErr  = new ObservableField<>();
    public ObservableField<Boolean> enableLogin;
    public Action signIn;

    public LoginViewModel() {

        Observable result = Observable.combineLatest(FieldUtils.toObservable(userName), FieldUtils.toObservable(password),
                FieldUtils.toObservable(email), new Function3<String, String, String, Boolean>() {
                    @Override
                    public Boolean apply(String userName, String password, String email) throws Exception {
                        int failCount = 0;
                        if (!InputValidator.validateUserName(userName)) {
                            ++failCount;
                            userNameErr.set("Username format not correct");
                        } else {
                            userNameErr.set("");
                        }

                        if (!InputValidator.validatePassword(password)) {
                            ++failCount;
                            passwordErr.set("Password format not correct");
                        } else {
                            passwordErr.set("");
                        }

                        if (!InputValidator.validateEmail(email)) {
                            ++failCount;
                            emailErr.set("Email format not correct");
                        } else {
                            emailErr.set("");
                        }
                        return failCount==0;
                    }});

        enableLogin = FieldUtils.toField(result);

        signIn = new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG, "signIn button clicked");
            }
        };
    }

    public void destroy() {
    }
}
