package com.jacksvarghese.rxsamples;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    private Observable<String> mUserNameObs;
    private Observable<String> mPasswordObs;
    private Observable<String> mEmailObs;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private TextInputLayout mUserNameLayout;
    private TextInputLayout mPasswordLayout;
    private TextInputLayout mEmailLayout;
    private Button mSignInButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setup(view);
        return view;
    }

    private void setup(View view) {
        mUserNameLayout = view.findViewById(R.id.userNameLayout);
        mPasswordLayout = view.findViewById(R.id.passwordLayout);
        mEmailLayout = view.findViewById(R.id.emailLayout);
        mSignInButton = view.findViewById(R.id.signInButton);
        mSignInButton.setEnabled(false);

        mUserNameObs = RxUtils.getEditTextObservable(mUserNameLayout);
        mPasswordObs = RxUtils.getEditTextObservable(mPasswordLayout);
        mEmailObs = RxUtils.getEditTextObservable(mEmailLayout);

        Observable.combineLatest(mUserNameObs, mPasswordObs, mEmailObs,
                new Function3<String, String, String, Boolean>() {
                    @Override
                    public Boolean apply(String userName, String password, String email) throws Exception {
                        int failCount = 0;
                        if (!InputValidator.validateUserName(userName)) {
                            ++failCount;
                            mUserNameLayout.setError("Username format not correct");
                        } else {
                            mUserNameLayout.setErrorEnabled(false);
                        }

                        if (!InputValidator.validatePassword(password)) {
                            ++failCount;
                            mPasswordLayout.setError("Password format not correct");
                        } else {
                            mPasswordLayout.setErrorEnabled(false);
                        }

                        if (!InputValidator.validateEmail(email)) {
                            ++failCount;
                            mEmailLayout.setError("Email format not correct");
                        } else {
                            mEmailLayout.setErrorEnabled(false);
                        }
                        return failCount==0;
                    }
                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mSignInButton.setEnabled(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onDestroyView() {
        mCompositeDisposable.dispose();
        super.onDestroyView();
    }
}
