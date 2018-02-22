package com.jacksvarghese.rxsamples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<ArrayList<Integer>> syncDataObservable;
    private Observable<ArrayList<Integer>> aSyncDataObservable;
    private Observer<ArrayList<Integer>> dataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Observer to listen for changes in observable
         */
        dataObserver = new Observer<ArrayList<Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                Toast.makeText(getApplicationContext(), "onSubscribe", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ArrayList<Integer> integers) {
                Toast.makeText(getApplicationContext(), "onNext: " + integers.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), "onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.d("RxJava", "onComplete: " + Thread.currentThread().getName());
                Toast.makeText(getApplicationContext(), "onComplete: " + Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
            }
        };

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
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
    }

    /**
     * remove all subscriptions when activity is getting destroyed
     */
    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
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
}
