package com.jacksvarghese.rxsamples;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroFragment extends Fragment {


    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<ArrayList<Integer>> syncDataObservable;
    private Observable<ArrayList<Integer>> aSyncDataObservable;
    private Observer<ArrayList<Integer>> dataObserver;

    public IntroFragment() {
        // Required empty public constructor
    }

    public static IntroFragment newInstance() {
        return new IntroFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        /**
         * Observer to listen for changes in observable
         */
        dataObserver = new Observer<ArrayList<Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                Toast.makeText(getContext(), "onSubscribe", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ArrayList<Integer> integers) {
                Toast.makeText(getContext(), "onNext: " + integers.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.d("RxJava", "onComplete: " + Thread.currentThread().getName());
                Toast.makeText(getContext(), "onComplete: " + Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
            }
        };

        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Create an observable object from the data we are interested in.
                 */
                syncDataObservable = Observable.just(fetchSmallData());

                /**
                 * Connect observable and observer.
                 */
                syncDataObservable.subscribe(dataObserver);
            }
        });

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Create an observable object from the data we are interested in.
                 */
                aSyncDataObservable = Observable.fromCallable(new Callable<ArrayList<Integer>>() {
                    @Override
                    public ArrayList<Integer> call() throws Exception {
                        return fetchLargeData();
                    }
                });

                /**
                 * Connect observable and observer.
                 */
                aSyncDataObservable
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(dataObserver);
            }
        });

        return view;
    }

    /**
     * Fetch data.
     * @return
     */
    private ArrayList<Integer> fetchSmallData() {
        Log.d("RxJava", "fetchSmallData: "+Thread.currentThread().getName());
        ArrayList<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(2);
        data.add(3);
        data.add(4);
        data.add(5);
        return data;
    }

    /**
     * Fetch large amount of data.
     * @return
     */
    private ArrayList<Integer> fetchLargeData() {
        Log.d("RxJava", "fetchLargeData: "+Thread.currentThread().getName());
        ArrayList<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(2);
        data.add(3);
        data.add(4);
        data.add(5);
        //Adding a loop to simulate long running operation.
        long count = 0;
        while (count < 9999999999l) {
            ++count;
        }
        return data;
    }

    @Override
    public void onDestroyView() {
        compositeDisposable.dispose();
        super.onDestroyView();
    }
}
