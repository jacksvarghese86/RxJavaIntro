package com.jacksvarghese.rxsamples;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksvarghese.rxsamples.databinding.FragmentLoginBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private LoginViewModel mLoginViewModel;

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
        FragmentLoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        mLoginViewModel = new LoginViewModel();
        //setViewModel method name changes based on variable name declared in XML
        binding.setViewModel(mLoginViewModel);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        mLoginViewModel.destroy();
        super.onDestroy();
    }
}
